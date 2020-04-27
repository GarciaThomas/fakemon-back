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

@Entity
@Table(name = "attaque")
public class Attaque implements Serializable {
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
	
	@Column(name = "effet_cumule", length = 25, nullable = true)
	String effetCumule;
	
	
/** Constructeur vide JPA
 **/
	public Attaque() {}
	
	/** Constructeur complet d'une attaque pour JDBC
	 * @param id : int ; C'est la clef primaire dans la BDD, sert � identifier quelles attaques peuvent �tres connues par chaque monstre
	 * @param puissance : int ; valeur indiquant la puissance intrinseque de l'attaque, c'est � dire les d�g�ts qu'elle engendre
	 * @param precision : int ; pr�cision sur 100 de l'attaque, une pr�cision >=100 indique qu'elle n'echoue jamais. Plus la valeur est faible, plus la probabilit� de rater l'attaque est �lev�
	 * @param nom : String ; l'intitul� de l'attaque
	 * @param etat : String ; donne de quelle fa�on doit �tre g�rer l'attaque : une attaque Physique utilisera les statistiques d'Attaque et de D�fense pour le calcul des d�g�ts, une attaque Sp�ciale utilisera les statistiques d'Attaque sp�ciale et de D�fense sp�ciale pour le calcul des d�gats et une attaque Statut n'infligera pas de d�g�ts mais impactera les statistique ou autre du fakemon 
	 * @param description
	 * @param type
	 */
	public Attaque(int id, int puissance, int precision, String nom, String etat, String description, Type type, String effetCumule) {
		this.id = id;
		this.puissance = puissance;
		this.precision = precision;
		this.nom = nom;
		this.etat = etat;
		this.description = description;
		this.type = type;
		this.effetCumule = effetCumule;
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
	public String getEffetCumule() {
		return effetCumule;
	}
	public void setEffetCumule(String effetCumule) {
		this.effetCumule = effetCumule;
	}


	@Override
	public String toString() {
		return "Attaque [puissance=" + puissance + ", precision=" + precision + ", nom=" + nom + ", description="
				+ description + ", type=" + type + "]";
	}

	public String toStringDescription() {
		return nom + " : " + description;
	}


	
}
