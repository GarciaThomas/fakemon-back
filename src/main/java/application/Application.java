
package application;

import java.util.ArrayList;
import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import config.fakemonConfig;
import dao.IDAOAttaque;
import dao.IDAOMonster;
import model.Dresseur;
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
					m1.selectionAttaqueCombat(m2);
					System.out.println(m2.getNom()+" attaque "+m1.getNom());
					m2.selectionAttaqueCombat(m1);
				}
				else {
					System.out.println(m2.getNom()+" attaque "+m1.getNom()+" en premier");
					m2.selectionAttaqueCombat(m1);
					System.out.println(m1.getNom()+" attaque "+m2.getNom());
					m1.selectionAttaqueCombat(m2);
				}
			}
		}
		catch (PVException e) {System.err.println(e);
		if (Player.getInstance().getEquipePlayer().getFirst().getPV()<=0 && Player.getInstance().checkEquipeJoueur()) {
			Player.getInstance().changeMonsterActif(1);
			combat (Player.getInstance().getEquipePlayer().getFirst(), m2);
		}
		/*		else if (Dresseur.
				getEquipeDresseur().getFirst().getPV()<=0 && Player.getInstance().checkEquipeJoueur()) {
			combat (m1, dresseur.getEquipeDresseur().getSuivant);
		}*/
		}
	}

	/** Permet de lancer plusieurs combat contre des monstres sauvages générés aléatoirement
	 * Pour le moment, le nombre de rencontres est parametré de base avec 10 en entré
	 * Les monstres rencontrés sont : 5 x niveau 1, 3 x niveau 2 (le 6eme+) et 2 x niveau 3(le 9eme+)
	 * Il y a de l'affichage dans la console
	 * @param nbSauvage : int ; nombre de créatures sauvages rencontrées d'affillées
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

	/**	Grosse méthode de combat avec les dresseurs : l'arène
	 * 
	 * @param nbDresseurIntermediaires int ; nombre de dresseurs intermédiaire, c'est a dire en dehors du premier et dernier dresseur qui eux sont fixes
	 */
	public static void arene(int nbDresseurIntermediaires) {

		System.out.println("Bienvenue dans l'arène ! Préparez-vous à affronter des adversaires de plus en plus corriace.");
		int pts = 35;

		Dresseur d = new Dresseur("FragileJordan",pts);
		System.out.println("Premier duel d'échauffement contre FragileJordan.");
		System.out.println(d.toStringEquipe());
		combat(Player.getInstance().getEquipePlayer().getFirst(),d.getEquipeDresseur().getFirst());
		for (Monster m : d.getEquipeDresseur()) {
			pts+=m.getExpGain();
		}
		pts=(int)(pts*1.08);

		for (int i = 0;i<nbDresseurIntermediaires;i++) {
			d = new Dresseur(pts);
			System.out.println("Duel numéro "+(i+1)+" contre "+d.getNom()+".");
			System.out.println(d.toStringEquipe());
			combat(Player.getInstance().getEquipePlayer().getFirst(),d.getEquipeDresseur().getFirst());
			for (Monster m : d.getEquipeDresseur()) {
				pts+=m.getExpGain();
			}
			pts=(int)(pts*1.08);
		}

		d = new Dresseur("BlackJordan",(int)(pts*1.1574));
		System.out.println("Dernier duel contre le maître BlackJordan.");
		System.out.println(d.toStringEquipe());
		combat(Player.getInstance().getEquipePlayer().getFirst(),d.getEquipeDresseur().getFirst());

		System.out.println("Bravo l'arène est finie !");

	}




	public static void main(String[] args) {	

		System.out.println("Version actuelle 27-07-2020. V6");


		Player.getInstance().selectionStarter();
		rencontreSauvage(10);
		arene(0);

	}

}
