package model;

public class PVException extends Exception {
	Monster mException;
	
	public PVException(Monster mException) {
		super(mException.getNom()+" ne peux plus se battre !");
		this.mException = mException;
	}
	
}
