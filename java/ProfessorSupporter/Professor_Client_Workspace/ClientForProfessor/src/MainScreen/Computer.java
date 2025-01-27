package MainScreen;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import Communication.Transit;
import Controller.Manager;
import FileTransfer.SendFile;

import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

//컴퓨터 자리 클래스
public class Computer extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private String id = null;
	private String userName = null;
	private String startedTime = null;
	private boolean isOccupied = false;
	private int pcNumber = 0;
	private JLabel numberLabel = null;
	private JLabel nameLabel = null;
	private JLabel timeLabel = null;
	private ImageIcon img = null;
	private ImageIcon standardImg = new ImageIcon(".\\image\\컴퓨터패널기본.png");
	private ImageIcon connecteImg = new ImageIcon(".\\image\\컴퓨터패널접속.png");
	private JPopupMenu computerPopUpMenu = null;
	private JMenuItem privateChatPopUpMenuItem = null;		//1:1 대화
	private JMenuItem sendNotePopUpMenuItem = null;			//쪽지보내기
	private JMenuItem sendFilePopUpMenuItem = null;			//파일보내기
	private JMenuItem screenShotPopUpMenuItem = null;		//스크린 샷 찍기
	private JMenuItem seeMovingImagePopUpMenuItem = null;	//연속 화면 보기
	private JMenuItem seeMenuItem = null;	//감시

	//생성자
	public Computer(int pcNumber){
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		this.initializeMember(pcNumber);
		this.setSize(80, 100);
		this.setLayout(null);
		this.createLabel();
		this.createPopUpMenu();
		this.mountEventHandler();
	}

	public void changeComputerState(String name, String id){
		System.out.println("Occupied");
		this.id = id;
		this.userName = name;
		if(name != null){
			nameLabel.setText(name);
			img = connecteImg;
		}
		else if(name == null){
			nameLabel.setText("");
			img = standardImg;
		}
		repaint();
	}

	public void initializeMember(int pcNumber){
		this.pcNumber = pcNumber;
		img = standardImg;
	}

	public int getPCNumber(){
		return pcNumber;
	}

	public void createLabel(){
		numberLabel = new JLabel("PC-" + Integer.toString(pcNumber));
		numberLabel.setBounds(5, 3, 45, 22); 
		numberLabel.setFont(new Font("Gulim",Font.BOLD, 12));
		this.add(numberLabel);

		nameLabel = new JLabel("");
		nameLabel.setBounds(5, 35, 111, 19);
		this.add(nameLabel);

		timeLabel = new JLabel("");
		timeLabel.setBounds(12, 85, 97, 15);
		this.add(timeLabel);
	}

	public void occupyPosition(String userName, String startedTime, boolean isOccupied, ImageIcon img){
		this.userName = userName;
		this.startedTime = startedTime;
		this.isOccupied = isOccupied;
		this.img = img;
		nameLabel.setText(userName);
		timeLabel.setText(startedTime);
		this.repaint();
	}

	public void paintComponent(Graphics g){
		g.drawImage(img.getImage(),0, 0, 121, 126, null, null);
	}

	public void mountEventHandler(){
		this.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				//마우스 오른쪽 버튼
				if(e.getButton() == MouseEvent.BUTTON3){
					setUpEdge();
					//팝업 띄우기
					computerPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});

		this.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				//마우스 왼쪽 버튼
				if(e.getButton() == MouseEvent.BUTTON1){
					setUpEdge();
					Transit.sendMsg("사람정보표시/" + id + "/" + userName + "/" + pcNumber);
				}
			}
		});

		sendNotePopUpMenuItem.addActionListener(this);
		privateChatPopUpMenuItem.addActionListener(this);
		sendFilePopUpMenuItem.addActionListener(this);
		screenShotPopUpMenuItem.addActionListener(this);
		seeMovingImagePopUpMenuItem.addActionListener(this);
		seeMenuItem.addActionListener(this);
	}

	public void setUpEdge(){
		Vector<Computer> list = ComputerScreen.computerList;
		for(int i=0; i<list.size(); i++){
			if(list.get(i).getPCNumber() != pcNumber){
				list.get(i).setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			}else{
				list.get(i).setBorder(new LineBorder(new Color(0, 0, 0), 3));
			}
		}
	}

	public void actionPerformed(ActionEvent e){
		//팝업메뉴의 쪽지보내기
		if(e.getSource() == sendNotePopUpMenuItem){
			//메시지 입력받는 다이얼로그 띄움
			String msg = JOptionPane.showInputDialog("메시지를 입력하십시오.");
			//아무것도 입력 안했을 경우 수행
			if(msg.length() == 0){
				JOptionPane.showMessageDialog(this, "메시지를 입력해 주십시오.", "쪽지 에러", JOptionPane.WARNING_MESSAGE);
			}
			//입력 한 경우 수행
			else{
				Transit.sendMsg("쪽지/" + msg + "/" + this.id);
			}
		}
		//팝업메뉴의 1:1대화
		else if(e.getSource() == privateChatPopUpMenuItem){
			Transit.sendMsg("1:1대화요청/" + this.id);
		}
		//팝업메뉴의 파일보내기
		else if(e.getSource() == sendFilePopUpMenuItem){
			//선택한 유저 세팅
			JFileChooser fileOpen = new JFileChooser("C:\\");
			fileOpen.showOpenDialog(this);
			File fileName = fileOpen.getSelectedFile();

			if(fileName == null){
				JOptionPane.showMessageDialog(fileOpen, "전송할 파일을 선택해 주시기 바랍니다.", "File Send Error", JOptionPane.WARNING_MESSAGE);
			}
			else{
				new SendFile(Manager.MAINFRAME, fileName);
				Transit.sendMsg("파일전송/" + this.id);
			}
		}
		//팝업메뉴의 스크린샷 찍기
		else if(e.getSource() == screenShotPopUpMenuItem){
			Transit.sendMsg("스크린샷요청/" + this.id);
		}
		//팝업메뉴의 연속화면 보기
		else if(e.getSource() == seeMovingImagePopUpMenuItem){
			Transit.sendMsg("연속화면요청/" + this.id);
		}
		//팝업메뉴의 감시
		else if(e.getSource() == seeMenuItem){
			Transit.sendMsg("원격제어요청/" + this.id);
		}
	}

	//팝업 메뉴 생성
	private void createPopUpMenu(){
		//팝업 메뉴 생성
		computerPopUpMenu = new JPopupMenu();
		sendNotePopUpMenuItem = new JMenuItem("쪽지 보내기");  
		privateChatPopUpMenuItem = new JMenuItem("1:1 대화");    
		sendFilePopUpMenuItem = new JMenuItem("파일 보내기"); 
		screenShotPopUpMenuItem = new JMenuItem("스크린 샷 찍기");
		seeMovingImagePopUpMenuItem = new JMenuItem("연속 화면 보기");
		seeMenuItem = new JMenuItem("감시");

		//팝업 메뉴에 메뉴아이템 집어 넣기
		computerPopUpMenu.add(sendNotePopUpMenuItem);
		computerPopUpMenu.add(privateChatPopUpMenuItem);
		computerPopUpMenu.add(sendFilePopUpMenuItem);
		computerPopUpMenu.add(screenShotPopUpMenuItem);
		computerPopUpMenu.add(seeMovingImagePopUpMenuItem);
		computerPopUpMenu.addSeparator();
		computerPopUpMenu.add(seeMenuItem);
		
		//접속자 리스트에 팝업 메뉴 추가
		this.add(computerPopUpMenu);
	} //팝업 메뉴 생성 함수 끝
}