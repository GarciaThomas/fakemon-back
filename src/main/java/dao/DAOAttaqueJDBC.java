
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


public class DAOAttaqueJDBC implements DAOAttaque {

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

		try(
				Connection connect=Context.getInstance().getConnection();
				PreparedStatement ps=connect.prepareStatement("SELECT * FROM attaque WHERE id IN ("+chaine+")");
				) {

			ResultSet rs = ps.executeQuery(); 

			while (rs.next()) {
				Attaque a = new Attaque(rs.getInt("id"),rs.getInt("puissance"),rs.getInt("precision"),rs.getString("nom"),rs.getString("etat"),rs.getString("description"),Type.valueOf(rs.getString("type")));
				listAttaques.add(a);
			}
		}catch (Exception e) {e.printStackTrace();}

		return listAttaques;
	}


	public double ratioEfficacite(Type attaque, Type defense) {
		
		double ratio = 0;
		
		try(
				Connection connect=Context.getInstance().getConnection();

				PreparedStatement ps=connect.prepareStatement("SELECT "+defense.toString()+" FROM typefficace WHERE typeattaque = ?");

				) {
			
			ps.setString(1, attaque.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				ratio = rs.getDouble(defense.toString());
			}
			
					
		}catch (Exception e) {e.printStackTrace();}

		return ratio;

	}

	}	

	


