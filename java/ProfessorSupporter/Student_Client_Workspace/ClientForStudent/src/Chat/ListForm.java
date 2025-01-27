package Chat;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import Communication.Transit;
import Controller.Manager;
import FileTransfer.SendFile;

//리스트 띄우는 폼 
public class ListForm extends JDialog{
	//유저 목록 담을 리스트
	public JList jl_ListForm_guestlist;
	//버튼들
	public JButton jb_ListForm_ok;
	private JButton jb_ListForm_cancle;
	private JScrollPane jsp_ListForm_forguestlist;
	
	private TitledBorder tborder_ListForm_forIds;
	//방이름
	private String roomname;
	//방 타이틀
	private String title;
	
	public ListForm(String roomname, String title , String[] users){
		//타이틀과 modal 세팅
		super(Manager.MAINFRAME, title, true);
		this.roomname = roomname;
		this.title = title;
		
		this.setLayout(null);
		this.setSize(200, 325);
		//크기 조절 불가능
		this.setResizable(false);
		
		Container con = this.getContentPane();
		
		//리스트에 유저 목록 세팅
		jl_ListForm_guestlist = new JList(users);
		jb_ListForm_ok = new JButton("확인");
		jb_ListForm_cancle = new JButton("취소");
		
		jsp_ListForm_forguestlist = new JScrollPane(jl_ListForm_guestlist, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		con.add(jsp_ListForm_forguestlist);
		con.add(jb_ListForm_ok);
		con.add(jb_ListForm_cancle);
		
		JButton[] buttons = {jb_ListForm_ok, jb_ListForm_cancle};
		
		//버튼의 서식 새로 세팅
		for(int i = 0; i < buttons.length; i++){
			buttons[i].setFont(new Font("Dialog", Font.BOLD, 12));
			buttons[i].setMargin(new Insets(0, 0, 0, 0));
			buttons[i].setHorizontalTextPosition(JButton.LEFT);
		}
		
		//리스트의 타이틀보더 세팅
		tborder_ListForm_forIds = BorderFactory.createTitledBorder("유저 리스트");
		tborder_ListForm_forIds.setTitleJustification(TitledBorder.LEFT);
		jsp_ListForm_forguestlist.setBorder(tborder_ListForm_forIds);
		
		jsp_ListForm_forguestlist.setBounds(0, 0, 200, 250);
		jb_ListForm_ok.setBounds(40, 255, 50, 30);
		jb_ListForm_cancle.setBounds(100, 255, 50, 30);
		
		//프레임을 화면 정중앙에 위치하게 하기 위한 좌표
		int x = (int)this.getToolkit().getScreenSize().getWidth() / 2 - (int)this.getSize().getWidth() / 2;
		int y = (int)this.getToolkit().getScreenSize().getHeight() / 2 - (int)this.getSize().getHeight() / 2;
		
		//확인 버튼 눌렀을 때 수행
		this.jb_ListForm_ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				handler();
			}
		});
		
		//취소 버튼 눌렀을 때 수행
		this.jb_ListForm_cancle.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				//프레임 안보이게 함
				ListForm.this.setVisible(false);
				//자원해제
				ListForm.this.dispose();
			}
		});
		
		//프레임의 X눌렀을 때 자원해제 작업함
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//보여줄 좌표 세팅
		this.setLocation(x, y);
		//프레임 보여줌
		this.setVisible(true);
	}
	
	private void handler(){
		//타이틀이 방정보일 때 수행
		if(title.equals("방정보")){
			//안보이게 함
			setVisible(false);
			//자원해제
			dispose();
		}
		//타이틀이 초대일 때 수행
		else if(title.equals("초대")){
			//선택한 사람이 없을 때 수행
			if(jl_ListForm_guestlist.isSelectionEmpty()){
				JOptionPane.showMessageDialog(this, "초대할 사람을 선택해 주시기 바랍니다.", "Invite Error", JOptionPane.WARNING_MESSAGE);
			}
			//선택한 사람이 있을 때 수행
			else{
				//선택한 유저 세팅
				String selectedId = jl_ListForm_guestlist.getSelectedValue().toString();
				//서버에 Flag정보 전송
				Transit.sendMsg("초대/" + "/" + roomname + "/" + selectedId);
				//프레임 안보이게 함
				setVisible(false);
				//자원해제
				dispose();
			}
		}
		//타이틀이 쪽지일 때 수행
		else if(title.equals("쪽지")){
			//선택한 사람이 없을 때 수행
			if(jl_ListForm_guestlist.isSelectionEmpty()){
				JOptionPane.showMessageDialog(this, "쪽지를 받을 사람을 선택해 주시기 바랍니다.", "InstantMsg Error", JOptionPane.WARNING_MESSAGE);
			}
			//선택한 사람이 있을 때 수행
			else{
				//선택한 유저 세팅
				String selectedId = jl_ListForm_guestlist.getSelectedValue().toString();
				//입력한 메시지 세팅
				String msg = JOptionPane.showInputDialog("메시지를 입력하십시오.");
				//입력한 메시지가 없을 때 수행
				if(msg.length() == 0){
					JOptionPane.showMessageDialog(this, "메시지를 입력해 주십시오.", "InstantMsg Error", JOptionPane.WARNING_MESSAGE);
				}
				//입력한 메시지가 있을 때 수행
				else{
					//서버에 Flag정보 전송
					Transit.sendMsg("쪽지/" + msg + "/" + selectedId);
					//프레임 안보이게 함
					setVisible(false);
					//자원해제
					dispose();
				}
			}
		}
		//타이틀이 1:1대화일 때 수행
		else if(title.equals("1:1대화")){
			//선택한 사람이 없을 때 수행
			if(jl_ListForm_guestlist.isSelectionEmpty()){
				JOptionPane.showMessageDialog(this, "1:1대화를 할 사람을 선택해 주시기 바랍니다.", "PrivateChat Error", JOptionPane.WARNING_MESSAGE);
			}
			//선택한 사람이 있을 때 수행
			else{
				//선택한 유저 세팅
				String selectedId = jl_ListForm_guestlist.getSelectedValue().toString();
				//서버에 Flag정보 전송
				Transit.sendMsg("1:1대화요청/" + selectedId);
				//프레임 안보이게 함
				setVisible(false);
				//자원해제
				dispose();
			}
		}
		//타이틀이 파일 보내기일 때 수행
		else if(title.equals("파일 보내기")){
			//선택한 사람이 없을 때 수행
			if(jl_ListForm_guestlist.isSelectionEmpty()){
				JOptionPane.showMessageDialog(this, "파일을 보낼 사람을 선택해 주시기 바랍니다.", "SendFile Error", JOptionPane.WARNING_MESSAGE);
			}
			//선택한 사람이 있을 때 수행
			else{
				//선택한 유저 세팅
				String selectedId = jl_ListForm_guestlist.getSelectedValue().toString();	
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
					//프레임 안보이게 함
					setVisible(false);
					//자원해제
					dispose();
				}
			}
		}
	}
}