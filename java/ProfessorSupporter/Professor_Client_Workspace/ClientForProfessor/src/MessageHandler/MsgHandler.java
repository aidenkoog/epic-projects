package MessageHandler;


import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

import Chat.ChatRoom;
import Chat.ListForm;
import Chat.PrivateChatForm;
import Communication.Transit;
import Controller.Manager;
import FileTransfer.ReceiveFile;
import MainScreen.MainFrame;
import Note.NoteForm;
import Note.NoteReceiveForm;
import ScreenShot.ImageServer;

//서버로부터 전송된 메시지 처리하는 클래스
public class MsgHandler{
	//입장한 방 주소
	private static ChatRoom ROOM;
	//1:1대화창 주소
	private static PrivateChatForm pcf;
	//컨트롤러 클래스
	private Manager manager;

	public MsgHandler(String msg){
		manager = new Manager();
		executeMsg(msg);
	}

	private void executeMsg(String msg){

		System.out.println("메시지 실행 : " + msg);
		System.out.println("Client MsgHandler : " + msg);
		//들어오는 메시지를 파싱함
		StringTokenizer st = new StringTokenizer(msg, "/");
		//파싱한 것들을 배열에 담
		String[] arr = new String[st.countTokens()];
		for(int i = 0; st.hasMoreTokens(); i++){
			arr[i] = st.nextToken();
		}
		//중복확인 처리 후
		if(arr[0].equals("중복확인처리후")){
			if(arr[1].equals("중복")){
				JOptionPane.showMessageDialog(Manager.JOINFORM, "해당 아이디가 이미 존재합니다.", "아이디 중복", JOptionPane.ERROR_MESSAGE);
			}
			else if(arr[1].equals("중복아님")){
				JOptionPane.showMessageDialog(Manager.JOINFORM, "해당 아이디는 사용 가능합니다.", "사용 가능한 아이디", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		//회원가입 처리 후
		else if(arr[0].equals("회원가입처리후")){
			if(arr[1].equals("성공")){
				JOptionPane.showMessageDialog(Manager.JOINFORM, "회원 가입에 성공하였습니다.", "회원 가입 성공", JOptionPane.INFORMATION_MESSAGE);
				Manager.JOINFORM.dispose();
				Manager.JOINFORM.initializeRegisterForm();
				Manager.JOINFORM.cardLayout.show(Manager.JOINFORM.contentPane, "JoinPolicy");
			}
			else if(arr[1].equals("실패")){
				JOptionPane.showMessageDialog(Manager.JOINFORM, "회원 가입에 실패하였습니다.", "회원 가입 실패", JOptionPane.ERROR_MESSAGE);
			}
		}
		//메인중복확인 처리 후
		if(arr[0].equals("메인중복확인처리후")){
			if(arr[1].equals("중복")){
				JOptionPane.showMessageDialog(Manager.MAINFRAME, "해당 아이디가 이미 존재합니다.", "아이디 중복", JOptionPane.ERROR_MESSAGE);
			}
			else if(arr[1].equals("중복아님")){
				JOptionPane.showMessageDialog(Manager.MAINFRAME, "해당 아이디는 사용 가능합니다.", "사용 가능한 아이디", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		//메인회원가입 처리 후
		else if(arr[0].equals("메인회원가입처리후")){
			if(arr[1].equals("성공")){
				JOptionPane.showMessageDialog(Manager.MAINFRAME, "회원 가입에 성공하였습니다.", "회원 가입 성공", JOptionPane.INFORMATION_MESSAGE);
				Manager.MAINJOINFORM.initializeRegisterForm();
				Manager.MAINFRAME.getTabbedPane().remove(Manager.MAINJOINFORM);
			}
			else if(arr[1].equals("실패")){
				JOptionPane.showMessageDialog(Manager.MAINFRAME, "회원 가입에 실패하였습니다.", "회원 가입 실패", JOptionPane.ERROR_MESSAGE);
			}
		}
		//Flag정보가 Login인경우 수행
		else if(arr[0].equals("Login")){
			//아이디와 비밀 번호가 매치하는지 체크
			System.out.println("매치체크");
			boolean isSuccess = false;
			try{	
				isSuccess = Boolean.parseBoolean(arr[1]);
			}catch(Exception e){
				isSuccess = false;
			}
			//매치하면 수행
			if(isSuccess){
				System.out.println("매치");
				//로그인 창 안보이게 하기
				Manager.LOGINFORM.setVisible(false);
				//로그인창 자원해제
				Manager.LOGINFORM.dispose();
				//로딩화면 프로그레스 바 시작
				Manager.SPLASHSCREEN.startProgressBar();
				//				//대기실 폼 띄우기
				//				manager.showNextForm(arr[0]);
			}
			//비밀 번호 틀렸을 경우 다이얼로그 띄움
			else
				JOptionPane.showMessageDialog(Manager.LOGINFORM, "아이디와 비밀번호가 일치하지 않습니다.", "로그인 실패", 	JOptionPane.WARNING_MESSAGE);
		}
		//Flag정보가 Logout인 경우 수행
		else if(arr[0].equals("Logout")){
			//대기실 폼 띄움
			manager.showNextForm(arr[0]);
		}
		//Flag정보가 대기실손님목록인 경우 수행
		else if(arr[0].equals("대기실손님목록")){
			System.out.println(msg);
			//대기실 폼 주소 얻어옴
			MainFrame frame = Manager.MAINFRAME;

			//대기실에 접속해 있는 유저 목록 세팅
			String[] waitroomuserlist = userlist(msg);

			//대기실 콤보 박스에 유저 목록 붙이기
			for(int i = 1;  i < arr.length; i++){
				frame.getConnectedStudentComboBox().insertItemAt(waitroomuserlist[i-1], i);
			}
			//여러 사람 들어왔을 때 중복 돼는 유저 목록 삭제 
			if(frame.getConnectedStudentComboBox().getItemCount() > arr.length){
				for(int i = frame.getConnectedStudentComboBox().getItemCount(); i > arr.length; i--){
					frame.getConnectedStudentComboBox().removeItemAt(i - 1);
				}
			}

			//대기실 유저 리스트에 유저 목록 세팅
			frame.getConnectedStudentList().setListData(waitroomuserlist);

			//대기실 접속자 수 텍스트 필드에 접속자 수 세팅
			frame.getNumberOfConnector().setText(Integer.toString(waitroomuserlist.length));
		}
		//Flag정보가 대기실대화인 경우 수행
		else if(arr[0].equals("대기실대화")){  // 대기실대화/jtf_waitRoom_write.getText()/fontName/fontSize/fontColor
			//대기실 폼 주소 얻어옴
			MainFrame frame = Manager.MAINFRAME;
			System.out.println("대기실대화 : " + arr[1]);
			//Flag정보의 개수 얻어옴
			int templength = frame.getChatContent().getText().length();

			//메시지 대기실 대화창에 출력
			try{
				frame.getChatContent().getDocument().insertString(templength, arr[1] + "\n", 
						frame.getChatContent().getCharacterAttributes());
			}catch(BadLocationException e){}

			//폰트 이름, 폰트 크기, 색깔등의 정보가 없으면 빠져나감
			if(arr.length != 5){
				frame.getChatContent().setCaretPosition(frame.getChatContent().getText().length());
				return;
			}

			//폰트 이름, 폰트 크기, 색깔등의 정보가 있으면 대기실 대화창에 출력한 것을 폰트 정보에 맞게 바꿔줌
			final Style textStyle = frame.getChatContent().getStyledDocument().addStyle("font", null);
			StyleConstants.setFontFamily(textStyle, arr[2]);
			StyleConstants.setFontSize(textStyle, Integer.parseInt(arr[3]));
			frame.getChatContent().getStyledDocument().setCharacterAttributes(templength, 
					arr[1].length(), textStyle, false);

			final Style textColor = frame.getChatContent().getStyledDocument().addStyle("color", null);
			StyleConstants.setForeground(textColor, new Color(Integer.parseInt(arr[4])));
			frame.getChatContent().getStyledDocument().setCharacterAttributes(templength,
					arr[1].length(), textColor, false);
			frame.getChatContent().setCaretPosition(frame.getChatContent().getText().length());
		}
		//Flag정보가 RoomError인 경우 수행
		else if(arr[0].equals("RoomError")){
			//다이얼로그 띄움
			JOptionPane.showMessageDialog(Manager.MAKEROOMFORM, arr[1], 
					"MakeRoom Error", JOptionPane.WARNING_MESSAGE);
		}
		//Flag정보가 방손님목록인 경우 수행
		else if(arr[0].equals("방손님목록")){
			System.out.println(msg);
			//방에 접속해 있는 유저 목록 세팅
			String[] roomguestlist = userlist(msg);
			//방의 유저 리스트에 유저 목록 세팅
			ROOM.jlt_Room_ids.setListData(roomguestlist);

			//방 콤보박스에 유저 목록 세팅
			for(int i = 0;  i < roomguestlist.length; i++){
				ROOM.jcb_Room_userlist.insertItemAt(roomguestlist[i], i + 1);
			}
			//방 콤보박스에 중복되는 유저 목록이 있으면 삭제 
			if(ROOM.jcb_Room_userlist.getItemCount() > arr.length){
				for(int i = ROOM.jcb_Room_userlist.getItemCount(); i > arr.length; i--){
					ROOM.jcb_Room_userlist.removeItemAt(i - 1);
				}
			}			
		}
		//Flag정보가 MakeRoom인 경우 수행
		else if(arr[0].equals("MakeRoom")){  //	MakeRoom/alias/roomname/personlimit/currentnum
			//방만드는 폼 안보이게 함
			Manager.MAKEROOMFORM.setVisible(false);
			//방만드는 폼 자원해제
			Manager.MAKEROOMFORM.dispose();
			//대기실 폼 안보이게 함
			//Manager.MAINFRAME.setVisible(false);
			//방의 객체 생성
			ROOM = new ChatRoom(arr[1], arr[2], arr[3], arr[4]);
		}
		//Flag정보가 방목록인 경우 수행
		else if(arr[0].equals("방목록")){  // 방목록/([비번])roomname(currentguest/personlimit)
			//방목록을 배열에 세팅
			String[] temp = new String[arr.length - 1];

			for(int i = 0 ; i < temp.length; i++){
				temp[i] = arr[i+1];
				System.out.println("temp[" + i + "] : " + temp[i]);
			}

			//대기실 방 목록 리스트에 방 목록 세팅
			Manager.MAINFRAME.getChatRoomList().setListData(temp);
		}
		//Flag정보가 MakeRoom인 경우 수행
		else if(arr[0].equals("JoinRoom")){	// JoinRoom/captain/roomname/personlimit/currentnum 이 와야됨
			//정보가 덜 왔을 때 수행
			if(arr.length == 2){
				JOptionPane.showMessageDialog(Manager.MAINFRAME, arr[1], 
						"Join Error", JOptionPane.WARNING_MESSAGE);
			}
			//정보가 다 왔을 때 수행
			else{
				//대기실 창 안보이게 함
				//Manager.MAINFRAME.setVisible(false);
				//방 객체 생성
				ROOM = new ChatRoom(arr[1], arr[2], arr[3], arr[4]);
			}
		}
		//Flag정보가 대기실입장인 경우 수행
		else if(arr[0].equals("대기실입장")){
			//대기실 주소 얻어옴
			MainFrame frame = Manager.MAINFRAME;
			//최초로 들어온 사람인 경우 입장메시지 대기실 대화창에 출력( 대기실 대화창에 아무런 글이 없다면 insertString을 쓸수가 없다)
			if(frame.getChatContent().getText().length() == 0){
				frame.getChatContent().setText("[" + arr[1] + "]님이 접속하셨습니다.\n");
			}
			//그 외의 경우 위와 같은 작업 
			else{
				try{
					frame.getChatContent().getDocument().insertString(frame.getChatContent().getText().length(), 
							"[" + arr[1] + "]님이 접속하셨습니다.\n", frame.getChatContent().getCharacterAttributes());
				}catch(BadLocationException e){}
			}
			System.out.println("대기실입장 처리");
			frame.getChatContent().setCaretPosition(frame.getChatContent().getText().length());
		}
		//Flag정보가 대기실퇴장인 경우 수행
		else if(arr[0].equals("대기실퇴장")){
			//대기실 폼 주소 얻어옴
			MainFrame frame = Manager.MAINFRAME;
			//퇴장 메시지를 대기실 대화창에 출력
			try{
				frame.getChatContent().getDocument().insertString(frame.getChatContent().getText().length(), 
						"[" + arr[1] + "]님이 퇴장하셨습니다.\n", frame.getChatContent().getCharacterAttributes());
			}catch(BadLocationException e){}
			//대기실 대화창 스크롤을 자동으로 내려가게 하기 위해서 캐럿 위치를 맨 마지막으로 세팅
			frame.getChatContent().setCaretPosition(frame.getChatContent().getText().length());
		}
		//Flag정보가 방입장인 경우 수행
		else if(arr[0].equals("방입장")){
			//최초로 입장했을 경우 방 대화창에 입장 메시지 출력(방 대화창에 아무런 글이 없다면 insertString을 쓸수가 없다)
			if(ROOM.jtp_Room_chat.getText().length() == 0){
				ROOM.jtp_Room_chat.setText(arr[1] + "님이 접속하셨습니다.\n");
			}
			//그 외의 경우 위와 같은 작업
			else{
				try{
					ROOM.jtp_Room_chat.getDocument().insertString(ROOM.jtp_Room_chat.getText().length(), 
							arr[1] + "님이 접속하셨습니다.\n", ROOM.jtp_Room_chat.getCharacterAttributes());
				}catch(BadLocationException e){}
			}
			//방 대화창 스크롤을 자동으로 내려가게 하기 위해서 캐럿 위치를 맨 마지막으로 세팅
			ROOM.jtp_Room_chat.setCaretPosition(ROOM.jtp_Room_chat.getText().length());
		}
		//Flag정보가 방퇴장인 경우 수행
		else if(arr[0].equals("방퇴장")){
			//방 대화창에 퇴장 메시지 출력
			try{
				ROOM.jtp_Room_chat.getDocument().insertString(ROOM.jtp_Room_chat.getText().length(), 
						"[" + arr[1] + "]님이 퇴장하셨습니다.\n", ROOM.jtp_Room_chat.getCharacterAttributes());
			}catch(BadLocationException e){}

			ROOM.jtp_Room_chat.setCaretPosition(ROOM.jtp_Room_chat.getText().length());
		}
		//Flag정보가 ChangeAlias인 경우 수행
		else if(arr[0].equals("ChangeAlias")){
			JOptionPane.showMessageDialog(Manager.MAINFRAME, arr[1], "Change Alias", JOptionPane.INFORMATION_MESSAGE);
		}
		//Flag정보가 UpdateRoomInfo인 경우 수행
		else if(arr[0].equals("UpdateRoomInfo")){  //UpdateRoomInfo/roomname/captain/limit/currentguest
			//방 정보가 바뀔때마다 title 변경
			ROOM.setTitle(arr[2] + "님 채팅창 ( 방제 : " + arr[1] + " ) " + arr[4] + "/" + arr[3]);
		}
		//Flag정보가 대기실귓속말인 경우 수행
		else if(arr[0].equals("대기실귓속말")){	// 대기실귓속말/msg/sender/receiver
			//대기실 폼 주소 얻어옴
			MainFrame frame = Manager.MAINFRAME;
			//귓속말을 받는 유저의 대화창에만 출력
			String whisperString = "[" + arr[2] + "님이 보낸 귓속말] ";
			if(arr[4].equals("자기자신")){
				whisperString = "[" + arr[3] + "에게 보내는 귓속말] ";
			}
			try{
				frame.getChatContent().getDocument().insertString(frame.getChatContent().getText().length(), 
						whisperString + arr[1] + "\n", frame.getChatContent().getCharacterAttributes());
			}catch(BadLocationException e){}
			//귓속말을 받는 유저의 대화창의 스크롤바를 자동으로 내려주는 작업
			frame.getChatContent().setCaretPosition(frame.getChatContent().getText().length());
		}
		//Flag정보가 Error인 경우 수행
		else if(arr[0].equals("Error")){
			JOptionPane.showMessageDialog(Manager.MAINFRAME, arr[1], "Error", JOptionPane.WARNING_MESSAGE);
		}
		//Flag정보가 쪽지인 경우 수행
		else if(arr[0].equals("쪽지")){	// 쪽지/jtp_NoteForm_text.getText()/sender/receiver
			System.out.println(msg);
			//노트폼 객체 생성
			NoteForm nf = new NoteForm();
			//타이틀에 보내는 사람 닉네임 세팅
			nf.setTitle("[" + arr[2] + "]님이 보낸 쪽지");
			//노트폼의 JTextPane에 메시지 출력
			nf.jtp_NoteForm_text.setText(arr[1]);
		}
		//Flag정보가 1:1대화요청인 경우 수행
		else if(arr[0].equals("1:1대화요청")){	// 1:1대화요청/님이 1:1대화를 신청하셨습니다.\n수락하시겠습니까?/sender/receiver
			//수락할 건지 묻는 다이얼로그 띄움
			int x = JOptionPane.showConfirmDialog(Manager.MAINFRAME, "[" + arr[2] + "]" + arr[1], 
					arr[0], JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			//확인 버튼 누른 경우 수행
			if(x == JOptionPane.OK_OPTION){
				//서버에 수락했다는 메시지 전송
				Transit.sendMsg("1:1대화수락/true/" + arr[3] + "/" + arr[2]);
				//1:1대화창 띄움
				pcf = new PrivateChatForm(arr[2]);
			}
			//아이오 버튼 누른 경우 수행
			else{
				//서버에 거부했다는 메시지 전송
				Transit.sendMsg("1:1대화수락/false/" + arr[2]);
			}
		}
		//Flag정보가 1:1대화수락인 경우 수행
		else if(arr[0].equals("1:1대화수락")){	// 1:1대화수락/boolen/receiver/sender
			System.out.println("Client 1:1대화수락 : " + msg);
			//대화 수락한 경우 수행
			if(Boolean.parseBoolean(arr[1])){
				//1:1대화 폼 객체 생성
				pcf = new PrivateChatForm(arr[2]);
			}
			//대화 거부한 경우 다이얼로그 띄움
			else{
				JOptionPane.showMessageDialog(Manager.MAINFRAME, arr[2] + "님이 거절하셨습니다.", 
						"Private Chat Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		//Flag정보가 1:1대화인 경우 수행
		else if(arr[0].equals("1:1대화")){	// 1:1대화/msg/sender/receiver
			//1:1대화를 1:1대화창에 출력
			pcf.jtp_PrivateChatForm_text.setText(pcf.jtp_PrivateChatForm_text.getText() + 
					"[" + arr[2] + "]" + arr[1] + "\n");
			//대화창 스크롤을 자동으로 내림
			pcf.jtp_PrivateChatForm_text.setCaretPosition(pcf.jtp_PrivateChatForm_text.getText().length());
		}
		//Flag정보가 방대화인 경우 수행
		else if(arr[0].equals("방대화")){	// 방대화/msg/fontName/fontSize/fontColor
			System.out.println("방대화 : " + arr[1]);
			//방 대화창의 텍스트의 마지막 위치를 알아옴
			int templength = ROOM.jtp_Room_chat.getText().length();
			//메시지를 텍스트의 마지막 위치 다음에 이어 붙이는 작업
			try{
				ROOM.jtp_Room_chat.getDocument().insertString(templength, arr[1] + "\n", 
						ROOM.jtp_Room_chat.getCharacterAttributes());
			}catch(BadLocationException e){}

			//폰트 크기, 폰트 이름, 색깔 정보등이 없는 경우 수행
			if(arr.length != 5){
				ROOM.jtp_Room_chat.setCaretPosition(ROOM.jtp_Room_chat.getText().length());
				return;
			}

			//폰트 크기, 폰트 이름, 색깔 정보등이 있는 경우 정보에 맞게 출력한 메시지를 바꿔줌
			final Style textStyle = ROOM.jtp_Room_chat.getStyledDocument().addStyle("font", null);
			StyleConstants.setFontFamily(textStyle, arr[2]);
			StyleConstants.setFontSize(textStyle, Integer.parseInt(arr[3]));
			ROOM.jtp_Room_chat.getStyledDocument().setCharacterAttributes(templength, 
					arr[1].length(), textStyle, false);

			final Style textColor = ROOM.jtp_Room_chat.getStyledDocument().addStyle("color", null);
			StyleConstants.setForeground(textColor, new Color(Integer.parseInt(arr[4])));
			ROOM.jtp_Room_chat.getStyledDocument().setCharacterAttributes(templength,
					arr[1].length(), textColor, false);

			//방 대화창의 스크롤이 자동으로 내려가게 하는 작업
			ROOM.jtp_Room_chat.setCaretPosition(ROOM.jtp_Room_chat.getText().length());
		}
		//Flag정보가 방귓속말인 경우 수행
		else if(arr[0].equals("방귓속말")){	// 방귓속말/msg/sender/receiver
			System.out.println("Client 방귓속말 : " + msg);
			//메시지를 방 대화창에 출력 
			try{
				ROOM.jtp_Room_chat.getDocument().insertString(ROOM.jtp_Room_chat.getText().length(), 
						"[" + arr[2] + "님이 보낸 귓속말] " + arr[1] + "\n", ROOM.jtp_Room_chat.getCharacterAttributes());
			}catch(BadLocationException e){}
			//방 대화창의 스크롤이 자동으로 내려가게 하는 작업
			ROOM.jtp_Room_chat.setCaretPosition(ROOM.jtp_Room_chat.getText().length());
		}
		//Flag정보가 방장인 경우 수행
		else if(arr[0].equals("방장")){
			//자신이 방장인지 정보를 세팅
			ROOM.isCaptain = Boolean.parseBoolean(arr[1]);
		}
		//Flag정보가 대기방유저목록인 경우 수행
		else if(arr[0].equals("대기방유저목록")){
			//대기실에 있는 유저들의 목록을 세팅
			String[] guestlist = new String[arr.length -2];
			for(int i = 1; i < arr.length - 1; i++){
				guestlist[i-1] = arr[i+1];
			}
			//타이틀이 초대인 리스트 폼 객체를 생성
			new ListForm(arr[1], "초대", guestlist);
		}
		//Flag정보가 초대인 경우 수행
		else if(arr[0].equals("초대")){	// 초대/msg/roomname/selectedId
			//초대에 응할 건지를 묻는 다이얼로그 띄움
			int x = JOptionPane.showConfirmDialog(Manager.MAINFRAME, arr[1], "초대", 
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			//초대를 수락한 경우 수행
			if(x == JOptionPane.OK_OPTION){
				//Flag정보를 서버로 전송
				Transit.sendMsg("초대수락/" + arr[2] + "/" + arr[3]);
			}
		}
		//Flag정보가 강퇴인 경우 수행
		else if(arr[0].equals("강퇴")){ // 강퇴/msg/roomname/seletedId
			//강퇴당했다는 다이얼로그 띄움
			JOptionPane.showMessageDialog(Manager.MAINFRAME, arr[1], "강퇴", JOptionPane.INFORMATION_MESSAGE);
			//Flag정보를 서버로 전송(방에서 자신이 방 나가기한 것과 같은 기능)
			Transit.sendMsg("Out/" + arr[2] + "/null");
			//대기실 폼의 주소 얻어옴
			MainFrame frame = Manager.MAINFRAME;
			//대기실 폼의 대화창 리셋
			frame.getChatContent().setText("");
			//방 안보이게 함
			ROOM.setVisible(false);
			//방 자원해제
			ROOM.dispose();
			//대기실 보이게 함
			frame.setVisible(true);
		}
		//Flag정보가 방정보인 경우 수행
		else if(arr[0].equals("방정보")){ // 방정보/ids
			//방에 입장해있는 유저 목록 세팅
			String[] roomguest = userlist(msg);

			//타이틀이 방정보인 리스트폼 객체를 생성
			new ListForm(null, "방정보", roomguest);
		}
		//Flag정보가 전체유저인 경우 수행
		else if(arr[0].equals("전체유저")){ // 전체유저/타입/ids
			//대기실에 있는 유저의 목록을 세팅
			String[] allconnectedguest = new String[arr.length-2];
			for(int i = 2; i < allconnectedguest.length + 2; i++){
				allconnectedguest[i-2] = arr[i];
			}
			//타입에 따라 리스트 폼의 이벤트를 제어하기 위한 메서드 수행
			msgType(allconnectedguest, arr[1]);
		}
		//Flag정보가 방퇴장완료인 경우 수행
		else if(arr[0].equals("방퇴장완료")){
			//대기실 폼 주소 얻어옴
			MainFrame frame = Manager.MAINFRAME;
			//대기실 폼 안보이게 함
			frame.setVisible(true);
		}
		else if(arr[0].equals("파일전송수락")){	// 파일전송수락/senderIPaddr/sender/receiver
			int x = JOptionPane.showConfirmDialog(null, arr[2] + "님이 파일을 보내려고 합니다.\n수락하시겠습니까?", 
					"Receive File", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(x == JOptionPane.YES_OPTION){
				new ReceiveFile(Manager.MAINFRAME, arr[1]);
			}
			else{
				Transit.sendMsg("파일전송수락여부/false/" + arr[2] + "/" + arr[3]);
			}
		}
		//파일 받기를 거부 했을 때 수행
		else if(arr[0].equals("파일전송수락여부")){ // 파일전송수락여부/false/receiver/sender
			JOptionPane.showMessageDialog(null, arr[2] + "님이 거절하셨습니다.", "Send File Error", JOptionPane.WARNING_MESSAGE);
		}
		//교수 클라이언트에 컴퓨터 자리표시
		else if(arr[0].equals("컴퓨터표시")){ 	//컴퓨터표시/id/pw/pcNumber/교수여부/이름
			System.out.println("컴퓨터 자리 표시 진입");
			Manager.MAINFRAME.computerScreen.computerList.get(Integer.parseInt(arr[3])-1).changeComputerState(arr[5], arr[1]);
		}
		//교수 클라이언트 콤보박스 설정   "학생수정화면콤보박스세팅" + id/name/id/name ...
		else if(arr[0].equals("학생수정화면콤보박스세팅")){
			System.out.println("학생수정화면 콤보박스 세팅 진입!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			String[] idAndName = userlist(msg);
			Manager.UPDATEFORM.setComboBox(idAndName);
		}
		//교수 클라이언트 콤보박스 삭제설정   "학생삭제화면콤보박스세팅" + id/name/id/name ...
		else if(arr[0].equals("학생삭제화면콤보박스세팅")){
			System.out.println("학생삭제화면 콤보박스 세팅 진입!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			String[] idAndName = userlist(msg);
			Manager.DELETEFORM.setComboBox(idAndName);
		}
		//컴퓨터 자리 제거
		else if(arr[0].equals("컴퓨터자리제거")){	//컴퓨터자리제거/피시넘버
			System.out.println("컴퓨터 자리 제거 진입");
			Manager.MAINFRAME.computerScreen.computerList.get(Integer.parseInt(arr[1])-1).changeComputerState(null, null);
		}
		//전체쪽지 받기
		else if(arr[0].equals("전체쪽지")){	//전체쪽지/메시지/보낸사람
			System.out.println("전체쪽지 받기");
			new NoteReceiveForm(arr[1]).setVisible(true);
		}
		//사람정보리스트표시		사람정보리스트표시" + "/" + pc번호 + result/정보 6개
		else if(arr[0].equals("사람정보리스트표시")){	
			System.out.println("사람정보리스트에 표시");
			//대기실에 접속해 있는 유저 목록 세팅
			String[] userInformation = userlist(msg);
			//대기실 유저 리스트에 유저 목록 세팅
			MainFrame frame = Manager.MAINFRAME;
			frame.getInformationOfConnectedStudentList().setListData(userInformation);
		}
		//회원수정정보가져오기완료
		else if(arr[0].equals("회원수정정보가져오기완료")){
			System.out.println("회원수정정보가져오기완료 진입!!!!");
			String[] list = userlist(msg);
			Manager.UPDATEFORM.setComponentValue(list);
		}
		//회원수정정보가져오기완료
		else if(arr[0].equals("회원삭제정보가져오기완료")){
			System.out.println("회원삭제정보가져오기완료 진입!!!!");
			String[] list = userlist(msg);
			Manager.DELETEFORM.setComponentValue(list);
		}
		//회원정보수정완료
		else if(arr[0].equals("회원정보수정완료")){
			System.out.println("회원정보수정완료 확인버튼 진입!!!!");
			if(arr[1].equals("성공")){
				JOptionPane.showMessageDialog(Manager.UPDATEFORM, "학생정보 수정에 성공하였습니다.", "학생정보 수정", JOptionPane.INFORMATION_MESSAGE);
				//Manager.UPDATEFORM.initialize();
				//Manager.DELETEFORM.initialize();
				Manager.MAINFRAME.getTabbedPane().remove(Manager.UPDATEFORM);
			}
			else if(arr[1].equals("실패")){
				JOptionPane.showMessageDialog(Manager.UPDATEFORM, "학생정보 수정에 실패하였습니다.", "학생정보 수정", JOptionPane.ERROR_MESSAGE);
			}
		}
		//회원정보삭제완료
		else if(arr[0].equals("회원정보삭제완료")){
			System.out.println("회원정보삭제완료 확인버튼 진입!!!!");
			if(arr[1].equals("성공")){
				JOptionPane.showMessageDialog(Manager.DELETEFORM, "학생정보 삭제에 성공하였습니다.", "학생정보 삭제", JOptionPane.INFORMATION_MESSAGE);
				//Manager.UPDATEFORM.initialize();
				//Manager.DELETEFORM.initialize();
				Manager.MAINFRAME.getTabbedPane().remove(Manager.DELETEFORM);
			}
			else if(arr[1].equals("실패")){
				JOptionPane.showMessageDialog(Manager.DELETEFORM, "학생정보 삭제에 실패하였습니다.", "학생정보 삭제", JOptionPane.ERROR_MESSAGE);
			}
		}
		//학생검색완료
		else if(arr[0].equals("학생검색완료")){ //"학생검색완료/" + result + "/성공"
			System.out.println("학생검색완료 확인버튼 진입!!!!");
			if(arr[2].equals("성공")){
				String[] splitedArr =arr[1].split("-");
				Manager.SEARCHFORM.setComponentValue(splitedArr);
				Manager.MAINFRAME.getTabbedPane().addTab("학생정보 검색결과", Manager.SEARCHFORM);
				JOptionPane.showMessageDialog(Manager.SEARCHFORM, "학생검색에 성공하였습니다.", "학생정보 검색", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(arr[2].equals("실패")){
				JOptionPane.showMessageDialog(Manager.SEARCHFORM, "학생검색에 실패하였습니다.", "학생정보 검색", JOptionPane.ERROR_MESSAGE);
			}
		}
		//학생검색메뉴아이템완료
		else if(arr[0].equals("학생검색메뉴아이템완료")){ //"학생검색완료/" + result + "/성공"
			System.out.println("학생검색메뉴아이템완료 확인버튼 진입!!!!");
			if(arr[2].equals("성공")){
				String[] splitedArr =arr[1].split("-");
				Manager.SEARCHFORM.setComponentValue(splitedArr);
				Manager.MAINFRAME.getTabbedPane().addTab("학생정보 검색결과", Manager.SEARCHFORM);
				JOptionPane.showMessageDialog(Manager.SEARCHFORM, "학생검색에 성공하였습니다.", "학생정보 검색", JOptionPane.INFORMATION_MESSAGE);
				Manager.SFORM.setVisible(false);
			}
			else if(arr[2].equals("실패")){
				JOptionPane.showMessageDialog(Manager.SEARCHFORM, "학생검색에 실패하였습니다.", "학생정보 검색", JOptionPane.ERROR_MESSAGE);
			}
		}
		//프레임창 보이기
		else if(arr[0].equals("프레임창보이기")){
			Manager.MAINFRAME.imageServerStart();
		}
	}	


	//배열에 유저 목록 세팅
	private String[] userlist(String msg){
		String[] arr = msg.split("/");

		String[] roomguestlist = new String[arr.length - 1];

		for(int i = 1; i < arr.length; i++){
			roomguestlist[i - 1] = arr[i];
		}

		return roomguestlist;
	}

	//타입에 따라 리스트 폼의 이벤트를 제어하기 위한 메서드 수행
	private void msgType(String[] allconnectedguest, String type){
		if(type.equals("쪽지")){
			new ListForm(null, "쪽지", allconnectedguest);
		}
		else if(type.equals("1:1대화")){
			new ListForm(null, "1:1대화", allconnectedguest);
		}
		else if(type.equals("파일 보내기")){
			new ListForm(null, "파일 보내기", allconnectedguest);
		}
	}
}