package model;

public class Action {
	
	Monster m;
	String message;
	
	/** Constructeur avec paramettres
	 * @param m : Monster
	 * @param msg : String
	 **/
	public Action(Monster m, String msg) {
		this.m = m;
		this.message = msg;
	}
	
	/** Constructeur vide JPA ?
	 **/
	public Action() {
		
	}

	//Getters et Setters
	public Monster getM() {
		return m;
	}
	public void setM(Monster m) {
		this.m = m;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Action append(String str) {
		if(this.message == null) {
			setMessage(str);
		}else {
			this.message += "\n"+str;
		}
		return this;
	}

	
	@Override
	public String toString() {
		return "Action [m=" + m + ", message=" + message + "]";
	}
}
