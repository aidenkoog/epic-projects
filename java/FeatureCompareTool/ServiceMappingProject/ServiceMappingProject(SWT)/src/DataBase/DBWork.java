package DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import Main.MainFrame;

public class DBWork {
	//새로운 서비스 등록
	public static void addService(String companyName, String parentServiceName, String childServiceName,		//등록할 서비스 위치 속성
			String name, String definition, String domainTask, String sType, String synonymousNames,		  	//Identifying 속성
			double priority, String condition, String interfaceName, String businessProcess, String component,
			String mandatoryFunction, String secondaryFunction, 											  	//Functional 속성
			String servicePropertyString,																	  	//Selective 속성
			String andProperty, String orProperty)															  	//Relational 속성
	{
		//테이블 생성
		new DBTable(companyName);

		//서비스 삽입 쿼리문
		String insertNewServiceQuery = "INSERT INTO " + companyName + 
				" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,? ,? ,?, ?, ?, ?)";
		try{
			//부모 서비스는 기입, 자식 서비스 공란
			if(!(parentServiceName.equals("No_Parent")) && childServiceName.equals("No_Child")){			
				processParentService(name, companyName, parentServiceName);
			}
			//자식 서비스는 기입, 부모 서비스 공란
			else if(parentServiceName.equals("No_Parent") && !(childServiceName.equals("No_Child"))){		
				processChildService(name, companyName, childServiceName);
			}
			//부모, 자식 서비스 둘다 기입시
			else if(!(parentServiceName.equals("No_Parent")) && !(childServiceName.equals("No_Child"))){	
				processParentService(name, companyName, parentServiceName);
				processChildService(name, companyName, childServiceName);
			}

			//Identifying Attributes
			PreparedStatement insertNewService = DBConnection.connection.prepareStatement(insertNewServiceQuery);
			insertNewService.setString(1, name);		//1,2,3, ~ 17은 필드
			insertNewService.setString(2, definition);
			insertNewService.setString(3, domainTask);
			insertNewService.setString(4, sType);
			insertNewService.setString(5, synonymousNames);
			insertNewService.setDouble(6, priority);
			insertNewService.setString(7, condition);
			insertNewService.setString(8, interfaceName);
			insertNewService.setString(9, businessProcess);
			insertNewService.setString(10, component);

			//Functional Attributes
			insertNewService.setString(11, mandatoryFunction);
			insertNewService.setString(12, secondaryFunction);

			//Selective Attributes
			insertNewService.setString(13, servicePropertyString);

			//Relational Attributes
			insertNewService.setString(14, andProperty);
			insertNewService.setString(15, orProperty);

			//등록할 서비스 위치 속성
			insertNewService.setString(16, parentServiceName);
			insertNewService.setString(17, childServiceName);
			insertNewService.executeUpdate();	

			//등록 성공 메시지 보여주기
			MessageBox createTableMessageBox = new MessageBox(MainFrame.shell, SWT.ICON_INFORMATION);
			createTableMessageBox.setText("Success");
			createTableMessageBox.setMessage("Registration Success");
			createTableMessageBox.open();	//성공 메시지 박스 표시
		}
		catch(SQLException e){
			e.printStackTrace();
			//등록 실패 메시지 보여주기
			MessageBox errorMessageBox = new MessageBox(MainFrame.shell, SWT.ICON_ERROR);
			errorMessageBox.setText("Failed");
			errorMessageBox.setMessage("Registration Failed");
			errorMessageBox.open();	//테이블 생성 성공 메시지 박스 표시
		}				
	} //새로운 서비스 등록 함수 끝

	//등록시 부모 서비스 기입 처리
	private static void processParentService(String name, String companyName, String parentServiceName) throws SQLException{
		String searchServiceQuery = "SELECT ChildService from "+companyName+" where Name = '"+parentServiceName+"'";
		Statement searchStatement = DBConnection.connection.createStatement();
		ResultSet searchResultSet = searchStatement.executeQuery(searchServiceQuery);

		String childService = null;
		while(searchResultSet.next()){ childService = searchResultSet.getString(1); }

		if(!(childService.equals("No_Child"))){	childService += ","+name; } //자식 서비스가 있다면
		else{ childService = name; } //자식 서비스가 없는 상태이면

		String updateChildServiceQuery = "UPDATE "+companyName+" SET ChildService = ? where Name = ?";
		PreparedStatement updateChildStatement = DBConnection.connection.prepareStatement(updateChildServiceQuery);
		updateChildStatement.setString(1, childService);
		updateChildStatement.setString(2, parentServiceName);
		updateChildStatement.executeUpdate();
	} //등록시 부모 서비스 기입 처리 함수 끝

	//등록시 자식 서비스 기입 처리
	private static void processChildService(String name, String companyName, String childServiceName) throws SQLException{
		String searchServiceQuery = "SELECT ParentService from "+companyName+" where Name = '"+childServiceName+"'";
		Statement searchStatement = DBConnection.connection.createStatement();
		ResultSet searchResultSet = searchStatement.executeQuery(searchServiceQuery);

		String parentService = null;
		while(searchResultSet.next()){ parentService = searchResultSet.getString(1); }

		if(!(parentService.equals("No_Parent"))){ parentService += ","+name; } //부모 서비스가 있다면
		else{ parentService = name;	} //부모 서비스가 없는 상태이면

		String updateParentQuery = "UPDATE "+companyName+" SET ParentService = ? where Name = ?";
		PreparedStatement updateParentStatement = DBConnection.connection.prepareStatement(updateParentQuery);
		updateParentStatement.setString(1, parentService);
		updateParentStatement.setString(2, childServiceName);
		updateParentStatement.executeUpdate();
	} //등록시 자식 서비스 기입 처리 함수 끝

	//서비스 모두 삭제
	public static void deleteAllService(){
		String[] companyList = setCompanyNameComboList();
		for(int i=0; i<companyList.length; i++){
			try{
				PreparedStatement deleteCompanyStatement = DBConnection.connection.prepareStatement("drop table "+companyList[i]);	//회사명 삭제
				deleteCompanyStatement.executeUpdate();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		MessageBox deleteMessageBox = new MessageBox(MainFrame.shell, SWT.ICON_INFORMATION);
		deleteMessageBox.setText("Delete All");
		deleteMessageBox.setMessage("Delete All Success");
		deleteMessageBox.open();
	} //서비스 모두 삭제 함수 끝
	
	//선택된 서비스 삭제
	public static void deleteService(String companyName, String serviceName){

		String deleteQuery = "delete from " + companyName + " where Name = ?";
		try{
			PreparedStatement deleteServiceStatement = DBConnection.connection.prepareStatement(deleteQuery);
			deleteServiceStatement.setString(1, serviceName);
			deleteServiceStatement.executeUpdate();

			Statement numberOfServiceStatement = DBConnection.connection.createStatement();
			ResultSet numberOfServiceResultSet = numberOfServiceStatement.executeQuery("select count(*) from "+companyName);
			int numberOfService = 0;
			while(numberOfServiceResultSet.next()){
				numberOfService = numberOfServiceResultSet.getInt(1);
			}
			if(numberOfService == 0){	//서비스가 없다면
				PreparedStatement deleteCompanyStatement = DBConnection.connection.prepareStatement("drop table "+companyName);	//회사명 삭제
				deleteCompanyStatement.executeUpdate();
			}

			MessageBox deleteMessageBox = new MessageBox(MainFrame.shell, SWT.ICON_INFORMATION);
			deleteMessageBox.setText("Delete");
			deleteMessageBox.setMessage("Delete Success");
			deleteMessageBox.open();	
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
			MessageBox errorMessageBox = new MessageBox(MainFrame.shell, SWT.ICON_ERROR);
			errorMessageBox.setText("Failed");
			errorMessageBox.setMessage("Delete Failed");
			errorMessageBox.open();	
		}
	} //선택된 서비스 삭제 함수 끝

	//서비스 속성값 수정
	public static void updateService(String companyName, String parentServiceName, String childServiceName,	//등록할 서비스 위치 속성
			String name, String definition, String domainTask, String sType, String synonymousNames,	//Identifying 속성
			double priority, String condition, String interfaceName, String businessProcess, 
			String component,   
			String mandatoryFunction, String secondaryFunction, 									  	//Functional 속성
			String servicePropertyString,														  		//Selective 속성
			String andProperty, String orProperty){												  		//Relational 속성

		//Update 쿼리문
		String updateQuery = "update " + companyName + " set Name = ?, Definition = ?, DomainTask = ?, SType = ?, SynonymousNames = ?," +
				" Priority = ?, ConditionProperty = ?, Interface = ?, businessProcess = ?, Component = ?," +
				" MandatoryFunction = ?, SecondaryFunction = ?, " +
				" Property = ?, andProperty = ?, orProperty = ?," +	
				" ParentService = ?, ChildService = ?" +
				" where Name = ?";
		try{
			PreparedStatement updateServiceStatement = DBConnection.connection.prepareStatement(updateQuery);
			//Identifying Attributes
			updateServiceStatement.setString(1, name);		
			updateServiceStatement.setString(2, definition);
			updateServiceStatement.setString(3, domainTask);
			updateServiceStatement.setString(4, sType);
			updateServiceStatement.setString(5, synonymousNames);
			updateServiceStatement.setDouble(6, priority);
			updateServiceStatement.setString(7, condition);
			updateServiceStatement.setString(8, interfaceName);
			updateServiceStatement.setString(9, businessProcess);
			updateServiceStatement.setString(10, component);

			//Functional Attributes
			updateServiceStatement.setString(11, mandatoryFunction);
			updateServiceStatement.setString(12, secondaryFunction);

			//Selective Attributes
			updateServiceStatement.setString(13, servicePropertyString);

			//Relational Attributes
			updateServiceStatement.setString(14, andProperty);
			updateServiceStatement.setString(15, orProperty);

			//등록할 서비스 위치 속성
			updateServiceStatement.setString(16, parentServiceName);
			updateServiceStatement.setString(17, childServiceName);

			//Where절
			updateServiceStatement.setString(18, name);
			updateServiceStatement.executeUpdate(); 

			MessageBox updateMessageBox = new MessageBox(MainFrame.shell, SWT.ICON_INFORMATION);
			updateMessageBox.setText("Update");
			updateMessageBox.setMessage("Update Success");
			updateMessageBox.open();	
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
			MessageBox errorMessageBox = new MessageBox(MainFrame.shell, SWT.ICON_ERROR);
			errorMessageBox.setText("Failed");
			errorMessageBox.setMessage("Update Failed");
			errorMessageBox.open();	
		}
	} //서비스 속성값 수정 함수 끝

	//테이블 내 튜플(행)의 개수 검색
	public static int getTableRowNumber(String companyName){
		Statement countRowNumberStatement = null;
		ResultSet countRowNumberResultSet = null;
		
		//테이블의 튜플(행) 개수 검색
		String countRowNumberQuery = "select count(*) from " + companyName;
		int tableRowNumber = -1;

		try{
			countRowNumberStatement = DBConnection.connection.createStatement();
			countRowNumberResultSet = countRowNumberStatement.executeQuery(countRowNumberQuery);

			while(countRowNumberResultSet.next()){
				tableRowNumber = countRowNumberResultSet.getInt(1);
			}
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}
		return tableRowNumber;
	} //테이블 내 튜플(행)의 개수 검색 함수 끝

	//테이블 아이템 속성 검색(Name, DomainTask, Stype)
	public static String[] getTableProperty(String companyName, String serviceName){
		Statement findTablePropertyStatement = null;
		ResultSet findTablePropertyResultSet = null;
		
		//Service Name, DomainTask, SType 검색
		String findTablePropertyQuery = "select Name, DomainTask, SType from " + companyName + " where Name= '" + serviceName + "'";
		String[] tablePropertyList = null;
		try{
			//해당 서비스의 Name, DomainTask, SType 필드
			findTablePropertyStatement = DBConnection.connection.createStatement();
			findTablePropertyResultSet = findTablePropertyStatement.executeQuery(findTablePropertyQuery);

			//해당 서비스의 Name, DomainTask, SType 속성 값을 얻음
			tablePropertyList = new String[4];
			while(findTablePropertyResultSet.next()){

				tablePropertyList[0] = companyName;
				tablePropertyList[1] = findTablePropertyResultSet.getString(1);	//Name
				tablePropertyList[2] = findTablePropertyResultSet.getString(2);	//DomainTask
				tablePropertyList[3] = findTablePropertyResultSet.getString(3);	//Stype
			}
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}
		return tablePropertyList;
	} //테이블 아이템 속성 검색(Name, DomainTask, Stype) 함수 끝

	//검색된 필드의 속성 검색
	public static String getSelectedServicePropertyByServiceName(String companyName, String serviceName, String selectedField){
		Statement findSelectedServiceByServiceNameStatement = null;
		ResultSet selectedServiceByServiceNameResultSet = null;
		
		//해당 서비스에 해당하는 ParentService 필드 검색
		String findSelectedServiceByServiceNameQuery = "select " + selectedField + " from " + companyName + " where Name= '" + serviceName + "'";
		String selectedServiceName = null;

		try{

			//해당 서비스의 ParentService 필드
			findSelectedServiceByServiceNameStatement = DBConnection.connection.createStatement();
			selectedServiceByServiceNameResultSet = findSelectedServiceByServiceNameStatement.executeQuery(findSelectedServiceByServiceNameQuery);

			//해당 서비스의 ParentService 속성 값을 얻음
			while(selectedServiceByServiceNameResultSet.next()){

				selectedServiceName = selectedServiceByServiceNameResultSet.getString(1);
			}
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}

		return selectedServiceName;
	} //검색된 필드의 속성 검색 함수 끝

	//서비스 이름으로 속성 검색
	public static String[] getPropertyByServiceName(String companyName, String serviceName){
		Statement findPropertyByServiceNameStatement = null;
		ResultSet propertyByServiceNameResultSet = null;
		
		//해당 서비스에 해당하는 모든 필드 검색
		String findPropertyByServiceNameQuery = "select * from " + companyName + " where Name= '" + serviceName + "'";
		String[] servicePropertyList = null;

		try{

			//해당 서비스의 속성 목록
			findPropertyByServiceNameStatement = DBConnection.connection.createStatement();
			propertyByServiceNameResultSet = findPropertyByServiceNameStatement.executeQuery(findPropertyByServiceNameQuery);

			//해당 서비스의 속성 목록을 얻음			
			servicePropertyList = new String[17];	//15개 속성 + 2개(임의로 정한 속성 - 부모, 자식서비스)			
			while(propertyByServiceNameResultSet.next()){ //ResultSet에 튜플(한행) 하나씩 담아 ResultSet의 next()메서드를 이용해 검색

				for(int index=0; index<servicePropertyList.length; index++){

					//속성이 Priority 이라면
					if(index == 5){
						double priorityProperty = propertyByServiceNameResultSet.getDouble(index + 1);
						servicePropertyList[index] = Double.toString(priorityProperty);
					}
					//나머지 속성 이라면
					else{
						servicePropertyList[index] = propertyByServiceNameResultSet.getString(index + 1);
					}
				}
			}		
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}

		return servicePropertyList;
	} //서비스 이름으로 속성 검색 함수 끝

	//부모 서비스 콤보 목록 설정
	public static String[] setParentServiceNameComboList(String selectedItem){	//인자로 회사 이름
		Statement findParentServiceStatement = null;
		Statement parentServiceNumberStatement = null;
		ResultSet parentServiceResultSet = null;
		ResultSet parentServiceNumberResultSet = null;

		//선택된 회사의 서비스 이름 값들을 검색
		String findParentServiceQuery = "select Name from " + selectedItem;

		//선택된 회사의 서비스 개수를 검색
		String parentServiceNumberQuery = "select count(*) from " + selectedItem;

		String[] serviceNameList = null;

		try{
			//서비스 목록
			findParentServiceStatement = DBConnection.connection.createStatement();
			parentServiceResultSet = findParentServiceStatement.executeQuery(findParentServiceQuery);

			//서비스 개수
			parentServiceNumberStatement = DBConnection.connection.createStatement();
			parentServiceNumberResultSet = parentServiceNumberStatement.executeQuery(parentServiceNumberQuery);

			//서비스 개수를 얻음
			int parentServiceNumber = 0;
			while(parentServiceNumberResultSet.next()){				

				parentServiceNumber = parentServiceNumberResultSet.getInt(1);
			}

			//서비스 목록을 얻음
			serviceNameList = new String[parentServiceNumber];							
			int serviceCount = 0;
			while(parentServiceResultSet.next()){

				serviceNameList[serviceCount++] = parentServiceResultSet.getString(1);			
			}	
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}

		return serviceNameList;		
	} //부모 서비스 콤보 목록 설정 함수 끝

	//자식 서비스 콤보 목록 설정
	public static String[] setChildServiceNameComboList(String selectedItem){	//인자로 회사 이름
		Statement findChildServiceStatement = null;
		Statement childServiceNumberStatement = null;	
		ResultSet childServiceResultSet = null;
		ResultSet childServiceNumberResultSet = null;
		
		//선택된 회사의 서비스 이름 값들을 검색
		String findChildServiceQuery = "select Name from " + selectedItem;

		//선택된 회사의 서비스 개수를 검색
		String childServiceNumberQuery = "select count(*) from " + selectedItem;

		String[] serviceNameList = null;

		try{

			//서비스 목록
			findChildServiceStatement = DBConnection.connection.createStatement();
			childServiceResultSet = findChildServiceStatement.executeQuery(findChildServiceQuery);

			//서비스 개수
			childServiceNumberStatement = DBConnection.connection.createStatement();
			childServiceNumberResultSet = childServiceNumberStatement.executeQuery(childServiceNumberQuery);

			//서비스 개수를 얻음
			int childServiceNumber = 0;
			while(childServiceNumberResultSet.next()){				
				childServiceNumber = childServiceNumberResultSet.getInt(1);
			}

			//서비스 목록을 얻음
			serviceNameList = new String[childServiceNumber];							
			int serviceCount = 0;
			while(childServiceResultSet.next()){

				serviceNameList[serviceCount++] = childServiceResultSet.getString(1);			
			}	
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}

		return serviceNameList;	
	} //자식 서비스 콤보 목록 설정 함수 끝

	//회사이름 콤보 목록 설정
	public static String[] setCompanyNameComboList(){
		Statement showTableListStatement = null;
		Statement showTableNumberStatement = null;	
		ResultSet tableNumberResultSet = null;
		ResultSet tableListResultSet = null;
		
		//모든 테이블 출력		
		String showTableListQuery = "show tables";

		//모든 테이블의 개수 출력
		String showTableNumberQuery = "select count(*) from information_schema.tables where table_schema = 'Company' and TABLE_TYPE='BASE TABLE'";	

		String[] tableList = null;		

		try{

			//테이블 개수
			showTableNumberStatement = DBConnection.connection.createStatement();
			tableNumberResultSet = showTableNumberStatement.executeQuery(showTableNumberQuery);

			//테이블 목록
			showTableListStatement = DBConnection.connection.createStatement();
			tableListResultSet = showTableListStatement.executeQuery(showTableListQuery);

			//테이블 개수를 얻음
			int tableNumber = 0;
			while(tableNumberResultSet.next()){				
				tableNumber = tableNumberResultSet.getInt(1);
			}

			//테이블 목록을 얻음
			tableList = new String[tableNumber];							
			int tableCount = 0;
			while(tableListResultSet.next()){

				tableList[tableCount++] = tableListResultSet.getString(1);			
			}	
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}				

		return tableList;	//테이블 목록을 반환
	} //회사이름 콤보 목록 설정 함수 끝
}