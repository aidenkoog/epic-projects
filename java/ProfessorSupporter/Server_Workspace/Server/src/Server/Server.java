package Server;

import java.net.*;
import java.util.*;

import Database.DBConnection;
import Database.DBTable;

public class Server extends Thread{
	//대기실에 접속하는 유저 목록하는 메서드
	public Vector<Guest> guestlist;
	
	//방 이름과 방에 접속하는 유저목록하는 메서드
	HashMap<String, Vector<Guest>> roomForGuest;
	
	//방에 대한 정보하는 메서드
	HashMap<String, String[]> roomInfo;
	
	//소켓열고 대기하는 메서드
	public Server(){
		this.initializeMember();
		this.createDatabase();
	}
	
	public void run(){
		try{
			@SuppressWarnings("resource")
			ServerSocket ss = new ServerSocket(20000);
			while(true){
				System.out.println("[서버] 클라이언트 대기중");
				Socket s = ss.accept();
				System.out.println("[서버] 클라이언트 접속");
				Guest g = new Guest(s, this);
				g.start();
			}
		}catch(Exception e){
			System.out.println("서버 에러");
		}
	}
	
	//멤버변수 초기화
	private void initializeMember(){
		guestlist = new Vector<Guest>();
		roomForGuest = new HashMap<String, Vector<Guest>>();
		roomInfo = new HashMap<String, String[]>();
	}
	
	//교수 클라이언트에 컴퓨터 자리표시
	public void occupyComputer(String msg){	//컴퓨터표시/id/pw/pcNumber/교수여부/
		System.out.println("교수 클라이언트에게 컴퓨터자리 표시  : " + msg);	
		for(Guest g : guestlist){
			if(g.isProfessor == true){
				g.sendMsg(msg);
			}
		}
	}
	
	//데이터베이스 연결 및 테이블 생성
	private void createDatabase(){
		new DBConnection();
		new DBTable();
	}
	
	//접속하는 인원을 대기실 유저리스트에 추가 및 대기실 대화창에 입장메시지 출력하는 메서드
	public void addGuest(Guest g){
		guestlist.add(g);
		broadcast("대기실입장/" + g.alias);
	}
	
	//방을 만들었을때 방목록에 방 추가
	public void addRoom(String roomname){
		roomForGuest.put(roomname, new Vector<Guest>());
	}
	
	//방에 입장했을 때 방의 유저리스트에 유저 추가 및 그 방의 제한 인원 체크하는 메서드
	public boolean addRoomguest(Guest g, String roomname){
		Vector<Guest> rguest = roomForGuest.get(roomname);
		String[] r = roomInfo.get(roomname);

		int limit = Integer.parseInt(r[1]);
		if(rguest.size() >= limit){
			g.sendMsg("JoinRoom/정원이 다 찼습니다.");
			return true;
		}
		else{
			rguest.add(g);
			return false;
		}
	}
	
	//대기실 대화창에 메시지 출력하는 메서드
	public void broadcast(String msg){
		System.out.println("broadcast : " + msg);
		for(Guest g : guestlist){
			g.sendMsg(msg);
		}
	}
	
	//대기실 유저목록을 대기실 리스트에 출력
	public void broadcastGuestlist(){
		String s = "대기실손님목록/";
		for(Guest g : guestlist){
			s += g.alias + "/";
		}
		broadcast(s);
	}
	
	//접속해 있는 특정한 유저에게 메시지 출력하는 메서드
	public void broadcastProfessor(String msg){	
		System.out.println("교수 클라이언트에게 전달 : " + msg);	
		for(Guest g : guestlist){
			if(g.isProfessor == true){
				g.sendMsg(msg);
			}
		}
	}
	
	//자신을 제외한 나머지사람들에게 메시지 출력
	public void broadcastExceptMe(String msg){	//전체쪽지/메시지/보낸사람
		String[] temp = msg.split("/");
		System.out.println("전체에게 전달 : " + msg);	
		for(Guest g : guestlist){
			if(g.alias.equals(temp[2])){}
			else{ g.sendMsg(msg); }
		}
	}
	
	//접속해 있는 특정한 유저에게 메시지 출력하는 메서드
	public void broadcastonlysomeone(String msg){	// 대기실귓속말/msg/sender/receiver, 쪽지/jtp_NoteForm_text.getText()/sender/receiver
		String[] temp = msg.split("/");				// 1:1대화/msg/sender/receiver,  파일전송수락/senderIPaddr/sender/receiver
		System.out.println("boradcastwhisper : " + msg);	// 1:1대화요청/님이 1:1대화를 신청하셨습니다.\n수락하시겠습니까?/sender/receiver
		for(Guest g : guestlist){		
			if(g.alias.equals(temp[3])){
				g.sendMsg(msg + "/ ");
			}
			else if(temp[0].equals("대기실귓속말") && g.alias.equals(temp[2])){
				g.sendMsg(msg + "/자기자신");
			}
		}
		
		Iterator<Vector<Guest>> iterator = roomForGuest.values().iterator();
		while(iterator.hasNext()){
			for(Guest g : iterator.next()){
				if(g.alias.equals(temp[3])){
					g.sendMsg(msg);
				}
			}
		}
	}
	
	//방의 대화창에 메시지 출력하는 메서드
	public void broadcastRoom(String msg, String roomname){
		Vector<Guest> rguest = roomForGuest.get(roomname);
		if(rguest == null){
			return;
		}
		for(Guest g : rguest){
			g.sendMsg(msg);
		}
	}
	
	//방에 입장해 있는 유저 목록을 방의 유저리스트에 출력하는 메서드
	public void broadcastRoomguestlist(String roomname){
		Vector<Guest> rguest = roomForGuest.get(roomname);
		if(rguest == null){
			return;
		}
		String s = "방손님목록/";
		for(Guest g : rguest){
			s += g.alias + "/";
		}
		broadcastRoom(s, roomname);
	}
	
	//생성돼 있는 방의 리스트를 대기방 방 리스트에 출력하는 메서드(비번방이면 방이름 앞에 [비번]표시)
	public void broadcastRoomlist(){
		String s = "방목록/";
		Iterator<String> iterator = roomForGuest.keySet().iterator();
		
		while(iterator.hasNext()){
			String room = iterator.next();
			Vector<Guest> currentguest = roomForGuest.get(room);
			String[] info = roomInfo.get(room); // captain/limit/roompw
			if(info[2] != null){
				s += "[비번] " + room;
			}
			else{
				s += room;
			}
			s += "(" + currentguest.size() + "\\" + info[1] + ")/";
		}
		broadcast(s);	// 방목록/([비번])roomname(currentguest/personlimit)
	}
	
	//방에 있는 특정 유저에게만 메시지 출력하는 메서드
	public void broadcastRoomonlysomeone(String msg, String roomname){ // flag/msg/something/selectedId
		String[] temp = msg.split("/");				
		Vector<Guest> rguest = roomForGuest.get(roomname);
		
		for(Guest g : rguest){
			if(g.alias.equals(temp[3])){
				g.sendMsg(msg);
			}
		}
	}
	
	//채팅 프로그램에 접속해 있는 모든 유저의 닉네임을 리턴하는 메서드
	public String getallconnectedguest(){
		String s = "";
		for(Guest g : guestlist){
			s += g.alias + "/";
		}
		Iterator<Vector<Guest>> iterator = roomForGuest.values().iterator();
		while(iterator.hasNext()){
			for(Guest g : iterator.next()){
				s += g.alias + "/";
			}
		}
		return s;
	}
	
	//방장이 방장을 넘기지 않고 방을 빠져나갔을 경우 방장 권한을 그 다음 유저에게 넘기기 위해서 그 유저를 리턴하는 메서드
	public String getNextGuest(String roomname){
		Vector<Guest> rguest = roomForGuest.get(roomname);
		return rguest.get(1).alias;
	}
	
	//현재 방에 접속해 있는 인원수를 리턴하는 메서드
	public int getRoomguestNumber(String roomname){
		return roomForGuest.get(roomname).size();
	}
	
	//특정 방의 방장, 제한 인원, 비밀번호등의 정보를 리턴하는 메서드
	public String[] getRoomInfo(String roomname){
		return roomInfo.get(roomname);
	}

	//대기실의 유저를 방에 초대하기 위해 대기실 유저 목록을 출력하는 메서드
	public void guestListForInvite(Guest guest, String roomname){
		String s = "대기방유저목록/" + roomname + "/";
		for(Guest g : guestlist){
			s += g.alias + "/";
		}
		guest.sendMsg(s);
	}

	//방장의 권한을 넘기는 메서드
	public void giveGrant(String alias, String roomname){
		Vector<Guest> rguest = roomForGuest.get(roomname);
		for(Guest g : rguest){
			g.isCaptain = false;
			if(g.alias.equals(alias)){
				g.isCaptain = true;
			}
			g.sendMsg("방장/" + g.isCaptain);
		}
	}
	
	//자신이 이미 접속해 있는지 체크하는 메서드
	public boolean isConnected(String alias){
		boolean isConnected = false;
		for(Guest g : guestlist){
			if(alias.equals(g.alias))
				isConnected = true;
		}
		Iterator<String> iterator = roomForGuest.keySet().iterator();
		while(iterator.hasNext()){
			Vector<Guest> roomguest = roomForGuest.get(iterator.next());
			for(Guest g : roomguest){
				if(alias.equals(g.alias))
					isConnected = true;
			}
		}
		return isConnected;
	}
	
	//프로그램 종료시 유저를 대기실 유저 목록에서 삭제하는 메서드 및 대기실 대화창에 퇴장 메시지 출력하는 메서드
	public void removeGuest(Guest g){
		guestlist.remove(g);
		broadcast("대기실퇴장/" + g.alias);
	}
	
	//방이 없어졌을 경우 방을 방 목록에서 제거하는 메서드
	public void removeRoom(String roomname){
		roomForGuest.remove(roomname);
	}
	
	//방에서 유저가 퇴장했을 때 그 방의 유저 목록에서 유저를 제거하는 메서드
	public void removeRoomguest(Guest g, String roomname){
		Vector<Guest> rguest = roomForGuest.get(roomname);
		rguest.remove(g);
	}
	
	//같은 이름의 방이 존재하는지 체크하는 메서드
	public boolean roomCheck(String roomname){
		Iterator<String> roomlist = roomForGuest.keySet().iterator();
		boolean isRoomExist = false;
		while(roomlist.hasNext()){
			if(roomname.equals(roomlist.next())){
				isRoomExist = true;
				break;
			}
		}
		return isRoomExist;
	}

	//방에 접속해 있는 유저들의 목록을 출력하는 메서드
	public void roomGuestList(Guest guest, String roomname){
		Vector<Guest> rguest = roomForGuest.get(roomname);
		String s = "방정보/";
		for(Guest g : rguest){
			s += g.alias + "/";
		}
		guest.sendMsg(s);
	}
	
	//방 만드는 유저에게 방장 권한을 부여하는 메서드
	public void setCaptain(String roomname){
		Vector<Guest> rguest = roomForGuest.get(roomname);
		for(Guest g : rguest){
			g.sendMsg("방장/" + g.isCaptain);
		}
	}
	
	//방 만들때 그 방의 정보를 세팅하는 메서드
	public void setRoomInfo(String roomname, String captain, String limit, String roompw){
		roomInfo.put(roomname, new String[]{captain, limit, roompw});
	}

	//방에 입장하거나 퇴장할 때마다 방의 정보를 갱신하는 메서드
	public void updateRoomInfo(String roomname, String captain, String limit, int currentguest){
		String s = "UpdateRoomInfo/" + roomname + "/" + captain + "/" + limit + "/" + currentguest;
		this.broadcastRoom(s, roomname);
	}
}