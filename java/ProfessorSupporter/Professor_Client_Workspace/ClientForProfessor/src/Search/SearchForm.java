package Search;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Window.Type;
import javax.swing.border.EtchedBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

import Communication.Transit;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SearchForm extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;


	public SearchForm() {
		this.setIconImage(new ImageIcon(".\\image\\학사모.png").getImage());		//프레임 이미지
		setTitle("학생검색");
		setBounds(100, 100, 281, 144);
		this.displayCenter();		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			contentPanel.add(panel, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("확인");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						SearchForm.this.setVisible(false);
						Transit.sendMsg("학생검색메뉴아이템/"+textField.getText());
					}
				});
				panel.add(okButton);
			}
			{
				JButton cancelButton = new JButton("취소");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						textField.setText("");
						SearchForm.this.dispose();
					}
				});
				panel.add(cancelButton);
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(null);
			{
				JLabel lblNewLabel = new JLabel("\uD559\uC0DD \uC774\uB984 : ");
				lblNewLabel.setBounds(12, 22, 64, 15);
				panel.add(lblNewLabel);
			}
			{
				textField = new JTextField();
				textField.setBounds(82, 19, 161, 21);
				panel.add(textField);
				textField.setColumns(10);
			}
		}
	}
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

	public void initializeComponent(){
		textField.setText("");
	}
}
