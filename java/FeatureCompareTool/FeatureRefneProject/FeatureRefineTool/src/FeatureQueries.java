import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

public class FeatureQueries {
	private static final String URL = "jdbc:mysql://localhost:3306/featureRefine";    	//DB SystemService
	private static final String USERNAME = "root";              	                	//사용자명 root
	private static final String PASSWORD = "2001";                          	    	//비밀번호 2001

	private Connection connection = null; 	
	private Statement createTableStatement = null;
	private Statement findPropertyByFeatureNameStatement = null;
	private ResultSet propertyByFeatureNameResultSet = null;
	
	private Statement findNamePropertyStatement = null;
	private ResultSet findNamePropertyResultSet = null;
	
	private Statement countRowNumberStatement = null;
	private ResultSet countRowNumberResultSet = null;
	
	private Statement findRelationshipByFeatureNameStatement = null;
	private ResultSet relationshipByFeatureNameResultSet = null;
	
	private Statement findSimanticsByFeatureNameStatement = null;
	private ResultSet simanticsByFeatureNameResultSet = null;
	
	private Statement findFeatureStatement = null;
	private ResultSet featureResultSet = null;
	private Statement featureNumberStatement = null;
	private ResultSet featureNumberResultSet = null;

	//생성자
	public FeatureQueries(){
		//드라이버 클래스를 검색
		try{
			Class.forName("org.gjt.mm.mysql.Driver");
		}catch(ClassNotFoundException e){
			//드라이버 찾기 에러 메시지 박스 생성
			MessageBox searchDriverErrorMessageBox = new MessageBox(MainFrame.shell, SWT.ICON_INFORMATION);
			searchDriverErrorMessageBox.setText("Driver Not Found");
			searchDriverErrorMessageBox.setMessage("Driver Not Found");
			searchDriverErrorMessageBox.open();
		}
		//Connection 객체 생성
		try{
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);  //DB서버와 연결하는 객체 생성
		}catch(SQLException sqlException){
			//DB연겶 에러 메시지 박스 생성
			MessageBox connectDatabaseErrorMessageBox = new MessageBox(MainFrame.shell, SWT.ICON_INFORMATION);
			connectDatabaseErrorMessageBox.setText("Database Connect Error");
			connectDatabaseErrorMessageBox.setMessage("Database Connect Error");
			connectDatabaseErrorMessageBox.open();
		}	
	} //생성자 끝
	
	//휘처의 이름 속성 검색
	public String[] getFeatureList(){
		String findFeatureNameQuery = "select Name from feature";
		String featureNumberQuery = "select count(*) from feature";
		String[] featureNameList = null;

		try{
			//목록
			findFeatureStatement = connection.createStatement();
			featureResultSet = findFeatureStatement.executeQuery(findFeatureNameQuery);

			//개수
			featureNumberStatement = connection.createStatement();
			featureNumberResultSet = featureNumberStatement.executeQuery(featureNumberQuery);

			int featureNumber = 0;
			while(featureNumberResultSet.next()){				
				featureNumber = featureNumberResultSet.getInt(1);
			}

			featureNameList = new String[featureNumber];							
			int index = 0;
			while(featureResultSet.next()){
				featureNameList[index++] = featureResultSet.getString(1);			
			}	
		}catch(SQLException sqlException){}
		return featureNameList;	
	} //휘처의 이름 속성 검색 함수 끝
	
	//Relationship 속성 검색
	public String[] getRelationshipByFeatureName(String featureName){
		String findRelationshipByFeatureNameQuery = "select Property, ConditionProperty from feature where Name= '" + featureName + "'";
		String[] featurePropertyList = null;
		try{
			//해당 휘처의 속성 목록
			findRelationshipByFeatureNameStatement = connection.createStatement(); 
			relationshipByFeatureNameResultSet = findRelationshipByFeatureNameStatement.executeQuery(findRelationshipByFeatureNameQuery);

			//해당 휘처의 속성 목록을 얻음			
			featurePropertyList = new String[2];	//4개 속성			
			while(relationshipByFeatureNameResultSet.next()){ //ResultSet에 튜플(한행) 하나씩 담아 ResultSet의 next()메서드를 이용해 검색
				featurePropertyList[0] = relationshipByFeatureNameResultSet.getString("Property");
				featurePropertyList[1] = relationshipByFeatureNameResultSet.getString("ConditionProperty");
			}		
		}
		catch(SQLException sqlException){}
		return featurePropertyList;
	} //Relationship 속성 검색 함수 끝
	
	//Simantics 속성 검색
	public String[] getSemanticsByFeatureName(String featureName){
		String findSimanticsByFeatureNameQuery = "select Classification, FType, Priority, Definition from feature where Name= '" + featureName + "'";
		String[] featurePropertyList = null;
		try{
			//해당 휘처의 속성 목록
			findSimanticsByFeatureNameStatement = connection.createStatement(); 
			simanticsByFeatureNameResultSet = findSimanticsByFeatureNameStatement.executeQuery(findSimanticsByFeatureNameQuery);

			//해당 휘처의 속성 목록을 얻음			
			featurePropertyList = new String[4];	//4개 속성			
			while(simanticsByFeatureNameResultSet.next()){ //ResultSet에 튜플(한행) 하나씩 담아 ResultSet의 next()메서드를 이용해 검색
				featurePropertyList[0] = simanticsByFeatureNameResultSet.getString("Classification");
				featurePropertyList[1] = simanticsByFeatureNameResultSet.getString("FType");
				double priorityProperty = simanticsByFeatureNameResultSet.getDouble("Priority");
				featurePropertyList[2] = Double.toString(priorityProperty);
				featurePropertyList[3] = simanticsByFeatureNameResultSet.getString("Definition");
			}		
		}
		catch(SQLException sqlException){}
		return featurePropertyList;
	} //Simantics 속성 검색 함수 끝
	
	//선택된 휘쳐의 전체 속성 검색
	public String[] getPropertyByFeatureName(String featureName){

		//해당 휘처에 해당하는 모든 필드 검색
		String findPropertyByFeatureNameQuery = "select * from Feature where Name= '" + featureName + "'";
		String[] featurePropertyList = null;
		
		try{
			//해당 휘처의 속성 목록
			findPropertyByFeatureNameStatement = connection.createStatement();
			propertyByFeatureNameResultSet = findPropertyByFeatureNameStatement.executeQuery(findPropertyByFeatureNameQuery);

			//해당 휘처의 속성 목록을 얻음			
			featurePropertyList = new String[11];	//11개 속성			
			while(propertyByFeatureNameResultSet.next()){ //ResultSet에 튜플(한행) 하나씩 담아 ResultSet의 next()메서드를 이용해 검색
				for(int index=0; index<featurePropertyList.length; index++){
					//속성이 Priority 이라면
					if(index == 4){
						double priorityProperty = propertyByFeatureNameResultSet.getDouble(index + 1);
						featurePropertyList[index] = Double.toString(priorityProperty);
					}
					//나머지 속성 이라면
					else{
						featurePropertyList[index] = propertyByFeatureNameResultSet.getString(index + 1);
					}
				}
			}		
		}
		catch(SQLException sqlException){
		}
		return featurePropertyList;
	} //선택된 휘쳐의 전체 속성 검색 함수 끝
	
	//테이블 내 튜플(행)의 개수 검색
	public int getTableRowNumber(){

		//테이블의 튜플(행) 개수 검색
		String countRowNumberQuery = "select count(*) from feature";
		int tableRowNumber = -1;

		try{
			countRowNumberStatement = connection.createStatement();
			countRowNumberResultSet = countRowNumberStatement.executeQuery(countRowNumberQuery);

			while(countRowNumberResultSet.next()){
				tableRowNumber = countRowNumberResultSet.getInt(1);
			}
		}
		catch(SQLException sqlException){
		}
		return tableRowNumber;
	} //테이블 내 튜플(행)의 개수 검색 함수 끝
	
	//휘처 테이블 안 모든 튜플 검색
	public String[] getNameProperty(){

		//해당 휘처에 해당하는 모든 필드 검색
		String findNamePropertyQuery = "select Name from Feature";
		String[] featurePropertyList = null;
		
		try{
			//해당 휘처의 이름 속성 목록
			findNamePropertyStatement = connection.createStatement();
			findNamePropertyResultSet = findNamePropertyStatement.executeQuery(findNamePropertyQuery);

			featurePropertyList = new String[getTableRowNumber()];					
			while(findNamePropertyResultSet.next()){ //ResultSet에 튜플(한행) 하나씩 담아 ResultSet의 next()메서드를 이용해 검색
				for(int index=0; index<featurePropertyList.length; index++){
						featurePropertyList[index] = findNamePropertyResultSet.getString(index + 1);
				}
			}		
		}
		catch(SQLException sqlException){
		}
		return featurePropertyList;
	} //휘처 테이블 안 모든 튜플 검색 함수 끝
	
	//새로운 서비스 등록
	public void addService(String name, String classification, String fType, String synonymousNames, double priority, 
							String condition, String definition, String serviceProperty, String relationalProperty,
							String identifier, String registrationAuthority){
		//테이블 생성		
		String createTableStatementQuery = "create table if not exists Feature";	//Feature 테이블 존재하지 않다면 생성        						 
		String tableFieldQuery = createTableStatementField();						//테이블 필드 구성

		//서비스 삽입 쿼리문
		String insertNewFeatureQuery = "INSERT INTO Feature VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)";
		try{
			//테이블 생성 처리
			createTableStatement = connection.createStatement();
			createTableStatement.executeUpdate(createTableStatementQuery + tableFieldQuery);   

			//테이블 생성 성공 메시지 박스 생성
			MessageBox createTableMessageBox = new MessageBox(MainFrame.shell, SWT.ICON_INFORMATION);
			createTableMessageBox.setText("Create Table Success");
			createTableMessageBox.setMessage("Create Table Success");
			createTableMessageBox.open();	

			//Identifying Attributes
			PreparedStatement insertNewFeature = connection.prepareStatement(insertNewFeatureQuery);
			insertNewFeature.setString(1, name);		//1,2,3... 가로
			insertNewFeature.setString(2, classification);
			insertNewFeature.setString(3, fType);
			insertNewFeature.setString(4, synonymousNames);
			insertNewFeature.setDouble(5, priority);
			insertNewFeature.setString(6, condition);
			insertNewFeature.setString(7, definition);

			//Selective Attributes
			insertNewFeature.setString(8, serviceProperty);

			//Relational Attributes
			insertNewFeature.setString(9, relationalProperty);

			//Managing Attributes
			insertNewFeature.setString(10, identifier);
			insertNewFeature.setString(11, registrationAuthority);

			insertNewFeature.executeUpdate();			
		}
		catch(SQLException sqlException){}				
	} //새로운 서비스 등록 함수 끝
	
	//테이블 필드 구성 - 총 11개 속성
	public String createTableStatementField(){
		//Identifying Attributes
		String tableFieldQuery = "(Name varchar(100), " +		//1
				"Classification varchar(100), " +				//2 
				"FType varchar(100), " +						//3
				"SynonymousNames varchar(100), " +				//4
				"Priority double, " +							//5
				"ConditionProperty varchar(100), " +			//6
				"Definition varchar(100), " +					//7

				//Selective Attributes
				"Property varchar(100), " +						//8 

	            //Relational Attributes
	            "RelationalProperty varchar(100), " +			//9

	            //Managing Attributes
	            "Identifier varchar(100), " +					//10
	            "RegistrationAuthority varchar(100))";			//11
		return tableFieldQuery;
	} //테이블 필드 구성 - 총 11개 속성 함수 끝
}