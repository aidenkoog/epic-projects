import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class ServiceQuery 
{                                 
	public static final String DATABASE_URL = "jdbc:mysql://localhost/oldservices";	//URL
	public static final String USERNAME = "root";									//User
	public static final String PASSWORD = "2001";									//Password

	Connection connection = null;      	
    PreparedStatement insertNewPerson = null;
	PreparedStatement selectPersonByName = null;	
	PreparedStatement selectPersonByNickName = null;
     
	//생성자
   	public ServiceQuery()
   	{
      	try
      	{                                
      		connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
      		
      		/*13개 속성*/
        	insertNewPerson = connection.prepareStatement("INSERT INTO users " + 
           			"(name, perID, perNickName, perPw, perPwRe, perRegNum, perRegNumPass" +
           			", phoneHTextFirst, phoneHTextSecond, phoneCTextFirst, " +
           			"phoneCTextSecond, perEmailText1, perEmailText2)" + 
            		"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");

			selectPersonByName = connection.prepareStatement("select * from users where perID = ?");
			selectPersonByNickName = connection.prepareStatement("select * from users where perNickName = ?");
		}
		catch(SQLException sqlException)
		{
			sqlException.printStackTrace();
			System.exit(1);
		}
	}
   	
   	/*닉네임에 의한 사람찾기*/
	public List<Users> getPersonByNickName(String perNickName)
	{
		List<Users> results = null;
		ResultSet resultSet = null;

		try
		{
			selectPersonByNickName.setString(1, perNickName);
			resultSet = selectPersonByNickName.executeQuery();

			results = new ArrayList<Users>();

			while(resultSet.next())
			{
				/*DB에서 Data 가져옴, Users클래스에 저장*/
				results.add(new Users(
							
					resultSet.getString("name"),
					resultSet.getString("perID"),
					resultSet.getString("perNickName"),
					resultSet.getString("perPw"),
					resultSet.getString("perPwRe"),
					resultSet.getString("perRegNum"),
					resultSet.getString("perRegNumPass"),
					resultSet.getString("phoneHTextFirst"),
					resultSet.getString("phoneHTextSecond"),
					resultSet.getString("phoneCTextFirst"),
					resultSet.getString("phoneCTextSecond"),
					resultSet.getString("perEmailText1"),
					resultSet.getString("perEmailText2"))
				);				
			}
		}
		catch(SQLException sqlException)
		{
			sqlException.printStackTrace();
		}
		finally
		{
			try
			{
				resultSet.close();
			}
			catch(SQLException sqlException)
			{
				sqlException.printStackTrace();
				close();
			}
		}

		return results;
	}
   	/*이름에 의한 사람찾기*/
	public List<Users> getPersonByID(String perID)
	{
		List<Users> results = null;
		ResultSet resultSet = null;

		try
		{
			selectPersonByName.setString(1, perID);
			resultSet = selectPersonByName.executeQuery();

			results = new ArrayList<Users>();

			while(resultSet.next())
			{
				/*DB에서 Data 가져옴, Users클래스에 저장*/
				results.add(new Users(
							
					resultSet.getString("name"),
					resultSet.getString("perID"),
					resultSet.getString("perNickName"),
					resultSet.getString("perPw"),
					resultSet.getString("perPwRe"),
					resultSet.getString("perRegNum"),
					resultSet.getString("perRegNumPass"),
					resultSet.getString("phoneHTextFirst"),
					resultSet.getString("phoneHTextSecond"),
					resultSet.getString("phoneCTextFirst"),
					resultSet.getString("phoneCTextSecond"),
					resultSet.getString("perEmailText1"),
					resultSet.getString("perEmailText2"))
				);				
			}
		}
		catch(SQLException sqlException)
		{
			sqlException.printStackTrace();
		}
		finally
		{
			try
			{
				resultSet.close();
			}
			catch(SQLException sqlException)
			{
				sqlException.printStackTrace();
				close();
			}
		}

		return results;
	}
	
	/*유저 인적 사항 추가*/
	public void addUsers(String name, String perID, String perNickName, String perPwPass, 
						String perPwRePass, String perRegNum, String perRegNumPass, String phoneHTextFirst, 
						String phoneHTextSecond, String phoneCTextFirst, String phoneCTextSecond, 
						String perEmailText1, String perEmailText2)
	{			
		try
		{
			insertNewPerson.setString(1,name);
			insertNewPerson.setString(2,perID);
			insertNewPerson.setString(3,perNickName);
			insertNewPerson.setString(4,perPwPass);
			insertNewPerson.setString(5,perPwRePass);
			insertNewPerson.setString(6,perRegNum);
			insertNewPerson.setString(7,perRegNumPass);
			insertNewPerson.setString(8,phoneHTextFirst);
			insertNewPerson.setString(9,phoneHTextSecond);
			insertNewPerson.setString(10,phoneCTextFirst);
			insertNewPerson.setString(11,phoneCTextSecond);
			insertNewPerson.setString(12,perEmailText1);
			insertNewPerson.setString(13,perEmailText2);			

			insertNewPerson.executeUpdate();
		}
		catch(SQLException sqlException)
		{
			sqlException.printStackTrace();
			close();
		}
	}

	/*연결 종료*/
	public void close()
	{
		try
		{
			connection.close();
		}
		catch(SQLException sqlException)
		{
			sqlException.printStackTrace();
		}
	}	
}
