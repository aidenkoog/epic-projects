package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import Main.MainFrame;

public class DBConnection {
	
	//MySQL DBMS
	private static final String URL = "jdbc:mysql://localhost/Company";	//데이터베이스 이름 : Company(3306)
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
		try{ Class.forName("org.gjt.mm.mysql.Driver"); }
		catch(ClassNotFoundException e){
			//드라이버 찾기 에러 메시지 박스 생성
			this.showMessageBox("Driver Not Found", "Driver Not Found");
		}
	} //Driver 찾기 함수 끝
	
	//Connection 객체 생성
	private void createConnectionObject(){
		//DB서버와 연결하는 객체 생성
		try{ 
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); } 
		catch(SQLException sqlException){
			//DB연결 에러 메시지 박스 생성
			this.showMessageBox("Database Connect Error", "Database Connect Error");
		}
	} //Connection 객체 생성 함수 끝

	//메시지 박스 생성
	private void showMessageBox(String title, String message){
		MessageBox searchDriverErrorMessageBox = new MessageBox(MainFrame.shell, SWT.ICON_ERROR);
		searchDriverErrorMessageBox.setText(title);
		searchDriverErrorMessageBox.setMessage(message);
		searchDriverErrorMessageBox.open();	//에러 메시지 박스 표시
	} //메시지 박스 생성 함수 끝
}