package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import config.fakemonConfig;
import dao.IDAOAttaque;
import dao.IDAOMonster;

public class Context {
	private static Context _instance = null;
	private Connection connect = null;
	AnnotationConfigApplicationContext myContext = new AnnotationConfigApplicationContext(fakemonConfig.class);
	private IDAOAttaque daoAttaque = myContext.getBean(IDAOAttaque.class);
	private IDAOMonster daoMonster = myContext.getBean(IDAOMonster.class);
	private ArrayList<Monster> monstresProposition = null;

	private Context() {
	}

	public static Context getInstance() {
		if(_instance==null) {
			_instance=new Context();
		}
		return _instance;
	}

	public Connection getConnect() {
		return connect;
	}
	
	public void setConnect(Connection connect) {
		this.connect = connect;
	}
	
	public IDAOMonster getDaoMonster() {
		return daoMonster;
	}

	public void setDaoMonster(IDAOMonster daoMonster) {
		this.daoMonster = daoMonster;
	}

	public IDAOAttaque getDaoAttaque() {
		return daoAttaque;
	}
	
	public void setDaoAttaque(IDAOAttaque daoAttaque) {
		this.daoAttaque = daoAttaque;
	}
	
	public void setMonstresProposition(ArrayList<Monster> monstresProposition) {
		this.monstresProposition = monstresProposition;
	}
	public Connection getConnection() throws SQLException, ClassNotFoundException {

		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/fakemon","root","");

		return connect;
	}


	public List<Monster> getMonstresProposition(){
		if(monstresProposition == null) {
			monstresProposition = new ArrayList<Monster>();
			monstresProposition.addAll(Player.getInstance().tableRencontre(10).stream()
					.collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Monster::getNom))),ArrayList::new)));

		}

		return monstresProposition;
	}

	public void rebuildPropositions() {
		this.monstresProposition = null;
	}





}
