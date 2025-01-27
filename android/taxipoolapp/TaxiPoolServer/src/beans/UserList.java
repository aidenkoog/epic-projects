package beans;

import java.util.ArrayList;

public class UserList extends Operation{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserList(int state){
		super(state);
	}
	
	public ArrayList<String> getAl() {
		return al;
	}

	public void setAl(ArrayList<String> al) {
		this.al = al;
	}

	private ArrayList<String> al;
	
	

}
