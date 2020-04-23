package model.creature;

import model.Monster;
import model.Type;

public class Pipeau extends Monster {
	protected static Type type1=Type.valueOf("Eau");
	protected int level;
	private static Integer[] poolAtk = {13,25,4,8,5,2,22,29,28,6};

	
	public Pipeau(int level) {
		super(level, 10, 10, 12, 10, 12, 12, creationAttaque(poolAtk),type1);
	}

}
