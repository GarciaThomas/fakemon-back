package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.query.criteria.internal.expression.function.LengthFunction;

@Entity
@Table(name = "attaque")
public class Attaque implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	int id;
	
	@Column(name = "puissance", nullable = false)
	int puissance;
	
	@Column(name = "precision", nullable = false)
	int precision;
	
	@Column(name = "nom", length = 20, nullable = false)
	String nom;
	
	@Column(name = "description", length = 100, nullable = true)
	String description;
	
	@Column(name = "etat", length = 10, nullable = false)
	String etat;
	
	@Column(name = "type", length = 15, nullable = false)
	@Enumerated(EnumType.STRING)
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
