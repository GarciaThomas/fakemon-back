package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import static application.Application.saisieInt;
import static application.Application.saisieString;
import service.ContextService;

//	Déclaration Attribut
@Entity
@Table(name = "fakemon_stats")
public class Monster {
	//Stats de l'état actuel du fakemon
	@Transient
	protected int level; 
	@Transient
	protected int pv;
	@Transient
	protected int pvMax;
	@Transient
	protected int atk;	
	@Transient
	protected int def;
	@Transient
	protected int aSp;	
	@Transient
	protected int dSp;
	@Transient
	protected int vit;

	//Pour le calcul des stats : base commune de l'espèce
	@JsonIgnore
	@Column (name = "pv", nullable = false)
	protected double basePV;

	@JsonIgnore
	@Column (name = "attaque", nullable = false)
	protected double baseAtk;	

	@JsonIgnore
	@Column (name = "defense", nullable = false)
	protected double baseDef;

	@JsonIgnore
	@Column (name = "atk_speciale", nullable = false)
	protected double baseASp;

	@JsonIgnore
	@Column (name = "def_speciale", nullable = false)
	protected double baseDSp;

	@JsonIgnore
	@Column (name = "vitesse", nullable = false)
	protected double baseVit;

	//Pour le calcul des stats : stats aléatoire de ce fakemon précis
	@JsonIgnore
	@Transient
	protected double ivPV;
	@JsonIgnore
	@Transient
	protected double ivAtk;
	@JsonIgnore
	@Transient
	protected double ivDef;
	@JsonIgnore
	@Transient
	protected double ivASp;
	@JsonIgnore
	@Transient
	protected double ivDSp;
	@JsonIgnore
	@Transient
	protected double ivVit;

	//Pour le calcul des stats : effet de la nature
	@JsonIgnore
	@Transient
	protected double[] tabNature={1,1,1,1,1,1};

	//Pour le combat : modificateurs des statistiques
	@JsonIgnore
	@Transient
	protected double modifAtk=1;
	@JsonIgnore
	@Transient	
	protected double modifDef=1;
	@JsonIgnore
	@Transient
	protected double modifASp=1;
	@JsonIgnore
	@Transient
	protected double modifDSp=1;
	@JsonIgnore
	@Transient
	protected double modifVit=1;

	//Autres attributs importants
	@Transient
	protected ArrayList<Attaque> listAttaque = new ArrayList<Attaque>();

	@JsonIgnore
	@Transient
	protected Situation equipeJoueur=Situation.valueOf("Sauvage");
	@Transient
	protected int exp = 0;
	@Transient
	protected int expNextLevel = 5;

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

	@Transient
	UUID uniqueId = UUID.randomUUID();


	@Transient
	private ContextService ctxtsvc;


	/* 	Stats non-utiles pour le moment : futures implementation ?
	 * 	Mana ? Remplace les PP des attaques
	 *	protected int modifEsquive;
	 *	protected int modifPrécision;
	 *	protected int modifCritique;
	 */


	//___________________________________________
	//	Constructeur


	/** Constructeur pour JPA avec initialisation à partir de la BDD
	 * L'ajout des attaques ne fonctionne pas dans ce construteur, probablement pas un effet de timing
	 * Donc l'ajout des attaques se réalise dans une autre méthode : init()
	 */
	public Monster() {
		this.level = 1;
		generationIV();	
		nature();
		calcStat();
	}

	/** Initilisation des attaques du monstre en dehors du constructeur car bug avec JPA
	 * Cette fonction est appellée après la construction de l'objet (PostLoad)
	 */
	//	@PostLoad
	//	public void init() {
	//		this.listAttaque = ctxtsvc.creationAttaque(poolAtkStringToInt(poolAtkString));
	//	}


	//___________________________________________
	//	Getters/Setters

	public void setEquipeDresseur() {
		equipeJoueur=Situation.valueOf("Adversaire");
	}

	public int getId() {
		return id;
	}
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


	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getPvMax() {
		return pvMax;
	}

	public void setPvMax(int pvMax) {
		this.pvMax = pvMax;
	}

	public int getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}


	public int getaSp() {
		return aSp;
	}

	public void setaSp(int aSp) {
		this.aSp = aSp;
	}

	public int getdSp() {
		return dSp;
	}

	public void setdSp(int dSp) {
		this.dSp = dSp;
	}

	public int getDef() {
		return def;
	}

	public int getVit() {
		return vit;
	}

	public Type getType() {
		return type;
	}
	public void setEquipeJoueur() {
		equipeJoueur=Situation.valueOf("Joueur");
	}
	@JsonIgnore
	public Situation getSituation() {
		return equipeJoueur;
	}
	public String getNom(){
		return this.nom;
	}

	public ArrayList<Attaque> getListAttaque() {
		return listAttaque;	
	}

	public int getExpNextLevel () {
		return expNextLevel;
	}

	public UUID getUniqueId() {
		return uniqueId;
	}

	@JsonIgnore
	public int getExpGain() {
		int c= (2 + (int)(level + Math.pow(level,2)) /2);
		return c;
	}
	@JsonIgnore
	public double getModifAtk() {
		return modifAtk;
	}
	@JsonIgnore
	public void setModifAtk(double modifAtk) {
		this.modifAtk = modifAtk;
	}
	@JsonIgnore
	public double getModifDef() {
		return modifDef;
	}
	@JsonIgnore
	public void setModifDef(double modifDef) {
		this.modifDef = modifDef;
	}
	@JsonIgnore
	public double getModifASp() {
		return modifASp;
	}
	@JsonIgnore
	public void setModifASp(double modifASp) {
		this.modifASp = modifASp;
	}
	@JsonIgnore
	public double getModifDSp() {
		return modifDSp;
	}
	@JsonIgnore
	public void setModifDSp(double modifDSp) {
		this.modifDSp = modifDSp;
	}
	@JsonIgnore
	public double getModifVit() {
		return modifVit;
	}
	@JsonIgnore
	public void setModifVit(double modifVit) {
		this.modifVit = modifVit;
	}
	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}
	public void setContextService(ContextService ctxtsvc) {
		this.ctxtsvc=ctxtsvc;
	}

	//___________________________________________
	//	Méthodes
	public void setListAttaque(ArrayList<Attaque> listAttaque) {
		this.listAttaque = listAttaque;
	}

	/**	Défini les IV (les statistiques cachées) du fakemon
	 *  N'est appellée que dans le constructeur et à aucun autre moment pour ne pas modifier ces valeurs en cours de route
	 */
	@JsonIgnore
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


	/**	Génére une nature (fictivement car elle n'est pas réellement définie) qui module 2 statistiques du monstre
	 * N'est appellée que dans le constructeur et à aucun autre moment pour ne pas modifier ces valeurs en cours de route
	 */
	@JsonIgnore
	private void nature() {
		Random r=new Random();
		int stUp=r.nextInt(6);
		r=new Random();
		int stDown=r.nextInt(6);
		if (stUp!=stDown) {
			tabNature[stUp]=1.1;
			tabNature[stDown]=0.9;
		}	
	}




	/** Converti à partir du movepool en String issu de la base de donnée la liste d'Integer necessaire pour les requêtes
	 * @param movepool String ; contenu dans la base de donnée avec les stats des fakemons
	 * @return 
	 */
	@JsonIgnore
	public Integer[] poolAtkStringToInt(String movepool) {

		String[] ids = movepool.split(",");
		Integer[] poolEntier = new Integer[ids.length];

		for(int i = 0; i < ids.length; i++){
			poolEntier[i]=Integer.valueOf(ids[i]);
		}	
		return poolEntier;
	}


	/**	Calcule les nouvelles stats du niveau actuel et met à jour les points de vie du monstre
	 * 	Utilisée dans constructeur et aussi dans levelUp()
	 **/
	@JsonIgnore
	public void calcStat() {
		final double cstLv = (double) 1/5;
		pvMax = (int) (10+level+((basePV+ivPV)*tabNature[0]*(level*cstLv)));
		pv = pvMax;
		atk = (int) (5+((baseAtk+ivAtk)*tabNature[1]*(level*cstLv)));	
		def = (int) (5+((baseDef+ivDef)*tabNature[2]*(level*cstLv)));
		aSp = (int) (5+((baseASp+ivASp)*tabNature[3]*(level*cstLv)));	
		dSp = (int) (5+((baseDSp+ivDSp)*tabNature[4]*(level*cstLv)));
		vit = (int) (5+((baseVit+ivVit)*tabNature[5]*(level*cstLv)));
	}


	/** Fait prendre un niveau et recalcule les statistiques
	 * Aucune sortie dans la console
	 */
	@JsonIgnore
	public void levelUp() {
		level++;
		expNextLevel=( (int) (7*this.getLevel() + Math.pow(this.getLevel(),2) )/2 )+1;
		calcStat();

		//--------------

		//--> ouvre un slot d'attaque!
		if (level==5) {

			//	Récupère nouvelle attaque et l'ajoute au moves du monstre
			listAttaque.add(this.newAttaque(ctxtsvc));
			if (this.equipeJoueur.equals(Situation.valueOf("Joueur"))) {
				System.out.println(this.nom+" à appris un nouveau move : "+listAttaque.get(3).getNom());
			}
		}

		//	-> propose trois nouvelle attaque en remplacement d'une actuelle
		if ( (level == 3 || level == 5 || level == 8 || level == 10) && this.equipeJoueur.equals(Situation.valueOf("Joueur"))) {
			System.out.println(this.getNom()+" peut remplacer une de ses attaque par l'une de ces attaque :");
			List<Attaque> proposition = new ArrayList<>();
			Attaque a = this.newAttaque(ctxtsvc);
			proposition.add(a);
			boolean b;

			while (proposition.size()<3) {
				a =  this.newAttaque(ctxtsvc);
				b = true;
				for (Attaque atk : proposition) {
					if (atk.getId() == a.getId()) {
						b = false;
					}
				}
				if (b) {
					proposition.add(a);
				}
			}
			System.out.println(proposition.stream().map(atk -> atk.toStringDetailAttaque()).collect(Collectors.joining("")));
			remplacementMove(0, 0, proposition, 0);
		}
	}


	/**
	 * 
	 * @param idMoveOublie
	 * @param idMoveAppris
	 * @param proposition
	 */
	@JsonIgnore
	private void remplacementMove(int idMoveOublie, int idMoveAppris, List<Attaque> proposition, int ouiOuNon) {

		boolean b = false;
		if (ouiOuNon == 0) {
			String sc = saisieString("\nVoulez-vous remplacer une attaque existante ? (Y : oui / N : non)");
			switch (sc) {
			case "Y" : b=true;break;
			case "N" : System.out.println("Pas de remplacement de move");break;
			default : System.out.println("Mauvaise saisie, veuillez recommencer");remplacementMove(0, 0, proposition, 0);break;
			}
		}

		if (b) {
			while (idMoveAppris<1) {
				int sc = saisieInt("Quelle move voulez-vous apprendre ? (1 à 3) : ");
				idMoveAppris=proposition.get(sc-1).getId();
			}

			while (idMoveOublie<1) {
				this.toStringDetailAttaque();
				int sc = saisieInt("\nQuelle move voulez-vous oublier ? (1 à 3) : ");
				idMoveOublie=this.getListAttaque().get(sc-1).getId();
			}

			int ocnzi = idMoveOublie;
			Attaque moveOublie = this.getListAttaque().stream().filter(atk -> atk.getId() == ocnzi ).findFirst().get();
			int dzdzd = idMoveAppris;
			Attaque moveAppris = proposition.stream().filter(atk -> atk.getId() == dzdzd).findFirst().get();
			this.getListAttaque().add(this.getListAttaque().indexOf(moveOublie), moveAppris);
			this.getListAttaque().remove(moveOublie);
			System.out.println("le move "+moveAppris.getNom()+" a remplacé le move "+moveOublie.getNom());
		}
	}


	/** Gére le gain d'exp pour le monstre en cours et fait le levelUp si besoin
	 * 	Texte indicatif dans console
	 * @param m Monster ; Monstre qui a été mis KO par le monstre actuel du joueur
	 */
	@JsonIgnore
	public void expGain(Monster m) {

		exp += m.getExpGain();
		if (exp>=expNextLevel) {
			System.out.println("Gain de niveau !");
			exp-=expNextLevel;
			System.out.println(this.getNom()+" est maintenant niveau "+(this.getLevel()+1)+" !");
			levelUp();
			System.out.println(this.toStringDetailStat());
		}
		System.out.println("Il reste "+(expNextLevel-exp)+" points d'expérience avant le niveau suivant\n");
	}



	@JsonIgnore
	public Attaque choixAttaque() {
		this.listAttaque.forEach(a -> System.out.println("- "+a.getNom()+" ["+a.getType()+", "+a.getEtat()+"] : Puissance = "+a.getPuissance()+", Precision = "+a.getPrecision()));
		int sc = saisieInt("Quelle attaque ? (1 � "+this.listAttaque.size()+")");

		while ( sc < 1 || sc > this.listAttaque.size() ) {
			System.out.println("Mauvaise saisie. Veuillez recommencer");
			sc = saisieInt("Quelle attaque ? (1 � "+this.listAttaque.size()+")");
		}
		
		return listAttaque.get(sc-1);
	}


	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public Attaque newAttaque(ContextService ctxtsvc) {

		//		Récupère et converti la liste de tous les moves dans une List<>  
		Integer[] listIdTotal = this.poolAtkStringToInt(this.getPoolAtkString());
		List<Integer> listeFormate = Arrays.asList(listIdTotal);

		//	Retire les id des attaques déjà connues. Ne fonctionne pas avec remove()
		for (Attaque a : this.listAttaque) {
			listeFormate = listeFormate.stream().filter(m -> m != a.getId()).collect(Collectors.toList());
		}

		//	Récupère id de la nouvelle attaque
		Random r = new Random();
		int idNewMove =listeFormate.get(r.nextInt(listeFormate.size()));

		return ctxtsvc.getAttaqueid(idNewMove);
	}


	/** Compare les deux vitesses des monstres en combat pour déterminer le plus rapide
	 * 	Si les deux monstres ont la même vitesse, réalise un 50/50 pour choisir le plus rapide
	 * @param m2 Monster ; Il s'agit du monstre adverse, qui n'appartient pas au Player
	 * @return Monster ; le monstre étant le plus rapide
	 */
	@JsonIgnore
	public Monster initiative(Monster m2) {

		Monster m = null;

		if (this.getVit()*this.modifVit > m2.getVit()*m2.getModifVit()) {
			m = this;
		}
		else if (this.getVit()*this.modifVit < m2.getVit()*m2.getModifVit()) {
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

	/**	Permet d'initialiser le combat dans la console
	 * Si c'est un monstre de l'équipe joueur, on utilise la méthode pour sélectionné son attaque
	 * Si c'est un monstre sauvage ou de dresseur, on utilise la méthode automatique
	 * @param m Monster ; Il s'agit du monstre adverse. Le monstre qui lance attaque est le "this"
	 * @throws PVException
	 */
	@JsonIgnore
	public void selectionAttaqueCombat(Monster m, ContextService ctxtsvc) throws PVException {
		//		Boolean qui permet soit au joueur de choisir son attaque, soit à l'IA de le faire
		this.ctxtsvc = ctxtsvc;
		Attaque a = (equipeJoueur.equals(Situation.valueOf("Joueur"))) ? choixAttaque() : choixAttaqueBOT(m, ctxtsvc);	
		combat(m,a.getId() , ctxtsvc);

	}
	@JsonIgnore
	public Attaque choixAttaqueBOT(Monster m) {
		return choixAttaqueBOT(m,null);
	}
	@JsonIgnore
	public Attaque choixAttaqueBOT(Monster m, ContextService ctxtsvc) {

		Random r = new Random();
		Attaque a = listAttaque.get(r.nextInt(this.listAttaque.size()));

		for (Attaque i : listAttaque) {
			if ( ctxtsvc.getRatioEfficacite(i,m) == 2 ) {
				r = new Random();
				if( r.nextInt(4) == 0 ) {
					a=i;
				}
			}
		}
		return a;
	}
	@JsonIgnore
	public ArrayList<Attaque> poolAttaque(ArrayList<Integer> ids) {

		this.listAttaque = ctxtsvc.poolAttaque(ids);
		return listAttaque;
	}


	/**	Calcule des dégâts et update les PV des monstres en fonction des différents paramettre : constantes, stab, efficacité, statistiques physiques ou spéciales
	 * 	Le premier test est de vérifier si l'attaque touche l'adversaire
	 * @param m : Monster ; Le monstre adverse qui vas se prendre l'attaque du monstre présent.
	 * @param idMove : int ; cet id est celui de l'attaque sélectionné dans une méthode précédente (ou par le formulaire en Front)
	 * @throws PVException : cette exception est renvoyée lorsque l'un des deux monstre ne peux plus se battre !
	 */
	@JsonIgnore
	public Action combat(Monster m, int idMove, ContextService ctxtsvc) throws PVException {

		this.ctxtsvc = ctxtsvc;
	/*	System.out.println(listAttaque);
		listAttaque.parallelStream().forEach(a -> System.out.println(a.getId()+" : "+a));*/
		Attaque a = listAttaque.parallelStream().filter(atk -> atk.getId() == idMove).findFirst().get();
		Random r = new Random();
		Action action = new Action();
		action.setM(m);
		action.append(m.nom+" utilise "+a.nom);
		System.out.println(m.nom+" utilise "+a.nom);

		if (r.nextInt(100)+1>a.getPrecision()) {
			System.out.println("L'attaque de "+this.getNom()+" a ratée !");
			action.setMessage("L'attaque de "+this.getNom()+" a ratée !");
		}
		else {
			//	set les paramettres de calcul des dégâts
			final double k1 = (double) 2/5;
			final double k2 = 50;

			//	set le bonus de stab
			double stab = 1.0;
			if (a.getType().equals(this.getType())) {
				stab = 1.5;
			}


			//	set si l'attaque utilis�e est efficace ou non
			double type = ctxtsvc.getRatioEfficacite(a,m);
			if (type == 2) {
				System.out.println("L'attaque est super efficace !");
				action.setMessage("L'attaque est super efficace !");
			}
			if (type == 0.5) {
				System.out.println("L'attaque est peu efficace ...");
				action.setMessage("L'attaque est peu efficace ...");
			}

			//	détermine si l'attaque est physique ou spéciale
			double statDegat = 0;
			double statProtection = 0;
			switch (a.getEtat()) {
			case "Physique": statDegat=this.atk*this.modifAtk ; statProtection=m.getDef()*m.getModifDef(); break;
			case "Special" : statDegat=this.aSp*this.modifASp ; statProtection=m.getdSp()*m.getModifDSp(); break;
			case "Statut" : break;
			default : System.out.println("erreur de dégât");break;
			}

			//	calcul des dégats
			int degat = (int) (((k1 * this.getLevel() + 2) * a.getPuissance() * (double) statDegat / (k2 * statProtection) + 2 ) * stab * type );
			m.pv-=degat;

			integrationEffetCumule(a, m);

			if (m.getPv()<=0) {
				if (this.equipeJoueur.equals(Situation.valueOf("Joueur"))) {
					this.expGain(m);
				}
				throw new PVException(m);
			}
			else {
				System.out.println("Il reste "+m.getPv()+" PV (/"+m.getPvMax()+") a "+m.getNom()+".\n");
				action.setM(m);
			}
		}
		return action;
	}



	/** Prise en compte des effets cumulé de l'attaque utilisée
	 *  Pour le moment un seul effet cumulé par attaque
	 *  Effet cumulé sur la modification temporaire de statistique ou des pv
	 */
	@JsonIgnore
	public void integrationEffetCumule(Attaque a, Monster m) throws PVException {
		if (a.getEffetCumule() != null) {

			//	Transforme le string de la BDD en liste, les infos sont organisé en Proba,Cible,Statistique,Sens,Cran
			String[] listeEffetCumule = a.getEffetCumule().split(",");
			Random r = new Random();
			Monster cible = null;

			if (r.nextInt(100)+1 < Integer.parseInt(listeEffetCumule[0])) {

				switch (listeEffetCumule[1]) {
				case "self" : cible = this; break;
				case "other" : cible = m; break;
				default : System.out.println("erreur de cible"); break;
				}

				switch (listeEffetCumule[2]) {
				case "pv" : modifPVCombat(listeEffetCumule[3], listeEffetCumule[4], cible);
				System.out.println("Les points de vie de "+cible.getNom()+" ont été "+listeEffetCumule[3]+" de "+listeEffetCumule[4]+" %"); break;
				case "atk" : cible.setModifAtk(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifAtk())); 
				System.out.println("L'attaque de "+cible.getNom()+" a été "+listeEffetCumule[3]+" de "+listeEffetCumule[4]+" cran"); break;
				case "def" : cible.setModifDef(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifDef())); 
				System.out.println("La défense de "+cible.getNom()+" a été "+listeEffetCumule[3]+" de "+listeEffetCumule[4]+" cran"); break;
				case "asp" : cible.setModifASp(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifASp())); 
				System.out.println("L'attaque spéciale de "+cible.getNom()+" a été "+listeEffetCumule[3]+" de "+listeEffetCumule[4]+" cran"); break;
				case "dsp" : cible.setModifDSp(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifDSp())); 
				System.out.println("La défense spéciale de "+cible.getNom()+" a été "+listeEffetCumule[3]+" de "+listeEffetCumule[4]+" cran"); break;
				case "vit" : cible.setModifVit(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifVit())); 
				System.out.println("La vitesse de "+cible.getNom()+" a été "+listeEffetCumule[3]+" de "+listeEffetCumule[4]+" cran"); break;
				case "all" : cible.setModifAtk(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifAtk())); 
				cible.setModifDef(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifDef()));
				cible.setModifASp(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifASp()));
				cible.setModifDSp(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifDSp()));
				cible.setModifVit(modifStatCombat(listeEffetCumule[3], listeEffetCumule[4], cible.getModifVit()));  
				System.out.println("Toutes les statistiques de "+cible.getNom()+" a été "+listeEffetCumule[3]+" de "+listeEffetCumule[4]+" cran");break;
				default : System.out.println("erreur de stat"); break;
				}
			}
		}
	}


	/** Methode qui met a jour la modif de stat du monstre actuel
	 * 
	 * @param sens : "up" ou "down" selon si la stat doit augmentée ou diminuée
	 * @param valeur : nombre de rang d'évolution, donné en string car converti en int à l'intérieur
	 * @param valeurModifActuelle : valeur de la modifStat à modifiée
	 * @return nouvelle valeur de la modification de statistique en combat
	 */
	@JsonIgnore
	private double modifStatCombat(String sens, String valeur, double valeurModifActuelle) {

		// Array de 13 valeurs avec valeur de base en position 6 
		double[] modifStats = {0.25, (double) 2/7, (double) 2/6, 0.4, 0.5, (double) 2/3, 1, 1.5, 2, 2.5, 3, 3.5, 4};

		int position = 0;
		int i = 0;

		for (double v : modifStats) {
			if (v == valeurModifActuelle) {
				position = i;
			}
			i++;
		}

		double newModif = 1;

		try  {
			switch (sens) {
			case "up" : newModif=modifStats[position+Integer.parseInt(valeur)]; break;
			case "down" : newModif=modifStats[position-Integer.parseInt(valeur)];break;
			default : System.out.println("Problem de sens"); break;
			}
		}catch (Exception e) {e.printStackTrace();
		if (position+Integer.parseInt(valeur)>13) {newModif = 4;}
		else if (position-Integer.parseInt(valeur)<0) {newModif = 0.25;}
		}

		return newModif;
	}

	@JsonIgnore
	private void modifPVCombat(String sens, String valeur, Monster cible) throws PVException {

		double ratio = 1;
		if (sens.equals("up")) {
			ratio = (double) Integer.parseInt(valeur)/100;	
		}
		else if (sens.equals("down")) {
			ratio = (double) -Integer.parseInt(valeur)/100;	
		}
		else {
			System.out.println("Problème de sens aux modifPV");
		}

		cible.setPv((int) (cible.getPv() + cible.getPvMax() * ratio));

		if (cible.getPv()>cible.getPvMax()) {
			cible.setPv(cible.getPvMax());
		}
		else if (cible.getPv()<=0) {
			throw new PVException(cible); 
		}
	}

	@Override
	@JsonIgnore
	public String toString() {
		return "Monster [level=" + level + ", PV=" + pv + ", Atk=" + atk + ", Def=" + def + ", ASp=" + aSp + ", DSp="
				+ dSp + ", Vit=" + vit + ", tabNature=" + Arrays.toString(tabNature) + ", listAttaque=" + listAttaque
				+ ", type=" + type + ", equipeJoueur=" + equipeJoueur.values() + "]";
	}

	@JsonIgnore
	public String toStringGeneralPV() {
		return "- "+nom+" ["+type+"] (PV = "+pv+"/"+pvMax+"): Attaques = "+listAttaque.stream().map( a -> a.getNom()).collect(Collectors.joining(", "));
	}
	@JsonIgnore
	public String toStringGeneral() {
		return "- "+nom+" ["+type+"] : Attaques = "+listAttaque.stream().map( a -> a.getNom()).collect(Collectors.joining(", "));
	}
	@JsonIgnore
	public String toStringDetailAttaque() {
		return "\n* "+listAttaque.stream().map( a -> a.getNom()+" ["+a.getType().toString()+", "+a.getEtat()+"] : Puissance = "+a.getPuissance()+", Precision = "+a.getPrecision()+" ("+a.getDescription()+")").collect(Collectors.joining("\n* "));
	}
	@JsonIgnore
	public String toStringDetailStat() {

		return "Niveau = " + level + ", Point de Vie = " + pv + ", Attaque = " + atk + ", D�fense = " + def + ", Attaque Spéciale = " + aSp + ", Défense Spéciale = " + dSp + ", Vitesse = " + vit + ", tabNature = " + Arrays.toString(tabNature);

	}


}
