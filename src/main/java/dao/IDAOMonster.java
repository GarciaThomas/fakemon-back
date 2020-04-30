package dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import model.Monster;

public interface IDAOMonster extends JpaRepository<Monster, Integer> {
	
	@Query("SELECT COUNT(m) FROM Monster m")
	public Integer countNombreMonstre();

}
