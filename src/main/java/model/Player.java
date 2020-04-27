package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import application.Application;

public class Player { //Singleton.
	//	Attributs
	private static Player _instance = null;
	protected LinkedList<Monster> equipePlayer = new LinkedList<Monster>();
	protected ArrayList<Monster> starters = new ArrayList<Monster>();
	protected int[] position = new int[] {0,0};
	private int maxRencontre = 10;
	private int cptRencontre = 0;


	//	Constructeur Singleton
	private Player() {
	}

	//______________________
	/**	Methode du singleton
	 * @return Player ; Retourne l'unique instance possible de l'objet Player
	 */
	public static Player getInstance() {
		if(_instance==null) {
			_instance=new Player();
		}
		return _instance;
	}

	//	Getters Setters et apparent�s
	public LinkedList<Monster> getEquipePlayer() {
		return equipePlayer;
	}
	public void addEquipePlayer(Monster m) {
		System.out.println("Call");
		m.setEquipeJoueur();
		equipePlayer.add(m);
	}
	public int[] getPosition() {
		return position;
	}
	public void setPosition(int[] position) {
		this.position = position;
	}
	//______________________________________________________________________________


	/**Revoie une liste de monstres de niveau 1 cr�e al�atoirement
	 * @param nbRencontre int ; le nombre de monstres souhait�s
	 * @return ArrayList<Monster> ; La liste des monstres al�atoire va rencontrer
	 **/
	public ArrayList<Monster> tableRencontre(int nbRencontre) {

		ArrayList<Monster> tableRencontre = new ArrayList<Monster>();
		//	ArrayList<Monster> tableCreation = new ArrayList<Monster>();
		Monster m = null;

		for (int i=0;i<nbRencontre;i++) {

			//-----------
			Random r = new Random();
			int choixMonstre = r.nextInt(Context.getInstance().getDaoMonster().countNombreMonstre());
			m = Context.getInstance().getDaoMonster().selectById(choixMonstre+1);
			tableRencontre.add(m);
			//-----------			
			/*		
			Monster pipeau = Context.getInstance().getDaoMonster().selectByNom("Pipeau");
			Monster crameleon = Context.getInstance().getDaoMonster().selectByNom("Crameleon");
			Monster foufoudre = Context.getInstance().getDaoMonster().selectByNom("Foufoudre");
			Monster renargile = Context.getInstance().getDaoMonster().selectByNom("Renargile");
			Monster bebesalt = Context.getInstance().getDaoMonster().selectByNom("Bebesalt");
			Monster thymtamarre = Context.getInstance().getDaoMonster().selectByNom("Thymtamarre");

			tableCreation.add(pipeau);
			tableCreation.add(crameleon);
			tableCreation.add(foufoudre);
			tableCreation.add(renargile);
			tableCreation.add(bebesalt);
			tableCreation.add(thymtamarre);		

			Random r = new Random();
			m = tableCreation.get(r.nextInt(tableCreation.size()));
			tableRencontre.add(m);
			tableCreation.clear();			
			 */
		}
		return tableRencontre;
	}


	/**	Cr�e une s�lection al�atoire de six monstres puis le joueur doit en choisir un comme monstre de d�part
	 **/
	public void selectionStarter () {

		ArrayList<Monster> table2Chen = tableRencontre(6);
		table2Chen.forEach(mi -> System.out.println(mi.toStringGeneral()));
		int i=0;
		while (i<1 || i>6) {
			i = Application.saisieInt("Quel Fakemon souhaitez-vous comme starter ? (1 � 6)");
		}
		addEquipePlayer(table2Chen.get(i-1));
		System.out.println("Vous avez choisi "+table2Chen.get(i-1).getNom()+" !");
		System.out.println("Ses moves sont : "+table2Chen.get(i-1).toStringDetailAttaque());
		System.out.println("Ses statistiques sont : "+table2Chen.get(i-1).toStringDetailStat());
	}


	/** Remet tout les monstres du joueur en sitation quiescente (PV, modifsStats...), par exemple apr�s un combat 
	 **/
	public void soinEquipeJoueur() {
		for (Monster m : equipePlayer) {
			m.setPV(m.getPVmax());
			m.setModifAtk(1);
			m.setModifDef(1);
			m.setModifASp(1);
			m.setModifDSp(1);
			m.setModifVit(1);
		}
	}


	/**	�change la place de deux monstres de l'�quipe du joueur
	 * Version avec scanner pour le back
	 **/
	public void changeMonster() {
		equipePlayer.forEach(m -> System.out.println(m.toStringGeneral()));
		int im = Application.saisieInt("Quel monstre voulez-vous changer de position ?");
		int ip = Application.saisieInt("� quelle position voulez-vous le mettre ?");
		Monster m = equipePlayer.get(im-1);
		equipePlayer.set(im-1, equipePlayer.get(ip-1));
		equipePlayer.set(ip-1, m);
	}

	
	/**	�change la place de deux monstres de l'�quipe du joueur
	 * Version avec les deux index en entr�e pour le front, aucune v�rification des monstres
	 * @param position1 int ; Index du premier monstre
	 * @param position2 int ; Index du second monstre
	 **/
	public void changeMonster(int position1, int position2) {
		Monster m = equipePlayer.get(position1-1);
		equipePlayer.set(position1-1, equipePlayer.get(position2-1));
		equipePlayer.set(position2-1, m);
	}
	
	
	/**	Remplace le monstre dit "actif" (celui en premi�re position de la liste de monstre du joueur) par un autre qui peux se battre
	 * il y a des v�rification sur ce swap et le reset des modifStat
	 * @param i int ; Index du monstre souhait� en remplacement du monstre actif
	 **/
	public void changeMonsterActif(int i) {
		while (equipePlayer.get(i-1).getPV()<=0) {
			i= Application.saisieInt("Le monstre s�lectionn� est hors-combat. Veuillez en s�lectionner un autre");
		}
		equipePlayer.getFirst().setModifAtk(0);
		equipePlayer.getFirst().setModifDef(0);
		equipePlayer.getFirst().setModifASp(0);
		equipePlayer.getFirst().setModifDSp(0);
		equipePlayer.getFirst().setModifVit(0);
		changeMonster(0, i);
	}
	

	/**	V�rifie et renvoie "true" s'il reste dans l'�quipe du joueur un monstre capable de se battre 
	 * Il faut qu'apr�s le joueur selectionne un fakemon compatible pour continuer le combat
	 * @return boolean ; "true" s'il reste un monstre capable de se battre, "false" sinon
	 **/
	public boolean checkEquipeJoueur() {
		boolean reponse = false;
		for (Monster m : equipePlayer) {
			if (m.getPV()>0) {
				reponse = true;
			}
		}
		return reponse;
	}
	
	public boolean peutRencontrer() {
		return this.cptRencontre <= this.maxRencontre;
	}
	
	/***
	 * 
	 * Generation liste starter
	 * @return 
	 * 
	 */
	public ArrayList<Monster> getStarters() {
		if(starters.isEmpty()) {
			starters = tableRencontre(6);
		return starters;
		}
	}
	/***
	 * 
	 * Selection starter
	 * 
	 */
	public void selectStarter(int index) {
		addEquipePlayer(starters.get(index));
	}
	/**
	 * 
	 * @return
	 **/
	public Monster rencontreSauvage() {
		this.cptRencontre++;
		Monster  m = null;
		if(this.cptRencontre <= this.maxRencontre) {
			m = this.tableRencontre(1).get(0);
			if (this.cptRencontre>=8) {
				m.levelUp();
				m.levelUp();
			}
			else if (this.cptRencontre>=5) {
				m.levelUp();
			}
		}
		return m;
	}

}
