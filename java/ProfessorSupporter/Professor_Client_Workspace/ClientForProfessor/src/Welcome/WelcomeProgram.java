package Welcome;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;

import Controller.Manager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

public class WelcomeProgram extends JPanel {
	private static final long serialVersionUID = 1L;

	public WelcomeProgram() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		add(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("»Æ¿Œ");
		panel_1.add(btnNewButton);
		
		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.NORTH);
		
		JLabel label_5 = new JLabel("Welcome to Professor Supporter");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setFont(new Font("Miriam", Font.BOLD, 34));
		label_5.setBackground(Color.LIGHT_GRAY);
		panel_2.add(label_5);
		
		JPanel panel_3 = new JPanel();
		add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("\u25A0 \uD504\uB85C\uADF8\uB7A8 \uC81C\uACF5 \uAE30\uB2A5");
		lblNewLabel_1.setBounds(23, 26, 212, 18);
		panel_3.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("±º∏≤", Font.BOLD, 16));
		
		JLabel lblNewLabel_2 = new JLabel("1. 1:1\uCABD\uC9C0 \uBC0F \uC804\uCCB4 \uCABD\uC9C0\uBCF4\uB0B4\uAE30");
		lblNewLabel_2.setBounds(41, 53, 194, 18);
		panel_3.add(lblNewLabel_2);
		lblNewLabel_2.setFont(new Font("±º∏≤", Font.PLAIN, 15));
		
		JLabel label = new JLabel("2. 1:1\uB300\uD654 \uBC0F \uB2E4\uC790\uAC04 \uB300\uD654");
		label.setBounds(41, 81, 166, 18);
		panel_3.add(label);
		label.setFont(new Font("±º∏≤", Font.PLAIN, 15));
		
		JLabel label_1 = new JLabel("4. \uC811\uC18D\uC790\uB4E4\uC758 \uBAA9\uB85D\uACFC PC\uBC30\uCE58\uB3C4");
		label_1.setBounds(41, 137, 202, 18);
		panel_3.add(label_1);
		label_1.setFont(new Font("±º∏≤", Font.PLAIN, 15));
		
		JLabel label_2 = new JLabel("3. \uBE44\uBC88\uBC29, \uADD3\uC18D\uB9D0, \uCD08\uB300, \uAC15\uC81C\uD1F4\uC7A5\uAE30\uB2A5");
		label_2.setBounds(41, 109, 244, 18);
		panel_3.add(label_2);
		label_2.setFont(new Font("±º∏≤", Font.PLAIN, 15));
		
		JLabel label_3 = new JLabel("5. \uC811\uC18D\uD55C \uD559\uC0DD\uB4E4\uC758 PC\uD654\uBA74 \uCEA1\uCCD0");
		label_3.setBounds(41, 165, 207, 18);
		panel_3.add(label_3);
		label_3.setFont(new Font("±º∏≤", Font.PLAIN, 15));
		
		JLabel label_4 = new JLabel("6. \uC811\uC18D\uD55C \uD559\uC0DD\uB4E4\uC758 PC\uD654\uBA74 \uBCF4\uAE30");
		label_4.setBounds(41, 193, 207, 18);
		panel_3.add(label_4);
		label_4.setFont(new Font("±º∏≤", Font.PLAIN, 15));
		
		JLabel lblNewLabel_3 = new JLabel("7. \uD559\uC0DD\uB4E4\uC758 \uC815\uBCF4\uB97C \uB4F1\uB85D, \uC218\uC815, \uC0AD\uC81C, \uAC80\uC0C9\uAC00\uB2A5");
		lblNewLabel_3.setBounds(41, 221, 296, 18);
		panel_3.add(lblNewLabel_3);
		lblNewLabel_3.setFont(new Font("±º∏≤", Font.PLAIN, 15));
		
		JLabel lblNewLabel_4 = new JLabel("8. \uD30C\uC77C\uBCF4\uB0B4\uAE30");
		lblNewLabel_4.setBounds(41, 249, 88, 18);
		panel_3.add(lblNewLabel_4);
		lblNewLabel_4.setFont(new Font("±º∏≤", Font.PLAIN, 15));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Manager.MAINFRAME.getTabbedPane().remove(WelcomeProgram.this);
			}
		});

	}
}
