package model;

import java.io.Serializable;

public class Attaque implements Serializable{
	int id, puissance, precision;
	String nom,description,etat;
	Type type;
	
	

	public Attaque(int id, int puissance, int precision, Type type) {
		this.id = id;
		this.puissance = puissance;
		this.precision = precision;
		this.type = type;
	}

	
	
	public Attaque(int id, int puissance, int precision, String nom, String etat, String description, Type type) {
		this.id = id;
		this.puissance = puissance;
		this.precision = precision;
		this.nom = nom;
		this.etat = etat;
		this.description = description;
		this.type = type;
	}



	public int getPuissance() {
		return puissance;
	}
	public void setPuissance(int puissance) {
		this.puissance = puissance;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getEtat() {
		return etat;
	}
	public void setEtat(String etat) {
		this.etat=etat;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
 	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}



	@Override
	public String toString() {
		return "Attaque [puissance=" + puissance + ", precision=" + precision + ", nom=" + nom + ", description="
				+ description + ", type=" + type + "]";
	}




	
}
