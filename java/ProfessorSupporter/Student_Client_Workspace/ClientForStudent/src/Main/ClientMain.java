package Main;

import javax.swing.*;

import Communication.Transit;
import Controller.Manager;

public class ClientMain{
	//룩앤필 적용
	private static void applyLookAndFeel(){
		//룩앤필 불러오기
		try{
			UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
			JFrame.setDefaultLookAndFeelDecorated(true);
			System.out.println("룩앤필 적용 성공");
		}catch(Exception e){
			System.out.println("룩앤필 적용 실패");
			e.printStackTrace();
		}
	} //룩앤필 적용 함수 끝
	
	//메인
	public static void main(String[] args){
		applyLookAndFeel();	//룩앤필 적용
		try{
			new Transit().start();						//서버와 통신하는 쓰레드 시작
			Manager.SPLASHSCREEN.setVisible(true);		//로딩 화면 보이기
			Manager.SPLASHSCREEN.createLoginWindow();	//로그인 창 표시
		}catch(Exception e){
			JOptionPane.showMessageDialog(Manager.LOGINFORM, "서버와 연결이 되지 않습니다.", "연결 에러", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
	}
}