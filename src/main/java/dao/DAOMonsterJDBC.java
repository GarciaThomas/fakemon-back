package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import model.Context;
import model.Monster;
import model.Type;

public class DAOMonsterJDBC implements DAOMonster {

	@Override
	public void insert(Monster t) {
		// TODO Auto-generated method stub

	}

	@Override
	public Monster selectById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Monster> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Monster t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Monster selectByNom(String nom) {

		Monster m=null;
		try(
				Connection connect=Context.getInstance().getConnection();
				PreparedStatement ps=connect.prepareStatement("SELECT * FROM fakemon_stats WHERE espece = ?");
				) {
			ps.setString(1, nom);
			ResultSet rs = ps.executeQuery();

			m = new Monster(1,rs.getDouble("pv"),rs.getDouble("attaque"),rs.getDouble("defense"),rs.getDouble("atk_speciale"),rs.getDouble("def_speciale"),rs.getDouble("vitesse"),rs.getString("movepool"),Type.valueOf(rs.getString("type")));

		}catch (Exception e) {e.printStackTrace();}

		return m;
	}

	

	@Override
	public Integer countNombreMonstre() {
		// TODO Auto-generated method stub
		return null;
	}



}
