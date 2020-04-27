package model;

public class PVException extends Exception {
	
	public PVException(Monster m) {
		super(m.getNom()+" ne peux plus se battre !");
	}
}
