
package application;

import java.util.ArrayList;
import java.util.Scanner;

import model.Monster;
import model.PVException;
import model.Player;

public class Application {

	/**	Fonction qui permet la saisie console d'un int. 
	 * Pas de v�rification que l'entr�e est correcte
	 * @param msg : String ; message qui sera affich� dans la console
	 * @return
	 **/
	public static int saisieInt(String msg) {
		Scanner sc = new Scanner(System.in);
		System.out.println(msg);
		int i = sc.nextInt();
		return i;
	}

/** Fonction qui va lancer le combat : calcul quel monstre � l'initiative (fonction appel�e) puis passe � la phase de combat (fonction appel�e) dans le bon ordre.
 * La sortie d'une PVexception dans la fonction combat apppel�e signifie que l'un des deux monstre au combat est KO
 * @param m1 : Monster ; le monstre du joueur, c�d le premier de sa liste au d�but du combat puis celui actif lors des tours suivants (si KO ou switch)
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
 * pour le moment le nombre de rencontre est parametr� de base � 10 en entr�e
 * les monstres rencontr�s sont 5 x niveau 1, 3 x niveau 2 (le 6�me+) et 2 x niveau 3(le 9�me+)
 * il y a de l'affichage dans la console
 * @param nbSauvage : int ; nombre de creatures sauvages recontr�e d'affill�es
 **/
	public static void rencontreSauvage(int nbSauvage) {

		System.out.println("Vous allez rencontrer "+nbSauvage+" Fakemon sauvages.");
		Monster m = null;	

		ArrayList<Monster> fakemonSauvage = new ArrayList<Monster>();
		fakemonSauvage = Player.getInstance().tableRencontre(nbSauvage);

		for(int i=0;i<nbSauvage;i++) {
			System.out.println("\n---------\nRencontre n�"+(i+1)+" :");
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

		Player.getInstance().selectionStarter();
		rencontreSauvage(10);
		
	}

}
