
package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.Attaque;
import model.ContextJpa;
import model.Type;


public class DAOAttaqueJPA extends ContextJpa implements DAOAttaque {

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
		
		ArrayList<Attaque> listAttaques = new ArrayList<Attaque>();

		String chaine = ids.stream().map(i -> i.toString()).collect(Collectors.joining(","));

		return (ArrayList<Attaque>) em.createQuery("select a from Attaque where id in (:ids)",Attaque.class).setParameter("ids", chaine).getResultList();
	}


	public double ratioEfficacite(Type attaque, Type defense) {

		return 0;

	}

}	




