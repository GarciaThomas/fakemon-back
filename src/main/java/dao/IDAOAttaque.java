package dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import model.Attaque;
import model.Type;

public interface IDAOAttaque extends JpaRepository<Attaque, Integer> {

	@Query("SELECT a FROM Attaque WHERE id IN (?1)")
	public ArrayList<Attaque> selectPoolId(ArrayList<Integer> ids);

	@Query("SELECT e FROM Efficacite e WHERE typeAttaque = ?1 AND typeDefense = ?2")
	public double ratioEfficacite(Type attaque, Type defense);

}
