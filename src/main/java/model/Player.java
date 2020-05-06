package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.persistence.PostLoad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.Application;
import service.ContextService;

@Service
public class Player { //Singleton.
	//	Attributs
	@Autowired
	private Context context;
	
	@Autowired
	private ContextService srvContext;
	
	
	protected LinkedList<Monster> equipePlayer = new LinkedList<Monster>();
	protected ArrayList<Monster> starters = new ArrayList<Monster>();
	protected int[] position = new int[] {0,0};
	private int maxRencontre = 10;
	private int cptRencontre = 0;


	//	Constructeur Singleton
	private Player() {
	}

	//______________________


	//	Getters Setters et apparentés
	public LinkedList<Monster> getEquipePlayer() {
		return equipePlayer;
	}
	public void addEquipePlayer(Monster m) {
//		System.out.println("Call");
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


	/**Revoie une liste de monstres de niveau 1 crée aléatoirement
	 * @param nbRencontre int ; le nombre de monstres souhaités
	 * @return ArrayList<Monster> ; La liste des monstres aléatoire va rencontrer
	 **/
	public ArrayList<Monster> tableRencontre(int nbRencontre) {

		ArrayList<Monster> tableRencontre = new ArrayList<Monster>();
		//	ArrayList<Monster> tableCreation = new ArrayList<Monster>();
		Monster m = null;

		for (int i=0;i<nbRencontre;i++) {

			//-----------
			Random r = new Random();
			int choixMonstre = r.nextInt(this.context.getDaoMonster().countNombreMonstre());
			m = this.context.getDaoMonster().findById(choixMonstre+1).get();
			
			m.setListAttaque(srvContext.creationAttaque(m.poolAtkStringToInt(m.getPoolAtkString())));
			
			tableRencontre.add(m);
			//-----------			
			/*		
			Monster pipeau = this.context.getDaoMonster().selectByNom("Pipeau");
			Monster crameleon = this.context.getDaoMonster().selectByNom("Crameleon");
			Monster foufoudre = this.context.getDaoMonster().selectByNom("Foufoudre");
			Monster renargile = this.context.getDaoMonster().selectByNom("Renargile");
			Monster bebesalt = this.context.getDaoMonster().selectByNom("Bebesalt");
			Monster thymtamarre = this.context.getDaoMonster().selectByNom("Thymtamarre");

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


	/**	Crée une sélection aléatoire de six monstres puis le joueur doit en choisir un comme monstre de départ
	 **/
	public void selectionStarter () {

		ArrayList<Monster> table2Chen = tableRencontre(6);
		table2Chen.forEach(mi -> System.out.println(mi.toStringGeneral()));
		int i=0;
		while (i<1 || i>6) {
			i = Application.saisieInt("Quel Fakemon souhaitez-vous comme starter ? (1 à 6)");
		}
		addEquipePlayer(table2Chen.get(i-1));
		System.out.println("Vous avez choisi "+table2Chen.get(i-1).getNom()+" !");
		System.out.println("Ses moves sont : "+table2Chen.get(i-1).toStringDetailAttaque());
		System.out.println("Ses statistiques sont : "+table2Chen.get(i-1).toStringDetailStat());
	}


	/** Remet tout les monstres du joueur en sitation quiescente (PV, modifsStats...), par exemple après un combat 
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


	/**	échange la place de deux monstres de l'équipe du joueur
	 * Version avec scanner pour le back
	 **/
	public void changeMonster() {
		equipePlayer.forEach(m -> System.out.println(m.toStringGeneral()));
		int im = Application.saisieInt("Quel monstre voulez-vous changer de position ?");
		int ip = Application.saisieInt("À quelle position voulez-vous le mettre ?");
		Monster m = equipePlayer.get(im-1);
		equipePlayer.set(im-1, equipePlayer.get(ip-1));
		equipePlayer.set(ip-1, m);
	}

	
	/**	Échange la place de deux monstres de l'équipe du joueur
	 * Version avec les deux index en entrée pour le front, aucune vérification des monstres
	 * @param position1 int ; Index du premier monstre
	 * @param position2 int ; Index du second monstre
	 **/
	public void changeMonster(int position1, int position2) {
		Monster m = equipePlayer.get(position1-1);
		equipePlayer.set(position1-1, equipePlayer.get(position2-1));
		equipePlayer.set(position2-1, m);
	}
	
	
	/**	Remplace le monstre dit "actif" (celui en première position de la liste de monstre du joueur) par un autre qui peux se battre
	 * il y a des vérification sur ce swap et le reset des modifStat
	 * @param i int ; Index du monstre souhaité en remplacement du monstre actif
	 **/
	public void changeMonsterActif(int i) {
		while (equipePlayer.get(i-1).getPV()<=0) {
			i= Application.saisieInt("Le monstre sélectionné est hors-combat. Veuillez en sélectionner un autre");
		}
		equipePlayer.getFirst().setModifAtk(0);
		equipePlayer.getFirst().setModifDef(0);
		equipePlayer.getFirst().setModifASp(0);
		equipePlayer.getFirst().setModifDSp(0);
		equipePlayer.getFirst().setModifVit(0);
		changeMonster(0, i);
	}
	

	/**	Vérifie et renvoie "true" s'il reste dans l'équipe du joueur un monstre capable de se battre 
	 * Il faut qu'après le joueur selectionne un fakemon compatible pour continuer le combat
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
	
	//	Vérifie si le monstre est capturable et réalise le test de capture. Si la capture réussie ajoute le monstre à l'équipe du joueur.
	public void captureMonstre(Monster m) {

		if (m.equipeJoueur.equals(Situation.valueOf("Sauvage"))) {

			System.out.println("Tentative de capture du "+m.getNom()+" sauvage");
			double txCap = 1;
			if ((double) (m.getPV()/m.getPVmax()) <= 0.05) {
				txCap = 4;
			}
			else if ((double) (m.getPV()/m.getPVmax()) <= 0.15) {
				txCap = 3;
			}
			else if ((double) (m.getPV()/m.getPVmax()) <= 0.25) {
				txCap = 2.5;
			}
			else if ((double) (m.getPV()/m.getPVmax()) <= 0.5) {
				txCap = 2;
			}
			else if ((double) (m.getPV()/m.getPVmax()) <= 0.75) {
				txCap = 1.5;
			}

			int captureRate = (int) (2 * txCap * (21-m.getLevel()));
			Random r = new Random();
			if (r.nextInt(100)+1>captureRate) {
				System.out.println("La capture de "+m.getNom()+" a échouée");
			}
			else {
				System.out.println("La capture de "+m.getNom()+" a réussi !");
				this.addEquipePlayer(m);
			}

		}
		else {
			System.out.println("Le monstre adverse n'est pas capturable");
		}
	}

	public Action captureMonstreFront(Monster m) {
		Action a = new Action();

		if (m.equipeJoueur.equals(Situation.valueOf("Sauvage"))) {

			System.out.println("Tentative de capture du "+m.getNom()+" sauvage");
			double txCap = 1;
			if ((double) (m.getPV()/m.getPVmax()) <= 0.05) {
				txCap = 4;
			}
			else if ((double) (m.getPV()/m.getPVmax()) <= 0.15) {
				txCap = 3;
			}
			else if ((double) (m.getPV()/m.getPVmax()) <= 0.25) {
				txCap = 2.5;
			}
			else if ((double) (m.getPV()/m.getPVmax()) <= 0.5) {
				txCap = 2;
			}
			else if ((double) (m.getPV()/m.getPVmax()) <= 0.75) {
				txCap = 1.5;
			}

			int captureRate = (int) (2 * txCap * (21-m.getLevel()));
			Random r = new Random();
			if (r.nextInt(100)+1>captureRate) {

				a.setMessage("La capture de "+m.getNom()+" a échouée");
			}
			else {
				a.setM(m);
				a.setMessage("La capture de "+m.getNom()+" a réussie !");
				this.addEquipePlayer(m);
			}

		}
		else {
			a.setMessage("Le monstre adverse n'est pas capturable");
		}
		return a;
	}
}
