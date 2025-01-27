package Note;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import Communication.Transit;

public class NoteAllForm extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JButton sendButton;
	JButton cancelButton;
	JTextArea noteContentTextArea;
	
	//생성자
	public NoteAllForm() {
		this.setIconImage(new ImageIcon(".\\image\\학사모학생.png").getImage());
		setTitle("전체 쪽지보내기");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 315, 262);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		sendButton = new JButton("보내기");
		buttonPanel.add(sendButton);
		
		cancelButton = new JButton("취소");
		buttonPanel.add(cancelButton);
		
		JPanel noteContentPanel = new JPanel();
		noteContentPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(noteContentPanel, BorderLayout.CENTER);
		noteContentPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		noteContentPanel.add(scrollPane, BorderLayout.CENTER);
		
		noteContentTextArea = new JTextArea();
		noteContentTextArea.setWrapStyleWord(true);
		noteContentTextArea.setLineWrap(true);
		scrollPane.setViewportView(noteContentTextArea);
		sendButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		//프레임의 X를 눌렀을 때 아무런 동작을 안함
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == sendButton){
			String message = noteContentTextArea.getText();
			Transit.sendMsg("전체쪽지/" + message);
			this.setVisible(false);
			this.dispose();
		}
		else if(e.getSource() == cancelButton){
			noteContentTextArea.setText("");
			this.setVisible(false);
			this.dispose();
		}
	}

}
