package model;

import java.util.LinkedList;
import java.util.Random;
import java.util.stream.Collectors;

public class Dresseur {
	protected LinkedList<Monster> equipeDresseur = new LinkedList<Monster>(); 
	String nom;

	//	Constructeurs : vide pour nom aléatoire ou donne un nom pour le fixer
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


	//	Choix aléatoire d'un nom
	private String choixNom() {
		Random r=new Random();
		String[] listeNom = {"Jean-loup","Giseline","Drogo","Vlad","Pueblo","Okko","Jean-Denis","Krugg","Sir Jaime","Saint-Paulin","Regis","Pr Cerizié"};
		return listeNom[r.nextInt(listeNom.length-1)];
	}


	//	Renvoie le monstre au niveau le plus faible
	private Monster lePlusFaible() {
		Monster leNul = equipeDresseur.getFirst();
		for (Monster m : equipeDresseur) {
			if (leNul.getLevel()>m.getLevel()) {
				leNul=m;
			}
		}
		return leNul;
	}


	//	Création de l'équipe du joueur à partir d'un certain nombre de points donnés
	private LinkedList<Monster> choixEquipeDresseur(int pts) {

		this.equipeDresseur.add(Player.getInstance().tableRencontre(1,1).get(0));
		Random r = new Random();

		while ( (pts>=3 && equipeDresseur.size()<6) || pts>=lePlusFaible().getExpNextLevel() ) {

			int p = r.nextInt(5);

			if (p<equipeDresseur.size() && pts>=lePlusFaible().getExpNextLevel()) {		//	Si le random est plus petit que le nombre de creature
				if (pts>=equipeDresseur.get(p).getExpNextLevel()) {
					pts-=equipeDresseur.get(p).getExpNextLevel();			
					equipeDresseur.get(p).levelUp();
					System.out.println("Levelup monstre choisi:"+equipeDresseur.get(p).getNom()+". pts = "+pts);
				}
				else {
					pts-=lePlusFaible().expNextLevel;
					lePlusFaible().levelUp();
					System.out.println("Levelup monstre faible:"+lePlusFaible().getNom()+". pts = "+pts);
				}
			}
			else if (pts>=3 && equipeDresseur.size()<6) {
				this.equipeDresseur.add(Player.getInstance().tableRencontre(1,1).get(0));
				pts-=3;		//	Coût d'un lv 1 : 3pts d'expérence (valeur du kill)
				System.out.println("ajout nouveau monstre :"+equipeDresseur.getLast().getNom()+". pts = "+pts);
			}
			else {
				pts=0;
				System.out.println("fin de la Creation : pts = "+pts);
			}
		}
		return equipeDresseur;
	}








	//	Vérifie et renvoie true s'il reste dans l'équipe du Dresseur un fakemon capable de se battre. ils faut qu'après le prochain fakemon compatible soit envoyer pour continuer le combat
	public boolean checkEquipeDresseur() {
		boolean reponse = false;
		for (Monster m : equipeDresseur) {
			if (m.getPV()>0) {
				reponse = true;
			}
		}
		return reponse;
	}



	@Override
	public String toString() {
		return "Dresseur [nom=" + nom + ", equipeDresseur=" + equipeDresseur + "]";
	}

	public String toString2() {
		return "Dresseur "+nom+"\nÉquipe : " + equipeDresseur.stream().map( m -> m.getNom()+", niveau "+m.getLevel()).collect(Collectors.joining("\n\t "));
	}

	public String toStringEquipe() {
		return "Son équipe est constitué de :\n\t" + equipeDresseur.stream().map( m -> m.getNom()+", niveau "+m.getLevel()).collect(Collectors.joining("\n\t "));
	}
}





