package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Attaque;
import model.Context;
import model.Efficacite;
import model.Monster;

@Service
public class ContextService {
	
	@Autowired
	Context context;
	

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
		return context.getDaoAttaque().selectPoolId(idsForQuery);

	}
	
	public Attaque getAttaqueid(int id) {
		return context.getDaoAttaque().findById(id).get();
	}

	public Double getRatioEfficacite(Attaque a,Monster m){
		return context.getDaoAttaque().ratioEfficacite(a.getType().toString(),m.getType().toString()).orElse(new Efficacite(1.0)).getRatio();
	}



	public ArrayList<Attaque> poolAttaque(ArrayList<Integer> ids) {

		return context.getDaoAttaque().selectPoolId(ids);

	}

	
}
