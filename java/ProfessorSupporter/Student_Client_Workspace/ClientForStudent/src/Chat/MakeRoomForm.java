package Chat;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Controller.Manager;

public class MakeRoomForm extends JFrame implements ActionListener, ItemListener{
	private static final long serialVersionUID = 1L;
	//접속 제한 인원 세팅하는 콤보박스
	private JComboBox<String> personNumberComboBox = null;
	//정보 입력 창
	private JTextField roomNameTextField = null;
	private JTextField roomPasswordTextField = null;
	//버튼
	private JButton makeRoomButton = null;
	private JButton cancelButton = null;
	//컨트롤러 클래스
	private Manager manager = null;
	//제한 인원과 폰트 색을 기본적으로 선택
	private String personLimit = "2";
	private String titleColor = "BLACK";
	private Container contentPane = null;
	private final String[] person = {"2", "4", "8", "16"};

	//생성자
	public MakeRoomForm(){
		super("방 만들기");			//프레임창 제목
		this.setLayout(null);		//전체 레이아웃
		this.initializeMember();	//멤버 변수 초기화
		this.createMakeRoomForm();	//방 만들기 양식 생성
		this.setSize(270, 220);		//프레임 크기
		this.displayCenter();		//화면 중앙 위치
		this.setResizable(false);	//크기 조절 불가능
		this.mountEventHandler();	//이벤트 처리기 등록
	} //생성자 끝

	//멤버 변수 초기화
	private void initializeMember(){
		contentPane = this.getContentPane();
		manager = new Manager();
	} //멤버 변수 초기화 함수 끝

	//이벤트 처리기 등록
	private void mountEventHandler(){
		personNumberComboBox.addItemListener(this);
		makeRoomButton.addActionListener(this);
		cancelButton.addActionListener(this);

		//프레임의 X눌렀을 때 아무 작업 안하게 세팅
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//프레임의 X눌렀을 때 수행
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				//프레임 안보이게 함
				MakeRoomForm.this.setVisible(true);
				//자원해제
				MakeRoomForm.this.dispose();
			}
		});
	} //이벤트 처리기 등록 함수 끝

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

	//방 정보 얻기
	public String getInfo(){
		//입력한 방이름 세팅
		String roomNameLabel = this.roomNameTextField.getText();
		//입력한 비밀번호 세팅
		String roomPassword = this.roomPasswordTextField.getText();
		return roomNameLabel + "/" + personLimit + "/" + titleColor + "/" + roomPassword;
	}

	//이벤트 처리기
	public void actionPerformed(ActionEvent e){
		//방만들기 버튼
		if(e.getSource() == makeRoomButton){
			//컨트롤러에 수행 할 키 값 전송
			manager.service("MakeRoom");
			roomNameTextField.setText("");
			roomPasswordTextField.setText("");
		}
		//취소 버튼
		else{
			this.setVisible(true);	//프레임 안보이게 설정
			this.dispose();			//자원해제 
		}
	}

	public void itemStateChanged(ItemEvent e){
		//제한 인원 선택했을 때 수행
		if(e.getSource() == this.personNumberComboBox){
			//제한 인원 세
			personLimit = this.personNumberComboBox.getSelectedItem().toString();
		}
	}

	//방 만들기 양식 생성
	private void createMakeRoomForm(){
		//컴포넌트 생성
		JLabel titleLabel = new JLabel("방 만들기");
		JLabel roomNameLabel = new JLabel("방제목");
		JLabel roomPasswordLabel = new JLabel("비밀번호");
		JLabel personNumberLabel = new JLabel("인원");
		personNumberComboBox = new JComboBox<String>(person);
		roomNameTextField = new JTextField(20);
		roomPasswordTextField = new JTextField(20);
		makeRoomButton = new JButton("만들기");
		cancelButton = new JButton("취소");

		//버튼 서식 설정
		makeRoomButton.setFont(new Font("Dialog", Font.BOLD, 12));
		makeRoomButton.setMargin(new Insets(0, 0, 0, 0));
		makeRoomButton.setHorizontalTextPosition(JButton.LEFT);

		cancelButton.setFont(new Font("Dialog", Font.BOLD, 12));
		cancelButton.setMargin(new Insets(0, 0, 0, 0));
		cancelButton.setHorizontalTextPosition(JButton.LEFT);

		//컨텐츠 영역에 추가
		contentPane.add(titleLabel);
		contentPane.add(roomNameLabel);
		contentPane.add(roomPasswordLabel);
		contentPane.add(personNumberLabel);
		contentPane.add(personNumberComboBox);
		contentPane.add(roomNameTextField);
		contentPane.add(roomPasswordTextField);
		contentPane.add(makeRoomButton);
		contentPane.add(cancelButton);

		//컴포넌트 넓이, 크기, 위치
		titleLabel.setBounds(108, 3, 80, 25);
		roomNameLabel.setBounds(5, 30, 80, 25);
		roomPasswordLabel.setBounds(5, 60, 80, 25);
		personNumberLabel.setBounds(5, 100, 80, 25);
		personNumberComboBox.setBounds(50, 100, 50, 25);
		roomNameTextField.setBounds(60, 30, 200, 25);
		roomPasswordTextField.setBounds(60, 60, 200, 25);
		makeRoomButton.setBounds(60, 150, 65, 30);
		cancelButton.setBounds(140, 150, 65, 30);
	} //방 만들기 양식 생성 함수 끝
}