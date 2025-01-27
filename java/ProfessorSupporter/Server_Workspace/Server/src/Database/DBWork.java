package Database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBWork {

	//아이디가 존재 유무 검사
	public static boolean isIdInTheExistence(String id){
		Statement findIdStatement = null;
		ResultSet findIdResultSet = null;
		boolean valueToReturn = false;

		//해당 아이디에 해당하는 모든 필드 검색
		String findPropertyByServiceNameQuery = "select count(*) from Guest where id= '" + id + "'";
		int numberOfId = 0;

		try{
			findIdStatement = DBConnection.connection.createStatement();
			findIdResultSet = findIdStatement.executeQuery(findPropertyByServiceNameQuery);
			//ResultSet에 튜플(한행) 하나씩 담아 ResultSet의 next()메서드를 이용해 검색
			while(findIdResultSet.next()){ 
				//아이디에 해당하는 튜플 개수를 얻어옴
				numberOfId = findIdResultSet.getInt(1);
				//튜플 개수가 1개 이상이면
				if(numberOfId > 0){
					System.out.println("[DB] 해당 아이디가 이미 존재");
					valueToReturn = true;
				}else{
					System.out.println("[DB] 해당 아이디 사용 가능");
				}
			}
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}
		return valueToReturn;
	} //아이디가 존재 유무 검사 함수 끝

	//새로운 사람 등록
	public static boolean addPeople(String id, String password, String name, String email, String sex,
			String homepage, String phone, String cellPhone, String address, String introduction)
	{
		boolean valueToReturn = false;
		//사람 삽입 쿼리문
		String insertNewPeopleQuery = "INSERT INTO Guest VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try{
			PreparedStatement insertNewService = DBConnection.connection.prepareStatement(insertNewPeopleQuery);
			insertNewService.setString(1, id);		
			insertNewService.setString(2, password);
			insertNewService.setString(3, name);
			insertNewService.setString(4, email);
			insertNewService.setString(5, sex);
			insertNewService.setString(6, homepage);
			insertNewService.setString(7, phone);
			insertNewService.setString(8, cellPhone);
			insertNewService.setString(9, address);
			insertNewService.setString(10, introduction);
			insertNewService.executeUpdate();
			System.out.println("[DB] 회원 등록 완료");
			valueToReturn = true;
		}
		catch(SQLException e){
			System.out.println("[DB] 회원 등록 실패");
			return false;
		}
		return valueToReturn;
	} //새로운 사람 등록 함수 끝

	//아이디와 비밀번호가 매핑되는지 검사
	public static boolean isEqualIdAndPassword(String id, String password){
		Statement findPasswordStatement = null;
		ResultSet findPasswordResultSet = null;
		boolean valueToReturn = false;

		//해당 아이디에 해당하는 비밀번호 검색
		String findPasswordByIdQuery = "select Password from Guest where id= '" + id + "'";
		String resultOfQuery = null;

		try{
			findPasswordStatement = DBConnection.connection.createStatement();
			findPasswordResultSet = findPasswordStatement.executeQuery(findPasswordByIdQuery);
			//ResultSet에 튜플(한행) 하나씩 담아 ResultSet의 next()메서드를 이용해 검색
			while(findPasswordResultSet.next()){ 
				//아이디에 해당하는 비밀번호를 얻어옴
				resultOfQuery = findPasswordResultSet.getString(1);
				//찾은 비밀번호와 로그인창에 입력한 비밀번호가 같은지 검사
				if(resultOfQuery.equals(password)){
					System.out.println("아이디와 비밀번호가 일치합니다.");
					valueToReturn = true;
				}
			}
			System.out.println("아이디와 비밀번호가 불일치합니다.");
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}
		return valueToReturn;
	} //아이디와 비밀번호가 매핑되는지 검사하는 함수 끝

	//아이디에 해당하는 이름 얻기
	public static String getName(String id){
		Statement findNameStatement = null;
		ResultSet findNameResultSet = null;

		//해당 아이디에 해당하는 이름 검색
		String findNameByIdQuery = "select Name from Guest where id= '" + id + "'";
		String resultOfQuery = null;

		try{
			findNameStatement = DBConnection.connection.createStatement();
			findNameResultSet = findNameStatement.executeQuery(findNameByIdQuery);
			//ResultSet에 튜플(한행) 하나씩 담아 ResultSet의 next()메서드를 이용해 검색
			while(findNameResultSet.next()){ 
				//아이디에 해당하는 비밀번호를 얻어옴
				resultOfQuery = findNameResultSet.getString(1);
			}
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}
		return resultOfQuery;
	}

	//아이디와 이름 얻기
	public static String getIdAndName(){
		Statement findIdNameStatement = null;
		ResultSet findIdNameResultSet = null;

		//아이디와 이름 검색
		String findIdNameQuery = "select Id, Name from Guest";
		String resultOfQuery = "";

		try{
			findIdNameStatement = DBConnection.connection.createStatement();
			findIdNameResultSet = findIdNameStatement.executeQuery(findIdNameQuery);
			//ResultSet에 튜플(한행) 하나씩 담아 ResultSet의 next()메서드를 이용해 검색
			while(findIdNameResultSet.next()){ 
				resultOfQuery += "/" + findIdNameResultSet.getString(1) + "/";
				resultOfQuery += findIdNameResultSet.getString(2);
			}
			System.out.println(resultOfQuery);
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}
		return resultOfQuery;
	}
	//아이디와 이름에 해당하는 사람의 속성
	public static String getOneManProperty(String id, String name){
		Statement findIdNameStatement = null;
		ResultSet findIdNameResultSet = null;

		//아이디와 이름 검색 6개 속성 1,3,4,7,8,9
		String findIdNameQuery = "select Id, Name, Email, Phone, CellPhone, Address from Guest where Id="+ 
				"'" + id + "'" + " and " + "Name=" + "'" + name + "'";
		String resultOfQuery = "";

		try{
			findIdNameStatement = DBConnection.connection.createStatement();
			findIdNameResultSet = findIdNameStatement.executeQuery(findIdNameQuery);
			//ResultSet에 튜플(한행) 하나씩 담아 ResultSet의 next()메서드를 이용해 검색
			while(findIdNameResultSet.next()){ 
				resultOfQuery += "/아이디 : " + findIdNameResultSet.getString(1) + "/이름 : ";
				resultOfQuery += findIdNameResultSet.getString(2) + "/이메일 주소 : ";
				resultOfQuery += findIdNameResultSet.getString(3) + "/전화번호 : ";
				resultOfQuery += findIdNameResultSet.getString(4) + "/휴대폰 번호 : ";
				resultOfQuery += findIdNameResultSet.getString(5) + "/주소 : ";
				resultOfQuery += findIdNameResultSet.getString(6);
			}
			System.out.println(resultOfQuery);
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}
		return resultOfQuery;
	}

	//모든 아이디들 가져오기
	public static String getAllIdAndName(){
		Statement findIdNameStatement = null;
		ResultSet findIdNameResultSet = null;

		//아이디
		String findIdNameQuery = "select Id, Name from Guest";
		String resultOfQuery = "";

		try{
			findIdNameStatement = DBConnection.connection.createStatement();
			findIdNameResultSet = findIdNameStatement.executeQuery(findIdNameQuery);
			//ResultSet에 튜플(한행) 하나씩 담아 ResultSet의 next()메서드를 이용해 검색
			while(findIdNameResultSet.next()){ 
				resultOfQuery += "/"+findIdNameResultSet.getString(1)+"/";
				resultOfQuery += findIdNameResultSet.getString(2);
			}
			System.out.println("수정결과 : " + resultOfQuery);
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}
		return resultOfQuery;
	}

	//아이디와 이름에 해당하는 사람의 속성
	public static String getAllProperty(String id, String name){
		Statement findIdNameStatement = null;
		ResultSet findIdNameResultSet = null;

		//10개 속성
		String findIdNameQuery = "select * from Guest where Id="+ 
				"'" + id + "'" + " and " + "Name=" + "'" + name + "'";
		String resultOfQuery = "";

		try{
			findIdNameStatement = DBConnection.connection.createStatement();
			findIdNameResultSet = findIdNameStatement.executeQuery(findIdNameQuery);
			//ResultSet에 튜플(한행) 하나씩 담아 ResultSet의 next()메서드를 이용해 검색
			while(findIdNameResultSet.next()){ 
				resultOfQuery += "/" + findIdNameResultSet.getString(1) + "/";
				resultOfQuery += findIdNameResultSet.getString(2) + "/";
				resultOfQuery += findIdNameResultSet.getString(3) + "/";
				resultOfQuery += findIdNameResultSet.getString(4) + "/";
				resultOfQuery += findIdNameResultSet.getString(5) + "/";
				resultOfQuery += findIdNameResultSet.getString(6) + "/";
				resultOfQuery += findIdNameResultSet.getString(7) + "/";
				resultOfQuery += findIdNameResultSet.getString(8) + "/";
				resultOfQuery += findIdNameResultSet.getString(9) + "/";
				resultOfQuery += findIdNameResultSet.getString(10);
			}
			System.out.println(resultOfQuery);
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}
		return resultOfQuery;
	}

	//삭제
	public static boolean deleteInformation(String id){

		String deleteQuery = "delete from guest where Id= '" + id + "'";
		try{
			PreparedStatement deleteStatement = DBConnection.connection.prepareStatement(deleteQuery);
			deleteStatement.executeUpdate();
			return true;
		}
		catch(SQLException sqlException){
			System.out.println("학생정보 삭제 에러");
		}
		return false;
	} //선택된 서비스 삭제 함수 끝

	//수정
	public static boolean updateInformation(String id, String password, String name,	
			String email, String sex, String homepage, String phone, String cellphone,	
			String address, String introduction){

		//Update 쿼리문
		String updateQuery = "update guest set Id = ?, Password = ?, Name = ?, Email = ?, Sex = ?," +
				" Homepage = ?, Phone = ?, CellPhone = ?, Address = ?," +
				" Introduction = ? " + "where Id = ?";
		try{
			PreparedStatement updateStatement = DBConnection.connection.prepareStatement(updateQuery);
			updateStatement.setString(1, id);		
			updateStatement.setString(2, password);
			updateStatement.setString(3, name);
			updateStatement.setString(4, email);
			updateStatement.setString(5, sex);
			updateStatement.setString(6, homepage);
			updateStatement.setString(7, phone);
			updateStatement.setString(8, cellphone);
			updateStatement.setString(9, address);
			updateStatement.setString(10, introduction);
			updateStatement.setString(11, id);
			updateStatement.executeUpdate();
			return true;
		}
		catch(SQLException sqlException){
			System.out.println("학생정보수정 실패");
		}
		return false;
	} //서비스 속성값 수정 함수 끝

	//아이디와 이름에 해당하는 사람의 속성
	public static String getAllPropertyByName(String name){
		Statement findIdNameStatement = null;
		ResultSet findIdNameResultSet = null;

		//10개 속성
		String findIdNameQuery = "select * from Guest where Name=" + "'" + name + "'";
		String resultOfQuery = "";

		try{
			findIdNameStatement = DBConnection.connection.createStatement();
			findIdNameResultSet = findIdNameStatement.executeQuery(findIdNameQuery);
			//ResultSet에 튜플(한행) 하나씩 담아 ResultSet의 next()메서드를 이용해 검색
			while(findIdNameResultSet.next()){ 
				resultOfQuery += "/" + findIdNameResultSet.getString(1) + "-";
				resultOfQuery += findIdNameResultSet.getString(2) + "-";
				resultOfQuery += findIdNameResultSet.getString(3) + "-";
				resultOfQuery += findIdNameResultSet.getString(4) + "-";
				resultOfQuery += findIdNameResultSet.getString(5) + "-";
				resultOfQuery += findIdNameResultSet.getString(6) + "-";
				resultOfQuery += findIdNameResultSet.getString(7) + "-";
				resultOfQuery += findIdNameResultSet.getString(8) + "-";
				resultOfQuery += findIdNameResultSet.getString(9) + "-";
				resultOfQuery += findIdNameResultSet.getString(10);
			}
			System.out.println(resultOfQuery);
		}
		catch(SQLException sqlException){
			sqlException.printStackTrace();
		}
		return resultOfQuery;
	}

}