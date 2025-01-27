package Note;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//쪽지 내용 띄우는 클래스
public class NoteForm extends JFrame{
	public JTextPane jtp_NoteForm_text;
	private JButton jb_NoteForm_ok;
	private JScrollPane jsp_NoteForm_forMsg;
	
	//생성자
	public NoteForm(){
		this.setIconImage(new ImageIcon(".\\image\\학사모학생.png").getImage());		//프레임 이미지
		this.setTitle("쪽지 보내기");
		
		this.setLayout(null);
		this.setSize(300, 300);
		//창 크기 조절 불가능
		this.setResizable(false);
		
		Container con = this.getContentPane();
		
		jtp_NoteForm_text = new JTextPane();
		jb_NoteForm_ok = new JButton("확인");
		jsp_NoteForm_forMsg	= new JScrollPane(jtp_NoteForm_text, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		//유저의 화면 정중앙에 위치하게 하는 좌표
		int x = (int)this.getToolkit().getScreenSize().getWidth() / 2 - (int)this.getSize().getWidth() / 2;
		int y = (int)this.getToolkit().getScreenSize().getHeight() / 2 - (int)this.getSize().getHeight() / 2;
		
		jtp_NoteForm_text.setEditable(false);
		
		con.add(jsp_NoteForm_forMsg);
		con.add(jb_NoteForm_ok);
		
		jsp_NoteForm_forMsg.setBounds(20, 10, 250, 200);
		jb_NoteForm_ok.setBounds(110, 220, 80, 25);
		
		//프레임의 X를 눌렀을 때 DISPOSE시킴
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//확인 버튼 눌렀을 때 창 DISPOSE시킴
		jb_NoteForm_ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				NoteForm.this.setVisible(false);
				NoteForm.this.dispose();
			}
		});
		//화면을 지정한 좌표로 위치 시킴
		this.setLocation(x, y);
		//창 보여
		this.setVisible(true);
	} //생성자 끝
}