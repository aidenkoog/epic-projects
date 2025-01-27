package beans;

import java.io.Serializable;


public class ChatMessage implements Serializable{
	
	private static final long serialVersionUID = 6L;
	
	public static final int SYS_MSG = 500;
	
	
	private int state;
	
	private String userName;
	private String msg;
	private String userPhoneNumber;
	private String time;
	private String roomId;
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUserPhoneNumber() {
		return userPhoneNumber;
	}

	public void setUserPhoneNumber(String userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}

	public ChatMessage(String userName, String msg){
		this.userName = userName;
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
}
