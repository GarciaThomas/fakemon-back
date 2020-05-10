package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.IDAOAttaque;
import dao.IDAOMonster;
import model.Attaque;
import model.Dresseur;
import model.Efficacite;
import model.Monster;

@Service
public class ContextService {

	/*private Connection connect = null;*/
	//AnnotationConfigApplicationContext myContext = new AnnotationConfigApplicationContext(fakemonConfig.class);
	@Autowired
	private IDAOAttaque daoAttaque;// = myContext.getBean(IDAOAttaque.class);
	@Autowired
	private IDAOMonster daoMonster; // = myContext.getBean(IDAOMonster.class);
	private ArrayList<Monster> monstresProposition = null;
	
	private LinkedList<Dresseur> arene;

	@Autowired
	private PlayerService player;

	/*public Connection getConnect() {
		return connect;
	}
	public void setConnect(Connection connect) {
		this.connect = connect;
	}*/
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
	/*
	public Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/fakemon","root","");

		return connect;
	}*/
	public List<Monster> getMonstresProposition(){
		if(monstresProposition == null) {
			monstresProposition = new ArrayList<Monster>();
			monstresProposition.addAll(player.tableRencontre(10).stream()
					.collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Monster::getNom))),ArrayList::new)));
		}
		return monstresProposition;
	}
	public void setMonstresProposition(ArrayList<Monster> monstresProposition) {
		this.monstresProposition = monstresProposition;
	}

	public void rebuildPropositions() {
		this.monstresProposition = null;
	}

	public LinkedList<Dresseur> getArene(){
		if(arene == null){
			arene = new LinkedList<Dresseur>();
			for(int i=0;i<3;i++) {
				arene.add(new Dresseur(i,player));
			}
		}
		return arene;
	}


	/** Génére à partir du movepool du fakemon (la totalité des attaques qu'il peut apprendre) les trois attaques qu'il aura à sa disposition à la création
	 * N'est appellée que dans le constructeur et à aucun autre moment pour ne pas modifier ces valeurs en cours de route	
	 * @param poolEntier Integer[] ; liste d'entier correspondant au movepool du fakemon
	 * @return ArrayList<Attaque> ; liste des trois attaques disponible à la creation
	 */
	public ArrayList<Attaque> creationAttaque(Integer[] poolEntier) {

		LinkedList<Integer> mesIds = new LinkedList<Integer>();
		mesIds.addAll(Arrays.asList(poolEntier));
		Collections.shuffle(mesIds);

		ArrayList<Integer> idsForQuery = new ArrayList<Integer>();

		for(int i=0; i < 3; i++) {
			idsForQuery.add(mesIds.poll());
		}
		return getDaoAttaque().selectPoolId(idsForQuery);

	}

	public Attaque getAttaqueid(int id) {
		return getDaoAttaque().findById(id).get();
	}

	public Double getRatioEfficacite(Attaque a,Monster m){
		return getDaoAttaque().ratioEfficacite(a.getType().toString(),m.getType().toString()).orElse(new Efficacite(1.0)).getRatio();
	}



	public ArrayList<Attaque> poolAttaque(ArrayList<Integer> ids) {

		return getDaoAttaque().selectPoolId(ids);

	}


}
