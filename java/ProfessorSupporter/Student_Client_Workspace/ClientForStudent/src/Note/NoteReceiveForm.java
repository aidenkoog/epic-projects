package Note;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;

public class NoteReceiveForm extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JButton okButton;
	JTextArea textArea;
	
	//생성자
	public NoteReceiveForm(String message) {
		this.setIconImage(new ImageIcon(".\\image\\학사모학생.png").getImage());
		setTitle("전체쪽지");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 327, 265);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		okButton = new JButton("확인");
		buttonPanel.add(okButton);
		
		JPanel noteContentPanel = new JPanel();
		noteContentPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(noteContentPanel, BorderLayout.CENTER);
		noteContentPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		noteContentPanel.add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setText(message);
		scrollPane.setViewportView(textArea);
		
		okButton.addActionListener(this);
		
		//프레임의 X를 눌렀을 때 아무런 동작을 안함
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
//	
//	public void setNoteContent(String message){
//		textArea.setText(message);
//	}
	
	public void actionPerformed(ActionEvent e){
		//확인
		if(e.getSource() == okButton){
			textArea.setText("");
			this.setVisible(false);
			this.dispose();
		}
	}
}
