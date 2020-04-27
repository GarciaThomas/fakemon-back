
package application;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import model.Context;
import model.Monster;
import model.PVException;
import model.Player;

public class Application {

	/**	Fonction qui permet la saisie console d'un int. 
	 * Pas de verification que l'entree est correcte
	 * @param msg : String ; message qui sera affiche dans la console
	 * @return
	 **/
	public static int saisieInt(String msg) {
		Scanner sc = new Scanner(System.in);
		System.out.println(msg);
		int i = sc.nextInt();
		return i;
	}

	/** Fonction qui va lancer le combat : calcul quel monstre a l'initiative (fonction appelee) puis passe a la phase de combat (fonction appelee) dans le bon ordre.
	 * La sortie d'une PVexception dans la fonction combat apppelee signifie que l'un des deux monstre au combat est KO
	 * @param m1 : Monster ; le monstre du joueur, cad le premier de sa liste au debut du combat puis celui actif lors des tours suivants (si KO ou switch)
	 * @param m2 : Monster ; Le monstre sauvage ou du dresseur adverse
	 **/
	public static void combat(Monster m1, Monster m2){

		try {
			while (m1.getPV()>0 && m2.getPV()>0) {
				if (m1.initiative(m2).equals(m1)) {
					System.out.println(m1.getNom()+" attaque "+m2.getNom()+" en premier");
					m1.combat(m2);
					System.out.println(m2.getNom()+" attaque "+m1.getNom());
					m2.combat(m1);
				}
				else {
					System.out.println(m2.getNom()+" attaque "+m1.getNom()+" en premier");
					m2.combat(m1);
					System.out.println(m1.getNom()+" attaque "+m2.getNom());
					m1.combat(m2);
				}
			}
		}
		catch (PVException e) {System.err.println(e);}
	}

	/** permet de lancer plusieurs combat contre des monstres sauvages
	 * pour le moment le nombre de rencontre est parametre de base e 10 en entree
	 * les monstres rencontres sont 5 x niveau 1, 3 x niveau 2 (le 6eme+) et 2 x niveau 3(le 9eme+)
	 * il y a de l'affichage dans la console
	 * @param nbSauvage : int ; nombre de creatures sauvages recontree d'affillees
	 **/
	public static void rencontreSauvage(int nbSauvage) {

		System.out.println("Vous allez rencontrer "+nbSauvage+" Fakemon sauvages.");
		Monster m = null;	

		ArrayList<Monster> fakemonSauvage = new ArrayList<Monster>();
		fakemonSauvage = Player.getInstance().tableRencontre(nbSauvage);

		for(int i=0;i<nbSauvage;i++) {
			System.out.println("\n---------\nRencontre n°"+(i+1)+" :");
			m = fakemonSauvage.get(i);

			if (i>=8) {
				m.levelUp();
				m.levelUp();
			}
			else if (i>=5) {
				m.levelUp();
			}

			System.out.println("Vous allez combatre un "+m.getNom()+" sauvage de niveau "+m.getLevel()+".");
			combat(Player.getInstance().getEquipePlayer().getFirst(),m);
			Player.getInstance().soinEquipeJoueur();
		}
	}


	public static void main(String[] args) {
		
		System.out.println("Version actuelle 27-07-2020. V5");
		Player.getInstance().selectionStarter();
		rencontreSauvage(10);


	}

}
