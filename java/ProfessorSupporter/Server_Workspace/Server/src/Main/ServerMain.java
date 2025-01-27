package Main;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import Server.Server;

public class ServerMain {
	//룩앤필 적용
	private static void applyLookAndFeel(){
		//룩앤필 불러오기
		try{
			UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
			JFrame.setDefaultLookAndFeelDecorated(true);
			System.out.println("[메인] 룩앤필 전용 성공");
		}catch(Exception e){
			System.out.println("[메인] 룩앤필 적용 실패");
			e.printStackTrace();
		}
	} //룩앤필 적용 함수 끝

	//메인
	public static void main(String[] args){
		applyLookAndFeel();	//룩앤필 적용
		try{
			new Server().start();						//서버 스레드 시작
		}catch(Exception e){
			System.out.println("[메인] 서버 시작에 실패하였습니다.");
			JOptionPane.showMessageDialog(null, "서버 시작에 실패하였습니다..", "서버 시작 에러", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
	}
}
