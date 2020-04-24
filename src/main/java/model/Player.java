package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import application.Application;
import model.Monster;
import model.creature.Bebesalt;
import model.creature.Crameleon;
import model.creature.Foufoudre;
import model.creature.Pipeau;
import model.creature.Renargile;
import model.creature.Thymtamarre;

public class Player { //Singleton.
	//	Attributs
	private static Player _instance = null;
	protected LinkedList<Monster> equipePlayer = new LinkedList<Monster>();
	protected ArrayList<Monster> starters = new ArrayList<Monster>();
	protected int[] position = new int[] {0,0};

	//	Constructeur Singleton
	private Player() {
	}

	//	Méthodes
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


	//	Revoie une liste de nbRencontre monstres crée aléatoirement et de niveau donné.
	public ArrayList<Monster> tableRencontre(int nbRencontre, int niveau) {

		ArrayList<Monster> tableRencontre = new ArrayList<Monster>();
		ArrayList<Monster> tableCreation = new ArrayList<Monster>();

		for (int i=0;i<nbRencontre;i++) {

			Monster test = new Crameleon(niveau);
			tableCreation.add(test);
			tableCreation.add(new Foufoudre(niveau));
			tableCreation.add(new Pipeau(niveau));
			tableCreation.add(new Renargile(niveau));
			tableCreation.add(new Thymtamarre(niveau));
			tableCreation.add(new Bebesalt(niveau));		

			Random r = new Random();
			Monster m = tableCreation.get(r.nextInt(tableCreation.size()));
			tableRencontre.add(m);
			tableCreation.clear();
		}
		return tableRencontre;
	}


	//	Crée une sélection aléatoire de 6 Fakemon puis le joueur en choisis 1 
	public void selectionStarter () {

		ArrayList<Monster> table2Chen = tableRencontre(6, 1);
		table2Chen.forEach(mi -> System.out.println(mi.toString2()));
		int i=0;
		while (i<1 || i>6) {
			i = Application.saisieInt("Quel Fakemon souhaitez-vous comme starter ? (1 à 6)");
		}
		addEquipePlayer(table2Chen.get(i-1));
		System.out.println("Vous avez choisi"+table2Chen.get(i-1).getNom()+" !");
	}

	
	/***
	 * 
	 * Generation liste starter
	 * @return 
	 * 
	 */
	
	public ArrayList<Monster> getStarters() {
		if(starters.isEmpty()) {
			starters = tableRencontre(6, 1);
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
	
	//	Remet tout les PV aux monstres du joueur, par exemple après un combat 
	public void soinEquipeJoueur() {
		for (Monster m : equipePlayer) {
			m.setPV(m.getPVmax());
		}
	}


	//	Change de place deux monstres du joueur dans sa liste de monstre
	public void changeMonster() {
		equipePlayer.forEach(m -> System.out.println(m.toString2()));
		int im = Application.saisieInt("Quel monstre voulez-vous changer de position ?");
		int ip = Application.saisieInt("À quelle position voulez-vous le mettre ?");
		Monster m = equipePlayer.get(im-1);
		equipePlayer.set(im-1, equipePlayer.get(ip-1));
		equipePlayer.set(ip-1, m);
		
	}


	//	Vérifie et renvoie true s'il reste dans l'équipe du joueur un fakemon capable de se battre. ils faut qu'après le joueur selectionne un fakemon compatible pour continuer le combat
	public boolean checkEquipeJoueur() {
		boolean reponse = false;
				for (Monster m : equipePlayer) {
					if (m.getPV()>0) {
						reponse = true;
					}
				}
		return reponse;
	}



}
