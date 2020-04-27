package model;

import java.util.LinkedList;
import java.util.Random;
import java.util.stream.Collectors;

public class Dresseur {
	protected LinkedList<Monster> equipeDresseur = new LinkedList<Monster>(); 
	String nom;

	//	Constructeurs : vide pour nom al�atoire ou donne un nom pour le fixer
	//	Le nombre de points d'exp�rience de base obtenus par le joueur pendant la phase de rencontre sauvage est de 48 points
	public Dresseur(int pts) {
		this.equipeDresseur = choixEquipeDresseur(pts);
		this.nom = choixNom();
	}

	public Dresseur(String nom, int pts) {
		this.equipeDresseur = choixEquipeDresseur(pts);
		this.nom = nom;
	}


	//	Getters et Setters
	public String getNom() {
		return nom;
	}
	public LinkedList<Monster> getEquipeDresseur() {
		return equipeDresseur;
	}


	/**	Choix al�atoire d'un nom pour le dresseur
	 * @return String ; Donne un nom au hasard dans la liste
	 **/
	private String choixNom() {
		Random r=new Random();
		String[] listeNom = {"Jean-loup","Giseline","Drogo","Vlad","Pueblo","Okko","Jean-Denis","Krugg","Sir Jaime","Saint-Paulin","Regis","Pr Cerizi�", "Lord I"};
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


	/** Cr�ation de l'�quipe du dresseur � partir d'un certain nombre de points donn�s	
	 * @param pts int ; Nombre de points d'exp�rience disponibles pour la cr�ation de l'�quipe
	 * @return LinkedList<Monster> ; L'�quipe du dresseur
	 **/
	private LinkedList<Monster> choixEquipeDresseur(int pts) {

		this.equipeDresseur.add(Player.getInstance().tableRencontre(1).get(0));
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
				this.equipeDresseur.add(Player.getInstance().tableRencontre(1).get(0));
				pts-=3;		//	Co�t d'un lv 1 : 3pts d'exp�rence (valeur du kill)
				//		System.out.println("ajout nouveau monstre :"+equipeDresseur.getLast().getNom()+". pts = "+pts);
			}
			else {
				pts=0;
				//		System.out.println("fin de la Creation : pts = "+pts);
			}
		}
		return equipeDresseur;
	}


	/**	V�rifie et renvoie "true" s'il reste dans l'�quipe du Dresseur un fakemon capable de se battre
	 * Il faut qu'apr�s le prochain monstre en �tat soit envoy� pour continuer le combat
	 * @return boolean ; "true" pour que le combat puisse continuer, "false" sinon
	 **/
	public boolean checkEquipeDresseur() {
		boolean reponse = false;
		for (Monster m : equipeDresseur) {
			if (m.getPV()>0) {
				reponse = true;
			}
		}
		return reponse;
	}


	
	
	
	
	
	
	// faire une fonction qui renvoie le prochain monstre non ko de la liste de montre
	// fait un fonction qui permet de r�cup le desseur � partir de monstre !!!
	
	
	
	
	
	

	@Override
	public String toString() {
		return "Dresseur [nom=" + nom + ", equipeDresseur=" + equipeDresseur + "]";
	}

	public String toString2() {
		return "Dresseur "+nom+"\n�quipe : " + equipeDresseur.stream().map( m -> m.getNom()+", niveau "+m.getLevel()).collect(Collectors.joining("\n\t "));
	}

	public String toStringEquipe() {
		return "Son �quipe est constitu� de :\n\t" + equipeDresseur.stream().map( m -> m.getNom()+", niveau "+m.getLevel()).collect(Collectors.joining("\n\t "));
	}
}





