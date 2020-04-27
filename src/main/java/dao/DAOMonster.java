package dao;

import model.Monster;

public interface DAOMonster extends DAO<Monster, Integer> {
	
	public Monster selectByNom(String nom);
	
	public Integer countNombreMonstre();

}
