package model;

import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import service.PlayerService;

public class Dresseur {
	protected LinkedList<Monster> equipeDresseur = new LinkedList<Monster>(); 
	String nom;
	UUID uniqueId = UUID.randomUUID();

	@Autowired
	private PlayerService player;



	//	Constructeurs : vide pour nom aléatoire ou donne un nom pour le fixer
	//	Le nombre de points d'expérience de base obtenus par le joueur pendant la phase de rencontre sauvage est de 48 points
	public Dresseur(int pts, PlayerService player) {
		this.equipeDresseur = choixEquipeDresseur(pts, player);
		this.nom = choixNom();
		this.player = player;
	}

	public Dresseur(String nom, int pts, PlayerService player) {
		this.equipeDresseur = choixEquipeDresseur(pts, player);
		this.nom = nom;
		this.player = player;
	}


	//	Getters et Setters
	public String getNom() {
		return nom;
	}
	public LinkedList<Monster> getEquipeDresseur() {
		return equipeDresseur;
	}
	
	

	public UUID getUniqueId() {
		return uniqueId;
	}


	/**	Choix aléatoire d'un nom pour le dresseur
	 * @return String ; Donne un nom au hasard dans la liste
	 **/
	private String choixNom() {
		Random r=new Random();
		String[] listeNom = {"Jean-loup","Giseline","Drogo","Vlad","Pueblo","Okko","Jean-Denis","Krugg","Sir Jaime","Saint-Paulin","Regis","Pr Cerizié", "Lord I"};
		return listeNom[r.nextInt(listeNom.length)];
	}


	/** Renvoie le monstre au niveau le plus faible dans la liste des monstres du dresseur
	 * @return Monster ; Le monstre qui a le niveau le plus faible de la liste du dresseur
	 **/
	private Monster lePlusFaible() {
		Monster leNul = equipeDresseur.getFirst();
		for (Monster m : equipeDresseur) {
			if (leNul.getLevel()>m.getLevel()) {
				leNul=m;
			}
		}
		return leNul;
	}


	/** Création de l'équipe du dresseur à partir d'un certain nombre de points donnés	
	 * @param pts int ; Nombre de points d'expérience disponibles pour la création de l'équipe
	 * @return LinkedList<Monster> ; L'équipe du dresseur
	 **/
	private LinkedList<Monster> choixEquipeDresseur(int pts, PlayerService player) {

		Monster m = player.tableRencontre(1).get(0);
		m.setEquipeDresseur();
		this.equipeDresseur.add(m);
		Random r = new Random();

		while ( (pts>=3 && equipeDresseur.size()<6) || pts>=lePlusFaible().getExpNextLevel() ) {

			int p = r.nextInt(5);

			if (p<equipeDresseur.size() && pts>=lePlusFaible().getExpNextLevel()) {		//	Si le random est plus petit que le nombre de creature
				if (pts>=equipeDresseur.get(p).getExpNextLevel()) {
					pts-=equipeDresseur.get(p).getExpNextLevel();			
					equipeDresseur.get(p).levelUp();
					//			System.out.println("Levelup monstre choisi:"+equipeDresseur.get(p).getNom()+". pts = "+pts);
				}
				else {
					pts-=lePlusFaible().expNextLevel;
					lePlusFaible().levelUp();
					//			System.out.println("Levelup monstre faible:"+lePlusFaible().getNom()+". pts = "+pts);
				}
			}
			else if (pts>=3 && equipeDresseur.size()<6) {
				m = player.tableRencontre(1).get(0);
				m.setEquipeDresseur();
				this.equipeDresseur.add(m);
				pts-=3;		//	Coût d'un lv 1 : 3pts d'expérence (valeur du kill)
				//		System.out.println("ajout nouveau monstre :"+equipeDresseur.getLast().getNom()+". pts = "+pts);
			}
			else {
				pts=0;
				//		System.out.println("fin de la Creation : pts = "+pts);
			}
		}
		return equipeDresseur;
	}


	/**	Vérifie et renvoie "true" s'il reste dans l'équipe du Dresseur un fakemon capable de se battre
	 * Il faut qu'aprés le prochain monstre en état soit envoyé pour continuer le combat
	 * @return boolean ; "true" pour que le combat puisse continuer, "false" sinon
	 **/
	public boolean checkEquipeDresseur() {
		boolean reponse = false;
		for (Monster m : equipeDresseur) {
			if (m.getPv()>0) {
				reponse = true;
			}
		}
		return reponse;
	}








	/** Change l'ordre de la liste de monstre
	 * Le monstre en première position devient le premier monstre non-KO de la liste actuelle
	 **/
	public void fakemonSuivant() {
		boolean b = true; 
		int i = 0; int index = 0; 
		Monster firstM = null;
		Monster mSwitch = this.equipeDresseur.getFirst();
		equipeDresseur.removeFirst();
		for (Monster m : equipeDresseur) {
			i++;
			if (m.getPv() > 0 && b) {
				firstM = m;
				index = i;
				b = false;
			}
		}
		equipeDresseur.addFirst(firstM);
		equipeDresseur.set(index, mSwitch);
	}







	@Override
	public String toString() {
		return "Dresseur [nom=" + nom + ", equipeDresseur=" + equipeDresseur + "]";
	}

	public String toString2() {
		return "Dresseur "+nom+"\néquipe : " + equipeDresseur.stream().map( m -> m.getNom()+", niveau "+m.getLevel()).collect(Collectors.joining("\n\t "));
	}

	public String toStringEquipe() {
		return "Son équipe est constitué de :\n\t" + equipeDresseur.stream().map( m -> m.getNom()+", niveau "+m.getLevel()).collect(Collectors.joining("\n\t "));
	}


}





