package Server;

import java.io.*;
import java.net.*;

import MessageHandler.MsgHandler;


public class Guest extends Thread{
	private BufferedReader br;
	private BufferedWriter bw;
	private Server server;
	
	//클라이언트에서 넘어오는 메시지를 처리하기위한 클래스
	private MsgHandler msgHandler;
	
	//클라이언트가 교수인지 학생인지 알려주는 변수
	public boolean isProfessor;
	
	//클라이언트의 피시 번호
	public String pcNumber;
	
	//클라이언트의 아이디
	public String id;
	
	//클라이언트의 닉네임
	public String alias;
	
	//클라이언트가 방장인지 알려주는 변수
	public boolean isCaptain;
	
	
	//클라이언트의 소켓
	public Socket s;
	
	public Guest(Socket s, Server server){
		this.server = server;
		this.s = s;
		msgHandler = new MsgHandler(server, this);
		try{
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		}catch(IOException e){
			System.out.println("Guest constructor Error");
		}
	}
	
	//클라이언트로 메시지를 전달하는 메서드
	public void sendMsg(String msg){
		try{
			System.out.println("guest : " + msg);
			bw.write(msg + "\n");
			bw.flush();
		}catch(IOException e){
			System.out.println("Guest To Client");
		}
	}
	
	//클라이언트에서 들어오는 메시지를 처리하는 클래스로 넘겨주는 메서
	public void run(){
		try{
			String line = null;
			while((line = br.readLine()) != null){
				msgHandler.manageMsg(line);
			}
		}catch(Exception e){
			System.out.println("Guest Exception");
		}
	}
}