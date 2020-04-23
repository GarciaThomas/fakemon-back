package model.creature;

import model.Monster;
import model.Type;

public class Thymtamarre extends Monster {
	protected static Type type1=Type.valueOf("Plante");
	protected int level;
	private static Integer[] poolAtk = {19,6,7,28,29,14,9,23,17,18};

	
	public Thymtamarre(int level) {
		super(level, 15, 14, 7, 5, 20, 5, creationAttaque(poolAtk),type1);
	}

}
