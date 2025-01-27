package MainScreen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import java.awt.SystemColor;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JToggleButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;

import Communication.Transit;
import Controller.Manager;
import FileTransfer.SendFile;
import FontDialog.FontDialog;
import Note.NoteAllForm;
import ScreenShot.CaptureScreen;
import ScreenShot.ImageClient;


public class MainFrame extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	//파일메뉴
	private JMenu fileMenu = null;
	private JMenuItem logoutMenuItem = null;	//로그아웃
	private JMenuItem exitMenuItem = null;		//프로그램 종료
	//채팅메뉴
	private JMenu chatMenu = null;
	private JMenuItem createChatRoomMenuItem = null;	//방 만들기
	private JMenuItem enterChatRoomMenuItem = null;		//방 입장하기
	private JMenuItem privateChatMenuItem = null;		//1:1대화하기
	private JMenu mainChatFormMenu = null;				//메인 대화방 서식
	private JMenuItem changeFontMenuItem = null;		//글꼴 바꾸기
	private JMenuItem changeColorMenuItem = null;		//색상 바꾸기
	private JMenuItem sendNoteMenuItem = null;			//쪽지 보내기
	private JMenuItem sendAllNoteMenuItem = null;		//전체쪽지보내기 메뉴아이템
	//전송메뉴
	private JMenu sendMenu = null;
	private JMenuItem sendFileMenuItem = null;	//파일 보내기
	private JMenuItem sendMailMenuItem = null;	//메일 보내기
	//보기메뉴
	private JCheckBoxMenuItem showStudentListCheckMenuItem = null;	//접속자 목록 보이기
	private JCheckBoxMenuItem showChatListCheckMenuItem = null;		//대화 목록 보이기
	private JCheckBoxMenuItem showToolBarCheckMenuItem = null;		//툴바 보이기
	private JCheckBoxMenuItem showStatusBarCheckMenuItem = null; 	//상태표시바 보이기
	//네트워크메뉴
	private JMenu networkMenu = null;
	private JMenuItem showCurrentIpAddressMenuItem = null;	//현재 IP주소 보기
	//도움말메뉴
	private JMenuItem informationMenuItem = null;	//프로그램 정보
	//전체 레이아웃
	private JPanel mainWestPanel = null;	//좌측
	private JPanel statusBarPanel = null;	//하단
	private JPanel toolBarPanel = null;		//상단
	private JPanel mainEastPanel = null;	//우측
	//기본 툴바
	private JToggleButton showStudentListToggleButton = null;	//접속자 보이기
	private JToggleButton showChatListToggleButton = null;		//대화 목록 보이기
	private JToggleButton showStatusToggleButton = null;		//상태표시바 보이기
	private JButton sendNoteButton = null;		//쪽지
	private JButton chattingButton = null;		//채팅 
	private JButton createChatRoomButton = null;	//방만들기
	private JButton sendFileButton = null;			//파일보내기
	private JButton sendEmailButton = null;			//메일보내기
	//대기실 대화방
	private JList<String> chatRoomList = null;					//대화방 리스트
	private JTextPane contentOfChattingTextPane = null;			//대화내용
	private JComboBox<String> connectedStudentComboBox = null;	//접속자 목록 콤보박스
	private JTextField inputTextField = null;					//대화 내용 입력 텍스트 필드
	private JButton sendTextButton = null;						//보내기 버튼
	//접속자 목록
	public JList<String> connectedStudentList = null;
	private JList<String> informationOfStudentList = null;
	//접속자 목록 팝업 메뉴
	private JPopupMenu connectorListPopUpMenu = null;
	private JMenuItem privateChatPopUpMenuItem = null;
	private JMenuItem sendNotePopUpMenuItem = null;
	private JMenuItem sendFilePopUpMenuItem = null;
	
	private final String TITLE = "Professor Supporter";	//프레임창 제목
	private JLabel statusBarLabel = null;
	private JPanel contentPane = null;
	private Manager manager = new Manager();

	private String roomName = null;		//선택된 방 이름
	private String fontName = null;		//폰트 이름
	private int fontSize = 0;			//폰트 크기
	private int fontColor = 0;			//폰트 색깔
	private String toUser = null;		//귓속말 받을 사람
	private String selectedId = null;	//선택한 유저

	//생성자
	public MainFrame() {
		this.setIconImage(new ImageIcon(".\\image\\학사모학생.png").getImage());
		this.setTitle(TITLE);		//프레임창 제목
		this.setSize(731, 500); 	//프레임 사이즈
		this.displayCenter();		//프레임 위치
		this.createMenu();			//메뉴 생성
		this.initializeMember();	//멤버 변수 초기화
		this.createToolBar();		//상단-툴바 생성
		this.createConnectorList();	//좌측-접속자 목록 생성
		this.createStatusBar();		//하단-상태표시바 생성
		this.createChatList();		//우측-대화 목록 생성
		this.createPopUpMenu();		//팝업 메뉴 생성
		this.mountEventHandler();	//이벤트 처리기 등록
	} //생성자 끝

	//화면 중앙 표시
	private void displayCenter(){
		//화면의 크기
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();	
		//프레임의 크기
		Dimension frameDimension = this.getSize();
		//화면 넓이/2 - 프레임 넓이/2
		int xPosition = (int)(screenDimension.getWidth()/2 - frameDimension.getWidth()/2);
		//화면 높이/2 - 프레임 높이/2
		int yPosition = (int)(screenDimension.getHeight()/2 - frameDimension.getHeight()/2) - 20;
		this.setLocation(xPosition, yPosition);
	} //화면 중앙 표시 함수 끝

	//선택된 방이름 가져오기
	public String getSelectedRoomName(){
		return roomName;
	}
	//대화 내용 가져오기
	public JTextPane getChatContent(){
		return contentOfChattingTextPane;
	}
	//접속자 목록 콤보박스 가져오기
	public JComboBox<String> getConnectedStudentComboBox(){
		return connectedStudentComboBox;
	}
	//접속자 목록 가져오기
	public JList<String> getConnectedStudentList(){
		return connectedStudentList;
	}
	//대화방 목록 가져오기
	public JList<String> getChatRoomList(){
		return chatRoomList;
	}
	//접속자 정보 목록 가져오기
	public JList<String> getInformationOfStudentList(){
		return informationOfStudentList;
	}

	//폰트 이름 설정
	public void setFontName(String fontName){
		this.fontName = fontName;
	}
	//폰트 크기설정
	public void setFontSize(int fontSize){
		this.fontSize = fontSize;
	}
	//폰트 색상설정
	public void setFontColor(int fontColor){
		this.fontColor = fontColor;
	}

	//멤버 변수 초기화 및 설정
	private void initializeMember(){
		this.contentPane = new JPanel();		//컨텐츠 영역을 가져옴
		this.fontName = "바탕";
		this.fontSize = 12;
		this.fontColor = Color.BLACK.getRGB();
		this.toUser = "모두에게";
		this.setContentPane(contentPane);
	} //멤버 변수 초기화 및 설정 함수 끝

	//이벤트 처리기 등록
	private void mountEventHandler(){
		//파일메뉴
		logoutMenuItem.addActionListener(this);
		exitMenuItem.addActionListener(this);	
		//채팅메뉴
		createChatRoomMenuItem.addActionListener(this);
		enterChatRoomMenuItem.addActionListener(this);
		privateChatMenuItem.addActionListener(this);
		changeFontMenuItem.addActionListener(this);
		changeColorMenuItem.addActionListener(this);
		sendNoteMenuItem.addActionListener(this);
		sendAllNoteMenuItem.addActionListener(this);
		//전송 메뉴
		sendFileMenuItem.addActionListener(this);
		sendMailMenuItem.addActionListener(this);
		//보기메뉴
		showToolBarCheckMenuItem.addActionListener(this);
		showChatListCheckMenuItem.addActionListener(this);
		showStudentListCheckMenuItem.addActionListener(this);
		showStatusBarCheckMenuItem.addActionListener(this);		
		//네트워크 메뉴
		showCurrentIpAddressMenuItem.addActionListener(this);
		//도움말 메뉴
		informationMenuItem.addActionListener(this);
		//접속자 목록 팝업 메뉴
		sendNotePopUpMenuItem.addActionListener(this); 
		privateChatPopUpMenuItem.addActionListener(this);   
		sendFilePopUpMenuItem.addActionListener(this);
		//기본 툴바
		showChatListToggleButton.addActionListener(this);
		showStudentListToggleButton.addActionListener(this);
		showStatusToggleButton.addActionListener(this);
		sendNoteButton.addActionListener(this);
		chattingButton.addActionListener(this);
		createChatRoomButton.addActionListener(this);
		sendFileButton.addActionListener(this);
		sendEmailButton.addActionListener(this);
		//메시지 전송 창에서 엔터키에 대한 이벤트
		inputTextField.addActionListener(this);
		sendTextButton.addActionListener(this);
		connectedStudentComboBox.addActionListener(this);
		//X버튼 눌렀을 때 수행
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.out.println("[메인화면] X 버튼 클릭");
				MainFrame.this.exit();
			}
		});
		//프레임의 X를 눌렀을 때 아무런 동작을 안함
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//대화방 리스트
		chatRoomList.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				//방을 선택하고 마우스 오른쪽버튼을 눌렀을 때만 발생
				if(e.getButton() == MouseEvent.BUTTON3 && !chatRoomList.isSelectionEmpty()){
					//방 이름 세팅
					roomName = (String)chatRoomList.getSelectedValue();
					//비번방이면 방이름에서 [비번]제거
					if(roomName.startsWith("[비번]")){
						roomName = roomName.substring(roomName.indexOf("]") + 2);
					}
					//방이름에서 현재 인원수와 제한인원수 표시 제거(1\2)
					roomName = roomName.substring(0, roomName.indexOf("("));
					//서버에 Flag정보 날
					Transit.sendMsg("방정보/" + roomName);
				}
			}
		});
		//접속자 리스트
		connectedStudentList.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				//유저를 선택하고 마우스 오른쪽버튼을 눌렀을 때만 발생
				if(e.getButton() == MouseEvent.BUTTON3 && !connectedStudentList.isSelectionEmpty()){
					//선택한 유저를 담기
					selectedId = connectedStudentList.getSelectedValue().toString();
					//팝업 띄우기
					connectorListPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
				}
				//유저 선택 후 마우스 왼쪽
				else if(e.getButton() == MouseEvent.BUTTON1 && !connectedStudentList.isSelectionEmpty()){
					//선택한 유저를 담기
					selectedId = connectedStudentList.getSelectedValue().toString();
					Transit.sendMsg("사람정보표시/" + selectedId + "/" + "없음" + "/" + "없음");
				}
			}
		});
	} //이벤트 처리기 등록 함수 끝

	//메시지 입력창에 메시지를 입력하고 엔터나 버튼 눌렀을 경우 수행
	private void sendMessage(){
		String message = inputTextField.getText();
		if(message.length() == 0){
			message = " ";
		}
		Transit.sendMsg("대기실대화/" + message + "/" + fontName + "/" + fontSize + "/" + fontColor + "/" + toUser);
		inputTextField.setText("");
	}

	//이벤트 처리기
	public void actionPerformed(ActionEvent e){
		//프로그램 정보 메뉴
		if(e.getSource() == informationMenuItem){
			//메시지 대화상자 표시(어떤 것의 중앙, 메시지, 타이틀, 메시지대화유형)
			JOptionPane.showMessageDialog(this, "Professor Supporter\n구동완이 제작한 프로그램입니다.", "프로그램 정보", JOptionPane.INFORMATION_MESSAGE);
		}
		//종료 메뉴아이템
		else if(e.getSource() == exitMenuItem){
			this.exit();
		}
		//상태표시바 보이기 메뉴아이템이나 토글버튼
		else if(e.getSource() == showStatusBarCheckMenuItem || e.getSource() == showStatusToggleButton){
			if (statusBarPanel.isVisible()) {
				statusBarPanel.setVisible(false);
				showStatusToggleButton.setSelected(false);
				showStatusBarCheckMenuItem.setSelected(false);
			} else {
				statusBarPanel.setVisible(true);
				showStatusToggleButton.setSelected(true);
				showStatusBarCheckMenuItem.setSelected(true);
			}
		}
		//접속자 목록 보이기 메뉴아이템이나 토글버튼
		else if(e.getSource() == showStudentListToggleButton || e.getSource() == showStudentListCheckMenuItem)
		{
			if (mainWestPanel.isVisible()) {
				mainWestPanel.setVisible(false);
				showStudentListToggleButton.setSelected(false);
				showStudentListCheckMenuItem.setSelected(false);
			} else {
				mainWestPanel.setVisible(true);
				showStudentListToggleButton.setSelected(true);
				showStudentListCheckMenuItem.setSelected(true);
			}
		}
		//대확 목록 보이기 메뉴아이템이나 토글버튼
		else if(e.getSource() == showChatListToggleButton || e.getSource() == showChatListCheckMenuItem){
			if (mainEastPanel.isVisible()) {
				mainEastPanel.setVisible(false);
				showChatListToggleButton.setSelected(false);
				showChatListCheckMenuItem.setSelected(false);
			} else {
				mainEastPanel.setVisible(true);
				showChatListToggleButton.setSelected(true);
				showChatListCheckMenuItem.setSelected(true);
			}
		}
		//툴바 보이기 메뉴아이템
		else if(e.getSource() == showToolBarCheckMenuItem){
			if (toolBarPanel.isVisible()) {
				toolBarPanel.setVisible(false);
			} else {
				toolBarPanel.setVisible(true);
			}
		}
		//대화방 입장하기 메뉴아이템
		else if(e.getSource() == enterChatRoomMenuItem){
			//방을 선택 안했을 경우 수행
			if(chatRoomList.isSelectionEmpty()){
				JOptionPane.showMessageDialog(this, "입장할 방을 선택해 주시기 바랍니다.", "참가 에러", JOptionPane.WARNING_MESSAGE);
			}
			//방을 선택 했을 경우 수행
			else{
				//선택한 방 이름 세팅
				roomName = (String)chatRoomList.getSelectedValue();
				//방이름에서 현재 인원수와 제한인원수 표시 제거(1\2)
				roomName = roomName.substring(0, roomName.indexOf("("));
				//컨트롤러에 키값 전송
				manager.service("JoinRoom");
			}
		}
		//대화방 만들기 메뉴아이템이나 토글 버튼
		else if(e.getSource() == createChatRoomMenuItem || e.getSource() == createChatRoomButton){
			Manager.MAKEROOMFORM.setVisible(true);
		}
		//1:1대화 메뉴아이템
		else if(e.getSource() == privateChatMenuItem){
			Transit.sendMsg("전체유저/1:1대화/");
		}
		//쪽지 메뉴아이템
		else if(e.getSource() == sendNoteMenuItem){
			Transit.sendMsg("전체유저/쪽지/");
		}
		//전체 쪽지 보내기 아이템
		else if(e.getSource() == sendAllNoteMenuItem){
			new NoteAllForm().setVisible(true);
		}
		//글꼴 바꾸기 메뉴아이템
		else if(e.getSource() == changeFontMenuItem){
			FontDialog fontChooser = new FontDialog();
			//폰트 다이얼로그 띄움
			int result = fontChooser.showDialog(this);
			//확인 버튼 눌렀을 경우 수행
			if (result == FontDialog.OK_OPTION){
				//선택한 폰트 세팅
				Font font = fontChooser.getSelectedFont(); 
				//폰트 선택 안 된 경우 빠져나감
				if(font == null){
					return;
				}
				//폰트 이름 세팅
				fontName = fontChooser.getSelectedFontFamily();
				//폰트 크기 세팅
				fontSize = fontChooser.getSelectedFontSize();
			}
		}
		//색상 바꾸기 메뉴아이템
		else if(e.getSource() == changeColorMenuItem){
			fontColor = JColorChooser.showDialog(this, "글자색", this.contentOfChattingTextPane.getForeground()).getRGB();
		}
		//파일 보내기 메뉴아이템이나 토글버튼
		else if(e.getSource() == sendFileMenuItem || e.getSource() == sendFileButton){
			Transit.sendMsg("전체유저/파일 보내기/");
		}
		//메시지 전송 창에서 엔터키
		else if(e.getSource() == inputTextField){
			this.sendMessage();
		}
		//메시지 보내는 버튼
		else if(e.getSource() == sendTextButton){
			this.sendMessage();
		}
		//로그아웃 메뉴아이템
		else if(e.getSource() == logoutMenuItem){
			//로그아웃 여부 묻는 다이얼로그 띄움
			int x = JOptionPane.showConfirmDialog(this, "정말 로그 아웃 하시겠습니까?", 
					"Logout", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			//확인 눌렀을 때 수행
			if(x == JOptionPane.OK_OPTION){
				//대화창 리셋
				this.contentOfChattingTextPane.setText("");
				//대기실 폼 안보이게 함
				this.setVisible(false);
				//대기실 폼 자원해제
				this.dispose();
				//컨트롤러에 키값 전송
				manager.service("Logout");
			}
		}
		//접속자 목록 콤보박스
		else if(e.getSource() == connectedStudentComboBox){
			//선택한 유저 세팅
			toUser = this.connectedStudentComboBox.getSelectedItem().toString();
		}
		//팝업메뉴의 쪽지 보내기
		else if(e.getSource() == sendNotePopUpMenuItem){
			//메시지 입력받는 다이얼로그 띄움
			String msg = JOptionPane.showInputDialog("메시지를 입력하십시오.");
			//아무것도 입력 안했을 경우 수행
			if(msg.length() == 0){
				JOptionPane.showMessageDialog(this, "메시지를 입력해 주십시오.", "쪽지 에러", JOptionPane.WARNING_MESSAGE);
			}
			//입력 한 경우 수행
			else{
				Transit.sendMsg("쪽지/" + msg + "/" + this.selectedId);
			}
		}
		//팝업 메뉴의 1:1 대화
		else if(e.getSource() == privateChatPopUpMenuItem){
			Transit.sendMsg("1:1대화요청/" + selectedId);
		}
		//팝업 메뉴의 파일 보내기
		else if(e.getSource() == sendFilePopUpMenuItem){
			//선택한 유저 세팅
			//String selectedId = this.connectedStudentList.getSelectedValue().toString();
			JFileChooser fileOpen = new JFileChooser("C:\\");
			fileOpen.showOpenDialog(this);
			File fileName = fileOpen.getSelectedFile();

			if(fileName == null){
				JOptionPane.showMessageDialog(fileOpen, "전송할 파일을 선택해 주시기 바랍니다.", "File Send Error", JOptionPane.WARNING_MESSAGE);
			}
			else{
				new SendFile(Manager.MAINFRAME, fileName);
				Transit.sendMsg("파일전송/" + selectedId);
			}
		}
		//전체쪽지보내기 툴바아이템 
		else if(e.getSource() == sendNoteButton){
			new NoteAllForm().setVisible(true);  
		}
		//1:1대화 툴바아이템
		else if(e.getSource() == chattingButton){
			Transit.sendMsg("전체유저/1:1대화/");
		}
	}
	
	//종료 처리
	private void exit(){
		//정말 종료할건지 물어보는 다이얼로그
		int x = JOptionPane.showConfirmDialog(this, "정말 끝내시겠습니까?", "Quit", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.QUESTION_MESSAGE);
		//확인 버튼 눌렀을 때 수행
		if(x == JOptionPane.OK_OPTION){
			//Flag 정보 Out 전송
			manager.service("Logout");
			//프로그램 종료
			System.exit(0);
		}			
	}
	
	//팝업 메뉴 생성
	private void createPopUpMenu(){
		//팝업 메뉴 생성
		connectorListPopUpMenu = new JPopupMenu();
		sendNotePopUpMenuItem = new JMenuItem("쪽지 보내기");  
		privateChatPopUpMenuItem = new JMenuItem("1:1 대화");    
		sendFilePopUpMenuItem = new JMenuItem("파일 보내기"); 
		
		//팝업 메뉴에 메뉴아이템 집어 넣기
		connectorListPopUpMenu.add(sendNotePopUpMenuItem);
		connectorListPopUpMenu.add(privateChatPopUpMenuItem);
		connectorListPopUpMenu.add(sendFilePopUpMenuItem);
		
		//접속자 리스트에 팝업 메뉴 추가
		connectedStudentList.add(connectorListPopUpMenu);
	} //팝업 메뉴 생성 함수 끝

	//메뉴 생성
	private void createMenu(){
		//메뉴바 생성
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		//파일메뉴
		fileMenu = new JMenu("파일");
		menuBar.add(fileMenu);
		logoutMenuItem = new JMenuItem("로그아웃");
		fileMenu.add(logoutMenuItem);
		fileMenu.addSeparator();
		fileMenu.addSeparator();
		exitMenuItem = new JMenuItem("프로그램 종료");
		exitMenuItem.setIcon(new ImageIcon(".\\image\\종료(23x20).jpg"));
		fileMenu.add(exitMenuItem);

		//채팅메뉴
		chatMenu = new JMenu("채팅");
		menuBar.add(chatMenu);
		createChatRoomMenuItem = new JMenuItem("대화방 만들기");
		createChatRoomMenuItem.setIcon(new ImageIcon(".\\image\\방만들기메뉴(23x20).jpg"));
		chatMenu.add(createChatRoomMenuItem);
		enterChatRoomMenuItem = new JMenuItem("대화방 입장하기");
		chatMenu.add(enterChatRoomMenuItem);
		privateChatMenuItem	= new JMenuItem("1:1대화");
		chatMenu.add(privateChatMenuItem);
		chatMenu.addSeparator();
		mainChatFormMenu = new JMenu("메인 대화방 서식");
		chatMenu.add(mainChatFormMenu);
		changeFontMenuItem = new JMenuItem("글꼴 바꾸기");
		mainChatFormMenu.add(changeFontMenuItem);
		changeColorMenuItem = new JMenuItem("색상 바꾸기");
		mainChatFormMenu.add(changeColorMenuItem);
		chatMenu.addSeparator();
		chatMenu.addSeparator();
		sendNoteMenuItem = new JMenuItem("쪽지보내기");
		sendNoteMenuItem.setIcon(new ImageIcon(".\\image\\쪽지보내기메뉴(23x20).jpg"));
		chatMenu.add(sendNoteMenuItem);
		sendAllNoteMenuItem = new JMenuItem("전체 쪽지보내기");
		chatMenu.add(sendAllNoteMenuItem);

		//전송메뉴
		sendMenu = new JMenu("전송");
		menuBar.add(sendMenu);
		sendFileMenuItem = new JMenuItem("파일 보내기");
		sendMenu.add(sendFileMenuItem);
		sendMenu.addSeparator();
		sendMenu.addSeparator();
		sendMailMenuItem = new JMenuItem("이메일 보내기");
		sendMenu.add(sendMailMenuItem);

		//보기 메뉴
		JMenu viewMenu = new JMenu("보기");
		menuBar.add(viewMenu);
		showToolBarCheckMenuItem = new JCheckBoxMenuItem("툴바 보이기");
		viewMenu.add(showToolBarCheckMenuItem);
		viewMenu.addSeparator();
		showStudentListCheckMenuItem = new JCheckBoxMenuItem("접속한 학생 목록 보이기");
		viewMenu.add(showStudentListCheckMenuItem);
		showChatListCheckMenuItem = new JCheckBoxMenuItem("대화방 목록 보이기");
		viewMenu.add(showChatListCheckMenuItem); 
		viewMenu.addSeparator();
		showStatusBarCheckMenuItem = new JCheckBoxMenuItem("상태표시바 보이기");
		viewMenu.add(showStatusBarCheckMenuItem);
		networkMenu = new JMenu("네트워크");
		menuBar.add(networkMenu);
		showCurrentIpAddressMenuItem = new JMenuItem("현재 PC의 IP주소 보이기");
		networkMenu.add(showCurrentIpAddressMenuItem);
		//선택된 상태로 설정
		JMenuItem[] menuItem = {showToolBarCheckMenuItem, showStudentListCheckMenuItem,
				showChatListCheckMenuItem, showStatusBarCheckMenuItem};
		for(int i=0; i<menuItem.length; i++){
			menuItem[i].setSelected(true);
		}

		//도움말 메뉴
		JMenu helpMenu = new JMenu("도움말");
		menuBar.add(helpMenu);
		informationMenuItem = new JMenuItem("프로그램 정보");
		informationMenuItem.setIcon(new ImageIcon(".\\image\\정보(23x20).jpg"));
		helpMenu.add(informationMenuItem);
	} //메뉴 생성 함수 끝

	//툴바 생성
	private void createToolBar(){
		//컨텐츠 영역 전체 레이아웃
		contentPane.setLayout(new BorderLayout(0, 0));

		toolBarPanel = new JPanel();
		toolBarPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		contentPane.add(toolBarPanel, BorderLayout.NORTH);
		toolBarPanel.setLayout(new BoxLayout(toolBarPanel, BoxLayout.X_AXIS));
		JToolBar toolBar = new JToolBar();
		toolBarPanel.add(toolBar);
		toolBar.setBackground(SystemColor.controlHighlight);

		showStudentListToggleButton = new JToggleButton(new ImageIcon(".\\image\\목록보기(25x22).jpg"));
		toolBar.add(showStudentListToggleButton);
		showChatListToggleButton = new JToggleButton(new ImageIcon(".\\image\\채팅보이기(25x22).jpg"));
		toolBar.add(showChatListToggleButton);
		showStatusToggleButton = new JToggleButton(new ImageIcon(".\\image\\상태표시보기(25x22).jpg"));
		toolBar.add(showStatusToggleButton);
		toolBar.add(new JLabel(new ImageIcon(".\\image\\구분자(3x26).jpg")));
		sendNoteButton = new JButton(new ImageIcon(".\\image\\쪽지(25x22).jpg"));
		toolBar.add(sendNoteButton);
		chattingButton = new JButton(new ImageIcon(".\\image\\채팅(25x22).jpg"));
		toolBar.add(chattingButton);
		createChatRoomButton = new JButton(new ImageIcon(".\\image\\방만들기(25x22).jpg"));
		toolBar.add(createChatRoomButton);
		toolBar.add(new JLabel(new ImageIcon(".\\image\\구분자(3x26).jpg")));
		sendFileButton = new JButton(new ImageIcon(".\\image\\파일보내기(25x22).jpg"));
		toolBar.add(sendFileButton);
		sendEmailButton = new JButton(new ImageIcon(".\\image\\이메일보내기(25x22).jpg"));
		toolBar.add(sendEmailButton);
		//선택된 상태로 설정
		JToggleButton[] toggleButton= {showStudentListToggleButton, showChatListToggleButton, showStatusToggleButton};
		for(int i=0; i<toggleButton.length; i++){
			toggleButton[i].setSelected(true);
		}
	} //툴바 생성 함수 끝

	//상태표시바 생성
	private void createStatusBar(){
		statusBarPanel = new JPanel();
		statusBarPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		contentPane.add(statusBarPanel, BorderLayout.SOUTH);
		statusBarPanel.setLayout(new GridLayout(0, 1, 0, 0));

		statusBarLabel = new JLabel("대기");
		statusBarPanel.add(statusBarLabel);
	} //상태표시바 생성 함수 끝
	
	//접속자 목록 생성
	private void createConnectorList(){
		mainWestPanel = new JPanel();
		mainWestPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		contentPane.add(mainWestPanel, BorderLayout.WEST);
		mainWestPanel.setLayout(new BorderLayout(0, 0));
		//접속자들의 정보
		JPanel infomationOfStudentTitledPanel = new JPanel();
		infomationOfStudentTitledPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "접속자의 개인정보 및 PC정보", 
												TitledBorder.LEADING, TitledBorder.TOP, null, null));
		mainWestPanel.add(infomationOfStudentTitledPanel, BorderLayout.NORTH);
		infomationOfStudentTitledPanel.setLayout(new BorderLayout(0, 0));

		informationOfStudentList = new JList<String>();
		informationOfStudentList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		informationOfStudentList.setOpaque(false);
		JScrollPane informationOfStudentScrollPane = new JScrollPane(informationOfStudentList);
		informationOfStudentScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		infomationOfStudentTitledPanel.add(informationOfStudentScrollPane, BorderLayout.CENTER);
		//접속자 목록
		JPanel connectedStudentTitledPanel = new JPanel();
		connectedStudentTitledPanel.setBorder(new TitledBorder(null, "접속자 목록", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		mainWestPanel.add(connectedStudentTitledPanel, BorderLayout.CENTER);
		connectedStudentTitledPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane connectedStudentScrollPane = new JScrollPane();
		connectedStudentTitledPanel.add(connectedStudentScrollPane, BorderLayout.CENTER);

		connectedStudentList = new JList<String>();
		connectedStudentList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		connectedStudentScrollPane.setViewportView(connectedStudentList);
		connectedStudentList.setOpaque(false);
	} //접속자 목록 생성 함수 끝
	
	//대화 목록 생성
	private void createChatList(){
		mainEastPanel = new JPanel();
		mainEastPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		contentPane.add(mainEastPanel, BorderLayout.CENTER);
		mainEastPanel.setLayout(new BorderLayout(0, 0));

		//대화방 목록
		JPanel chatRoomTitledPanel = new JPanel();
		chatRoomTitledPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\uB300\uD654\uBC29 \uBAA9\uB85D", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		mainEastPanel.add(chatRoomTitledPanel, BorderLayout.NORTH);
		chatRoomTitledPanel.setLayout(new BorderLayout(0, 0));

		chatRoomList = new JList<String>();
		chatRoomList.setToolTipText("\uC120\uD0DD\uD55C \uCEF4\uD4E8\uD130\uC758 \uC790\uB9AC \uC815\uBCF4\uB97C \uBCF4\uC5EC\uC90D\uB2C8\uB2E4.");
		chatRoomList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		chatRoomList.setOpaque(false);
		JScrollPane chatRoomScrollPane = new JScrollPane(chatRoomList);
		chatRoomTitledPanel.add(chatRoomScrollPane, BorderLayout.CENTER);

		//대화 내용 목록
		JPanel contentOfChattingTitledPanel = new JPanel();
		contentOfChattingTitledPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\uB300\uD654 \uBAA9\uB85D", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		mainEastPanel.add(contentOfChattingTitledPanel, BorderLayout.CENTER);
		contentOfChattingTitledPanel.setLayout(new BorderLayout(0, 0));

		contentOfChattingTextPane = new JTextPane();
		contentOfChattingTextPane.setToolTipText("\uC811\uC18D\uC790\uB4E4\uC758 \uB85C\uADF8 \uAE30\uB85D\uC785\uB2C8\uB2E4.");
		contentOfChattingTextPane.setEditable(false);
		contentOfChattingTextPane.setSize(50, 100);

		JScrollPane contentOfChattingScrollPane = new JScrollPane(contentOfChattingTextPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		contentOfChattingTitledPanel.add(contentOfChattingScrollPane, BorderLayout.CENTER);
		Vector<String> userlist = new Vector<String>();
		userlist.add("모두에게");	//기본적으로 모두에게 보내게 설정
		connectedStudentComboBox = new JComboBox<String>(userlist);
		
		//대화 내용 입력
		JPanel inputTextPanel = new JPanel();
		inputTextPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentOfChattingTitledPanel.add(inputTextPanel, BorderLayout.SOUTH);
		inputTextPanel.setLayout(new BoxLayout(inputTextPanel, BoxLayout.X_AXIS));
		inputTextPanel.add(connectedStudentComboBox);

		inputTextField = new JTextField();
		inputTextPanel.add(inputTextField);
		inputTextField.setColumns(10);

		sendTextButton = new JButton("보내기");
		inputTextPanel.add(sendTextButton);
	} //대화 목록 생성 함수 끝
}