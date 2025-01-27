package beans;

import java.io.Serializable;


public class Operation implements Serializable{

	
	private static final long serialVersionUID = 3L;
	
	public static final int OP_CREATE_ROOM = 1001;
	public static final int OP_ENTER_ROOM = 1002;
	public static final int OP_PRINT_ROOM_LIST = 1003;
	public static final int OP_ESCAPE_ROOM = 1004;
	public static final int OP_REJECT_ENTER_ROOM = 1005;
	public static final int OP_APPROVE_ENTER_ROOM = 1006;
	public static final int OP_NOT_EXIST_ROOM = 1007;
	public static final int OP_GET_ROOM_INFO = 1008;
	
	public static final int OP_ADD_USER = 2001;
	public static final int OP_UPDATE_USER = 2002;
	public static final int OP_DELETE_USER = 2003;
	public static final int OP_GET_ROOM_USER_LIST = 2004;
	public static final int OP_GET_ENTIRE_USER_LIST = 2005;
	
	
	
	public static final int OP_SYS_MSG = 7777;
	
	public static final int OP_FIRST_CONNECTION = 9999;
	
	
	
	public Operation(){
		
	}
	
	public Operation(int state){
		this.state = state;
	}
	
	protected int state;
	
	public void setState(int state){
		this.state = state;
		
	}
	public int getState(){
		return state;
	}
	
}
