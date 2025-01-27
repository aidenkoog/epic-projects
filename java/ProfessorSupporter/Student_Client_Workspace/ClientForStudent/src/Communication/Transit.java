package Communication;


import java.io.*;
import java.net.*;
import javax.swing.*;

import Controller.Manager;
import MessageHandler.MsgHandler;

//서버와 통신하기 위한 클래스
public class Transit extends Thread{
	public static BufferedReader br;
	public static BufferedWriter bw;
	
	public Transit(){
		try{
			Socket s = new Socket("localhost", 20000);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		}catch(Exception e){
			JOptionPane.showMessageDialog(Manager.LOGINFORM, "서버가 꺼진 상태 입니다.", "Server Error", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	//서버로 메시지 전송하는 메서드
	public static void sendMsg(String msg){
		System.out.println("Client To Server msg : " + msg);
		try{
			bw.write(msg + "\n");
			bw.flush();
		}catch(IOException ioe){
			ioe.getStackTrace();
		}
	}
	
	//서버로부터 메시지를 받는 메서드
	public void run(){
		try{
			String line = null;
			
			while((line = br.readLine())!=null){
				System.out.println("서버로 받은 내용 : " + line);
				new MsgHandler(line);
			}
		}catch(Exception e){
			System.exit(0);
		}
	}
}