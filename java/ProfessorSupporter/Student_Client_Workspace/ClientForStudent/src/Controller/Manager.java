package Controller;


import javax.swing.*;

import Chat.MakeRoomForm;
import Join.JoinForm;
import Login.LoginForm;
import MainScreen.MainFrame;
import ScreenShot.CaptureScreen;
import SplashScreen.SplashScreen;

import java.util.*;

//컨트롤러 클래스
public class Manager{
	private static HashMap<String, JFrame> commands;
	
	//폼들의 주소
	public static LoginForm LOGINFORM;
	public static MainFrame MAINFRAME;
	public static MakeRoomForm MAKEROOMFORM;
	public static JoinForm JOINFORM;
	public static SplashScreen SPLASHSCREEN;
	public static CaptureScreen captureScreen = null;
	
	static{
		commands = new HashMap<String, JFrame>();
		
		LOGINFORM = new LoginForm();
		MAINFRAME = new MainFrame();
		MAKEROOMFORM = new MakeRoomForm();
		JOINFORM = new JoinForm();
		captureScreen = new CaptureScreen();
		SPLASHSCREEN = new SplashScreen();
		
		commands.put("Login", MAINFRAME);
		commands.put("Abort", LOGINFORM);
		commands.put("MakeRoom", null);
		commands.put("Logout", LOGINFORM);
		commands.put("JoinRoom", null);
		commands.put("Update", null);
	}
	//키를 CommandSet으로 전달
	public void service(String command){
		if(commands.containsKey(command)){
			new CommandSet(command);
		}
		else{
			
		}
	}
	//키에 해당하는 프레임을 보여주는 메서
	public void showNextForm(String command){
		if(commands.containsKey(command)){
			commands.get(command).setVisible(true);
		}
	}
}