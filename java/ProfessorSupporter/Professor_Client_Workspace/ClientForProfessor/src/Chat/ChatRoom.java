package Chat;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import Communication.Transit;
import Controller.Manager;
import FileTransfer.SendFile;
import FontDialog.FontDialog;
import MainScreen.MainFrame;

public class ChatRoom extends JFrame implements ActionListener{
	//컨트롤러 클래스
	private Manager manager;
	
	//메뉴바
	private JMenuBar mb_Room_menubar;
	
	//파일 메뉴들
	private JMenu menu_Room_filemenu;
	private JMenuItem mi_Room_invite;
	private JMenuItem mi_Room_chat;
	private JMenuItem mi_Room_instantMsg;
	private JMenuItem mi_Room_sendfile;
	private JMenuItem mi_Room_out;
	
	//서식 메뉴들
	private JMenu menu_Room_optionmenu;
	private JMenuItem mi_Room_font;
	private JMenuItem mi_Room_color;
	
	private JPanel background;
	private ImageIcon img;
	
	//방에 접속한 유저 출력하는 리스트
	public JList jlt_Room_ids;
	//방대화 출력하는 창
	public JTextPane jtp_Room_chat;
	//귓속말 보내기 위해 방에 접속한 유저들을 담는 콤보박스
	public JComboBox jcb_Room_userlist;
	//메시지 쓰는 창
	private JTextField jtf_Room_write;
	//버튼들
	private JButton jb_Room_changeAlias;
	private JButton jb_Room_sendFile;
	private JButton jb_Room_sendMsg;
	private JButton jb_Room_out;
		
	//유저 리스트와 대화창을 담을 JScrollPane
	private JScrollPane jsp_Room_forIds;
	private JScrollPane jsp_Room_forChat;
	
	//팝업 메뉴들
	private JPopupMenu jpm_Room_popupforIds;
	private JMenuItem mi_Room_privatechat;
	private JMenuItem mi_Room_sendnote;
	private JMenuItem mi_Room_sendFile;
	private JMenuItem mi_Room_givegrant;
	private JMenuItem mi_Room_getout;
	
	//유저 리스트의 타이틀 보더
	private TitledBorder tborder_Room_forIds;
	
	//방 이름
	public String roomname;
	//폰트 이름
	private String fontName;
	//폰트 크기
	private int fontSize;
	//폰트 색깔
	private int fontColor;
	//귓속말 할 대상
	private String toUser;
	//1:1대화나 쪽지를 보낼 대상
	public String selectedId;
	//방장인지 여부 
	public boolean isCaptain;

	public ChatRoom(String id, String roomname, String personlimit, String currentNum){
		this.setIconImage(new ImageIcon(".\\image\\학사모.png").getImage());		//프레임 이미지
		//방정보
		this.setTitle(id + "님 채팅창 ( 방제 : " + roomname + " ) " + currentNum + "/" + personlimit);
		this.roomname = roomname;
		
		this.setSize(800, 450);
		//크기 조절 불가능
		this.setResizable(false);
		
		img = new ImageIcon("bg.gif"); //이미지..
		  
		background = new JPanel(null){
			public void paintComponent(Graphics e){ //대기실전체이미지
				e.drawImage(img.getImage(),0, 0, 800, 450, null, null);
			}
		};
		
		manager = new Manager();
		
		//귓속말을 보내기 위해 방에 입장한 유저들을 콤보박스에 담을 유저 목록
		Vector<String> userlist = new Vector<String>();
		//기본적으로 모두에게 메시지를 보낼 수 있게 추가
		userlist.add("모두에게");
		
		mb_Room_menubar 		= new JMenuBar();
		
		menu_Room_filemenu 		= new JMenu("메뉴");
		mi_Room_invite 			= new JMenuItem("초대하기");
		mi_Room_chat			= new JMenuItem("1:1대화");
		mi_Room_instantMsg		= new JMenuItem("쪽지보내기");
		mi_Room_sendfile		= new JMenuItem("파일보내기");
		mi_Room_out 			= new JMenuItem("나가기");
		
		menu_Room_optionmenu 	= new JMenu("서식");
		mi_Room_font 			= new JMenuItem("폰트");
		mi_Room_color 			= new JMenuItem("컬러");
		
		jlt_Room_ids 			= new JList();
		jtp_Room_chat 			= new JTextPane();
		jcb_Room_userlist 		= new JComboBox(userlist);
		jtf_Room_write 			= new JTextField();
		jb_Room_changeAlias 	= new JButton("닉네님 바꾸기");
		jb_Room_sendFile 		= new JButton("파일 보내기");
		jb_Room_sendMsg 		= new JButton("보내기");
		jb_Room_out 			= new JButton("나가기");
		
		jpm_Room_popupforIds 	= new JPopupMenu();
		mi_Room_privatechat 	= new JMenuItem("1:1대화");
		mi_Room_sendnote 		= new JMenuItem("쪽지 보내기");
		mi_Room_sendFile 		= new JMenuItem("파일 보내기");
		mi_Room_givegrant 		= new JMenuItem("방장 넘기기");
		mi_Room_getout 			= new JMenuItem("강퇴하기");
		
		//파일 메뉴에 들어갈 메뉴아이템 집어넣기
		menu_Room_filemenu.add(mi_Room_invite);
		menu_Room_filemenu.add(mi_Room_chat);
		menu_Room_filemenu.add(mi_Room_instantMsg);
		menu_Room_filemenu.add(mi_Room_sendfile);
		menu_Room_filemenu.add(mi_Room_out);
		
		//서식 메뉴에 들어갈 메뉴아이템 집어넣기
		menu_Room_optionmenu.add(mi_Room_font);
		menu_Room_optionmenu.add(mi_Room_color);
			
		//메뉴바에 메뉴 넣기
		mb_Room_menubar.add(menu_Room_filemenu);
		mb_Room_menubar.add(menu_Room_optionmenu);
		
		//팝업 메뉴에 메뉴아이템 집어 넣기
		jpm_Room_popupforIds.add(mi_Room_privatechat);
		jpm_Room_popupforIds.add(mi_Room_sendnote);
		jpm_Room_popupforIds.add(mi_Room_sendFile);
		jpm_Room_popupforIds.add(mi_Room_givegrant);
		jpm_Room_popupforIds.add(mi_Room_getout);
		
		//유저 리스트에 팝업 메뉴 추가
		jlt_Room_ids.add(jpm_Room_popupforIds);
		
		JButton[] buttons = {jb_Room_changeAlias, jb_Room_sendFile, jb_Room_sendMsg, jb_Room_out};
		
		//유저 리스트와 대화창을 JScrollPane에 얹기
		jsp_Room_forIds = new JScrollPane(jlt_Room_ids, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jsp_Room_forChat = new JScrollPane(jtp_Room_chat, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		//프레임이 유저 모니터의 정중앙에 위치하게 하기 위한 좌표
		int x = (int)this.getToolkit().getScreenSize().getWidth() / 2 - (int)this.getSize().getWidth() / 2;
		int y = (int)this.getToolkit().getScreenSize().getHeight() / 2 - (int)this.getSize().getHeight() / 2;
		
		//폰트 관련 기본적인 세팅
		fontName = "바탕";
		fontSize = 12;
		fontColor = -39322;
		toUser = "모두에게";
		
		//버튼들에 대한 서식 세팅
		for(int i = 0; i < buttons.length; i++){
			//버튼 폰트 정보 세팅
			buttons[i].setFont(new Font("Dialog", Font.BOLD, 12));
			//공백 제거
			buttons[i].setMargin(new Insets(0, 0, 0, 0));
			//버튼의 왼쪽 정렬
			buttons[i].setHorizontalTextPosition(JButton.LEFT);
		}
		
		//리스트에 "유저리스트"라는 타이틀을 왼쪽 위에 달기
		tborder_Room_forIds = BorderFactory.createTitledBorder("유저 리스트");
		tborder_Room_forIds.setTitleJustification(TitledBorder.LEFT);
		jsp_Room_forIds.setBorder(tborder_Room_forIds);
		
		//메뉴바를 프레임에 넣기
		setJMenuBar(mb_Room_menubar);
		
		//컴포넌트들 추가
		background.add(jsp_Room_forChat);
		background.add(jsp_Room_forIds);
		background.add(jcb_Room_userlist);
		background.add(jtf_Room_write);
		//background.add(jb_Room_changeAlias);
		//background.add(jb_Room_sendFile);
		background.add(jb_Room_sendMsg);
		background.add(jb_Room_out);
		
		//컴포넌트들 배치
		jsp_Room_forChat.setBounds(35, 15, 450, 250);
		jsp_Room_forIds.setBounds(515, 15, 250, 250);
		jcb_Room_userlist.setBounds(35, 350, 80, 23);
		jtf_Room_write.setBounds(120, 350, 465, 25);
		jb_Room_changeAlias.setBounds(590, 320, 90, 25);
		jb_Room_sendFile.setBounds(685, 320, 90, 25);
		jb_Room_sendMsg.setBounds(590, 350, 90, 25);
		jb_Room_out.setBounds(685, 350, 90, 25);
		
		add(background);
		
		DefaultListCellRenderer idList = (DefaultListCellRenderer)jlt_Room_ids.getCellRenderer();
		idList.setOpaque(false);
		
		background.setOpaque(false);
		jlt_Room_ids.setOpaque(false);
		jtp_Room_chat.setOpaque(false);
		jsp_Room_forChat.getViewport().setOpaque(false);
		jsp_Room_forIds.getViewport().setOpaque(false);
		jsp_Room_forChat.setOpaque(false);
		jsp_Room_forIds.setOpaque(false);
		jcb_Room_userlist.setOpaque(false);
		jtf_Room_write.setOpaque(false);
		jb_Room_changeAlias.setOpaque(false);
		jb_Room_sendFile.setOpaque(false);
		jb_Room_sendMsg.setOpaque(false);
		jb_Room_out.setOpaque(false);
		
		//메시지 전송 창에서 엔터키에 대한 이벤트
		this.jtf_Room_write.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				ChatRoom.this.sendMsg();
			}
		});
		
		//팝업 메뉴를 띄우기 위한 유저 리스트에 대한 이벤트
		jlt_Room_ids.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				//유저를 선택하고 마우스 오른쪽버튼을 눌렀을 때만 발생
				if(e.getButton() == MouseEvent.BUTTON3 && !jlt_Room_ids.isSelectionEmpty()){
					System.out.println("MouseEvent : " + isCaptain);
					//클라이언트가 손님이면 방장넘기기와 강퇴 기능 비활성화
					if(!isCaptain){
						mi_Room_givegrant.setEnabled(false);
						mi_Room_getout.setEnabled(false);
					}
					//클라이언트가 방장이면 방장넘기기와 강퇴 기능 다시 활성화
					else{
						mi_Room_givegrant.setEnabled(true);
						mi_Room_getout.setEnabled(true);
					}
					//선택한 유저를 담기
					selectedId = jlt_Room_ids.getSelectedValue().toString();
					System.out.println("selectedId : " + selectedId);
					//팝업 띄우기
					jpm_Room_popupforIds.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
		
		//이벤트 등록
		this.jcb_Room_userlist.addActionListener(this);
		this.jb_Room_changeAlias.addActionListener(this);
		this.jb_Room_sendMsg.addActionListener(this);
		this.jb_Room_sendFile.addActionListener(this);
		this.jb_Room_out.addActionListener(this);
		this.mi_Room_chat.addActionListener(this);
		this.mi_Room_instantMsg.addActionListener(this);
		this.mi_Room_sendfile.addActionListener(this);
		this.mi_Room_color.addActionListener(this);
		this.mi_Room_font.addActionListener(this);
		this.mi_Room_getout.addActionListener(this);
		this.mi_Room_givegrant.addActionListener(this);
		this.mi_Room_invite.addActionListener(this);
		this.mi_Room_out.addActionListener(this);
		this.mi_Room_privatechat.addActionListener(this);
		this.mi_Room_sendFile.addActionListener(this);
		this.mi_Room_sendnote.addActionListener(this);
		
		//프레임의 X를 눌렀을 때 발생하는 이벤트
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				ChatRoom.this.exit();
			}
		});
		
		//대화창 에디트 불가능
		this.jtp_Room_chat.setEditable(false);
		//프레임의 X를 눌렀을 때 아무런 동작을 안함
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//프레임위치 세팅
		this.setLocation(x, y);
		//프레임 보여줌
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ae){
		//닉네임 변경 버튼 눌렀을 때 수행
		if(ae.getSource() == jb_Room_changeAlias){
			//닉네임 입력받아서 세팅
			String ChangeAlias = JOptionPane.showInputDialog(this, "변경할 닉네임을 적어주세요");
			//변경한 닉네임이 있으면 수행
			if(ChangeAlias != null)
				//Flag정보 ChangeAlias 를 전송
				Transit.sendMsg("ChangeAlias/" + ChangeAlias + "/" + roomname);
		}
		//메시지 전송 버튼을 눌렀을 때 수행
		else if(ae.getSource() == jb_Room_sendMsg){
			sendMsg();
		}
		//메뉴의 서식에 컬러 메뉴아이템 눌렀을 때 수행
		else if(ae.getSource() == mi_Room_color){
			//컬러의 RGB코드 값 세팅
			fontColor = JColorChooser.showDialog(this, "글자색", this.jtp_Room_chat.getForeground()).getRGB();
		}
		//메뉴의 서식에 폰트 메뉴아이템 눌렀을 때 수행
		else if(ae.getSource() == mi_Room_font){
			FontDialog fontChooser = new FontDialog();
			//폰트 다이얼로그 창 보여주기
			int result = fontChooser.showDialog(this);
			//확인 버튼 눌렀을 때 수행
			if (result == FontDialog.OK_OPTION){
				//폰트를 선택한 폰트로 세팅
				Font font = fontChooser.getSelectedFont(); 
				//폰트 아무것도 선택하지 않으면 메서드 종료
				if(font == null){
					return;
				}
				//선택한 폰트 이름 세팅
				fontName = fontChooser.getSelectedFontFamily();
				//선택한 폰트 사이즈 세팅
				fontSize = fontChooser.getSelectedFontSize();
			}
		}
		//방 나가기 버튼이나 메뉴아이템 선택시 수행
		else if(ae.getSource() == mi_Room_out || ae.getSource() == jb_Room_out){
			//대기실 주소 갖고 옴
			MainFrame frame = Manager.MAINFRAME;
			//대기실 대화창을 클리어
			frame.getChatContent().setText("");
			//방에서 나가는 Flag 정보 전송
			Transit.sendMsg("Out/" + roomname + "/null");
//			frame.fontName = "바탕";
//			frame.fontSize = 12;
//			frame.fontColor = frame.contentOfChattingTextPane.getForeground().getRGB();
			frame.setFontName("바탕");
			frame.setFontSize(12);
			frame.setFontColor(frame.getChatContent().getForeground().getRGB());
			//방 안보이게 함
			this.setVisible(false);
			//방 자원해제
			this.dispose();
		}
		//콤보박스에서 유저 를 선택했을 때 발생
		else if(ae.getSource() == jcb_Room_userlist){
			//선택된 유저 세팅
			toUser = this.jcb_Room_userlist.getSelectedItem().toString();
		}
		//팝업 메뉴에서 강퇴 메뉴아이템 선택시 수행
		else if(ae.getSource() == mi_Room_getout){
			//강퇴 Flag 정보를 전송
			Transit.sendMsg("강퇴/" + roomname + "/" + selectedId);
		}
		//팝업 메뉴에서 방장넘기기 메뉴아이템을 선택시 수행
		else if(ae.getSource() == mi_Room_givegrant){
			//방장넘기기 Flag 정보를 전송
			Transit.sendMsg("방장넘기기/" + roomname + "/" + selectedId);
		}
		//파일 메뉴에서 초대 메뉴아이템을 선택시 수행
		else if(ae.getSource() == mi_Room_invite){
			//대기실에 있는 유저 목록을 불러오기 위한 Flag정보 전송
			Transit.sendMsg("대기방유저목록/" + roomname);
		}
		//파일 메뉴에서 1:1대화 메뉴아이템을 선택시 수행
		else if(ae.getSource() == mi_Room_chat){
			//프로그램에 접속한 모든 유저 목록을 불러오기 위한 Flag정보 전송
			Transit.sendMsg("전체유저/1:1대화/");
		}
		//파일 메뉴에서 쪽지 메뉴아이템을 선택시 수행
		else if(ae.getSource() == mi_Room_instantMsg){
			//프로그램에 접속한 모든 유저 목록을 불러오기 위한 Flag정보 전송
			Transit.sendMsg("전체유저/쪽지/");
		}
		//팝업 메뉴에서 1:1대화 메뉴아이템을 선택시 수행
		else if(ae.getSource() == mi_Room_privatechat){
			//선택된 아이디에 대화요청 Flag정보 전송
			Transit.sendMsg("1:1대화요청/" + selectedId);
		}
		//팝업 메뉴에서 1:1대화 메뉴아이템을 선택시 수행
		else if(ae.getSource() == mi_Room_sendFile || ae.getSource() == jb_Room_sendFile){
			//선택한 유저 세팅
			String selectedId = this.jlt_Room_ids.getSelectedValue().toString();
			
			if(jlt_Room_ids.isSelectionEmpty()){
				JOptionPane.showMessageDialog(this, "파일을 보낼 사람을 선택해 주시기 바랍니다.", "SendFile Error", JOptionPane.WARNING_MESSAGE);
			}
			
			//JFileChooser 열기
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
		//팝업 메뉴에서 쪽지 메뉴아이템 선택시 수행
		else if(ae.getSource() == mi_Room_sendnote){
			//입력된 메시지 세팅
			String msg = JOptionPane.showInputDialog("메시지를 입력하십시오.");
			//선택된 아이디에 쪽지 Flag정보 전송
			Transit.sendMsg("쪽지/" + msg + "/" + this.selectedId);
		}
		//메뉴에서 파일 보내기 메뉴아이템 선택시 수행
		else if(ae.getSource() == mi_Room_sendfile){
			Transit.sendMsg("전체유저/파일 보내기/");
		}
	}
	
	//방대화 이벤트 발생시 수
	private void sendMsg(){
		//입력한 메시지 입력 받아서 세팅
		String msg = jtf_Room_write.getText();
		//아무것도 입력 안했을 때 공백을 세팅
		if(msg.length() == 0){
			msg = " ";
		}
		//Flag정보 방대화 전송
		Transit.sendMsg("방대화/" + msg + "/" + fontName + "/" + fontSize + 
				"/" + fontColor + "/" + toUser + "/" + roomname);
		//메시지 전송 창 초기화
		jtf_Room_write.setText("");
	}
	
	//프로그램 종료할 때 수행
	private void exit(){
		//정말 종료할건지 물어보는 다이얼로그
		int x = JOptionPane.showConfirmDialog(this, "정말 끝내시겠습니까?", "Quit", JOptionPane.OK_CANCEL_OPTION, 
					JOptionPane.QUESTION_MESSAGE);
		//확인 버튼 눌렀을 때 수행
		if(x == JOptionPane.OK_OPTION){
			//Flag 정보 Out 전송
			Transit.sendMsg("Out/" + roomname + "/null");
			//Logout수행
			//manager.service("Logout");
			//프로그램 종료
			//System.exit(0);
			
			//대기실 주소 갖고 옴
			MainFrame frame = Manager.MAINFRAME;
			//대기실 대화창을 클리어
			frame.getChatContent().setText("");
			frame.setFontName("바탕");
			frame.setFontSize(12);
			frame.setFontColor(frame.getChatContent().getForeground().getRGB());
			//방 안보이게 함
			this.setVisible(false);
			//방 자원해제
			this.dispose();
		}
	}
}