package model.creature;

import model.Monster;
import model.Type;

public class Crameleon extends Monster {
	protected static Type type1=Type.valueOf("Feu");
	protected int level;
	private static Integer[] poolAtk = {26,15,16,2,18,20,3,10,24,9};
	
	
	public Crameleon(int level){
		super(level, 10, 2, 9, 19, 11, 15, creationAttaque(poolAtk),type1);
	}

}

