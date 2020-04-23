package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import application.Application;

//	Déclaration Attribut
@Entity
@Table(name = "fakemon_stats")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="espece")
public abstract class Monster {
	//Stats de l'État actuel du fakemon
	@Transient
	protected int level; 
	@Transient
	protected int PV;
	@Transient
	protected int PVmax;
	@Transient
	protected int Atk;	
	@Transient
	protected int Def;
	@Transient
	protected int ASp;	
	@Transient
	protected int DSp;
	@Transient
	protected int Vit;

	//Pour le calcul des stats : base commune de l'espèce
	@Column (name = "pv", nullable = false)
	protected double basePV;

	@Column (name = "attaque", nullable = false)
	protected double baseAtk;	

	@Column (name = "defense", nullable = false)
	protected double baseDef;

	@Column (name = "atk_speciale", nullable = false)
	protected double baseASp;

	@Column (name = "defk_speciale", nullable = false)
	protected double baseDSp;

	@Column (name = "vitesse", nullable = false)
	protected double baseVit;

	//Pour le calcul des stats : stats aléatoire de ce fakemon précis
	@Transient
	protected double ivPV;
	@Transient
	protected double ivAtk;	
	@Transient
	protected double ivDef;
	@Transient
	protected double ivASp;
	@Transient
	protected double ivDSp;	
	@Transient
	protected double ivVit;

	//Pour le calcul des stats : effet de la nature
	@Transient
	protected double[] tabNature={1,1,1,1,1,1};

	//Pour le combat : modificateurs. Pour le moment pas de moyen de les modifier MAIS important à avoir pour futures améliorations du système de combat
	@Transient
	protected double modifAtk=1;
	@Transient	
	protected double modifDef=1;
	@Transient
	protected double modifASp=1;
	@Transient
	protected double modifDSp=1;
	@Transient
	protected double modifVit=1;

	//Autres attributs importants
	@Transient
	protected ArrayList<Attaque> listAttaque = new ArrayList<Attaque>();
	@Transient
	protected Situation equipeJoueur=Situation.valueOf("Sauvage");
	@Transient
	protected int exp = 0;
	@Transient
	protected int expNextLevel =5;

	@Column(name = "type", length = 15, nullable = false)
	@Enumerated(EnumType.STRING)
	protected Type type; 
	
	@Column (name ="espece", length = 15, nullable = false)
	protected String nom= this.getClass().getSimpleName();
	
	
	/* 	Stats non-utiles pour le moment : futures implementation ?
	 * 	Mana ? Remplace les PP des attaques
	 *	protected int modifEsquive;
	 *	protected int modifPrécision;
	 *	protected int modifCritique;
	 */


	//___________________________________________
	//	Constructeur

	public Monster(int level, double basePV, double baseAtk, double baseDef,double baseASp,double baseDSp, double baseVit, ArrayList<Attaque> listAttaque,Type type){
		this.level=level;
		this.basePV=basePV;
		this.baseAtk=baseAtk;
		this.baseDef=baseDef;
		this.baseASp=baseASp;
		this.baseDSp=baseDSp;
		this.baseVit=baseVit;
		this.type = type;
		generationIV();	
		nature();
		calcStat();
		this.listAttaque = listAttaque;
	}


	//___________________________________________
	//	Getters/Setters
	public int getLevel() {
		return level;
	}
	public int getPV() {
		return PV;
	}
	public void setPV(int pV) {
		PV = pV;
	}
	public int getAtk() {
		return Atk;
	}
	public int getDef() {
		return Def;
	}
	public int getASp() {
		return ASp;
	}
	public int getDSp() {
		return DSp;
	}
	public int getVit() {
		return Vit;
	}
	public Type getType() {
		return type;
	}
	protected void setEquipeJoueur() {
		equipeJoueur=Situation.valueOf("Joueur");
	}
	public String getNom(){
		return this.nom;
	}
	public Integer getPVmax() {
		return this.PVmax;
	}
	public ArrayList<Attaque> getListAttaque() {
		return listAttaque;	
	}
	public int getExpNextLevel () {
		return expNextLevel;
	}
	public int getExpGain() {
		int c= (2 + (int)(level + Math.pow(level,2)) /2);
		return c;
	}

	//___________________________________________
	//	Méthodes
	//	Défini les IV du fakemon : ne doit être utilisé que dans le constructeur
	private void generationIV () {
		Random r=new Random();
		ivPV=r.nextInt(6);
		r=new Random();
		ivAtk=r.nextInt(6);
		r=new Random();
		ivDef=r.nextInt(6);
		r=new Random();
		ivASp=r.nextInt(6);
		r=new Random();
		ivDSp=r.nextInt(6);
		r=new Random();
		ivVit=r.nextInt(6);
		/*Je ne sais pas ce qui est le plus efficace...
		 int[] tabRand=new int[6];
		for (int i=1;i<tabRand.length;i++) {
			Random r=new Random();
			tabRand[i]=r.nextInt(6);
		}
		ivPV=r.tabRand(0);
		ivAtk=r.tabRand(1);
		ivDef=r.tabRand(2);
		ivASp=r.tabRand(3);
		ivDSp=r.tabRand(4);
		ivVit=r.tabRand(5);
		System.out.println(Arrays.toString(tabRand));*/
	}


	//	Génère une nature qui module 2 statistiques du monstre : ne doit être utilisé que dans le constructeur 
	private void nature() {
		//Nature sur 4 stats
		/*		int stUp=4; int stDown=4;
		while (stUp==3 || stUp==4) {
			Random r1=new Random();
			stUp=r1.nextInt(4);
		}
		while (stDown==3 || stDown==4) {
			Random r1=new Random();
			stDown=r1.nextInt(4);
		}
		if (stUp!=stDown) {
			tabNature[stUp]=1.1;
			tabNature[stDown]=0.9;
		}*/
		//Nature sur 6 stats
		Random r=new Random();
		int stUp=r.nextInt(6);
		r=new Random();
		int stDown=r.nextInt(6);
		if (stUp!=stDown) {
			tabNature[stUp]=1.1;
			tabNature[stDown]=0.9;
		}	
	}


	//	Calcul les nouvelles stats au niveau actuel + soigne le monstre
	public void calcStat() {
		final double cstLv = (double) 2/5;
		PVmax = (int) (10+level+((basePV+ivPV)*tabNature[0]*(level*cstLv)));
		PV = PVmax;
		Atk = (int) (5+((baseAtk+ivAtk)*tabNature[1]*(level*cstLv)));	
		Def = (int) (5+((baseDef+ivDef)*tabNature[2]*(level*cstLv)));
		ASp = (int) (5+((baseASp+ivASp)*tabNature[3]*(level*cstLv)));	
		DSp = (int) (5+((baseDSp+ivDSp)*tabNature[4]*(level*cstLv)));
		Vit = (int) (5+((baseVit+ivVit)*tabNature[5]*(level*cstLv)));
	}


	//	Fait prendre un niveau et recalcule les stats
	public void levelUp() {
		level++;
		expNextLevel=( (int) (7*this.getLevel() + Math.pow(this.getLevel(),2) )/2 )+1;
		calcStat();
		/*if (level==5) --> ouvre un slot d'attaque!
		 * if(level==3 || 5 || 8 || 10) -> propose nouvelle attaque*/
		
	}


	//	Gère le gain d'exp pour le monstre en cours et qui fait le levelUp si besoin
	public void expGain(Monster m) {

		exp += m.getExpGain();
		if (exp>=expNextLevel) {
			exp-=expNextLevel;
			levelUp();
		}

	}


	//	Vérifie si le monstre est capturable et réalise le test de capture. Si la capture réussie ajoute le monstre à l'équipe du joueur.
	public void captureMonstre() {

		if (equipeJoueur.equals(Situation.valueOf("Sauvage"))) {

			System.out.println("Tentative de capture du "+this.getNom()+" sauvage");
			double txCap = 1;
			if ((double) (this.getPV()/this.getPVmax()) <= 0.05) {
				txCap = 4;
			}
			else if ((double) (this.getPV()/this.getPVmax()) <= 0.15) {
				txCap = 3;
			}
			else if ((double) (this.getPV()/this.getPVmax()) <= 0.25) {
				txCap = 2.5;
			}
			else if ((double) (this.getPV()/this.getPVmax()) <= 0.5) {
				txCap = 2;
			}
			else if ((double) (this.getPV()/this.getPVmax()) <= 0.75) {
				txCap = 1.5;
			}

			int captureRate = (int) (2 * txCap * (21-this.getLevel()));
			Random r = new Random();
			if (r.nextInt(100)+1>captureRate) {
				System.out.println("La capture de "+this.getNom()+" a échouée");
			}
			else {
				System.out.println("La capture de "+this.getNom()+" a réussi !");
				Player.getInstance().addEquipePlayer(this);
			}

		}
		else {
			System.out.println("Le monstre adverse n'est pas capturable");
		}
	}


	public Attaque choixAttaque() {
		this.listAttaque.forEach(a -> System.out.println("- "+a.getNom()+" ["+a.getType()+", "+a.getEtat()+"] : Puissance = "+a.getPuissance()+", Precision = "+a.getPrecision()));
		int sc = Application.saisieInt("Quelle attaque ? (1 à 3)");

		Attaque a=null;
		switch (sc) {
		case 1 : a = listAttaque.get(0);break;
		case 2 : a = listAttaque.get(1);break;
		case 3 : a = listAttaque.get(2);break;
		//		case 4 : a = listAttaque.get(3);break;  à utiliser que si on décide d'utiliser 4 slots d'attaques
		default : System.out.println("Mauvaise saisie. Veuillez recommencer");choixAttaque();break;
		}
		return a;
	}


	public Attaque choixAttaqueBOT(Monster m) {

		Attaque a=null;
		Random r = new Random();
		switch (r.nextInt(3)) {
		case 0 : a = listAttaque.get(0);break;
		case 1 : a = listAttaque.get(1);break;
		case 2 : a = listAttaque.get(2);break;
		//		case 4 : a = listAttaque.get(3);break;  à utiliser que si on décide d'utiliser 4 slots d'attaques
		default : choixAttaqueBOT(m);break;
		}

		for (Attaque i : listAttaque) {
			if (Context.getInstance().getDaoAttaque().ratioEfficacite(i.getType(),m.getType())==2) {
				r = new Random();
				if(r.nextInt(4)==4) {
					a=i;
				}
			}
		}
		return a;
	}


	public static ArrayList<Attaque> creationAttaque(Integer[] poolEntier) {

		LinkedList<Integer> mesIds = new LinkedList<Integer>();
		mesIds.addAll(Arrays.asList(poolEntier));
		Collections.shuffle(mesIds);

		ArrayList<Integer> idsForQuery = new ArrayList<Integer>();

		for(int i=0; i < 3; i++) {
			idsForQuery.add(mesIds.poll());
		}
		return Context.getInstance().getDaoAttaque().selectPoolId(idsForQuery);

	}


	public ArrayList<Attaque> poolAttaque(ArrayList<Integer> ids) {

		this.listAttaque = Context.getInstance().getDaoAttaque().selectPoolId(ids);
		return listAttaque;
	}


	//	Compare les deux vitesses
	public Monster initiative(Monster m2) {

		Monster m = null;

		if (this.getVit()>m2.getVit()) {
			m = this;
		}
		else if (this.getVit()<m2.getVit()) {
			m = m2;
		}
		else {
			Random r = new Random();
			;
			switch (r.nextInt(2)) {
			case 0 : m = this ; break;
			case 1 : m = m2 ; break;
			}
		}
		return m;
	}


	//	Calcul des dégâts et renvoie erreur si PV tombe à 0
	public void combat(Monster m) throws PVException {

		Attaque a = (equipeJoueur.equals(Situation.valueOf("Joueur"))) ? choixAttaque() : choixAttaqueBOT(m);	

		Random r = new Random();

		if (r.nextInt(100)>a.getPrecision()) {
			System.out.println("L'attaque de "+this.getClass().getSimpleName()+" a raté !");
		}
		else {

			//set les paramettres de calcul des dégâts
			final double k1 = (double) 2/5;
			final double k2 = 50;

			//set le bonus de stab
			double stab = 1.0;
			if (a.getType().equals(this.getType())) {
				stab = 1.5;
			}

			//set si l'attaque utilisée est efficace ou non
			double type = Context.getInstance().getDaoAttaque().ratioEfficacite(a.getType(),m.getType());
			if (type == 2) {
				System.out.println("L'attaque est super efficace !");
			}
			if (type == 0.5) {
				System.out.println("L'attaque est peu efficace ...");
			}

			//dedermine si l'attaque est physique ou spéciale
			int statDegat = 0;
			int statProtection = 0;
			switch (a.getEtat()) {
			case "Physique": statDegat=this.Atk ; statProtection=m.getDef(); break;
			case "Special" : statDegat=this.ASp ; statProtection=m.getDSp(); break;
			default : System.out.println("erreur de degat");break;
			}

			//calcul des dégats
			int degat = (int) (((k1 * this.getLevel() + 2) * a.getPuissance() * (double) statDegat / (k2 * statProtection) + 2 ) * stab * type );
			m.PV-=degat;


			if (m.getPV()<=0) {
				throw new PVException();
			}
			else {
				System.out.println("Il reste "+m.getPV()+" PV a "+m.getClass().getSimpleName()+".\n");
			}
		}
	}


	//	Doublon action combat pour le front
	public Action combat(Monster m,int id) throws PVException {

		Attaque a = listAttaque.parallelStream().filter(atk -> atk.getId() == id).findFirst().get();	
		Random r = new Random();
		Action action = new Action();

		if (r.nextInt(100)>a.getPrecision()) {
			System.out.println("L'attaque de "+this.getClass().getSimpleName()+" a raté !");
			action.setM(m);
			action.setMessage("L'attaque de "+this.getClass().getSimpleName()+" a raté !");
		}
		else {

			//set les paramettres de calcul des dégâts
			final double k1 = (double) 2/5;
			final double k2 = 50;

			//set le bonus de stab
			double stab = 1.0;
			if (a.getType().equals(this.getType())) {
				stab = 1.5;
			}

			//set si l'attaque utilisée est efficace ou non
			double type = Context.getInstance().getDaoAttaque().ratioEfficacite(a.getType(),m.getType());
			if (type == 2) {
				System.out.println("L'attaque est super efficace !");
				action.setMessage("L'attaque est super efficace !");
			}
			if (type == 0.5) {
				System.out.println("L'attaque est peu efficace ...");
				action.setMessage("L'attaque est peu efficace ...");
			}

			//dedermine si l'attaque est physique ou spéciale
			int statDegat = 0;
			int statProtection = 0;
			switch (a.getEtat()) {
			case "Physique": statDegat=this.Atk ; statProtection=m.getDef(); break;
			case "Special" : statDegat=this.ASp ; statProtection=m.getDSp(); break;
			default : System.out.println("erreur de degat");break;
			}

			//calcul des dégats
			int degat = (int) (((k1 * this.getLevel() + 2) * a.getPuissance() * (double) statDegat / (k2 * statProtection) + 2 ) * stab * type );
			m.PV-=degat;

			
			if (m.getPV()<=0) {
				m.setPV(0);
				action.setM(m);
				throw new PVException();
				
			}
			else {
				action.setM(m);
				System.out.println("Il reste "+m.getPV()+" PV a "+m.getClass().getSimpleName()+".\n");
				
			}
		}
		return action;
	}







	@Override
	public String toString() {
		return "Monster [level=" + level + ", PV=" + PV + ", Atk=" + Atk + ", Def=" + Def + ", ASp=" + ASp + ", DSp="
				+ DSp + ", Vit=" + Vit + ", tabNature=" + Arrays.toString(tabNature) + ", listAttaque=" + listAttaque
				+ ", type=" + type + ", equipeJoueur=" + equipeJoueur.values() + "]";
	}



	public String toString2() {
		return "- "+nom+" ["+type+"] : Attaques = "+listAttaque.stream().map( a -> a.getNom()).collect(Collectors.joining(", "));
	}






}
