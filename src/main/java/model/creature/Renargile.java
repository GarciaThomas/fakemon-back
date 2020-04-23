package model.creature;

import model.Monster;
import model.Type;

public class Renargile extends Monster {
	protected static Type type1=Type.valueOf("Sol");
	protected int level;
	private static Integer[] poolAtk = {21,10,22,3,24,2,14,8,12,27};

	
	public Renargile(int level) {
		super(level, 6, 18, 3, 18, 3, 18, creationAttaque(poolAtk),type1);
	}

}
