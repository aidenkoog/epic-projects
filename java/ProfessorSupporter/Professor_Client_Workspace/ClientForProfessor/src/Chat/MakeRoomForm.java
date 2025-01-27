package Chat;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Controller.Manager;

public class MakeRoomForm extends JFrame implements ActionListener, ItemListener{
	//라벨들
	private JLabel jl_MakeRoomForm_title;
	private JLabel jl_MakeRoomForm_roomname;
	private JLabel jl_MakeRoomForm_roompw;
	private JLabel jl_MakeRoomForm_personnumber;
	//접속 제한 인원 세팅하는 콤보박스
	private JComboBox jcb_MakeRoomForm_personnumber;
	//정보 입력 창
	private JTextField jtf_MakeRoomForm_roomname;
	private JTextField jtf_MakeRoomForm_roompw;
	//버튼들
	private JButton jb_MakeRoomForm_makeroom;
	private JButton jb_MakeRoomForm_cancle;
	
	//컨트롤러 클래스
	private Manager manager;
	//제한 인원과 폰트 색을 기본적으로 선택돼게 함
	private String personlimit = "2";
	private String titlecolor = "BLACK";
	
	public MakeRoomForm(){
		super("방만들기");
		
		this.setLayout(null);
		this.setSize(270, 250);
		//크기 조절 불가능
		this.setResizable(false);
		
		manager = new Manager();
		
		//방 제한 인원
		String[] person = {"2", "4", "8", "16"};
		
		Container con = this.getContentPane();
		
		jl_MakeRoomForm_title    		= new JLabel("방 만들기");
		jl_MakeRoomForm_roomname  		= new JLabel("방제목");
		jl_MakeRoomForm_roompw			= new JLabel("비밀번호");
		jl_MakeRoomForm_personnumber	= new JLabel("인원");
		jcb_MakeRoomForm_personnumber 	= new JComboBox(person);
		jtf_MakeRoomForm_roomname 		= new JTextField(20);
		jtf_MakeRoomForm_roompw			= new JTextField(20);
		jb_MakeRoomForm_makeroom 		= new JButton("만들기");
		jb_MakeRoomForm_cancle 			= new JButton("취소");
		
		//버튼 서식 세팅
		jb_MakeRoomForm_makeroom.setFont(new Font("Dialog", Font.BOLD, 12));
		jb_MakeRoomForm_makeroom.setMargin(new Insets(0, 0, 0, 0));
		jb_MakeRoomForm_makeroom.setHorizontalTextPosition(JButton.LEFT);
		
		jb_MakeRoomForm_cancle.setFont(new Font("Dialog", Font.BOLD, 12));
		jb_MakeRoomForm_cancle.setMargin(new Insets(0, 0, 0, 0));
		jb_MakeRoomForm_cancle.setHorizontalTextPosition(JButton.LEFT);
		
		//프레임에 컴포넌트 붙임
		con.add(jl_MakeRoomForm_title);
		con.add(jl_MakeRoomForm_roomname);
		con.add(jl_MakeRoomForm_roompw);
		con.add(jl_MakeRoomForm_personnumber);
		con.add(jcb_MakeRoomForm_personnumber);
		con.add(jtf_MakeRoomForm_roomname);
		con.add(jtf_MakeRoomForm_roompw);
		con.add(jb_MakeRoomForm_makeroom);
		con.add(jb_MakeRoomForm_cancle);
	    
		//컴포넌트 세팅
		jl_MakeRoomForm_title.setBounds(108, 3, 80, 25);
		jl_MakeRoomForm_roomname.setBounds(5, 30, 80, 25);
		jl_MakeRoomForm_roompw.setBounds(5, 60, 80, 25);
		jl_MakeRoomForm_personnumber.setBounds(5, 100, 80, 25);
		jcb_MakeRoomForm_personnumber.setBounds(50, 100, 50, 25);
		jtf_MakeRoomForm_roomname.setBounds(60, 30, 200, 25);
		jtf_MakeRoomForm_roompw.setBounds(60, 60, 200, 25);
		jb_MakeRoomForm_makeroom.setBounds(60, 150, 65, 30);
		jb_MakeRoomForm_cancle.setBounds(140, 150, 65, 30);
		
		//프레임이 유저의 화면 정중앙에 위치하게 하기 위한 좌표
		int x = (int)this.getToolkit().getScreenSize().getWidth() / 2 - (int)this.getSize().getWidth() / 2;
		int y = (int)this.getToolkit().getScreenSize().getHeight() / 2 - (int)this.getSize().getHeight() / 2;
		
		//프레임 띄울 좌표 세팅
		this.setLocation(x, y);
		//프레임의 X눌렀을 때 아무 작업 안하게 세팅
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//프레임의 X눌렀을 때 수행
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				//프레임 안보이게 함
				MakeRoomForm.this.setVisible(true);
				//자원해제
				MakeRoomForm.this.dispose();
			}
		});
		
		//액션 등록
		jcb_MakeRoomForm_personnumber.addItemListener(this);
		jb_MakeRoomForm_makeroom.addActionListener(this);
		jb_MakeRoomForm_cancle.addActionListener(this);
	}
	
	//방 정보 얻는 메서드
	public String getInfo(){
		//입력한 방이름 세팅
		String roomname = this.jtf_MakeRoomForm_roomname.getText();
		//입력한 비밀번호 세팅
		String roompw = this.jtf_MakeRoomForm_roompw.getText();
		return roomname + "/" + personlimit + "/" + titlecolor + "/" + roompw;
	}

	public void actionPerformed(ActionEvent e){
		//방만들기 버튼 눌렀을 때 수행
		if(e.getSource() == jb_MakeRoomForm_makeroom){
			//컨트롤러에 수행 할 키 값 전송
			manager.service("MakeRoom");
			jtf_MakeRoomForm_roomname.setText("");
			jtf_MakeRoomForm_roompw.setText("");
		}
		//취소 버튼 눌렀을 때 수행
		else{
			//프레임 안보이게 함
			this.setVisible(true);
			//자원해제 
			this.dispose();
		}
	}

	public void itemStateChanged(ItemEvent e){
		//제한 인원 선택했을 때 수행
		if(e.getSource() == this.jcb_MakeRoomForm_personnumber){
			//제한 인원 세
			personlimit = this.jcb_MakeRoomForm_personnumber.getSelectedItem().toString();
		}
	}
}