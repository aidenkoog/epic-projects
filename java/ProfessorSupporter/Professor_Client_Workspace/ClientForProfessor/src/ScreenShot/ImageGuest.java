package ScreenShot;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.imageio.ImageIO;

import Controller.Manager;

public class ImageGuest extends Thread{
	//이미지 서버 참조 변수
	private ImageServer server = null;
	//스트림 변수
	ObjectInputStream objectInputStream = null;
	//클라이언트의 소켓
	public Socket socket = null;
	
	BufferedImage bufferedImage = null;
	
	//생성자
	public ImageGuest(Socket socket, ImageServer server){
		this.socket = socket;
		this.server = server;
		try{
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			System.out.println("스트림 생성 성공");
			
			bufferedImage = ImageIO.read(objectInputStream);
//			if(server.check == false){
				Manager.CAPTUREDSCREEN.setVisible(true);
//				server.check = true;
//			}
			Manager.CAPTUREDSCREEN.showScreenShot(bufferedImage);
			
		}catch(IOException e){
			System.out.println("이미지 게스트 생성자 에러");
			try {
				socket.close();
				objectInputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
