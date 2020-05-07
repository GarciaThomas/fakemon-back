
package application;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import config.FakemonConfig;
import model.Dresseur;
import model.Monster;
import model.PVException;
import service.ContextService;
import service.PlayerService;

public class Application {

	@Autowired
	PlayerService player;

	@Autowired
	ContextService ctxtsvc;

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
	public static String saisieString(String msg) {
		Scanner sc = new Scanner(System.in);
		System.out.println(msg);
		String i = sc.next();
		return i;
	}	


	/** Fonction qui va lancer le combat : calcul quel monstre a l'initiative (fonction appelee) puis passe a la phase de combat (fonction appelee) dans le bon ordre.
	 * La sortie d'une PVexception dans la fonction combat apppelee signifie que l'un des deux monstre au combat est KO
	 * @param m1 : Monster ; le monstre du joueur, cad le premier de sa liste au debut du combat puis celui actif lors des tours suivants (si KO ou switch)
	 * @param m2 : Monster ; Le monstre sauvage ou du dresseur adverse
	 **/
	public void combat(Monster m1, Monster m2){

		try {
			while (m1.getPV()>0 && m2.getPV()>0) {
				if (m1.initiative(m2).equals(m1)) {
					System.out.println(m1.getNom()+" attaque "+m2.getNom()+" en premier");
					m1.selectionAttaqueCombat(m2, ctxtsvc);
					System.out.println(m2.getNom()+" attaque "+m1.getNom());
					m2.selectionAttaqueCombat(m1, ctxtsvc);
				}
				else {
					System.out.println(m2.getNom()+" attaque "+m1.getNom()+" en premier");
					m2.selectionAttaqueCombat(m1, ctxtsvc);
					System.out.println(m1.getNom()+" attaque "+m2.getNom());
					m1.selectionAttaqueCombat(m2, ctxtsvc);
				}
			}
		}
		catch (PVException e) {System.err.println(e);
		if (player.getEquipePlayer().getFirst().getPV()<=0 && player.checkEquipeJoueur()) {
			player.changeMonsterActif(1);
			combat (player.getEquipePlayer().getFirst(), m2);
		}
		}
	}

	/** Permet de lancer plusieurs combat contre des monstres sauvages générés aléatoirement
	 * Pour le moment, le nombre de rencontres est parametré de base avec 10 en entré
	 * Les monstres rencontrés sont : 5 x niveau 1, 3 x niveau 2 (le 6eme+) et 2 x niveau 3(le 9eme+)
	 * Il y a de l'affichage dans la console
	 * @param nbSauvage : int ; nombre de créatures sauvages rencontrées d'affillées
	 **/
	public void rencontreSauvage(int nbSauvage) {

		System.out.println("Vous allez rencontrer "+nbSauvage+" Fakemon sauvages.");
		Monster m = null;	

		ArrayList<Monster> fakemonSauvage = new ArrayList<Monster>();
		fakemonSauvage = player.tableRencontre(nbSauvage);

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
			combat(player.getEquipePlayer().getFirst(),m);
			player.soinEquipeJoueur();
		}
	}

	public void combatDresseur(PlayerService player, Dresseur dresseur){
		Monster mPlayer = player.getEquipePlayer().getFirst();
		Monster mDresseur = dresseur.getEquipeDresseur().getFirst();
		try {
			while (mPlayer.getPV()>0 && mDresseur.getPV()>0) {
				if (mPlayer.initiative(mDresseur).equals(mPlayer)) {
					System.out.println("Votre "+mPlayer.getNom()+" attaque le "+mDresseur.getNom()+" adverse en premier");
					mPlayer.selectionAttaqueCombat(mDresseur, ctxtsvc);
					System.out.println("Le "+mDresseur.getNom()+" adverse attaque votre "+mPlayer.getNom());
					mDresseur.selectionAttaqueCombat(mPlayer, ctxtsvc);
				}
				else {
					System.out.println("Le "+mDresseur.getNom()+" adverse attaque votre "+mPlayer.getNom()+" en premier");
					mDresseur.selectionAttaqueCombat(mPlayer, ctxtsvc);
					System.out.println("Votre "+mPlayer.getNom()+" attaque le "+mDresseur.getNom()+" adverse");
					mPlayer.selectionAttaqueCombat(mDresseur, ctxtsvc);
				}
			}
		}
		catch (PVException e) {System.err.println(e);
			if (mPlayer.getPV()<=0 && player.checkEquipeJoueur()) {
				System.out.println("Vous devez changer de Fakemon. Qui voulez-vous envoyer ?");
				player.changeMonsterActif(1);
				combatDresseur(player, dresseur);
			}
			else if (mDresseur.getPV()<=0 && dresseur.checkEquipeDresseur()) {
				System.out.println("Le dresseur adverse change de fakemon.");
				dresseur.fakemonSuivant();
				combatDresseur(player, dresseur);
			}
		}
	}












	/**	Grosse méthode de combat avec les dresseurs : l'arène
	 * 
	 * @param nbDresseurIntermediaires int ; nombre de dresseurs intermédiaire, c'est a dire en dehors du premier et dernier dresseur qui eux sont fixes
	 */
	public void arene(int nbDresseurIntermediaires) {

		System.out.println("Bienvenue dans l'arène ! Préparez-vous à affronter des adversaires de plus en plus corriace.");
		int pts = 35;

		Dresseur d = new Dresseur("FragileJordan", pts, player);
		System.out.println("Premier duel d'échauffement contre FragileJordan.");
		System.out.println(d.toStringEquipe());
		combatDresseur(player, d);
		player.soinEquipeJoueur();
		for (Monster m : d.getEquipeDresseur()) {
			pts+=m.getExpGain();
		}
		pts=(int)(pts*1.08);

		for (int i = 0;i<nbDresseurIntermediaires;i++) {
			d = new Dresseur(pts, player);
			System.out.println("Duel numéro "+(i+1)+" contre "+d.getNom()+".");
			System.out.println(d.toStringEquipe());
			combatDresseur(player, d);
			player.soinEquipeJoueur();
			for (Monster m : d.getEquipeDresseur()) {
				pts+=m.getExpGain();
			}
			pts=(int)(pts*1.08);
		}

		d = new Dresseur("BlackJordan",(int)(pts*1.1574), player);
		System.out.println("Dernier duel contre le maître BlackJordan.");
		System.out.println(d.toStringEquipe());
		combatDresseur(player, d);
		player.soinEquipeJoueur();
		System.out.println("Bravo l'arène est finie !");

	}

	public void run() {
		player.selectionStarter();
		rencontreSauvage(10);
		arene(0);
	}
	
	public void test1() {
		ArrayList<Monster> ltArray = player.tableRencontre(3);
		LinkedList<Monster> ltLinked = new LinkedList<>();
		ltLinked.addAll(ltArray);
		player.setEquipePlayer(ltLinked);
		for (Monster m : player.getEquipePlayer()) {
			m.setContextService(ctxtsvc);
			m.levelUp();
			m.levelUp();
			m.levelUp();
		}
		arene(0);
	}
	public void test() {
		ArrayList<Monster> ltArray = player.tableRencontre(3);
		LinkedList<Monster> ltLinked = new LinkedList<>();
		ltLinked.addAll(ltArray);
		player.setEquipePlayer(ltLinked);
		for (Monster m : player.getEquipePlayer()) {
			m.setContextService(ctxtsvc);
			m.levelUp();
			m.levelUp();
			m.levelUp();
		}
		player.getEquipePlayer().getFirst().setPV(0);
		player.changeMonsterActif(1);
		for (Monster m : player.getEquipePlayer()) {
			System.out.println(m.toStringGeneral());
		}
	}

	public static void main(String[] args) {	
		AnnotationConfigApplicationContext monContext = new AnnotationConfigApplicationContext(FakemonConfig.class);
		monContext.getBeanFactory().createBean(Application.class).test1();

		/*	Monster m = player.tableRencontre(1).get(0);
		System.out.println(m.toStringGeneral()+"\n-----------------------");
		System.out.println(m.toStringDetailStat());
		m.levelUp();
		System.out.println(m.toStringDetailStat());
		m.levelUp();*/



	}

}
