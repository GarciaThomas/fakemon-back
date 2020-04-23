package model;

import dao.DAOAttaque;
import dao.DAOAttaqueJPA;
import dao.DAOMonster;
import dao.DAOMonsterJPA;

public abstract class ContextJpa {
	private static DAOAttaque daoAttaque = new DAOAttaqueJPA();
	private static DAOMonster daoMonster = new DAOMonsterJPA();

	
	public static DAOAttaque GetDaoAttaque() {
		if (daoAttaque == null) {
			daoAttaque = new DAOAttaqueJPA();
		}
		return daoAttaque;
	}
	public static DAOMonster GetDaoMonster() {
		if (daoMonster == null) {
			daoMonster = new DAOMonsterJPA();
		}
		return daoMonster;
	}


}
