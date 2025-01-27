package ScreenShot;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;


public class CapturedScreen extends JFrame{
	private static final long serialVersionUID = 1L;
	private Image img = null;
	private BufferedImage bufImage = null;
	//	private JPanel contentPane = null;

	//생성자
	public CapturedScreen() {
//		//메뉴바 생성
//		JMenuBar menuBar = new JMenuBar();
//		this.setJMenuBar(menuBar);
//		
//		JMenu menu = new JMenu("학생화면");
//		menuBar.add(menu);
		//		contentPane = new JPanel();
		//		this.setContentPane(contentPane);
		System.out.println("캡쳐 스크린 생성자 진입");
		this.setIconImage(new ImageIcon(".\\image\\학사모.png").getImage());		//프레임 이미지
		this.setTitle("학생 화면");	//프레임창 제목
		this.setSize(400,400);		//프레임 크기
		//		this.setFocusable(false);
		this.displayCenter();		//화면 중앙 표시
		//X버튼 눌렀을 때 수행
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				CapturedScreen.this.dispose();
			}
		});
	} //생성자 끝

	//스샷 화면 보이기
	public void showScreenShot(BufferedImage bufImage){
		this.bufImage = bufImage;	//버퍼 이미지 저장
		int w = this.getWidth();
		int h = this.getHeight();
		this.img = bufImage.getScaledInstance(w, h, Image.SCALE_SMOOTH);
		this.drawImage(img, w, h);
	}

	private void drawImage(Image img, int x, int y) {
		Graphics g = this.getGraphics();
		g.drawImage(img, 0, 0, x, y, this);
		this.paint(g);
		this.repaint();
	}

	public void paint(Graphics g) {
		if (this.img != null) {
			g.drawImage(this.img, 0, 0, this.img.getWidth(this), this.img.getHeight(this), this);
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
}
