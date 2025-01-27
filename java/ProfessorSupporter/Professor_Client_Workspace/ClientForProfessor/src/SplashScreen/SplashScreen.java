package SplashScreen;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

import Controller.Manager;

public class SplashScreen extends JWindow implements Runnable{
	private static final long serialVersionUID = 1L;
	private Container contentPane = null;		//컨텐츠 영역을 받기 위한 변수
	private JProgressBar progressBar = null;	//프로그레스 바 변수
	private ImageIcon img = null;				//로딩 화면 이미지 변수
	//이미지의 경로
	private final String imagePath = ".\\image\\Professor-EyeVersion(450x208).png";
	
	//생성자
	public SplashScreen(){
		this.initializeMember();			//멤버 변수 초기화
		this.setSize(450,208);				//로딩 화면 크기 
		this.displayCenter();				//로딩 화면 중앙 위치
	} //생성자 끝
	
	//멤버 변수 초기화 및 설정
	private void initializeMember(){
		contentPane = this.getContentPane();				//컨텐츠 영역을 가져옴
		img = new ImageIcon(imagePath);						//이미지 아이콘 생성
		progressBar = new JProgressBar();					//프로그레스 바 생성
		progressBar.setVisible(false);						//프로그레스 바 보이지 않기
		progressBar.setMinimum(0);							//프로그레스 바 최소값 0	
		progressBar.setMaximum(100);						//프로그레스 바 최대값 100
		contentPane.setLayout(new BorderLayout());			//전체 보더 레이아웃
		contentPane.add(progressBar, BorderLayout.SOUTH);	//프로그레스 바 장착
	} //멤버 변수 초기화 및 설정 함수 끝
	
	//그리기
	public void paint(Graphics g){
		g.drawImage(img.getImage(),0, 0, 450, 208, null, null);
	} //그리기 함수 끝
	
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
	
	//로그인 창 표시
	public void createLoginWindow(){
		threadSleep(1000);					//로그인 창 표시되기 전 잠시 슬립
		Manager.LOGINFORM.setVisible(true);	//로그인 화면 보이기
	} //로그인 창 표시 함수 끝
	
	//프로그레스 바 진행 시작
	public void startProgressBar(){
		System.out.println("[로딩화면] 프로그레스 바 진행 시작");
		this.setAlwaysOnTop(true);	//맨 위로 표시
		new Thread(this).start();
	} //프로그레스 바 진행 시작 함수 끝
	
	//프로그레스 바 진행 스레드
	public void run(){
		threadSleep(1000);					//프로그레스 바 표시되기 전 잠시 슬립
		progressBar.setVisible(true);	//프로그레스 바 표시		
		threadSleep(1200);					//프로그레스 바 표시된 후 잠시 슬립
		//프로그레스 바에 진행 정도 표시
		for(int i=0; i<100; i++){
			progressBar.setValue(i+1);
			threadSleep(15);
		}
		this.setVisible(false); //로딩 화면을 보여주지 않음
		this.dispose();			//로딩 화면 리소스 제거
		Manager.MAINFRAME.setVisible(true);	//메인화면 표시
	} //프로그레스 바 진행 스레드 함수 끝
	
	//스레드 슬립
	private void threadSleep(long mills){
		try {
			Thread.sleep(mills);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	} //스레드 슬립 함수 끝
}