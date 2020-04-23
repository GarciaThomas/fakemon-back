package model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class ContextJpa {
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("FakemonUnit");
	protected static EntityManager em = emf.createEntityManager();
	
	public void close() {
		emf.close();
	}


}
