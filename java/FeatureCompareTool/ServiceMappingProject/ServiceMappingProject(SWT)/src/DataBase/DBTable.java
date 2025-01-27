package DataBase;
import DataBase.DBConnection;

import java.sql.SQLException;
import java.sql.Statement;

public class DBTable {
	private Statement createTableStatement = null;
	
	//생성자
	public DBTable(String companyName){
		this.createTable(companyName);	//테이블 생성 함수 호출
	} //생성자 끝

	//테이블 생성
	private void createTable(String companyName){
		////////////////////////////////////////////////////////////////////////////////////
		//Table 구조
		String tableStructure = 	//Identifying Attributes
									"(Name varchar(100) primary key, " +			//1
									"Definition TEXT, " +							//2
									"DomainTask varchar(100), " +					//3
									"SType varchar(2000), " +						//4
									"SynonymousNames varchar(100), " +				//5
									"Priority double, " +							//6
									"ConditionProperty varchar(100), " +			//7
									"Interface varchar(100), " +					//8
									"BusinessProcess varchar(2000), " +				//9
									"Component varchar(2000), " +					//10

		                             //Functional Attributes
		                             "MandatoryFunction TEXT, " +		//11
		                             "SecondaryFunction TEXT, " +		//12

		                             //Selective Attributes
		                             "Property varchar(100), " +		//13

		                             //Relational Attributes
		                             "andProperty TEXT, " +				//14
		                             "orProperty TEXT, " +				//15

		        					 //부모 서비스, 자식 서비스 속성
		        					 "ParentService varchar(100), " +	//16
		        					 "ChildService varchar(100))";		//17
		////////////////////////////////////////////////////////////////////////////////////
				
		//테이블 생성을 위한 쿼리문		
        String createTableQuery = "create table if not exists " + companyName;	//Company 테이블 존재하지 않다면 생성        						 
		
        //테이블 생성 처리
        try{
			createTableStatement = DBConnection.connection.createStatement();
			createTableStatement.executeUpdate(createTableQuery + tableStructure);	//쿼리문 + 테이블 구조 실행
		}catch(SQLException e){	e.printStackTrace(); }
        
        //Close 작업
        try{
        	createTableStatement.close();
        }catch(SQLException e){	e.printStackTrace(); }
	} //테이블 생성 함수 끝
}