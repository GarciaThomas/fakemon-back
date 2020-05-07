package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "efficacejpa")
public class Efficacite {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	int id;
	
	@Column(name="typeAttaque")
	String typeAttaque;
	
	@Column(name="typeDefense")
	String typeDefense;
	
	@Column(name="ratio")
	Double ratio;
	
	
	public Efficacite() {}

	public Efficacite(Double ratio) {
		this.ratio = ratio;
	}

	public String getTypeAttaque() {
		return typeAttaque;
	}
	public void setTypeAttaque(String typeAttaque) {
		this.typeAttaque = typeAttaque;
	}
	public String getTypeDefense() {
		return typeDefense;
	}
	public void setTypeDefense(String typeDefense) {
		this.typeDefense = typeDefense;
	}
	public Double getRatio() {
		return ratio;
	}
	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}


	@Override
	public String toString() {
		return "Efficacite [id=" + id + ", typeAttaque=" + typeAttaque + ", typeDefense=" + typeDefense + ", ratio="
				+ ratio + "]";
	}
	
	
	
}
