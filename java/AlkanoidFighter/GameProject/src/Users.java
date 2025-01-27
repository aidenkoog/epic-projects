public class Users
{
	/*13개 회원등록 속성*/
	private String name;
	private String perID;
	private String perNickName;
	private String perPwPass;
	private String perPwRePass;
	private String perRegNum;
	private String perRegNumPass;
	private String phoneHTextFirst;
	private String phoneHTextSecond;
	private String phoneCTextFirst;
	private String phoneCTextSecond;
	private String phoneEmailText1;
	private String phoneEmailText2;

	/*생성자*/
	public Users()
	{
		/*13개의 항목*/
		name = null;
		perID = null;
		perNickName = null;
		perPwPass = null;
		perPwRePass = null;
		perRegNum = null;
		perRegNumPass = null;
		phoneHTextFirst = null;
		phoneHTextSecond = null;
		phoneCTextFirst = null;
		phoneCTextSecond = null;
		phoneEmailText1 = null;
		phoneEmailText2 = null;
	}

	/*매개변수 생성자*/
	public Users(String name, String perID, String perNickName, String perPwPass, String perPwRePass,
			String perRegNum, String perRegNumPass, String phoneHTextFirst, String phoneHTextSecond,
			String phoneCTextFirst, String phoneCTextSecond, String phoneEmailText1, String phoneEmailText2)
	{
		this.name = name;
		this.perID = perID;
		this.perNickName = perNickName;
		this.perPwPass = perPwPass;
		this.perPwRePass = perPwRePass;
		this.perRegNum = perRegNum;
		this.perRegNumPass = perRegNumPass;
		this.phoneHTextFirst = phoneHTextFirst;
		this.phoneHTextSecond = phoneHTextSecond;
		this.phoneCTextFirst = phoneCTextFirst;
		this.phoneCTextSecond = phoneCTextSecond;
		this.phoneEmailText1 = phoneEmailText1;
		this.phoneEmailText2 = phoneEmailText2;
	}	
	public String getName()
	{
		return name;
	}	
	public String getPerID()
	{
		return perID;
	}
	public String getPerNickName()
	{
		return perNickName;
	}
	public String getPerPwPass()
	{
		return perPwPass;
	}
	public String getPerPwRePass()
	{
		return perPwRePass;
	}
	public String getPerRegNum()
	{
		return perRegNum;
	}
	public String getRegNumPass()
	{
		return perRegNumPass;
	}
	public String getPhoneHTextFirst()
	{
		return phoneHTextFirst;
	}	
	public String getPhoneHTextSecond()
	{
		return phoneHTextSecond;
	}
	public String getPhoneCTextFirst()
	{
		return phoneCTextFirst;
	}
	public String getPhoneCTextSecond()
	{
		return phoneCTextSecond;
	}
	public String getPhoneEmailText1()
	{
		return phoneEmailText1;
	}
	public String getPhoneEmailText2()
	{
		return phoneEmailText2;
	}
}