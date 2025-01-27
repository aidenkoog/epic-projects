import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import db.DBConnection;
import db.DBWork;

import beans.ChatMessage;
import beans.Operation;
import beans.RoomInfo;
import beans.TaxiUser;
import beans.UserInfo;
import beans.UserList;



public class TaxiServer extends Thread{

	
	HashMap<String, TaxiUser> clients;
	
	HashMap<String, ArrayList<TaxiUser>> chatRoom;
	
	
	//HashMap<String, RoomInfo> roomList;
	
	int port;
	
	ServerSocket serverSocket;
	Socket socket;
	
	public TaxiServer(){
		port = 9031;
		clients = new HashMap<String, TaxiUser>();
		chatRoom = new HashMap<String, ArrayList<TaxiUser>>();
		//roomList = new HashMap<String, RoomInfo>();
		Collections.synchronizedMap(clients);
		Collections.synchronizedMap(chatRoom);
		
		//Collections.synchronizedMap(roomList);
	}
	
	public void run(){
		try{
			DBWork dbWork = new DBWork();
			dbWork.initRoomInfo(DBConnection.getConnection());
			serverSocket = new ServerSocket(port);
			System.out.println("������ ���۵Ǿ����ϴ�.");
			
			while(true){
				socket = serverSocket.accept();
				System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "] ���� �����Ͽ����ϴ�.");
				
				ServerReceiver thread = new ServerReceiver(socket);
				thread.start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
	
	public void createRoom(Connection conn, RoomInfo info, TaxiUser user){
		//roomList.put(info.getRoomId(), info);
		DBWork dbWork = new DBWork();
		dbWork.insertRoomInfo(conn, info);
		ArrayList<TaxiUser> al = new ArrayList<TaxiUser>();
		al.add(user);
		chatRoom.put(info.getRoomId(), al);
		//System.out.println(chatRoom);
		//System.out.println(al);
	
	}
	public void enterRoom(RoomInfo info, TaxiUser user){
		ArrayList<TaxiUser> userList;
		userList = chatRoom.get(info.getRoomId()); 
		if(userList == null) userList = new ArrayList<TaxiUser>();
		userList.add(user);
		
	}
	
	public ArrayList<RoomInfo> getRoomList(Connection conn){
//		ArrayList<RoomInfo> al = new ArrayList<RoomInfo>();
//		Iterator<String> it = roomList.keySet().iterator();
//		while(it.hasNext()){
//			RoomInfo info = roomList.get(it.next());
//			al.add(info);
//			System.out.println("info : " +info);
//			
//		}
		DBWork dbWork = new DBWork();
		return dbWork.selectActivatedRoomInfoList(conn);
	}
	
	public void sendToAll(Object msg){
		Iterator it = clients.keySet().iterator();
		ObjectOutputStream out;
		while(it.hasNext()){
			out = clients.get(it.next()).getOutputStream();
			try {
				out.writeObject(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<String> getUserNameListInRoom(String roomId){
		ArrayList<String> al = new ArrayList<String>();
		ArrayList<TaxiUser> tmp;
		
		tmp = chatRoom.get(roomId);
		if(tmp != null)
		for(int i=0; i<tmp.size(); i++){
			al.add(((TaxiUser)tmp.get(i)).getName());
		}
		
		return al;
		
	}
	
	public void sendToRoom(Connection conn, RoomInfo roomInfo, ChatMessage msg){
		ArrayList<TaxiUser> al;
		TaxiUser user;
		ObjectOutputStream out;
		al = chatRoom.get(roomInfo.getRoomId());
		//System.out.println("room= " + al);
		if(al !=null)
		for(int i=0; i<al.size(); i++){
			user = al.get(i);
			//System.out.println("user = "+user.getPhoneNumber());
			out = user.getOutputStream();
			try {
				out.writeObject(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				al.remove(user);
				e.printStackTrace();
			}
		}
	}
	
	
	class ServerReceiver extends Thread{
		
		Socket socket;
		ObjectInputStream in;
		ObjectOutputStream out;
		
		public ServerReceiver(Socket socket){
			this.socket = socket;
			try{
				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());
				
			}catch(IOException e){
				
			}
		}
		
		
		public void run(){
			
			Connection conn = DBConnection.getConnection();
			DBWork dbWork = new DBWork();
			
			ChatMessage msg;
			Object obj;
			Operation op;
			TaxiUser user = null;
			RoomInfo roomInfo = null;
			UserInfo userInfo = null;
			try{
				System.out.println("������ ���Ի�");
				
				while(in != null){
					obj = in.readObject();
					if(obj instanceof ChatMessage){
						msg = (ChatMessage) obj;
						msg.setRoomId(roomInfo.getRoomId());
						System.out.println("[" + msg.getUserName() + "] : " + msg.getMsg());
						sendToRoom(conn, roomInfo, msg);
						dbWork.insertChatMessageLog(conn, msg);
						out.writeObject(roomInfo);
					}else if(obj instanceof Operation){
						op = (Operation) obj;
						switch(op.getState()){
						case Operation.OP_CREATE_ROOM: //�� ����
							System.out.println("����� ���� ����");
							if(obj instanceof RoomInfo){
								roomInfo = (RoomInfo) obj;
								roomInfo.setRoomId(dbWork.selecLastRoomId(conn)+"");
								user.setRoomId(roomInfo.getRoomId());
								user.setName(userInfo.getNickname());
								createRoom(conn, roomInfo, user);
								
								sendToAll(getRoomList(conn));
							}
							
							break;
						case Operation.OP_ENTER_ROOM: //�� ����
							System.out.println("������ ���� ����");
							if(obj instanceof RoomInfo){
								roomInfo = (RoomInfo) obj;
								int cur = dbWork.selectCurrNumberOfPeopleInRoomInfo(conn, roomInfo);
								int max = dbWork.selectMaxNumberOfPeopleInRoomInfo(conn, roomInfo);
								if(max > cur){
									if(dbWork.selectRoomState(conn, roomInfo) == 0){
										out.writeObject(getRoomList(conn));
										out.writeObject(new Operation(Operation.OP_NOT_EXIST_ROOM));
									}else{
										//�� ���� ���� �� �ܼ��� -+ �������� ê�뿡 �ִ� ��� ī��Ʈ�� ���� ������!
										dbWork.insertCurrNumberOfPeopleInRoomInfo(conn, roomInfo, cur+1);
										user.setRoomId(roomInfo.getRoomId());
										user.setName(userInfo.getNickname());
										enterRoom(roomInfo, user);
										out.writeObject(new Operation(Operation.OP_APPROVE_ENTER_ROOM));
										sendToRoom(conn, roomInfo, new ChatMessage("TaxiPool", "["+user.getName()+"] ���� �����ϼ̽��ϴ�."));
									}
								}else{
									out.writeObject(new Operation(Operation.OP_REJECT_ENTER_ROOM));
									out.writeObject(getRoomList(conn));
								}
								//sendToRoom(roomInfo.getRoomId(), msg); �ý��� �޽��� Ŭ���̾�Ʈ�� ������ ������!
								
							}
							break;
						case Operation.OP_PRINT_ROOM_LIST: //�渮��Ʈ ����
							System.out.println("�� ����Ʈ ��� ���� ����");
							out.writeObject(getRoomList(conn));
							break;
							
						case Operation.OP_ESCAPE_ROOM:
							System.out.println("�� ������ ���� ����");
							dbWork.insertCurrNumberOfPeopleInRoomInfo(conn, roomInfo, dbWork.selectCurrNumberOfPeopleInRoomInfo(conn, roomInfo)-1);
							if(dbWork.selectCurrNumberOfPeopleInRoomInfo(conn, roomInfo) < 1)
								dbWork.inActivationRoom(conn, roomInfo);
							((ArrayList<TaxiUser>)chatRoom.get(user.getRoomId())).remove(user);
							sendToRoom(conn, roomInfo, new ChatMessage("TaxiPool", "["+user.getName()+"] ���� �����ϼ̽��ϴ�."));
							roomInfo = null;
							break;
							
						case Operation.OP_FIRST_CONNECTION:
							System.out.println("������ ��������");
							userInfo = (UserInfo) obj;
							user = new TaxiUser(userInfo.getPhoneNumber(), out);
							user.setName(userInfo.getNickname());
							System.out.println("User : " + userInfo.getNickname() + "���� ������");
							clients.put(user.getPhoneNumber(), user);
							break;
							
						case Operation.OP_ADD_USER:
							System.out.println("���� �߰� ���� ����");
							if(obj instanceof UserInfo){
								userInfo = (UserInfo) obj;
								dbWork.insertUserInfo(conn, userInfo);
								break;
							}
							
						case Operation.OP_GET_ROOM_INFO:
							System.out.println("������ ��û ����");
							if(roomInfo != null){
								//�ϴ��� ����
							}
							break;
						
						case Operation.OP_GET_ROOM_USER_LIST:
							System.out.println("�濡 �ִ� ��������Ʈ ��û ����");
							if(roomInfo != null){
								UserList ul = new UserList(Operation.OP_GET_ROOM_USER_LIST);
								ul.setAl(getUserNameListInRoom(roomInfo.getRoomId()));
								System.out.println(ul.getAl());
								out.writeObject(ul);
							}
							break;
							
						}
						
						
					}
					
					//System.out.println(obj);
					//System.out.println("���� ���� ������ ���� " +  clients.size() + "�Դϴ�.");
				}
			}catch(IOException e){
				e.printStackTrace();
				if(roomInfo != null){
					dbWork.insertCurrNumberOfPeopleInRoomInfo(conn, roomInfo, dbWork.selectCurrNumberOfPeopleInRoomInfo(conn, roomInfo)-1);
					if(dbWork.selectCurrNumberOfPeopleInRoomInfo(conn, roomInfo) < 1)
						dbWork.inActivationRoom(conn, roomInfo);
					((ArrayList<TaxiUser>)chatRoom.get(user.getRoomId())).remove(user);
					//����� �������� �ý��۸޽��� �濡 �ѷ��ִ� ó�� ���ٰ�!
					sendToRoom(conn, roomInfo, new ChatMessage("TaxiPool", "["+user.getName()+"] ���� �����ϼ̽��ϴ�."));
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				//sendToAll("#" + name + "���� �����˾��.");
				clients.remove(user.getPhoneNumber());
				//System.out.println(socket.getInetAddress() + " ��������");
				//System.out.println("���� ���� ������ ���� " +  clients.size() + "�Դϴ�.");
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
