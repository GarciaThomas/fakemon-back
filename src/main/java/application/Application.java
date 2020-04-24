
package application;

import java.util.ArrayList;
import java.util.Scanner;

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

		ArrayList<Monster> fakemonSauvage = new ArrayList<Monster>();
		fakemonSauvage = Player.getInstance().tableRencontre(nbSauvage);

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
			combat(Player.getInstance().getEquipePlayer().getFirst(),m);
			Player.getInstance().soinEquipeJoueur();
		}
	}


	public static void main(String[] args) {

		Player.getInstance().selectionStarter();
		rencontreSauvage(10);
		

	}

}
