package model.creature;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import model.Monster;
import model.Type;

@Entity
@DiscriminatorValue("Bebesalt")
public class Bebesalt extends Monster {
	@Transient
	protected int level;
	
	@Column (name = "pv", nullable = false)
	protected double basePV;	//17

	@Column (name = "attaque", nullable = false)
	protected double baseAtk;	//21

	@Column (name = "defense", nullable = false)
	protected double baseDef;	//19

	@Column (name = "atk_speciale", nullable = false)
	protected double baseASp;	//3

	@Column (name = "defk_speciale", nullable = false)
	protected double baseDSp;	//3

	@Column (name = "vitesse", nullable = false)
	protected double baseVit;	//3
	
	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	protected static Type type1;	//Type.valueOf("Roche")
	
	
	@Column(name = "movepool", nullable = false)
	private static Integer[] poolAtkString;

	//	private static Integer[] poolAtkInt = poolAtkStringToInt(poolAtkString);
	private static Integer[] poolAtkInt = {30,5,9,23,17,7,1,16,25,4};

	
	public Bebesalt(int level) {
		super(level, basePV, baseAtk, baseDef, baseASp, baseDSp, baseVit, creationAttaque(poolAtkInt),type1);
	}

}
