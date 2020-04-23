package model.creature;

import model.Monster;
import model.Type;

public class Bebesalt extends Monster {
	protected static Type type1=Type.valueOf("Roche");
	protected int level;
	private static Integer[] poolAtk = {30,5,9,23,17,7,1,16,25,4};

	
	public Bebesalt(int level) {
		super(level, 17, 21, 19, 3, 3, 3, creationAttaque(poolAtk),type1);
	}

}
