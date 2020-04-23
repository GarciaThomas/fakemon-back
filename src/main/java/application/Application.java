
package application;

import java.util.ArrayList;
import java.util.Scanner;

import model.Context;
import model.Monster;
import model.PVException;
import model.Player;

public class Application {

	public static int saisieInt(String msg) {
		Scanner sc = new Scanner(System.in);
		System.out.println(msg);
		int i = sc.nextInt();
		return i;
	}



	public static void combat(Monster m1, Monster m2){
			
		try {
			while (m1.getPV()>0 && m2.getPV()>0) {
				if (m1.initiative(m2).equals(m1)) {
					System.out.println(m1.getClass().getSimpleName()+" attaque "+m2.getClass().getSimpleName()+" en premier");
					m1.combat(m2);
					System.out.println(m2.getClass().getSimpleName()+" attaque "+m1.getClass().getSimpleName());
					m2.combat(m1);
				}
				else {
					System.out.println(m2.getClass().getSimpleName()+" attaque "+m1.getClass().getSimpleName()+" en premier");
					m2.combat(m1);
					System.out.println(m1.getClass().getSimpleName()+" attaque "+m2.getClass().getSimpleName());
					m1.combat(m2);
				}
			}
		}
		catch (PVException e) {System.err.println(e);}
	}


	public static void rencontreSauvage(int nbSauvage) {

		System.out.println("Vous allez rencontrer "+nbSauvage+" Fakemon sauvages.");
		Monster m = null;	

		Player sacha = null;
		ArrayList<Monster> fakemonSauvage = new ArrayList<Monster>();
		fakemonSauvage = sacha.getInstance().tableRencontre(nbSauvage, 1);

		for(int i=0;i<nbSauvage;i++) {
			System.out.println("Rencontre n°"+(i+1)+" :");
			m = fakemonSauvage.get(i);

			if (i>=9) {
				m.levelUp();				
			}
			else if (i>=5) {
				m.levelUp();
			}
			
			System.out.println("Vous allez combatre un "+m.getNom()+" sauvage de niveau "+m.getLevel()+".");
			combat(sacha.getInstance().getEquipePlayer().getFirst(),m);
			sacha.getInstance().soinEquipeJoueur();
		}
	}


	public static void main(String[] args) {

		Player sacha = null;
		sacha.getInstance().selectionStarter();
		rencontreSauvage(10);
		

		/*	Renargile c2 = new Renargile(1);
		Thymtamarre c1 = new Thymtamarre(1);
		c1.setEquipeJoueur();

		System.out.println(c1.toString());
		System.out.println(c2.toString());

		System.out.println("Votre "+c1.getClass().getSimpleName()+" ["+c1.getPV()+"PV] va se battre contre un "+c2.getClass().getSimpleName()+" adverse ["+c2.getPV()+"PV]");
		combat(c1,c2);
		 */
	}

}
