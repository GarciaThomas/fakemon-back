package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.query.criteria.internal.expression.function.LengthFunction;

import application.Application;

//	D�claration Attribut
@Entity
@Table(name = "fakemon_stats")
public class Monster {
	//Stats de l'�tat actuel du fakemon
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

	//Pour le calcul des stats : base commune de l'esp�ce
	@Column (name = "pv", nullable = false)
	protected double basePV;

	@Column (name = "attaque", nullable = false)
	protected double baseAtk;	

	@Column (name = "defense", nullable = false)
	protected double baseDef;

	@Column (name = "atk_speciale", nullable = false)
	protected double baseASp;

	@Column (name = "def_speciale", nullable = false)
	protected double baseDSp;

	@Column (name = "vitesse", nullable = false)
	protected double baseVit;

	//Pour le calcul des stats : stats al�atoire de ce fakemon pr�cis
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

	//Pour le combat : modificateurs. Pour le moment pas de moyen de les modifier MAIS important � avoir pour futures am�liorations du syst�me de combat
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

	@Column (name ="espece", length = 15, nullable = false, insertable = false, updatable = false)
	protected String nom;

	@Column(name = "movepool", nullable = false)
	private String poolAtkString;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;


	/* 	Stats non-utiles pour le moment : futures implementation ?
	 * 	Mana ? Remplace les PP des attaques
	 *	protected int modifEsquive;
	 *	protected int modifPr�cision;
	 *	protected int modifCritique;
	 */


	//___________________________________________
	//	Constructeur

	public Monster(int level, double basePV, double baseAtk, double baseDef,double baseASp,double baseDSp, double baseVit, String poolAtkString,Type type){
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
		this.listAttaque = creationAttaque(poolAtkStringToInt(poolAtkString));
	}
	
	public Monster() {
		this.level = 1;
		generationIV();	
		nature();
		calcStat();
	}
	public void init() {
		this.listAttaque = creationAttaque(poolAtkStringToInt(poolAtkString));
	}
	

	//___________________________________________
	//	Getters/Setters
	public String getPoolAtkString() {
		return poolAtkString;
	}
	public void setPoolAtkString(String poolAtkString) {
		this.poolAtkString = poolAtkString;
	}
	public void setLevel(int level) {
		this.level = level;
	}
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
	//	M�thodes
	//	D�fini les IV du fakemon : ne doit �tre utilis� que dans le constructeur
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
	}

	//	G�n�re une nature qui module 2 statistiques du monstre : ne doit �tre utilis� que dans le constructeur 
	private void nature() {
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

	//	G�n�re � partir du movepool (=la totalit� des attaques que peut apprendre le fakemon) les 3 attaques que le fakemon aura � sa disposition
	protected static ArrayList<Attaque> creationAttaque(Integer[] poolEntier) {

		LinkedList<Integer> mesIds = new LinkedList<Integer>();
		mesIds.addAll(Arrays.asList(poolEntier));
		Collections.shuffle(mesIds);

		ArrayList<Integer> idsForQuery = new ArrayList<Integer>();

		for(int i=0; i < 3; i++) {
			idsForQuery.add(mesIds.poll());
		}
		return Context.getInstance().getDaoAttaque().selectPoolId(idsForQuery);

	}

	//	G�n�re � partir du movepool en string de la base de donn�e la liste d'Integer
	private Integer[] poolAtkStringToInt(String movepool) {

		String[] ids = movepool.split(",");
		Integer[] poolEntier = new Integer[ids.length];

		for(int i = 0; i < ids.length; i++){
			poolEntier[i]=Integer.valueOf(ids[i]);
		}	
		return poolEntier;
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


	//	G�re le gain d'exp pour le monstre en cours et qui fait le levelUp si besoin
	public void expGain(Monster m) {

		exp += m.getExpGain();
		if (exp>=expNextLevel) {
			exp-=expNextLevel;
			levelUp();
		}

	}


	//	V�rifie si le monstre est capturable et r�alise le test de capture. Si la capture r�ussie ajoute le monstre � l'�quipe du joueur.
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
				System.out.println("La capture de "+this.getNom()+" a �chou�e");
			}
			else {
				System.out.println("La capture de "+this.getNom()+" a r�ussi !");
				Player.getInstance().addEquipePlayer(this);
			}

		}
		else {
			System.out.println("Le monstre adverse n'est pas capturable");
		}
	}


	public Attaque choixAttaque() {
		this.listAttaque.forEach(a -> System.out.println("- "+a.getNom()+" ["+a.getType()+", "+a.getEtat()+"] : Puissance = "+a.getPuissance()+", Precision = "+a.getPrecision()));
		int sc = Application.saisieInt("Quelle attaque ? (1 � 3)");

		Attaque a=null;
		switch (sc) {
		case 1 : a = listAttaque.get(0);break;
		case 2 : a = listAttaque.get(1);break;
		case 3 : a = listAttaque.get(2);break;
		//		case 4 : a = listAttaque.get(3);break;  � utiliser que si on d�cide d'utiliser 4 slots d'attaques
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
		//		case 4 : a = listAttaque.get(3);break;  � utiliser que si on d�cide d'utiliser 4 slots d'attaques
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


	//	Calcul des d�g�ts et renvoie erreur si PV tombe � 0
	public void combat(Monster m) throws PVException {

		Attaque a = (equipeJoueur.equals(Situation.valueOf("Joueur"))) ? choixAttaque() : choixAttaqueBOT(m);	

		Random r = new Random();

		if (r.nextInt(100)>a.getPrecision()) {
			System.out.println("L'attaque de "+this.getNom()+" a rat� !");
		}
		else {

			//set les paramettres de calcul des d�g�ts
			final double k1 = (double) 2/5;
			final double k2 = 50;

			//set le bonus de stab
			double stab = 1.0;
			if (a.getType().equals(this.getType())) {
				stab = 1.5;
			}

			//set si l'attaque utilis�e est efficace ou non
			double type = Context.getInstance().getDaoAttaque().ratioEfficacite(a.getType(),m.getType());
			if (type == 2) {
				System.out.println("L'attaque est super efficace !");
			}
			if (type == 0.5) {
				System.out.println("L'attaque est peu efficace ...");
			}

			//dedermine si l'attaque est physique ou sp�ciale
			int statDegat = 0;
			int statProtection = 0;
			switch (a.getEtat()) {
			case "Physique": statDegat=this.Atk ; statProtection=m.getDef(); break;
			case "Special" : statDegat=this.ASp ; statProtection=m.getDSp(); break;
			default : System.out.println("erreur de degat");break;
			}

			//calcul des d�gats
			int degat = (int) (((k1 * this.getLevel() + 2) * a.getPuissance() * (double) statDegat / (k2 * statProtection) + 2 ) * stab * type );
			m.PV-=degat;


			if (m.getPV()<=0) {
				throw new PVException();
			}
			else {
				System.out.println("Il reste "+m.getPV()+" PV a "+m.getNom()+".\n");
			}
		}
	}


	//	Doublon action combat pour le front
	public Action combat(Monster m,int id) throws PVException {

		Attaque a = listAttaque.parallelStream().filter(atk -> atk.getId() == id).findFirst().get();	
		Random r = new Random();
		Action action = new Action();

		if (r.nextInt(100)>a.getPrecision()) {
			System.out.println("L'attaque de "+this.getNom()+" a rat� !");
			action.setM(m);
			action.setMessage("L'attaque de "+this.getNom()+" a rat� !");
		}
		else {

			//set les paramettres de calcul des d�g�ts
			final double k1 = (double) 2/5;
			final double k2 = 50;

			//set le bonus de stab
			double stab = 1.0;
			if (a.getType().equals(this.getType())) {
				stab = 1.5;
			}

			//set si l'attaque utilis�e est efficace ou non
			double type = Context.getInstance().getDaoAttaque().ratioEfficacite(a.getType(),m.getType());
			if (type == 2) {
				System.out.println("L'attaque est super efficace !");
				action.setMessage("L'attaque est super efficace !");
			}
			if (type == 0.5) {
				System.out.println("L'attaque est peu efficace ...");
				action.setMessage("L'attaque est peu efficace ...");
			}

			//dedermine si l'attaque est physique ou sp�ciale
			int statDegat = 0;
			int statProtection = 0;
			switch (a.getEtat()) {
			case "Physique": statDegat=this.Atk ; statProtection=m.getDef(); break;
			case "Special" : statDegat=this.ASp ; statProtection=m.getDSp(); break;
			default : System.out.println("erreur de degat");break;
			}

			//calcul des d�gats
			int degat = (int) (((k1 * this.getLevel() + 2) * a.getPuissance() * (double) statDegat / (k2 * statProtection) + 2 ) * stab * type );
			m.PV-=degat;


			if (m.getPV()<=0) {
				m.setPV(0);
				action.setM(m);
				throw new PVException();

			}
			else {
				action.setM(m);
				System.out.println("Il reste "+m.getPV()+" PV a "+m.getNom()+".\n");

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



	public String toStringGeneral() {
		return "- "+nom+" ["+type+"] : Attaques = "+listAttaque.stream().map( a -> a.getNom()).collect(Collectors.joining(", "));
	}

	public String toStringDetailAttaque() {
		return "\n* "+listAttaque.stream().map( a -> a.getNom()+" ["+a.getType().toString()+", "+a.getEtat()+"] : Puissance = "+a.getPuissance()+", Precision = "+a.getPrecision()).collect(Collectors.joining("\n* "));
	}

	public String toStringDetailStat() {
		return "Niveau =" + level + ", Point de Vie = " + PV + ", Attaque = " + Atk + ", D�fense = " + Def + ", Attaque Sp�ciale = " + ASp + ", D�fense Sp�ciale = " + DSp + ", Vitesse = " + Vit + ", tabNature = " + Arrays.toString(tabNature);
	}


}
