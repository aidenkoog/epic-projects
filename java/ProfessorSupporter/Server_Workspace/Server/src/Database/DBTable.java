package Database;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTable {
	private Statement createTableStatement = null;

	//생성자
	public DBTable(){
		this.createTable();	//테이블 생성 함수 호출
	} //생성자 끝

	//테이블 생성
	private void createTable(){
		////////////////////////////////////////////////////////////////////////////////////
		//Table 구조
		String tableStructure = 	
				"(ID varchar(100) primary key, " +	//1
				"Password varchar(100), " +			//2
				"Name varchar(100), " +				//3
				"Email varchar(2000), " +			//4
				"Sex varchar(100), " +				//5
				"Homepage varchar(2000), " +		//6
				"Phone varchar(200), " +			//7
				"CellPhone varchar(200), " +		//8
				"Address TEXT, " +					//9
				"Introduction TEXT)";				//10
		////////////////////////////////////////////////////////////////////////////////////

		//테이블 생성을 위한 쿼리문		
		String createTableQuery = "create table if not exists Guest";	//Guest 테이블 존재하지 않다면 생성        						 

		//테이블 생성 처리
		try{
			createTableStatement = DBConnection.connection.createStatement();
			createTableStatement.executeUpdate(createTableQuery + tableStructure);	//쿼리문 + 테이블 구조 실행
			System.out.println("[DB] 테이블 생성");
		}catch(SQLException e){	
			System.out.println("[DB] 테이블 생성 실패"); 
		}
		//Close 작업
		try{
			createTableStatement.close();
		}catch(SQLException e){	
			e.printStackTrace(); 
		}
	} //테이블 생성 함수 끝
}