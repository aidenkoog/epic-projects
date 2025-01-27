package Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class DBConnection {
	//MySQL DBMS
	private static final String URL = "jdbc:mysql://localhost/Guest";	//데이터베이스 이름 : Guest(3306)
	private static final String USERNAME = "root";              	    //사용자명 root
	private static final String PASSWORD = "2001";                      //비밀번호 2001

	//데이터베이스 서버와 연결하는 Connection 참조 변수
	static Connection connection = null;

	//생성자
	public DBConnection(){
		this.findDriver();				//Driver 찾기 함수 호출
		this.createConnectionObject();	//Connection 객체 생성 함수 호출
	} //생성자 끝

	//Driver 찾기
	private void findDriver(){
		//드라이버 클래스를 검색
		try{ 
			Class.forName("org.gjt.mm.mysql.Driver");
			System.out.println("[DB] 데이터베이스 드라이버 검색 성공");
		}catch(ClassNotFoundException e){
			//드라이버 찾기 에러 메시지 박스 생성
			System.out.println("[DB] 데이터베이스 드라이버 검색 실패.");
			JOptionPane.showMessageDialog(null, "드라이버를 찾을 수 없습니다.", "프로그램 메시지", JOptionPane.WARNING_MESSAGE);
		}
	} //Driver 찾기 함수 끝

	//Connection 객체 생성
	private void createConnectionObject(){
		//DB서버와 연결하는 객체 생성
		try{ 
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			System.out.println("[DB] 데이터베이스에 연결 성공");
		}catch(SQLException sqlException){
			//DB연결 에러 메시지 박스 생성
			System.out.println("[DB] 데이터베이스에 연결 실패");
			JOptionPane.showMessageDialog(null, "데이터베이스를 연결할 수 없습니다.", "프로그램 메시지", JOptionPane.WARNING_MESSAGE);
		}
	} //Connection 객체 생성 함수 끝
}