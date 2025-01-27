package beans;

import java.io.Serializable;


public class TaxiUser implements Serializable {

	
	private static final long serialVersionUID = 5L;
	
	private String name;
	private String phoneNumber;
	
	
	
	public TaxiUser(String name, String phoneNumber){
		this.name = name;
		this.phoneNumber = phoneNumber;
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
		return name + ":" + phoneNumber;
	}
	
	
}
