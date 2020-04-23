
package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.Attaque;
import model.Type;


public class DAOAttaqueJPA extends DAOJPA implements DAOAttaque {

	@Override
	public void insert(Attaque a) {
		try {
			this.em.getTransaction().begin();
			this.em.persist(a);
			this.em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			this.em.getTransaction().rollback();
		}
	}

	@Override
	public Attaque selectById(Integer id) {
		return this.em.find(Attaque.class,id);
	}

	@Override
	public List<Attaque> selectAll() {
		return this.em.createQuery("SELECT a FROM Attaque a", Attaque.class).getResultList();
	}

	@Override
	public void update(Attaque a) {
		try {
			this.em.getTransaction().begin();
			this.em.merge(a);
			this.em.getTransaction().commit();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
		}
	}

	@Override
	public void delete(Integer id) {
		try {
			this.em.getTransaction().begin();

			Attaque remov = new Attaque();
			remov.setId(id);
			this.em.remove(this.em.merge(remov)); 

			this.em.getTransaction().commit();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
		} 
	}


	/** Obtient les id random des attaques initiales du fakemon et doit renvoyer la liste d'attaques adaptée
	 * **/
	public ArrayList<Attaque> selectPoolId(ArrayList<Integer> ids) {
		String chaine = ids.stream().map(i -> i.toString()).collect(Collectors.joining(","));

		try {
			this.em.getTransaction().begin();
			ArrayList<Attaque> listAtt = (ArrayList<Attaque>) this.em.createQuery("SELECT a FROM Attaque a WHERE a.id="+chaine, Attaque.class).getResultList();



			this.em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			this.em.getTransaction().rollback();
		}
		return listAtt;
	}


	public double ratioEfficacite(Type attaque, Type defense) {

		return 0;

	}

}	




