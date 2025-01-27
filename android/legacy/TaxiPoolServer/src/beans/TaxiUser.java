package beans;

import java.io.ObjectOutputStream;
import java.io.Serializable;


public class TaxiUser implements Serializable {

	
	private static final long serialVersionUID = 5L;
	
	private String name;
	private String phoneNumber;
	ObjectOutputStream out;
	String roomId;
	
	public TaxiUser(String phoneNumber, ObjectOutputStream out){
		this.phoneNumber = phoneNumber;
		this.out = out;
	}
	
	public void setRoomId(String roomId){
		this.roomId = roomId;
		
	}
	public String getRoomId(){
		return roomId;
	}
	
	public ObjectOutputStream getOutputStream(){
		return out;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getPhoneNumber(){
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
		
	}
	
	public String toString(){
		return name + ":" + phoneNumber + ":" + roomId;
	}
	
	
}
