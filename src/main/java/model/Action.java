package model;

public class Action {
	
	Monster m;
	String message;
	
	public Action(Monster m, String msg) {
		this.m = m;
		this.message = msg;
	}
	
	public Action() {
		
	}

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

	@Override
	public String toString() {
		return "Action [m=" + m + ", message=" + message + "]";
	}
	
	

}
