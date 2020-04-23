package model.creature;

import model.Monster;
import model.Type;

public class Foufoudre extends Monster {
	protected static Type type1=Type.valueOf("Electrique");
	protected int level;
	private static Integer[] poolAtk = {11,12,17,27,8,3,20,28,15,1};
	
	
	public Foufoudre(int level){
		super(level, 7, 3, 6, 21, 7, 22, creationAttaque(poolAtk),type1);
	}

}

