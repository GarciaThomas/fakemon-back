package model;

public class PVException extends Exception {

	public PVException(Monster mException) {
		super(debutPhrase(mException)+" ne peux plus se battre !");
	}

	private static String debutPhrase(Monster mException) {
		String texte = "";
		if(mException.getSituation().equals(Situation.valueOf("Joueur"))) {
			texte = "Votre "+mException.getNom();}
		else if (mException.getSituation().equals(Situation.valueOf("Sauvage"))) {
			texte = "Le "+mException.getNom()+" sauvage";}
		else if (mException.getSituation().equals(Situation.valueOf("Adversaire"))) {
			texte = "Le "+mException.getNom()+" du dresseur adverse";}
		return texte;
	}

}
