package dao;

import java.util.ArrayList;

import model.Attaque;
import model.Type;

public interface DAOAttaque extends DAO<Attaque, Integer> {

	public ArrayList<Attaque> selectPoolId(ArrayList<Integer> ids);

	public double ratioEfficacite(Type attaque, Type defense);

}
