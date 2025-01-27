import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class ServiceQuery 
{   
	static final String DATABASE_URL = "jdbc:mysql://localhost/oldservices";
	static final String USERNAME = "root";
	static final String PASSWORD = "2001";

	Connection connection = null;      	
	
    PreparedStatement insertNewService = null;
	PreparedStatement selectServiceBySName = null;
	PreparedStatement deleteServiceBySName = null;
	PreparedStatement deleteServiceAll = null;
	PreparedStatement updateService = null;  
	
	
	Statement testStm = null;
	
   	public ServiceQuery(){
      		try	{                                
      			connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);

      			//서비스 추가
        		insertNewService = connection.prepareStatement(
	    			"INSERT INTO oldservices " + 
           			"(SName, UDName, Task, SSName, InterfaceName, Component, Definition, ServiceID, RegAuthority)" + 
            			"VALUES(?,?,?,?,?,?,?,?,?)");

        		//해당 서비스 이름의 모든 행 검색
        		selectServiceBySName = connection.prepareStatement("select * from oldservices where SName = ?");

        		//해당 서비스 이름의 모든 행 삭제
        		deleteServiceBySName = connection.prepareStatement("delete from oldservices where SName = ?");
        		
        		//모든 서비스 삭제
        		deleteServiceAll = connection.prepareStatement("delete from oldservices");
        		
        		//서비스 수정
        		updateService = connection.prepareStatement("update oldservices set SName = ?, UDName = ?, Task = ?," +
        				" SSName = ?, InterfaceName = ?, Component = ?, Definition = ?, ServiceID = ?, RegAuthority = ?" +
        				"where SName = ?");
        		
        		testStm = connection.createStatement();

      		}
      		catch(SQLException sqlException){
      			sqlException.printStackTrace();
      			System.exit(1);
      		}
	}
   	//모든 서비스 삭제
   	public void deleteServiceAll(){
   		try{			
   			deleteServiceAll.executeUpdate();			
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
			close();
		}
   	}
   	//서비스 수정
	public void updateServiceOne(String sname, String sName, String uDName, String task, String sSName, String interfaceName, String component,
			String definition, String serviceID, String regAuthority){
		try{
			updateService.setString(1, sName);
			updateService.setString(2, uDName);
			updateService.setString(3, task);
			updateService.setString(4, sSName);
			updateService.setString(5, interfaceName);
			updateService.setString(6, component);
			updateService.setString(7, definition);
			updateService.setString(8, serviceID);
			updateService.setString(9, regAuthority);
			updateService.setString(10, sname);

			updateService.executeUpdate();
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
			close();
		}
	}
	//해당 서비스 이름의 모든 행 삭제
	public void deleteServiceOne(String sname){
		List<Service> results = null;
		ResultSet resultSet = null;

		try	{
			deleteServiceBySName.setString(1, sname);
			//deleteServiceBySName.executeQuery();
			deleteServiceBySName.executeUpdate();			
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
			close();
		}			
	}
	public String[] Test(){
		
		ResultSet resultSet = null;
		String[] str = new String[2];
		
		try{
			resultSet = testStm.executeQuery("show tables");
			
			while(resultSet.next()){
				str[0] = resultSet.getString(1);
				str[1] = resultSet.getString(2);
			}
			
		}
		catch(SQLException sqlException){
			//sqlException.printStackTrace();
		}
		return str;
	}
	//해당 서비스 이름의 모든 행 검색
	public List<Service> getServiceBySName(String sname){
		List<Service> results = null;
		ResultSet resultSet = null;

		try	{
			selectServiceBySName.setString(1, sname);
			resultSet = selectServiceBySName.executeQuery();

			results = new ArrayList<Service>();

			while(resultSet.next())	{
				results.add(new Service(

					resultSet.getString("SName"),
					resultSet.getString("UDName"),
					resultSet.getString("Task"),
					resultSet.getString("SSName"),
					resultSet.getString("InterfaceName"),
					resultSet.getString("Component"),
					resultSet.getString("Definition"),
					resultSet.getString("ServiceID"),
					resultSet.getString("RegAuthority")));
			}
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}
		finally	{
			try	{
				resultSet.close();
			}
			catch(SQLException sqlException){
				sqlException.printStackTrace();
				close();
			}
		}
		return results;
	}	
	//서비스 등록
	public void addService(String sName, String uDName, String task, String sSName, String interfaceName, String component,
				String definition, String serviceID, String regAuthority/*, String company*/){			
		try	{
			
			insertNewService.setString(1, sName);
			insertNewService.setString(2, uDName);
			insertNewService.setString(3, task);
			insertNewService.setString(4, sSName);
			insertNewService.setString(5, interfaceName);
			insertNewService.setString(6, component);
			insertNewService.setString(7, definition);
			insertNewService.setString(8, serviceID);
			insertNewService.setString(9, regAuthority);

			insertNewService.executeUpdate();
			
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
			close();
		}
	}
	//DB연결 닫기
	public void close()	{
		try	{
			connection.close();
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}
	}		
}
