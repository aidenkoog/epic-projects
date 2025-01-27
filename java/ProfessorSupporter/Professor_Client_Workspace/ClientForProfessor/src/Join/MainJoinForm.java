package Join;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import Communication.Transit;
import Controller.Manager;

public class MainJoinForm extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JTextField idTextField = null;
	private JPasswordField passwordTextField = null;
	private JPasswordField passwordCheckTextField = null;
	private JTextField nameTextField = null;
	private JTextField emailTextField = null;
	private JComboBox<String> sexComboBox = null;
	private JTextField homepageTextField = null;
	private JTextField phoneTextField = null;
	private JTextField cellPhoneTextField = null;
	private JTextField addressField = null;
	private JTextArea introductionTextArea = null;
	private JButton okButtonInJoinForm = null;
	private JButton initializeButton = null;
	private JButton cancelButton = null;
	private JButton duplicationCheckButton = null;

	//생성자
	public MainJoinForm() {
		this.createJoinForm();
		this.mountEventHandler();
	}

	//이벤트 처리기 등록
	public void mountEventHandler(){
		//회원가입 양식 화면
		duplicationCheckButton.addActionListener(this);
		okButtonInJoinForm.addActionListener(this);
		initializeButton.addActionListener(this);
		cancelButton.addActionListener(this);
	} //이벤트 처리기 등록 함수 끝

	//이벤트 처리기
	public void actionPerformed(ActionEvent e){
		//회원가입 양식 화면
		//중복확인 버튼
		if(e.getSource() == duplicationCheckButton){
			System.out.println("중복확인 버튼이 눌려졌습니다.");
			String id = idTextField.getText();
			Transit.sendMsg("메인중복확인/" + id);		
		}
		//확인 버튼
		else if(e.getSource() == okButtonInJoinForm){
			System.out.println("[회원가입 화면] 확인 버튼 클릭");
			this.registerMember();	//회원 등록 처리
		}
		//초기화 버튼
		else if(e.getSource() == initializeButton){
			this.initializeRegisterForm();
		}
		//취소 버튼
		else if(e.getSource() == cancelButton){
			Manager.MAINFRAME.getTabbedPane().remove(this);
		}
	} //이벤트 처리기 함수 끝

	//회원 등록 처리
	private void registerMember(){
		//각각 컴포넌트들의 값들을 저장
		String id = idTextField.getText();
		String password = "";
		for(char c : passwordTextField.getPassword()){
			password += c;
		}
		String name = nameTextField.getText();
		String email = emailTextField.getText();
		String sex = (String)sexComboBox.getSelectedItem();
		String homepage = homepageTextField.getText();
		String phone = phoneTextField.getText();
		String cellPhone = cellPhoneTextField.getText();
		String address = addressField.getText();
		String introduction = introductionTextArea.getText();
		
//		String[] componentValues = {id, password, name, email, sex, homepage, phone, cellPhone, address, introduction};
//		for(int i=0; i<componentValues.length; i++){
//			if(componentValues[i].length() == 0){
//				componentValues[i] = " ";
//			}
//		}

		//서버로 회원가입 속성 10개 전송
		Transit.sendMsg("메인회원가입/" + id + "/" + password + "/" + name + "/" + email + "/" + sex + "/" + homepage
				+ "/" + phone + "/" + cellPhone + "/" + address + "/" + introduction);
	} //회원 등록 처리 함수 끝

	//회원 등록 양식 컴포넌트 초기화
	public void initializeRegisterForm(){
		idTextField.setText("");
		passwordTextField.setText("");
		passwordCheckTextField.setText("");
		nameTextField.setText("");
		emailTextField.setText("");
		sexComboBox.setSelectedIndex(0);
		homepageTextField.setText("");
		phoneTextField.setText("");
		cellPhoneTextField.setText("");
		addressField.setText("");
		introductionTextArea.setText("");
	} //회원 등록 양식 컴포넌트 초기화 함수 끝

	//회원 가입 양식 생성
	private void createJoinForm(){
		//회원 등록 양식 폼 패널 레이아웃
		this.setLayout(null);					
		//아이디
		JPanel idLabelPanel = new JPanel();
		idLabelPanel.setBackground(SystemColor.inactiveCaption);
		idLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		idLabelPanel.setBounds(12, 44, 132, 32);
		this.add(idLabelPanel);
		idLabelPanel.setLayout(null);

		JLabel idLabel = new JLabel("아이디");
		idLabel.setBounds(12, 10, 57, 15);
		idLabelPanel.add(idLabel);

		//비밀번호
		JPanel passwordLabelPanel = new JPanel();
		passwordLabelPanel.setBackground(SystemColor.inactiveCaption);
		passwordLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		passwordLabelPanel.setLayout(null);
		passwordLabelPanel.setBounds(12, 75, 132, 32);
		this.add(passwordLabelPanel);

		JLabel passwordLabel = new JLabel("비밀번호");
		passwordLabel.setBounds(12, 10, 57, 15);
		passwordLabelPanel.add(passwordLabel);

		//비밀번호 확인
		JPanel passwordCheckLabelPanel = new JPanel();
		passwordCheckLabelPanel.setBackground(SystemColor.inactiveCaption);
		passwordCheckLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		passwordCheckLabelPanel.setLayout(null);
		passwordCheckLabelPanel.setBounds(12, 106, 132, 32);
		this.add(passwordCheckLabelPanel);

		JLabel passwordCheckLabel = new JLabel("비밀번호 확인");
		passwordCheckLabel.setBounds(12, 10, 89, 15);
		passwordCheckLabelPanel.add(passwordCheckLabel);

		//이름
		JPanel nameLabelPanel = new JPanel();
		nameLabelPanel.setBackground(SystemColor.inactiveCaption);
		nameLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		nameLabelPanel.setLayout(null);
		nameLabelPanel.setBounds(12, 137, 132, 32);
		this.add(nameLabelPanel);

		JLabel nameLabel = new JLabel("이름");
		nameLabel.setBounds(12, 10, 57, 15);
		nameLabelPanel.add(nameLabel);

		//이메일
		JPanel emailLabelPanel = new JPanel();
		emailLabelPanel.setBackground(SystemColor.inactiveCaption);
		emailLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		emailLabelPanel.setLayout(null);
		emailLabelPanel.setBounds(12, 168, 132, 32);
		this.add(emailLabelPanel);

		JLabel emailLabel = new JLabel("E-mail");
		emailLabel.setBounds(12, 10, 57, 15);
		emailLabelPanel.add(emailLabel);

		//성별
		JPanel sexLabelPanel = new JPanel();
		sexLabelPanel.setBackground(SystemColor.inactiveCaption);
		sexLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		sexLabelPanel.setLayout(null);
		sexLabelPanel.setBounds(12, 199, 132, 32);
		this.add(sexLabelPanel);

		JLabel sexLabel = new JLabel("성별");
		sexLabel.setBounds(12, 10, 57, 15);
		sexLabelPanel.add(sexLabel);

		//홈페이지
		JPanel homepageLabelPanel = new JPanel();
		homepageLabelPanel.setBackground(SystemColor.inactiveCaption);
		homepageLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		homepageLabelPanel.setLayout(null);
		homepageLabelPanel.setBounds(12, 230, 132, 32);
		this.add(homepageLabelPanel);

		JLabel homepageLabel = new JLabel("홈페이지");
		homepageLabel.setBounds(12, 10, 57, 15);
		homepageLabelPanel.add(homepageLabel);

		//전화번호
		JPanel phoneLabelPanel = new JPanel();
		phoneLabelPanel.setBackground(SystemColor.inactiveCaption);
		phoneLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		phoneLabelPanel.setLayout(null);
		phoneLabelPanel.setBounds(12, 261, 132, 32);
		this.add(phoneLabelPanel);

		JLabel phoneLabel = new JLabel("전화번호");
		phoneLabel.setBounds(12, 10, 57, 15);
		phoneLabelPanel.add(phoneLabel);

		//핸드폰 번호
		JPanel cellPhoneLabelPanel = new JPanel();
		cellPhoneLabelPanel.setBackground(SystemColor.inactiveCaption);
		cellPhoneLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		cellPhoneLabelPanel.setLayout(null);
		cellPhoneLabelPanel.setBounds(12, 292, 132, 32);
		this.add(cellPhoneLabelPanel);

		JLabel cellPhoneLabel = new JLabel("핸드폰 번호");
		cellPhoneLabel.setBounds(12, 10, 72, 15);
		cellPhoneLabelPanel.add(cellPhoneLabel);

		//주소
		JPanel addressLabelPanel = new JPanel();
		addressLabelPanel.setBackground(SystemColor.inactiveCaption);
		addressLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		addressLabelPanel.setLayout(null);
		addressLabelPanel.setBounds(12, 323, 132, 32);
		this.add(addressLabelPanel);

		JLabel addressLabel = new JLabel("주소");
		addressLabel.setBounds(12, 10, 57, 15);
		addressLabelPanel.add(addressLabel);

		//자기소개
		JPanel introductionLabelPanel = new JPanel();
		introductionLabelPanel.setBackground(SystemColor.inactiveCaption);
		introductionLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		introductionLabelPanel.setLayout(null);
		introductionLabelPanel.setBounds(12, 354, 132, 111);
		this.add(introductionLabelPanel);

		JLabel introductionLabel = new JLabel("자기소개");
		introductionLabel.setBounds(12, 46, 57, 15);
		introductionLabelPanel.add(introductionLabel);

		//아이디 입력란
		JPanel idTextFieldPanel = new JPanel();
		idTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		idTextFieldPanel.setBounds(144, 44, 422, 32);
		this.add(idTextFieldPanel);
		idTextFieldPanel.setLayout(null);

		idTextField = new JTextField();
		idTextField.setBounds(6, 5, 116, 21);
		idTextFieldPanel.add(idTextField);
		idTextField.setColumns(10);

		//중복확인 버튼
		duplicationCheckButton = new JButton("중복확인");
		duplicationCheckButton.setBounds(134, 4, 91, 23);
		idTextFieldPanel.add(duplicationCheckButton);

		//비밀번호 입력란
		JPanel passwordTextFieldPanel = new JPanel();
		passwordTextFieldPanel.setLayout(null);
		passwordTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		passwordTextFieldPanel.setBounds(144, 75, 422, 32);
		this.add(passwordTextFieldPanel);

		passwordTextField = new JPasswordField();
		passwordTextField.setBounds(6, 5, 116, 21);
		passwordTextFieldPanel.add(passwordTextField);

		//비밀번호 확인 입력란
		JPanel passwordCheckTextFieldPanel = new JPanel();
		passwordCheckTextFieldPanel.setLayout(null);
		passwordCheckTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		passwordCheckTextFieldPanel.setBounds(144, 106, 422, 32);
		this.add(passwordCheckTextFieldPanel);

		passwordCheckTextField = new JPasswordField();
		passwordCheckTextField.setBounds(6, 5, 116, 21);
		passwordCheckTextFieldPanel.add(passwordCheckTextField);

		//이름 입력란
		JPanel nameTextFieldPanel = new JPanel();
		nameTextFieldPanel.setLayout(null);
		nameTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		nameTextFieldPanel.setBounds(144, 137, 422, 32);
		this.add(nameTextFieldPanel);

		nameTextField = new JTextField();
		nameTextField.setColumns(10);
		nameTextField.setBounds(6, 5, 116, 21);
		nameTextFieldPanel.add(nameTextField);

		//이메일 입력란
		JPanel emailTextFieldPanel = new JPanel();
		emailTextFieldPanel.setLayout(null);
		emailTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		emailTextFieldPanel.setBounds(144, 168, 422, 32);
		this.add(emailTextFieldPanel);

		emailTextField = new JTextField();
		emailTextField.setColumns(10);
		emailTextField.setBounds(6, 5, 220, 21);
		emailTextFieldPanel.add(emailTextField);

		//성별 선택란
		JPanel sexComboBoxPanel = new JPanel();
		sexComboBoxPanel.setLayout(null);
		sexComboBoxPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		sexComboBoxPanel.setBounds(144, 199, 422, 32);
		this.add(sexComboBoxPanel);

		String[] sex = {"남", "여"};
		sexComboBox = new JComboBox(sex);
		sexComboBox.setBounds(6, 5, 60, 21);
		sexComboBoxPanel.add(sexComboBox);

		//홈페이지 입력란
		JPanel homepageTextFieldPanel = new JPanel();
		homepageTextFieldPanel.setLayout(null);
		homepageTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		homepageTextFieldPanel.setBounds(144, 230, 422, 32);
		this.add(homepageTextFieldPanel);

		homepageTextField = new JTextField();
		homepageTextField.setColumns(10);
		homepageTextField.setBounds(6, 5, 221, 21);
		homepageTextFieldPanel.add(homepageTextField);

		//전화번호 입력란
		JPanel phoneTextFieldPanel = new JPanel();
		phoneTextFieldPanel.setLayout(null);
		phoneTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		phoneTextFieldPanel.setBounds(144, 261, 422, 32);
		this.add(phoneTextFieldPanel);

		phoneTextField = new JTextField();
		phoneTextField.setColumns(10);
		phoneTextField.setBounds(6, 5, 116, 21);
		phoneTextFieldPanel.add(phoneTextField);

		//핸드폰 번호 입력란
		JPanel cellPhoneTextFieldPanel = new JPanel();
		cellPhoneTextFieldPanel.setLayout(null);
		cellPhoneTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		cellPhoneTextFieldPanel.setBounds(144, 292, 422, 32);
		this.add(cellPhoneTextFieldPanel);

		cellPhoneTextField = new JTextField();
		cellPhoneTextField.setColumns(10);
		cellPhoneTextField.setBounds(6, 5, 116, 21);
		cellPhoneTextFieldPanel.add(cellPhoneTextField);

		//주소 입력란
		JPanel addressTextFieldPanel = new JPanel();
		addressTextFieldPanel.setLayout(null);
		addressTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		addressTextFieldPanel.setBounds(144, 323, 422, 32);
		this.add(addressTextFieldPanel);

		addressField = new JTextField();
		addressField.setColumns(10);
		addressField.setBounds(6, 5, 404, 21);
		addressTextFieldPanel.add(addressField);

		//자기소개 입력란
		JPanel introductionTextAreaPanel = new JPanel();
		introductionTextAreaPanel.setLayout(null);
		introductionTextAreaPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		introductionTextAreaPanel.setBounds(144, 354, 422, 111);
		this.add(introductionTextAreaPanel);

		JScrollPane introductionScrollPane = new JScrollPane();
		introductionScrollPane.setBounds(6, 10, 404, 91);
		introductionTextAreaPanel.add(introductionScrollPane);
		introductionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		introductionTextArea = new JTextArea(); 
		introductionScrollPane.setViewportView(introductionTextArea);
		introductionTextArea.setWrapStyleWord(true);
		introductionTextArea.setLineWrap(true);

		//버튼 패널
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(12, 493, 578, 36);
		this.add(buttonPanel);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		//확인 버튼
		okButtonInJoinForm = new JButton("확인");
		buttonPanel.add(okButtonInJoinForm);
		//초기화 버튼
		initializeButton = new JButton("초기화");
		buttonPanel.add(initializeButton);
		//취소 버튼
		cancelButton = new JButton("취소"); 
		buttonPanel.add(cancelButton);
	} //회원 가입 양식 생성 함수 끝
}