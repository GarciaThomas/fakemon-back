package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class DAOJPA {
	private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("FakemonUnit");
	protected final EntityManager em = emf.createEntityManager();
	
	public static void close() {
		emf.close();
	}
}