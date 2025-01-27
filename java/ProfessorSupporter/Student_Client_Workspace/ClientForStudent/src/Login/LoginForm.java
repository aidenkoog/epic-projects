package Login;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Controller.Manager;


public class LoginForm extends JFrame implements ActionListener, MouseListener{
	private static final long serialVersionUID = 1L;
	//컨텐츠 영역을 받기 위한 변수
	private Container contentPane = null;
	//컴포넌트 변수
	private JTextField numberTextField = null;
	private JTextField iDTextField = null;
	private JPasswordField passwordTextField = null;
	private JLabel registerLabel = null;
	private JButton loginButton = null;
	private JButton cancelButton = null;
	//컨트롤러 클래스 변수
	private Manager manager = null;	
	//이미지의 경로
	private final ImageIcon img = new ImageIcon(".\\image\\LoginBanner(320x57).png");
	
	private String pcNumber = null;

	//생성자
	public LoginForm(){
		this.setIconImage(new ImageIcon(".\\image\\학사모학생.png").getImage());
		this.setTitle("Log-In");			//프레임창 제목
		this.setAlwaysOnTop(true);			//항상 맨위 표시
		this.setSize(325, 230);				//로그인 창 크기
		this.displayCenter();				//로그인 화면 중앙 위치
		this.initializeMember();			//멤버변수 초기화
		this.setLayout(null);				//전체 레이아웃 설정
		this.setResizable(false);			//크기 조절 불가능
		this.createLoginForm();				//로그인 양식 생성
		this.addEventHandler();				//이벤트 처리기 등록
		
		numberTextField.setText("3");
		iDTextField.setText("DDD123");
		passwordTextField.setText("1");
	} //생성자 끝

	//멤버변수 설정 및 초기화
	private void initializeMember(){
		manager = new Manager();		//컨트롤러 클래스 객체 생성
		contentPane = getContentPane();	//컨텐츠 영역을 가져옴
	} //멤버변수 설정 및 초기화 끝
	
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
	private void addEventHandler(){
		loginButton.addActionListener(this);	//로그인 버튼
		cancelButton.addActionListener(this);	//취소 버튼
		registerLabel.addMouseListener(this);	//등록 레이블
		//프레임의 X눌렀을 때 아무 작업 안하게 세팅
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//프레임의 X버튼 눌렀을 때 수행
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.out.println("[로그인 창] X 버튼 클릭");
				LoginForm.this.exit();
			}
		});
	} //이벤트 처리기 등록 함수 끝
	
	//프레임의 X눌렀을 때 수행
	private void exit(){
		int result = JOptionPane.showConfirmDialog(this, "정말 끝내시겠습니까?", "Quit", JOptionPane.OK_CANCEL_OPTION, 
																					JOptionPane.QUESTION_MESSAGE);
		if(result == JOptionPane.OK_OPTION){ //0
			System.exit(0);	//프로그램 종료
		}
	}
	
	//피시번호 가져오기
	public String getPCNumber(){
		return numberTextField.getText();
	}
	
	//피시번호 스트링 가져오기
	public String getPCNumberString(){
		return pcNumber;
	}

	//아이디 가져오기
	public String getID(){
		return iDTextField.getText();
	}

	//비밀번호 가져오기
	public String getPassword(){
		String password = "";
		for(char c : passwordTextField.getPassword()){
			password += c;
		}
		return password;
	}
	
	
	//이벤트 처리기
	public void actionPerformed(ActionEvent actionEvent){
		//로그인 버튼
		if(actionEvent.getSource() == loginButton){
			System.out.println("[로그인 창] 로그인 버튼 클릭");
			pcNumber = numberTextField.getText();
			//컨트롤러에 수행 할 키 전송
			manager.service("Login");	
			this.numberTextField.setText("");
			this.iDTextField.setText("");		
			this.passwordTextField.setText(""); 
		}
		//취소 버튼
		else if(actionEvent.getSource() == cancelButton){
			System.out.println("[로그인 창] 취소 버튼 클릭");
			this.exit();
		}
	}
	
	//마우스 이벤트 처리
	//범위 진입시
	public void mouseEntered(MouseEvent e) {
		//손 모양 커서로 변환
		Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
		LoginForm.this.setCursor(cursor);
	} //범위 진입시 처리 끝
	
	//범위 탈출시
	public void mouseExited(MouseEvent e) {
		//기본 커서로 변환
		Cursor cursor = Cursor.getDefaultCursor();
		LoginForm.this.setCursor(cursor);
	} //범위 탈출시 처리 끝
	
	//범위 안에서 마우스 버튼 누를 시
	public void mousePressed(MouseEvent e) {
		//등록 레이블
		if(e.getSource() == registerLabel){
			//글자색 파란색으로 변경
			registerLabel.setForeground(Color.BLUE);
		}
	} //범위 안에서 마우스 버튼 누를 시 처리 끝
	
	//범위 안에서 마우스 버튼이 눌려진 후 릴리스될 시
	public void mouseReleased(MouseEvent e) {
		//등록 레이블
		if(e.getSource() == registerLabel){
			//글자색 검은색으로 변경
			registerLabel.setForeground(Color.BLACK);
		}
	} //범위 안에서 마우스 버튼이 눌려진 후 릴리스될 시 처리 끝
	
	//범위 안에서 마우스 버튼이 눌려지고 해제될 시
	public void mouseClicked(MouseEvent e) {
		//등록 양식 생성
		this.setAlwaysOnTop(false);
		Manager.JOINFORM.setVisible(true);
	} //범위 안에서 마우스 버튼이 눌려지고 해제될 시 처리 끝
	//
	//마우스 이벤트 처리 함수 끝
	
	//로그인 양식 생성
	private void createLoginForm(){
		//로그인 창 배너 이미지
		JPanel imagePanel = new JPanel(null){
			private static final long serialVersionUID = 1L;
			public void paintComponent(Graphics g){
				g.drawImage(img.getImage(),0, 0, 320, 57, null, null);
			}
		};
		imagePanel.setBounds(0, 0, 320, 57);

		//접속 번호
		JLabel numberLabel = new JLabel("PC번호");
		numberLabel.setBounds(23, 70, 62, 15);
		numberTextField = new JTextField(); 
		numberTextField.setBounds(97, 67, 197, 21);
		//아이디
		JLabel idLabel = new JLabel("아이디");
		idLabel.setBounds(23, 95, 62, 15);
		iDTextField = new JTextField(); 
		iDTextField.setBounds(97, 92, 197, 21);
		//비밀번호
		JLabel passwordLabel = new JLabel("비밀번호");
		passwordLabel.setBounds(23, 120, 62, 15);
		passwordTextField = new JPasswordField();
		passwordTextField.setBounds(97, 117, 197, 21);
		//등록 레이블
		registerLabel = new JLabel("등록"); 
		registerLabel.setBounds(23, 147, 31, 15);
		//로그인 버튼
		loginButton = new JButton("로그인");
		loginButton.setBounds(134, 147, 74, 23);
		//취소 버튼
		cancelButton = new JButton("취소");
		cancelButton.setBounds(220, 147, 74, 23);
		//컨텐츠 영역에 컴포넌트 추가
		contentPane.add(imagePanel);
		contentPane.add(numberLabel);
		contentPane.add(numberTextField);
		contentPane.add(idLabel);
		contentPane.add(iDTextField);
		contentPane.add(passwordLabel);
		contentPane.add(passwordTextField);
		contentPane.add(registerLabel);
		contentPane.add(loginButton);
		contentPane.add(cancelButton);
	} //로그인 양식 생성 함수 끝
}