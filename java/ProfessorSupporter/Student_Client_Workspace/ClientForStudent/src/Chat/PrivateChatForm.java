package Chat;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Communication.Transit;

//1:1대화 폼 띄우는 클래스
public class PrivateChatForm extends JFrame implements ActionListener{
	public JTextPane jtp_PrivateChatForm_text;
	private JButton jb_PrivateChatForm_send;
	private JTextField jtf_PrivateChatForm_sendMsg;
	private JScrollPane jsp_PrivateChatForm_forchat;
	//상대방 아이디 
	public String receiver;
	
	public PrivateChatForm(String receiver){
		this.setIconImage(new ImageIcon(".\\image\\학사모학생.png").getImage());		//프레임 이미지
		this.receiver = receiver;
		this.setTitle("[" + receiver + "]님과 1:1채팅");
		
		this.setLayout(null);
		this.setSize(300, 300);
		//프레임 크기 조절 불가능
		this.setResizable(false);
		
		Container con = this.getContentPane();
		
		jtp_PrivateChatForm_text = new JTextPane();
		jb_PrivateChatForm_send = new JButton("보내기");
		jtf_PrivateChatForm_sendMsg = new JTextField();
		
		jsp_PrivateChatForm_forchat	= new JScrollPane(jtp_PrivateChatForm_text, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		//유저의 화면 정중앙에 위치시키기 위한 좌표
		int x = (int)this.getToolkit().getScreenSize().getWidth() / 2 - (int)this.getSize().getWidth() / 2;
		int y = (int)this.getToolkit().getScreenSize().getHeight() / 2 - (int)this.getSize().getHeight() / 2;
		
		jtp_PrivateChatForm_text.setEditable(false);
		
		//버튼 속성 리셋
		jb_PrivateChatForm_send.setFont(new Font("Dialog", Font.BOLD, 12));
		jb_PrivateChatForm_send.setMargin(new Insets(0, 0, 0, 0));
		jb_PrivateChatForm_send.setHorizontalTextPosition(JButton.LEFT);
		
		con.add(jsp_PrivateChatForm_forchat);
		con.add(jb_PrivateChatForm_send);
		con.add(jtf_PrivateChatForm_sendMsg);
		
		jsp_PrivateChatForm_forchat.setBounds(20, 10, 250, 200);
		jtf_PrivateChatForm_sendMsg.setBounds(20, 220, 180, 25);
		jb_PrivateChatForm_send.setBounds(210, 220, 60, 25);
		
		//이벤트 등록
		jtf_PrivateChatForm_sendMsg.addActionListener(this);
		jb_PrivateChatForm_send.addActionListener(this);
		
		//프레임의 X를 눌렀을 때 수행
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				//서버에 Flag정보 전송
				Transit.sendMsg("1:1대화/대화창에서 나가셨습니다./" + PrivateChatForm.this.receiver);
				//창 안보이게 함
				PrivateChatForm.this.setVisible(false);
				//창 자원해제 
				PrivateChatForm.this.dispose();
			}
		});
		//프레임의 X를 눌렀을 때 아무 작업도 안함
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.setLocation(x, y);
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ae){
		//메시지 입력창에 입력한 메시지 세팅
		String msg = jtf_PrivateChatForm_sendMsg.getText();
		//아무것도 입력하지 않았을 시 공백 세팅
		if(msg.length() == 0){
			msg = " ";
		}
		//서버에 Flag정보 전송
		Transit.sendMsg("1:1대화/" + msg + "/" + receiver);
		//메시지 입력창 리
		jtf_PrivateChatForm_sendMsg.setText("");
	}
}