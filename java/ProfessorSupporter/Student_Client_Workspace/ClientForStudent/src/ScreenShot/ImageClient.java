package ScreenShot;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

import Communication.Transit;


public class ImageClient extends Thread{
	BufferedImage bufImage = null;
	//스트림 변수
	ObjectOutputStream objectOutputStream = null;
	//소켓
	Socket socket = null;
	//생성자
	public ImageClient(String option){
		try {
			socket = new Socket("localhost", 20001);
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("스트림 생성 성공");
			
			if(option.equals("ScreenShot")){
				this.captureScreen();
			}
			else if(option.equals("NonStop")){
				this.captureScreenNonstop();
			}
			this.sendImage();
			
			//소켓 스트림 닫기
			socket.close();
			objectOutputStream.close();
		}catch (IOException e) {
			System.out.println("서버 연결 실패");
		}
	} //생성자 끝
	
	//이미지 전송
	public void sendImage(){
		this.captureScreen();
		try {//이미지 객체 전송
			ImageIO.write(bufImage, "jpg", objectOutputStream);
			System.out.println("서버로 이미지 전송 완료");
		} catch (IOException e) {
			System.out.println("이미지 전송 실패");
		}
	}
	//연속화면
	private void captureScreenNonstop(){
		Robot robot = null;
		try{
			robot = new Robot();
			Rectangle area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			//캡쳐 하기
			bufImage = createJustScaledImage(robot.createScreenCapture(area), 400);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//스크린 캡쳐
	private void captureScreen(){
		Robot robot = null;
		try{
			robot = new Robot();
			Rectangle area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			//캡쳐 하기
			bufImage = robot.createScreenCapture(area);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//쓰레드
	public void run(){
		while(true){
			try {
				System.out.println("이미지 찍기 스레드 진입");
				socket = new Socket("localhost", 20001);
				objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
				this.captureScreenNonstop();
				ImageIO.write(bufImage, "jpg", objectOutputStream);
				System.out.println("이미지 전송 완료");
				
				socket.close();
				objectOutputStream.close();
			} catch (IOException e) {
				System.out.println("이미지 찍기 스레드 에러");
				break;
			}
		}
		Transit.sendMsg("프레임창보이기/");
	}

	//썸네일 처리
	private BufferedImage createJustScaledImage(BufferedImage image, int targetSize) throws Exception{
		double resizeRatio = 1.0;
		if(image.getWidth() > image.getHeight()){
			resizeRatio = (double)targetSize / image.getWidth();
		}
		else{
			resizeRatio = (double)targetSize / image.getHeight();
		}

		if(resizeRatio > 1){
			throw new Exception("Error");
		}

		int width = (int)(image.getWidth() * resizeRatio);
		int height = (int)(image.getHeight() * resizeRatio);

		int type = (image.getTransparency() == Transparency.OPAQUE)? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB; 
		BufferedImage bufferedImage = new BufferedImage(width, height, type);
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		AffineTransform xform = AffineTransform.getScaleInstance(resizeRatio,  resizeRatio);
		g2.drawRenderedImage((RenderedImage)image, xform);

		g2.dispose();

		return bufferedImage;
	}
}