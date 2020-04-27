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
	private int maxRencontre = 10;
	private int cptRencontre = 0;
	private int[] position = {0,0};

	//	Constructeur Singleton
	private Player() {
	}

	//	M�thodes
	//	Methode du singleton
	public static Player getInstance() {
		if(_instance==null) {
			_instance=new Player();
		}
		return _instance;
	}

	//	Getters Setters
	public LinkedList<Monster> getEquipePlayer() {
		return equipePlayer;
	}
	public void addEquipePlayer(Monster m) {
		m.setEquipeJoueur();
		equipePlayer.add(m);
	}
	public int[] getPosition() {
		return position;
	}

	public void setPosition(int[] position) {
		this.position = position;
	}

	//	Revoie une liste de nbRencontre monstres cr�e al�atoirement et de niveau 1.
	public ArrayList<Monster> tableRencontre(int nbRencontre) {

		ArrayList<Monster> tableRencontre = new ArrayList<Monster>();
		ArrayList<Monster> tableCreation = new ArrayList<Monster>();
		Monster m = null;

		for (int i=0;i<nbRencontre;i++) {

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
		}
		return tableRencontre;
	}


	//	Cr�e une s�lection al�atoire de 6 Fakemon puis le joueur en choisis 1 
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

	
	/***
	 * 
	 * Generation liste starter
	 * @return 
	 * 
	 */
	
	public ArrayList<Monster> getStarters() {
		if(starters.isEmpty()) {
			starters = tableRencontre(6);
		}
		return starters;
	}
	
	/***
	 * 
	 * Selection starter
	 * 
	 */
	
	public void selectStarter(int index) {
		addEquipePlayer(starters.get(index));
	}
	
	//	Remet tout les PV aux monstres du joueur, par exemple apr�s un combat 
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


	//	Change de place deux monstres du joueur dans sa liste de monstre
	public void changeMonster() {
		equipePlayer.forEach(m -> System.out.println(m.toStringGeneral()));
		int im = Application.saisieInt("Quel monstre voulez-vous changer de position ?");
		int ip = Application.saisieInt("� quelle position voulez-vous le mettre ?");
		Monster m = equipePlayer.get(im-1);
		equipePlayer.set(im-1, equipePlayer.get(ip-1));
		equipePlayer.set(ip-1, m);
		
	}


	//	V�rifie et renvoie true s'il reste dans l'�quipe du joueur un fakemon capable de se battre. ils faut qu'apr�s le joueur selectionne un fakemon compatible pour continuer le combat
	public boolean checkEquipeJoueur() {
		boolean reponse = false;
				for (Monster m : equipePlayer) {
					if (m.getPV()>0) {
						reponse = true;
					}
				}
		return reponse;
	}
	
	public Monster rencontreSauvage() {
		this.cptRencontre++;
		Monster  m = null;
		if(this.cptRencontre <= this.maxRencontre) {
			m = this.tableRencontre(1).get(0);	
		}
		return m;
	}


}
