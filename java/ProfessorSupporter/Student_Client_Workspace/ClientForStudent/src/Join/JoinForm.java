package Join;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import Communication.Transit;
import Controller.Manager;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class JoinForm extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	public Container contentPane = null;		//컨텐츠 영역을 받기 위한 변수 
	public CardLayout cardLayout = null;		//전체 카드 레이아웃
	//첫번째 화면(약관 및 방침)
	private JPanel joinPolicyPanel = null;		//회원가입약관, 개인정보수집방침, 버튼을 담는 패널 변수
	private JPanel joinFormPanel = null;		//회원 등록 양식 패널 변수
	private JPanel contentPanel = null;			//회원가입약관 및 개인정보수집방침 양식 패널 변수
	private JPanel buttonPanel = null;			//버튼 패널 변수
	private JButton okButton = null;			//확인 버튼 변수
	private JRadioButton agreeRadioButtonOfJoinTerms = null;		//회원가입약관의 동의합니다 버튼
	private JRadioButton disagreeRadioButtonOfJoinTerms = null;		//회원가입약관의 동의하지 않습니다 버튼
	private JRadioButton agreeRadioButtonOfPrivacyPolicy = null;	//개인정보수집방침의 동의합니다 버튼
	private JRadioButton disagreeRadioButtonOfPrivacyPolicy = null;	//개인정보수집방침의 동의하지 않습니다 버튼
	//두번째 화면(등록양식)
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
	
	//회원가입약관
	private final String joinTerms = "1. 이 프로그램은 경북대학교 컴퓨터소프트웨어전공에서 수업지원을 목적으로 운영하는 사이트입니다.\n" +
			"2. 회원 가입 및 이용 자격은 경북대학교 컴퓨터정보학부 컴퓨터소프트웨어전공의 구성원으로 국한합니다.\n" +
			"3. 이 프로그램에서 제공되는 학습 내용 및 각종 자료들은 개인의 학습 목적을 위해서만 활용하고, 본인이 아닌 다른 사람에게 전달한다든지 다른 외부 사이트에 공개하는 것은 금지합니다.\n" + 
			"4. 개인의 아이디 및 비밀번호는 타인이 이용하는 것을 금지합니다. 타인의 아이디를 도용하는 경우에는, "+
			"경북대학교의 학칙에서 규정하고 있는 시험 부정행위에 준하는 위반사항으로 다루어질 것이므로 각별히 주의하시기 바랍니다.";
	//개인정보수집방침
	private final String privacyPolicy = "1. 이 프로그램에서는 주민등록번호, 전화번호, 주소 등과 같은 개인정보는 강제적으로 수집하지 않습니다.\n" +  
			"2. 학생의 학번과 이름은 본인여부를 확인하기 위한 절차에만 사용됩니다.\n" +
			"3. 수업과 관련된 학생의 게시물과 성과물은 해당 과목의 학습 지도 및 학점 평가에만 활용됩니다.\n" + 
			"4. 로그인된 공간에서 구성원들이 올려놓은 여러가지 자료 및 게시물들은 컴퓨터소프트웨어 전공의 구성원들의 학업적인 성취와 학문적인 발전을 위해서 활용되는 것을 목적으로 한 것입니다.\n" + 
			"5. 이 프로그램에서 제공되는 자료들은 컴퓨터소프트웨어 전공 내에서만 활용되는 것을 원칙으로 합니다. " +
			"외부에 유출되는 것을 절대로 금지하며, 만약 외부에 유출되어 벌어지는 일들에 대해서는 이 프로그램의 운영자는 어떠한 법적인 책임을 지지 않습니다.";

	//생성자
	public JoinForm() {
		this.setIconImage(new ImageIcon(".\\image\\학사모학생.png").getImage());
		this.setModalityType(ModalityType.APPLICATION_MODAL);	//모달 대화상자
		this.initializeMember();		//멤버 변수 초기화
		this.setSize(594, 554);			//회원가입 창 크기
		this.displayCenter();			//화면 위치 중앙 설정
		this.createJoinPolicyForm();	//회원가입 약관 및 방침 양식 생성
		this.createJoinForm();			//회원가입 양식 생성
		this.initializeRegisterForm();	//회원 등록 양식 컴포넌트 초기화
		this.mountEventHandler();		//이벤트 처리기 등록
	} //생성자 끝

	//멤버 변수 초기화 및 설정
	private void initializeMember(){
		contentPane = this.getContentPane();			//컨텐츠 영역을 가져옴
		cardLayout = new CardLayout();					//카드 레이아웃 생성
		contentPane.setLayout(cardLayout); 				//전체 카드 레이아웃
		joinPolicyPanel = new JPanel();					//회원가입약관, 개인정보수집방침, 버튼을 담는 패널 생성
		joinPolicyPanel.setLayout(new BorderLayout());	//회원가입약관, 개인정보수집방침, 버튼을 담는 패널 레이아웃
		contentPanel = new JPanel();					//회원가입약관 및 개인정보수집방침 양식을 담는 패널 생성
		contentPanel.setLayout(null);					//회원가입약관 및 개인정보수집방침 양식 패널 레이아웃
		buttonPanel = new JPanel();						//버튼 패널 생성
		joinFormPanel = new JPanel();					//회원 등록 양식 폼 패널 생성
		joinFormPanel.setLayout(null);					//회원 등록 양식 폼 패널 레이아웃
		contentPane.add("JoinPolicy", joinPolicyPanel);	//JoinPolicy라는 이름의 패널 등록
		contentPane.add("JoinForm", joinFormPanel);		//JoinForm이라는 이름의 패널 등록
	} //멤버 변수 초기화 및 설정 함수 끝

	//화면 중앙 표시
	private void displayCenter(){
		//화면의 크기
		Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();	
		//프레임의 크기
		Dimension frameDimension = this.getSize();
		//화면 넓이/2 - 프레임 넓이/2
		int xPosition = (int)(screenDimension.getWidth()/2 - frameDimension.getWidth()/2);
		//화면 높이/2 - 프레임 높이/2
		int yPosition = (int)(screenDimension.getHeight()/2 - frameDimension.getHeight()/2);
		this.setLocation(xPosition, yPosition);
	} //화면 중앙 표시 함수 끝

	//이벤트 처리기 등록
	public void mountEventHandler(){
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		//약관 및 방침 화면
		okButton.addActionListener(this);
		//회원가입 양식 화면
		duplicationCheckButton.addActionListener(this);
		okButtonInJoinForm.addActionListener(this);
		initializeButton.addActionListener(this);
		cancelButton.addActionListener(this);
		//X버튼 눌렀을 때 수행
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.out.println("[회원가입 창] X 버튼 클릭");
				cardLayout.show(contentPane, "JoinPolicy");
				Manager.LOGINFORM.setAlwaysOnTop(true);
			}
		});
	} //이벤트 처리기 등록 함수 끝

	//이벤트 처리기
	public void actionPerformed(ActionEvent e){
		//약관 및 방침 화면
		//확인 버튼
		if(e.getSource() == okButton){
			//회원가입약관과 개인정보수집방침 둘 다 동의할 시
			if(agreeRadioButtonOfJoinTerms.isSelected() && agreeRadioButtonOfPrivacyPolicy.isSelected()){
				cardLayout.show(contentPane, "JoinForm");
			}
			//동의하지 않습니다에 체크되있는 경우
			else{
				//메시지 대화상자 표시(어떤 것의 중앙, 메시지, 타이틀, 메시지대화유형)
				JOptionPane.showMessageDialog(this, "각각 해당 란에 동의하셔야 가입하실 수 있습니다.", "프로그램 메시지", JOptionPane.WARNING_MESSAGE);
			}
		}
		
		//회원가입 양식 화면
		//중복확인 버튼
		else if(e.getSource() == duplicationCheckButton){
			System.out.println("중복확인 버튼이 눌려졌습니다.");
			String id = idTextField.getText();
			Transit.sendMsg("중복확인/" + id);		
		}
		//확인 버튼
		else if(e.getSource() == okButtonInJoinForm){
			System.out.println("[회원가입 화면] 확인 버튼 클릭");
			//비밀번호와 비밀번호확인 란이 일치
			if(this.checkPassword()){
				this.registerMember();	//회원 등록 처리
			}
			else{
				JOptionPane.showMessageDialog(this, "비밀번호를 다시 입력하여 주십시오.", "비밀번호 확인", JOptionPane.ERROR_MESSAGE);
				passwordTextField.setText("");
				passwordCheckTextField.setText("");
			}
			
		}
		//초기화 버튼
		else if(e.getSource() == initializeButton){
			this.initializeRegisterForm();
		}
		//취소 버튼
		else if(e.getSource() == cancelButton){
			Manager.LOGINFORM.setAlwaysOnTop(true);
			this.dispose();
			cardLayout.show(contentPane, "JoinPolicy");
		}
	} //이벤트 처리기 함수 끝
	
	//비밀번호 체크 함수
	private boolean checkPassword(){
		String password = null;
		for(char c : passwordTextField.getPassword()){
			password += c;
		}
		String passwordCheck = null;
		for(char c : passwordCheckTextField.getPassword()){
			passwordCheck += c;
		}
		//비밀번호와 비밀번호 확인란이 일치
		if(password.equals(passwordCheck)){
			return true;
		}
		return false;
	}
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
		
		//서버로 회원가입 속성 10개 전송
		Transit.sendMsg("회원가입/" + id + "/" + password + "/" + name + "/" + email + "/" + sex + "/" + homepage
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

	//등록 양식 생성
	private void createJoinPolicyForm(){
		//회원가입약관
		//
		//회원가입약관 패널 생성
		JPanel joinTermsPanel = new JPanel();
		joinTermsPanel.setLayout(null);	
		joinTermsPanel.setBackground(SystemColor.menu);
		joinTermsPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		joinTermsPanel.setBounds(12, 10, 554, 222);
		contentPanel.add(joinTermsPanel);
		//회원가입약관 레이블
		JLabel joinTermsLabel = new JLabel("회원가입약관");
		joinTermsLabel.setBounds(12, 10, 79, 15);
		joinTermsPanel.add(joinTermsLabel);
		//스크롤 영역 생성
		JScrollPane joinTermsScrollPane = new JScrollPane();
		joinTermsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		joinTermsScrollPane.setBounds(12, 35, 530, 152);
		joinTermsPanel.add(joinTermsScrollPane);
		//텍스트 상자 생성
		JTextArea joinTermsTextArea = new JTextArea();
		joinTermsTextArea.setEditable(false);		//편집 불가능 설정
		joinTermsTextArea.setWrapStyleWord(true);	//단어 단위 줄바꿈 설정
		joinTermsTextArea.setLineWrap(true);		//자동 줄바꿈 설정
		joinTermsTextArea.setText(joinTerms);		//텍스트 설정
		joinTermsScrollPane.setViewportView(joinTermsTextArea);
		
		//라디오 버튼 생성
		agreeRadioButtonOfJoinTerms = new JRadioButton("동의합니다.");
		agreeRadioButtonOfJoinTerms.setSelected(true);
		agreeRadioButtonOfJoinTerms.setBackground(SystemColor.menu);
		agreeRadioButtonOfJoinTerms.setBounds(12, 193, 121, 23);
		joinTermsPanel.add(agreeRadioButtonOfJoinTerms); 

		disagreeRadioButtonOfJoinTerms = new JRadioButton("동의하지 않습니다.");
		disagreeRadioButtonOfJoinTerms.setBackground(SystemColor.menu);
		disagreeRadioButtonOfJoinTerms.setBounds(137, 193, 139, 23);
		joinTermsPanel.add(disagreeRadioButtonOfJoinTerms);
		//라디오 버튼 그룹 생성
		ButtonGroup joinTermsGroup = new ButtonGroup();
		joinTermsGroup.add(agreeRadioButtonOfJoinTerms);
		joinTermsGroup.add(disagreeRadioButtonOfJoinTerms);
		//
		//회원가입 약관 끝

		//개인정보수집방침
		//
		//개인정보수집방침 패널 생성
		JPanel privacyPolicyPanel = new JPanel();
		privacyPolicyPanel.setBackground(SystemColor.menu);
		privacyPolicyPanel.setLayout(null);
		privacyPolicyPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		privacyPolicyPanel.setBounds(12, 254, 554, 213);
		contentPanel.add(privacyPolicyPanel);
		//개인정보수집방침 레이블
		JLabel privacyPolicyLabel = new JLabel("개인정보취급방침");
		privacyPolicyLabel.setBounds(12, 10, 132, 15);
		privacyPolicyPanel.add(privacyPolicyLabel);
		//스크롤 영역 생성
		JScrollPane privacyPolicyScrollPane = new JScrollPane(); 
		privacyPolicyScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		privacyPolicyScrollPane.setBounds(12, 35, 530, 143);
		privacyPolicyPanel.add(privacyPolicyScrollPane);
		//텍스트 상자 생성
		JTextArea privacyPolicyTextArea = new JTextArea();
		privacyPolicyTextArea.setEditable(false);		//편집 불가능 설정
		privacyPolicyTextArea.setWrapStyleWord(true);	//단어 단위 줄바꿈 설정
		privacyPolicyTextArea.setLineWrap(true);		//자동 줄바꿈 설정
		privacyPolicyTextArea.setText(privacyPolicy);	//텍스트 설정
		privacyPolicyScrollPane.setViewportView(privacyPolicyTextArea);
		
		//라디오 버튼 생성
		agreeRadioButtonOfPrivacyPolicy = new JRadioButton("동의합니다.");
		agreeRadioButtonOfPrivacyPolicy.setSelected(true);
		agreeRadioButtonOfPrivacyPolicy.setBackground(SystemColor.menu);
		agreeRadioButtonOfPrivacyPolicy.setBounds(12, 184, 121, 23);
		privacyPolicyPanel.add(agreeRadioButtonOfPrivacyPolicy); 

		disagreeRadioButtonOfPrivacyPolicy = new JRadioButton("동의하지 않습니다.");
		disagreeRadioButtonOfPrivacyPolicy.setBackground(SystemColor.menu);
		disagreeRadioButtonOfPrivacyPolicy.setBounds(137, 184, 139, 23);
		privacyPolicyPanel.add(disagreeRadioButtonOfPrivacyPolicy);
		//라디오 버튼 그룹 생성
		ButtonGroup privacyPolicyGroup = new ButtonGroup();
		privacyPolicyGroup.add(agreeRadioButtonOfPrivacyPolicy);
		privacyPolicyGroup.add(disagreeRadioButtonOfPrivacyPolicy);
		//
		//개인정보수집방침 끝

		//확인 버튼 배치
		okButton = new JButton("확인");
		buttonPanel.add(okButton);
		//컨텐츠 영역에 배치
		joinPolicyPanel.add(contentPanel, BorderLayout.CENTER);
		joinPolicyPanel.add(buttonPanel, BorderLayout.SOUTH);
	} //등록 양식 생성 함수 끝

	//회원 가입 양식 생성
	private void createJoinForm(){
		//아이디
		JPanel idLabelPanel = new JPanel();
		idLabelPanel.setBackground(SystemColor.inactiveCaption);
		idLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		idLabelPanel.setBounds(12, 73, 132, 32);
		joinFormPanel.add(idLabelPanel);
		idLabelPanel.setLayout(null);

		JLabel idLabel = new JLabel("아이디");
		idLabel.setBounds(12, 10, 57, 15);
		idLabelPanel.add(idLabel);

		//비밀번호
		JPanel passwordLabelPanel = new JPanel();
		passwordLabelPanel.setBackground(SystemColor.inactiveCaption);
		passwordLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		passwordLabelPanel.setLayout(null);
		passwordLabelPanel.setBounds(12, 104, 132, 32);
		joinFormPanel.add(passwordLabelPanel);

		JLabel passwordLabel = new JLabel("비밀번호");
		passwordLabel.setBounds(12, 10, 57, 15);
		passwordLabelPanel.add(passwordLabel);

		//비밀번호 확인
		JPanel passwordCheckLabelPanel = new JPanel();
		passwordCheckLabelPanel.setBackground(SystemColor.inactiveCaption);
		passwordCheckLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		passwordCheckLabelPanel.setLayout(null);
		passwordCheckLabelPanel.setBounds(12, 135, 132, 32);
		joinFormPanel.add(passwordCheckLabelPanel);

		JLabel passwordCheckLabel = new JLabel("비밀번호 확인");
		passwordCheckLabel.setBounds(12, 10, 89, 15);
		passwordCheckLabelPanel.add(passwordCheckLabel);

		//이름
		JPanel nameLabelPanel = new JPanel();
		nameLabelPanel.setBackground(SystemColor.inactiveCaption);
		nameLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		nameLabelPanel.setLayout(null);
		nameLabelPanel.setBounds(12, 166, 132, 32);
		joinFormPanel.add(nameLabelPanel);

		JLabel nameLabel = new JLabel("이름");
		nameLabel.setBounds(12, 10, 57, 15);
		nameLabelPanel.add(nameLabel);

		//이메일
		JPanel emailLabelPanel = new JPanel();
		emailLabelPanel.setBackground(SystemColor.inactiveCaption);
		emailLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		emailLabelPanel.setLayout(null);
		emailLabelPanel.setBounds(12, 197, 132, 32);
		joinFormPanel.add(emailLabelPanel);

		JLabel emailLabel = new JLabel("E-mail");
		emailLabel.setBounds(12, 10, 57, 15);
		emailLabelPanel.add(emailLabel);

		//성별
		JPanel sexLabelPanel = new JPanel();
		sexLabelPanel.setBackground(SystemColor.inactiveCaption);
		sexLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		sexLabelPanel.setLayout(null);
		sexLabelPanel.setBounds(12, 228, 132, 32);
		joinFormPanel.add(sexLabelPanel);

		JLabel sexLabel = new JLabel("성별");
		sexLabel.setBounds(12, 10, 57, 15);
		sexLabelPanel.add(sexLabel);

		//홈페이지
		JPanel homepageLabelPanel = new JPanel();
		homepageLabelPanel.setBackground(SystemColor.inactiveCaption);
		homepageLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		homepageLabelPanel.setLayout(null);
		homepageLabelPanel.setBounds(12, 259, 132, 32);
		joinFormPanel.add(homepageLabelPanel);

		JLabel homepageLabel = new JLabel("홈페이지");
		homepageLabel.setBounds(12, 10, 57, 15);
		homepageLabelPanel.add(homepageLabel);

		//전화번호
		JPanel phoneLabelPanel = new JPanel();
		phoneLabelPanel.setBackground(SystemColor.inactiveCaption);
		phoneLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		phoneLabelPanel.setLayout(null);
		phoneLabelPanel.setBounds(12, 290, 132, 32);
		joinFormPanel.add(phoneLabelPanel);

		JLabel phoneLabel = new JLabel("전화번호");
		phoneLabel.setBounds(12, 10, 57, 15);
		phoneLabelPanel.add(phoneLabel);

		//핸드폰 번호
		JPanel cellPhoneLabelPanel = new JPanel();
		cellPhoneLabelPanel.setBackground(SystemColor.inactiveCaption);
		cellPhoneLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		cellPhoneLabelPanel.setLayout(null);
		cellPhoneLabelPanel.setBounds(12, 321, 132, 32);
		joinFormPanel.add(cellPhoneLabelPanel);

		JLabel cellPhoneLabel = new JLabel("핸드폰 번호");
		cellPhoneLabel.setBounds(12, 10, 72, 15);
		cellPhoneLabelPanel.add(cellPhoneLabel);

		//주소
		JPanel addressLabelPanel = new JPanel();
		addressLabelPanel.setBackground(SystemColor.inactiveCaption);
		addressLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		addressLabelPanel.setLayout(null);
		addressLabelPanel.setBounds(12, 352, 132, 32);
		joinFormPanel.add(addressLabelPanel);

		JLabel addressLabel = new JLabel("주소");
		addressLabel.setBounds(12, 10, 57, 15);
		addressLabelPanel.add(addressLabel);

		//자기소개
		JPanel introductionLabelPanel = new JPanel();
		introductionLabelPanel.setBackground(SystemColor.inactiveCaption);
		introductionLabelPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		introductionLabelPanel.setLayout(null);
		introductionLabelPanel.setBounds(12, 383, 132, 77);
		joinFormPanel.add(introductionLabelPanel);

		JLabel introductionLabel = new JLabel("자기소개");
		introductionLabel.setBounds(12, 31, 57, 15);
		introductionLabelPanel.add(introductionLabel);

		//아이디 입력란
		JPanel idTextFieldPanel = new JPanel();
		idTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		idTextFieldPanel.setBounds(144, 73, 422, 32);
		joinFormPanel.add(idTextFieldPanel);
		idTextFieldPanel.setLayout(null);

		idTextField = new JTextField();
		idTextField.setBounds(6, 5, 116, 21);
		idTextFieldPanel.add(idTextField);
		idTextField.setColumns(10);

		//중복확인 버튼
		duplicationCheckButton = new JButton("중복확인");
		duplicationCheckButton.setBounds(134, 4, 91, 23);
		idTextFieldPanel.add(duplicationCheckButton);

		//회원가입 배너 이미지
		JPanel joinBannerPanel = new JPanel();
		joinBannerPanel.setBackground(SystemColor.inactiveCaption);
		joinBannerPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		joinBannerPanel.setBounds(0, 0, 578, 57);
		joinFormPanel.add(joinBannerPanel);
		joinBannerPanel.setLayout(null);

		JLabel joinBannerLabel = new JLabel("Join"); 
		joinBannerLabel.setFont(new Font("Miriam", Font.BOLD, 42));
		joinBannerLabel.setBounds(12, 10, 181, 37);
		joinBannerPanel.add(joinBannerLabel);

		//비밀번호 입력란
		JPanel passwordTextFieldPanel = new JPanel();
		passwordTextFieldPanel.setLayout(null);
		passwordTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		passwordTextFieldPanel.setBounds(144, 104, 422, 32);
		joinFormPanel.add(passwordTextFieldPanel);

		passwordTextField = new JPasswordField();
		passwordTextField.setBounds(6, 5, 116, 21);
		passwordTextFieldPanel.add(passwordTextField);

		//비밀번호 확인 입력란
		JPanel passwordCheckTextFieldPanel = new JPanel();
		passwordCheckTextFieldPanel.setLayout(null);
		passwordCheckTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		passwordCheckTextFieldPanel.setBounds(144, 135, 422, 32);
		joinFormPanel.add(passwordCheckTextFieldPanel);

		passwordCheckTextField = new JPasswordField();
		passwordCheckTextField.setBounds(6, 5, 116, 21);
		passwordCheckTextFieldPanel.add(passwordCheckTextField);

		//이름 입력란
		JPanel nameTextFieldPanel = new JPanel();
		nameTextFieldPanel.setLayout(null);
		nameTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		nameTextFieldPanel.setBounds(144, 166, 422, 32);
		joinFormPanel.add(nameTextFieldPanel);

		nameTextField = new JTextField();
		nameTextField.setColumns(10);
		nameTextField.setBounds(6, 5, 116, 21);
		nameTextFieldPanel.add(nameTextField);

		//이메일 입력란
		JPanel emailTextFieldPanel = new JPanel();
		emailTextFieldPanel.setLayout(null);
		emailTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		emailTextFieldPanel.setBounds(144, 197, 422, 32);
		joinFormPanel.add(emailTextFieldPanel);

		emailTextField = new JTextField();
		emailTextField.setColumns(10);
		emailTextField.setBounds(6, 5, 220, 21);
		emailTextFieldPanel.add(emailTextField);

		//성별 선택란
		JPanel sexComboBoxPanel = new JPanel();
		sexComboBoxPanel.setLayout(null);
		sexComboBoxPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		sexComboBoxPanel.setBounds(144, 228, 422, 32);
		joinFormPanel.add(sexComboBoxPanel);

		String[] sex = {"남", "여"};
		sexComboBox = new JComboBox<String>(sex);
		sexComboBox.setBounds(6, 5, 60, 21);
		sexComboBoxPanel.add(sexComboBox);

		//홈페이지 입력란
		JPanel homepageTextFieldPanel = new JPanel();
		homepageTextFieldPanel.setLayout(null);
		homepageTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		homepageTextFieldPanel.setBounds(144, 259, 422, 32);
		joinFormPanel.add(homepageTextFieldPanel);

		homepageTextField = new JTextField();
		homepageTextField.setColumns(10);
		homepageTextField.setBounds(6, 5, 221, 21);
		homepageTextFieldPanel.add(homepageTextField);

		//전화번호 입력란
		JPanel phoneTextFieldPanel = new JPanel();
		phoneTextFieldPanel.setLayout(null);
		phoneTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		phoneTextFieldPanel.setBounds(144, 290, 422, 32);
		joinFormPanel.add(phoneTextFieldPanel);

		phoneTextField = new JTextField();
		phoneTextField.setColumns(10);
		phoneTextField.setBounds(6, 5, 116, 21);
		phoneTextFieldPanel.add(phoneTextField);

		//핸드폰 번호 입력란
		JPanel cellPhoneTextFieldPanel = new JPanel();
		cellPhoneTextFieldPanel.setLayout(null);
		cellPhoneTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		cellPhoneTextFieldPanel.setBounds(144, 321, 422, 32);
		joinFormPanel.add(cellPhoneTextFieldPanel);

		cellPhoneTextField = new JTextField();
		cellPhoneTextField.setColumns(10);
		cellPhoneTextField.setBounds(6, 5, 116, 21);
		cellPhoneTextFieldPanel.add(cellPhoneTextField);

		//주소 입력란
		JPanel addressTextFieldPanel = new JPanel();
		addressTextFieldPanel.setLayout(null);
		addressTextFieldPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		addressTextFieldPanel.setBounds(144, 352, 422, 32);
		joinFormPanel.add(addressTextFieldPanel);

		addressField = new JTextField();
		addressField.setColumns(10);
		addressField.setBounds(6, 5, 404, 21);
		addressTextFieldPanel.add(addressField);

		//자기소개 입력란
		JPanel introductionTextAreaPanel = new JPanel();
		introductionTextAreaPanel.setLayout(null);
		introductionTextAreaPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		introductionTextAreaPanel.setBounds(144, 383, 422, 77);
		joinFormPanel.add(introductionTextAreaPanel);

		JScrollPane introductionScrollPane = new JScrollPane();
		introductionScrollPane.setBounds(6, 10, 404, 58);
		introductionTextAreaPanel.add(introductionScrollPane);
		introductionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		introductionTextArea = new JTextArea(); 
		introductionScrollPane.setViewportView(introductionTextArea);
		introductionTextArea.setWrapStyleWord(true);
		introductionTextArea.setLineWrap(true);
		
		//버튼 패널
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(0, 470, 578, 36);
		joinFormPanel.add(buttonPanel);
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