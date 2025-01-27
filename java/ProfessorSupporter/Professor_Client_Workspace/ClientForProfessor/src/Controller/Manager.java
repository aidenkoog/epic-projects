package Controller;


import javax.swing.*;

import Chat.MakeRoomForm;
import Delete.DeleteForm;
import Join.JoinForm;
import Join.MainJoinForm;
import Login.LoginForm;
import MainScreen.MainFrame;
import ScreenShot.CapturedScreen;
import Search.SearchForm;
import Search.SearchResultForm;
import SplashScreen.SplashScreen;
import Update.UpdateForm;
import Welcome.WelcomeProgram;

import java.util.*;

//컨트롤러 클래스
public class Manager{
	private static HashMap<String, JFrame> commands;
	
	//폼들의 주소
	public static LoginForm LOGINFORM;
	public static MainFrame MAINFRAME;
	public static MakeRoomForm MAKEROOMFORM;
	public static JoinForm JOINFORM;
	public static MainJoinForm MAINJOINFORM;
	public static UpdateForm UPDATEFORM;
	public static DeleteForm DELETEFORM;
	public static SearchResultForm SEARCHFORM;
	public static SearchForm SFORM;
	public static WelcomeProgram WELCOMEPROGRAM;
	public static CapturedScreen CAPTUREDSCREEN;
	public static SplashScreen SPLASHSCREEN;
	
	static{
		commands = new HashMap<String, JFrame>();
		
		LOGINFORM = new LoginForm();
		MAINFRAME = new MainFrame();
		MAKEROOMFORM = new MakeRoomForm();
		JOINFORM = new JoinForm();
		MAINJOINFORM = new MainJoinForm();
		UPDATEFORM = new UpdateForm();
		DELETEFORM = new DeleteForm();
		SEARCHFORM = new SearchResultForm();
		SFORM = new SearchForm();
		WELCOMEPROGRAM = new WelcomeProgram();
		CAPTUREDSCREEN = new CapturedScreen();
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