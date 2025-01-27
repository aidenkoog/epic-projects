package Controller;


import javax.swing.*;

import Communication.Transit;
import Login.LoginForm;


public class CommandSet{
	//메니저 클래스에서 키값을 받음
	public CommandSet(String key){
		Manager manager = new Manager();
		
		//키값이 Join일 때 수행 
		if(key.equals("Join")){
			//회원 가입창 띄움
			manager.showNextForm(key);
		}
		//키값이 Abort일 때 수행
		else if(key.equals("Abort")){
			//로그인 폼 띄움
			manager.showNextForm(key);
		}
		//키값이 Login인 경우 수행
		else if(key.equals("Login")){
			//로그인 폼의 주소얻어옴
			LoginForm frame = Manager.LOGINFORM;
			
			//입력한 정보 세팅
			String id = frame.getID();
			String pw = frame.getPassword();
			String pcNumber = frame.getPCNumber();
			Manager.MAINFRAME.setTitle(id);
			//서버로 전송
			System.out.println("[로그인 창 - 로그인 버튼] 아이디, 비밀번호를 서버로 전송");
			Transit.sendMsg("Login/" + id + "/" + pw + "/" + pcNumber + "/" + "false");
		}
		//키 값이 Logout인 경우 수행
		else if(key.equals("Logout")){
			//서버로 Flag정보 보내줌
			Transit.sendMsg("Logout/");
		}
		//키 값이 MakeRoom인 경우 수행
		else if(key.equals("MakeRoom")){
			//방의 정보를 얻어와서 세팅
			String roominfo = Manager.MAKEROOMFORM.getInfo();
			//방 정보를 서버로 전송
			Transit.sendMsg("MakeRoom/" + roominfo);
		}
		//키 값이 JoinRoom인 경우 수행
		else if(key.equals("JoinRoom")){
			//방이릉 세팅
			String roomname = Manager.MAINFRAME.getSelectedRoomName();
			//비번 방인 경우 수행
			if(roomname.startsWith("[")){
				//입력한 비밀번호 세팅
				String roompw = JOptionPane.showInputDialog(Manager.MAINFRAME, "비밀번호를 입력하십시오.");
				//비밀 번호 입력하지 않았을 경우 수행
				if(roompw == null){
					JOptionPane.showMessageDialog(Manager.MAINFRAME, "비밀번호를 입력하지 않으셨습니다.", "PW Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
				//서버에 Flag정보 전송
				Transit.sendMsg("JoinRoom/" + roomname.substring(roomname.indexOf("]") + 2) + "/" + roompw);
			}
			else{
				//서버에 Flag정보 전송
				Transit.sendMsg("JoinRoom/" + roomname + "/" + null);
			}
		}
		//키 값이 Adjustment인 경우 수행
		else if(key.equals("Adjustment")){
			//서버에 Flag정보 전송
			Transit.sendMsg("Adjustment/");
		}
	}
}