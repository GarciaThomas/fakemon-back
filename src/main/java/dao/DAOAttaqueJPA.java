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


	/** Obtient les id random des attaques initiales du fakemon et doit renvoyer la liste d'attaques adaptee
	 * **/
	public ArrayList<Attaque> selectPoolId(ArrayList<Integer> ids) {

		ArrayList<Attaque> listAttaques = new ArrayList<Attaque>();

		String chaine = ids.stream().map(i -> i.toString()).collect(Collectors.joining(","));

		return (ArrayList<Attaque>) em.createQuery("select a from Attaque where id in (:ids)",Attaque.class).setParameter("ids", chaine).getResultList();
	}

	@Override
	public double ratioEfficacite(Type attaque, Type defense) {
		// TODO Auto-generated method stub
		return Double.valueOf(em.createQuery("select e from Efficacite e where typeAttaque = :attaque and typeDefense = :defense")
				.setParameter("typeAttaque", attaque.toString())
				.setParameter("typeDefense", defense.toString())
				.getFirstResult());

	}






}	




