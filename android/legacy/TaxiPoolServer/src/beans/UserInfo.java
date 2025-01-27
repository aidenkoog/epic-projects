package beans;

public class UserInfo  extends Operation{

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}


	private int userId;
	private String phoneNumber;
	private String nickname;
	private String dept;
	
	public UserInfo(int state){
		super(state);
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public String getDept() {
		return dept;
	}


	public void setDept(String dept) {
		this.dept = dept;
	}


	private static final long serialVersionUID = 1L;
	
	
	

}
