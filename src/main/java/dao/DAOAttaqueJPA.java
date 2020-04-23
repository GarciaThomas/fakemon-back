
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.Attaque;
import model.Context;
import model.Type;


public class DAOAttaqueJPA implements DAOAttaque {

	@Override
	public void insert(Attaque t) {

	}

	@Override
	public Attaque selectById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Attaque> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Attaque t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub

	}



	public ArrayList<Attaque> selectPoolId(ArrayList<Integer> ids) {

		return null;
	}


	public double ratioEfficacite(Type attaque, Type defense) {

		return 0;

	}

}	




