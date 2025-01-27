package ScreenShot;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;



public class ImageServer extends Thread{
	Vector<ImageGuest> guestlist;
	ServerSocket serverSocket;
	public static boolean check = false;
	//생성자
	public ImageServer(){
		guestlist = new Vector<ImageGuest>();
	}
	
	public void run(){
		try {
			serverSocket = new ServerSocket(20001);
			while(true){
				//클라이언트 대기
				System.out.println("[교수 클라이언트] 클라이언트 대기중");
				Socket socket = serverSocket.accept();
				System.out.println("[교수 클라이언트] 클라이언트 접속");
				
				ImageGuest imageGuest = new ImageGuest(socket, this);
				imageGuest.start();
			}
		} catch (IOException e) {
			System.out.println("[이미지 서버] 생성자 에러");
		}
	}
}
