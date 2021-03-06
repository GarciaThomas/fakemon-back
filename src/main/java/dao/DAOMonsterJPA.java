package dao;

import java.util.List;

import model.Monster;

public class DAOMonsterJPA extends DAOJPA implements DAOMonster {

	@Override
	public void insert(Monster m) {
		try {
			this.em.getTransaction().begin();
			this.em.persist(m);
			this.em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			this.em.getTransaction().rollback();
		}
	}

	@Override
	public Monster selectById(Integer id) {
		Monster m = this.em.find(Monster.class,id);
		m.init();
		this.em.clear();
		return m;
	}

	@Override
	public List<Monster> selectAll() {
		return this.em.createQuery("SELECT m FROM Monster m", Monster.class).getResultList();
	}

	@Override
	public void update(Monster m) {
		try {
			this.em.getTransaction().begin();
			this.em.merge(m);
			this.em.getTransaction().commit();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
		}
	}

	//	Cette fonctionalit� est bloqu�e car il ne doit pas �tre possible de supprimer les bases stats des cr�atures mise dans la BDD
	@Override
	public void delete(Integer id) {
	/*	try {
			this.em.getTransaction().begin();

			Monster remov = new Monster();
			remov.setId(id);
			this.em.remove(this.em.merge(remov)); 

			this.em.getTransaction().commit();
		} catch (Exception e) {
			this.em.getTransaction().rollback();
		} */
	}

	public Monster selectByNom(String nom) {
		Monster m = this.em.createQuery("SELECT m FROM Monster m WHERE m.nom = ?1",Monster.class)
				.setParameter(1,nom)
				.getSingleResult();
		m.init();
		this.em.clear();
		return m;
	}

	/**
	 * @return
	 */
	public Integer countNombreMonstre() {
		return (int) (long) this.em.createQuery("SELECT COUNT(m) FROM Monster m").getSingleResult();
	}
	
}
