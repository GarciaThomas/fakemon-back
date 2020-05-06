package service;

import java.util.Random;

import model.Action;
import model.Player;
import model.Situation;

public class PlayerService {
	
	
	
	//	Vérifie si le monstre est capturable et réalise le test de capture. Si la capture réussie ajoute le monstre à l'équipe du joueur.
	public void captureMonstre() {

		if (equipeJoueur.equals(Situation.valueOf("Sauvage"))) {

			System.out.println("Tentative de capture du "+this.getNom()+" sauvage");
			double txCap = 1;
			if ((double) (this.getPV()/this.getPVmax()) <= 0.05) {
				txCap = 4;
			}
			else if ((double) (this.getPV()/this.getPVmax()) <= 0.15) {
				txCap = 3;
			}
			else if ((double) (this.getPV()/this.getPVmax()) <= 0.25) {
				txCap = 2.5;
			}
			else if ((double) (this.getPV()/this.getPVmax()) <= 0.5) {
				txCap = 2;
			}
			else if ((double) (this.getPV()/this.getPVmax()) <= 0.75) {
				txCap = 1.5;
			}

			int captureRate = (int) (2 * txCap * (21-this.getLevel()));
			Random r = new Random();
			if (r.nextInt(100)+1>captureRate) {
				System.out.println("La capture de "+this.getNom()+" a échouée");
			}
			else {
				System.out.println("La capture de "+this.getNom()+" a réussi !");
				Player.getInstance().addEquipePlayer(this);
			}

		}
		else {
			System.out.println("Le monstre adverse n'est pas capturable");
		}
	}

	public Action captureMonstreFront() {
		Action a = new Action();

		if (equipeJoueur.equals(Situation.valueOf("Sauvage"))) {

			System.out.println("Tentative de capture du "+this.getNom()+" sauvage");
			double txCap = 1;
			if ((double) (this.getPV()/this.getPVmax()) <= 0.05) {
				txCap = 4;
			}
			else if ((double) (this.getPV()/this.getPVmax()) <= 0.15) {
				txCap = 3;
			}
			else if ((double) (this.getPV()/this.getPVmax()) <= 0.25) {
				txCap = 2.5;
			}
			else if ((double) (this.getPV()/this.getPVmax()) <= 0.5) {
				txCap = 2;
			}
			else if ((double) (this.getPV()/this.getPVmax()) <= 0.75) {
				txCap = 1.5;
			}

			int captureRate = (int) (2 * txCap * (21-this.getLevel()));
			Random r = new Random();
			if (r.nextInt(100)+1>captureRate) {

				a.setMessage("La capture de "+this.getNom()+" a échouée");
			}
			else {
				a.setM(this);
				a.setMessage("La capture de "+this.getNom()+" a réussie !");
				Player.getInstance().addEquipePlayer(this);
			}

		}
		else {
			a.setMessage("Le monstre adverse n'est pas capturable");
		}
		return a;
	}

}
