package com.example.taxiclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketManager {

	

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String ip;
	private int port; 
	private int ServerConnectTimeout;
	
	private static SocketManager instance = null;
	
	private SocketManager(){
		ip = "59.23.25.148";
		port = 9031;
		ServerConnectTimeout = 5;
	}
	
	public static SocketManager getInstance(){
		if(instance == null) instance = new SocketManager();
		return instance;
	}
	
	public void connect() throws IOException{
		InetSocketAddress socketAddress = new InetSocketAddress(ip, port);
		socket = new Socket();
		socket.connect(socketAddress, ServerConnectTimeout * 1000);
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
	}
	
	public void writeObj(Object obj) throws IOException{
		out.writeObject(obj);
	}
	public Object readObj() throws IOException, ClassNotFoundException{
		return in.readObject();
	}
	
	public void socketClose(){
		if(!socket.isClosed()){
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {}
		}
	}
	
}
