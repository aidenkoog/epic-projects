package MessageHandler;
import java.util.*;

import Database.DBWork;
import Server.Guest;
import Server.Server;

public class MsgHandler{
	private Server server;
	private Guest guest;

	public MsgHandler(Server server, Guest guest){
		this.server = server;
		this.guest = guest;
	}

	private String getAllAlias(){
		String alias = server.getallconnectedguest();
		return alias;
	}

	//클라이언트에서 들어온 메시지를 처리하는 메서드
	public void manageMsg(String msg){
		System.out.println("Server MsgHandler : " + msg);

		//들어온 메시지를 파싱함
		StringTokenizer st = new StringTokenizer(msg, "/");
		String[] arr = new String[st.countTokens()];
		for(int i = 0; st.hasMoreTokens(); i++){
			arr[i] = st.nextToken();
		}

		//회원가입 창에서 중복확인 버튼
		if(arr[0].equals("중복확인")){	
			//해당 아이디가 이미 있는지 없는지 검사
			boolean isDuplicated = DBWork.isIdInTheExistence(arr[1]);
			//아이디 중복
			if(isDuplicated){
				guest.sendMsg("중복확인처리후/중복");
			}
			//아이디 중복아님
			else{
				guest.sendMsg("중복확인처리후/중복아님");
			}
		}
		//회원가입
		else if(arr[0].equals("회원가입")){
			//해당 아이디가 이미 있는지 없는지 검사
			boolean isDuplicated = DBWork.isIdInTheExistence(arr[1]);
			//아이디 중복
			if(isDuplicated){
				guest.sendMsg("중복확인처리후/중복");
			}
			//아이디 중복아님
			else{
				if(DBWork.addPeople(arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7], arr[8], arr[9], arr[10])){
					guest.sendMsg("회원가입처리후/성공");
				}
				else{
					guest.sendMsg("회원가입처리후/실패");
				}
			}
		}
		//메인회원가입 창에서 중복확인 버튼
		if(arr[0].equals("메인중복확인")){	
			//해당 아이디가 이미 있는지 없는지 검사
			boolean isDuplicated = DBWork.isIdInTheExistence(arr[1]);
			//아이디 중복
			if(isDuplicated){
				guest.sendMsg("메인중복확인처리후/중복");
			}
			//아이디 중복아님
			else{
				guest.sendMsg("메인중복확인처리후/중복아님");
			}
		}
		//메인회원가입
		else if(arr[0].equals("메인회원가입")){
			//해당 아이디가 이미 있는지 없는지 검사
			boolean isDuplicated = DBWork.isIdInTheExistence(arr[1]);
			//아이디 중복
			if(isDuplicated){
				guest.sendMsg("메인중복확인처리후/중복");
			}
			//아이디 중복아님
			else{
				if(DBWork.addPeople(arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7], arr[8], arr[9], arr[10])){
					guest.sendMsg("메인회원가입처리후/성공");
				}
				else{
					guest.sendMsg("메인회원가입처리후/실패");
				}
			}
		}
		//프로그램에 로그인하는 작업
		else if(arr[0].equals("Login")){	// Login/id/pw/pcNumber/교수여부
			boolean isNull = false;
			System.out.println("Login");

			//입력하지 않은 정보가 있는지 체크
			if(arr.length != 5)
				isNull = true;

			//입력하지 않은 정보가 있을 때
			if(isNull){
				guest.sendMsg(arr[0] + "/입력하지 않은 정보가 있습니다.");
			}
			//아이디와 비밀번호를 모두 입력하였을 때
			else{
				//String pw = "";
				String alias = "";

				alias = arr[1];
				String[] all = getAllAlias().split("/");
				boolean isConn = false;
				for(String s: all){
					if(s.equals(alias)){
						isConn = true;
						break;
					}
				}
				if(isConn){
					guest.sendMsg(arr[0] + "/이미 접속되어 있습니다.");
				}
				else{
					boolean isMatch = DBWork.isEqualIdAndPassword(arr[1], arr[2]);
					//아이디와 비밀번호와 일치되면
					if(isMatch){
						System.out.println("[서버] 메인화면 호출을 위한 준비");
						//클라이언트의 교수여부에 셋팅
						guest.isProfessor = Boolean.parseBoolean(arr[4]);
						//클라이언트의 피시 번호에 번호를 세팅
						guest.pcNumber = arr[3];
						//클라이언트의 아이디에 아이디를 세팅
						guest.id = arr[1];
						//클라이언트의 닉네임에 닉네임을 세팅
						if(guest.isProfessor == true){
							guest.alias = alias + "(교수)";
						}else{
							guest.alias = alias;
						}
						//대기실 접속 유저 목록에 클라이언트 추가
						server.addGuest(guest);
						//대기실 접속 유저를 대기실의 유저리스트에 출력
						server.broadcastGuestlist();
						//대기실의 방 리스트를 출력
						server.broadcastRoomlist();

						if(guest.isProfessor == false){
							//교수 클라이언트의 컴퓨터 자리화면에 표시
							String name = DBWork.getName(guest.id);
							// 컴퓨터표시/id/pw/pcNumber/교수여부/이름
							server.occupyComputer("컴퓨터표시" + "/" + arr[1] + "/" + arr[2] + "/" + arr[3] + "/" + arr[4] + "/" + name); 
							System.out.println("컴퓨터 자리 화면에 표시!!!");
						}

						//대기실로 넘어가기 위한 메시지 전송
						guest.sendMsg(arr[0] + "/true");

						System.out.println("[서버] 메인화면 호출을 위한 준비");
					}
					//아이디와 비밀번호가 일치되지 않으면
					else{
						guest.sendMsg(arr[0] + "/false");
					}
				}
			}
		}
		//로그아웃했을 때 로그인 폼을 띄우는 작
		else if(arr[0].equals("Logout")){
			//로그인 폼을 띄우기 위한 메시지 전송
			guest.sendMsg(arr[0]);

			//교수 클라이언트에서 컴퓨터 자리 제거
			server.broadcastProfessor("컴퓨터자리제거/"+ guest.pcNumber);

			//대기실의 유저 목록에서 클라이언트를 제거
			server.removeGuest(guest);
			//대기실의 유저 목록을 대기실의 유저 리스트에 출력
			server.broadcastGuestlist();
		}
		//대기실 대화를 처리하는 작업
		else if(arr[0].equals("대기실대화")){ // 대기실대화/jtf_waitRoom_write.getText()/fontName/fontSize/fontColor/toUser
			System.out.println("Server 대기실대화 : " + msg);
			//대기실에 접속한 모든 사람에게 메시지를 출력
			if(arr[5].equals("모두에게")){
				server.broadcast(arr[0] + "/" + "[" + guest.alias + "] " + arr[1] + "/" + arr[2] + "/" + arr[3] + "/" + arr[4]);
			}
			//귓속말을 할 대상이 자신이면 에러메시지 출력
			else if(arr[5].equals(guest.alias)){
				guest.sendMsg("Error/자신에게 귓속말을 할 수 없습니다.");
			}
			//대기실의 특정한 유저에게만 메시지 출력(귓말 기능)
			else
				server.broadcastonlysomeone("대기실귓속말/" + arr[1] + "/" + guest.alias + "/" + arr[5]);
		}
		//방 만드는 작업
		else if(arr[0].equals("MakeRoom")){     //	MakeRoom/roomname/personlimit/roompw
			//만들려고 하는 방 이름이 이미 존재하는지 체크
			if(server.roomCheck(arr[1])){
				guest.sendMsg("RoomError/이미 존재하는 방입니다.");
			}
			else{
				//클라이언트가 방장이라는 정보
				guest.isCaptain = true;
				//만든 방을 해쉬맵에 추가
				server.addRoom(arr[1]);
				//비밀번호가 있는 방을 만드는 작업
				if(arr.length == 5){
					//방 정보를 해쉬맵에 추가
					server.setRoomInfo(arr[1], guest.alias, arr[2], arr[4]);
				}
				//비밀번호가 없는 방을 만드는 작업
				else{
					//방 정보를 해쉬맵에 추가
					server.setRoomInfo(arr[1], guest.alias, arr[2], null);
				}
				//클라이언트를 대기실 유저 목록에서 제거
				server.removeGuest(guest);

				//방 유저 목록에 자신을 추가
				server.addRoomguest(guest, arr[1]);
				//방에 현재 접속한 인원을 얻는 작업
				int num = server.getRoomguestNumber(arr[1]);
				//자신에게 방만드는데 필요한 메시지 전송
				guest.sendMsg("MakeRoom/" + guest.alias + "/" + arr[1] + "/" + arr[2] + "/" + num);

				//생성한 방에 입장메시지 출력
				server.broadcastRoom("방입장/" + "[" + guest.alias + "]", arr[1]);
				//자신을 방장으로 세팅
				server.setCaptain(arr[1]);
				//방 유저 목록을 방의 유저리스트에 출력
				server.broadcastRoomguestlist(arr[1]);
				//대기실 유저 목록을 대기실 유저리스트에 출력
				server.broadcastGuestlist();
				//대기실 방 목록리스트에 방 목록을 출력
				server.broadcastRoomlist();
			}
		}
		//방에 입장하는 작업
		else if(arr[0].equals("JoinRoom")){ //JoinRoom/roomname/roompw
			//비밀번호가 있는 방에 입장하는 작업
			if(!arr[2].equals("null")){
				//선택한 방에 대한 정보를 얻는 작업
				String[] roomInfo = server.getRoomInfo(arr[1]); //roomname/captain/limit/roompw
				//방의 비밀번호가 입력한 비밀번호와 일치하는지 체크
				if(roomInfo[2].equals(arr[2])){
					//입장하는 작업 처리
					joinRoom(arr[1]);
				}
				else{
					guest.sendMsg("JoinRoom/비밀번호가 틀렸습니다.");
				}
			}
			//비밀번호가 없는 방에 입장하는 작업
			else{
				joinRoom(arr[1]);
			}
		}
		//닉네임 변경 작업
		else if(arr[0].equals("ChangeAlias")){	// ChangeAlias/changedalias/roomname

			String[] allalias = getAllAlias().split("/");

			boolean isaliasexist = false;
			//이미 접속해 있는 닉네임인지 체크
			for(String s : allalias){
				if(s.equals(arr[1])){
					isaliasexist = true;
					break;
				}
			}
			if(isaliasexist){
				guest.sendMsg(arr[0] + "/이미 등록된 닉네임입니다.");
			}
			else{
				try{
					//대기실에서 닉네임 변경 작업이 이뤄졌을 때 수행
					if(arr[2].equals("WaitingRoomForm")){
						//대기실의 대화창에 메세지 출력
						server.broadcast("대기실대화/[" + guest.alias + "]님의 닉네임이 [" + arr[1] + "]로 변경되었습니다.");
						guest.alias = arr[1];
						//대기실 유저목록을 대기실 유저리스트에 출력
						server.broadcastGuestlist();
					}
					//방 안에서 닉네임 변경 작업이 이뤄졌을 때 수행
					else{
						//방의 대화창에 메세지 출력
						server.broadcastRoom("방대화/[" + guest.alias + "]님의 닉네임이 [" + arr[1] + "]로 변경되었습니다.", arr[2]);
						guest.alias = arr[1];
						//방에 접속한 유저 목록을 방 유저리스트에 출력
						server.broadcastRoomguestlist(arr[2]);
					}
					//클라이언트의 닉네임에 변경된 닉네임 세팅
					guest.sendMsg(arr[0] + "/닉네임이 변경되었습니다.");
				}catch(Exception e){
					System.out.println("닉네임 변경 에러");
				}
			}
		}
		//쪽지 보내기 작업 수행
		else if(arr[0].equals("쪽지")){	// 쪽지/msg/receiver
			//자신에게 쪽지 보냈을 때 수행
			if(arr[2].equals(guest.alias))
				guest.sendMsg("Error/자신에게 쪽지를 보낼 수 없습니다.");
			else
				//선택한 유저에게 쪽지 보내는 메세지 전송
				server.broadcastonlysomeone(arr[0] + "/" + arr[1] + "/" + guest.alias + "/" + arr[2]);
		}
		else if(arr[0].equals("전체쪽지")){	//전체쪽지/메시지
			server.broadcastExceptMe("전체쪽지/" + arr[1] + "/" + guest.alias);	//전체쪽지/메시지/보낸사람
		}
		//스크린 샷 요청
		else if(arr[0].equals("스크린샷요청")){	//스크린샷요청/receiver
			//자신에게 스크린샷요청 보냈을 때 수행
			if(arr[1].equals(guest.alias))
				guest.sendMsg("Error/자신에게 스크린샷 요청을 할 수 없습니다.");
			else
				//선택한 유저에게 스크린샷요청 보내는 메세지 전송
				System.out.println("[서버] 선택한 접속자에게 스크린샷 요청 보냄");
			server.broadcastonlysomeone(arr[0] + "/" + arr[1] + "/" + guest.alias + "/" + arr[1]);
		}
		//연속화면 요청
		else if(arr[0].equals("연속화면요청")){	//연속화면요청/receiver
			//자신에게 연속화면요청 보냈을 때 수행
			if(arr[1].equals(guest.alias))
				guest.sendMsg("Error/자신에게 연속화면 요청을 할 수 없습니다.");
			else
				//선택한 유저에게 연속화면요청 보내는 메세지 전송
				System.out.println("[서버] 선택한 접속자에게 연속화면 요청 보냄");
			server.broadcastonlysomeone(arr[0] + "/" + arr[1] + "/" + guest.alias + "/" + arr[1]);
		}
		//원격제어 요청
		else if(arr[0].equals("원격제어요청")){	//원격제어요청/receiver
			//자신에게 원격제어요청 보냈을 때 수행
			if(arr[1].equals(guest.alias))
				guest.sendMsg("Error/자신에게 원격제어 요청을 할 수 없습니다.");
			else
				//선택한 유저에게 원격제어요청 보내는 메세지 전송
				System.out.println("[서버] 선택한 접속자에게 원격제어 요청 보냄");
			server.broadcastonlysomeone(arr[0] + "/" + arr[1] + "/" + guest.alias + "/" + arr[1]);
		}
		//감시해제
		else if(arr[0].equals("감시해제")){	//감시해제/receiver
			//자신에게 원격제어요청 보냈을 때 수행
			if(arr[1].equals(guest.alias))
				guest.sendMsg("Error/자신에게 감시해제 요청을 할 수 없습니다.");
			else
				//선택한 유저에게 원격제어요청 보내는 메세지 전송
				System.out.println("[서버] 선택한 접속자에게 원격제어 요청 보냄");
			server.broadcastonlysomeone(arr[0] + "/" + arr[1] + "/" + guest.alias + "/" + arr[1]);
		}
		//1:1대화요청 작업 수행
		else if(arr[0].equals("1:1대화요청")){	// 1:1대화/receiver
			//자신에게 쪽지 보냈을 때 수행
			if(arr[1].equals(guest.alias))
				guest.sendMsg("Error/자신에게 1:1대화 요청을 할 수 없습니다.");
			else
				//선택된 유저에게 1:1 대화 수락여부 묻는 메시지 전송
				server.broadcastonlysomeone(arr[0] + "/님이 1:1대화를 신청하셨습니다. 수락하시겠습니까?/"  + guest.alias + "/" + arr[1]);
		}
		//1:1 대화수락 작업 수행
		else if(arr[0].equals("1:1대화수락")){	// 1:1대화수락/boolean/receiver/sender or 1:1대화수락/false/sender
			//1:1대화 수락 했을 때 수행
			if(Boolean.parseBoolean(arr[1]))
				server.broadcastonlysomeone(arr[0] + "/true/" + arr[2] + "/" + arr[3]);
			//1:1대화 거부했을 때 거부 메시지 전송
			else{
				server.broadcastonlysomeone(arr[0] + "/false/상대방이 거절하였습니다./" + arr[2]);
			}
		}
		//1:1대화 작업 수행
		else if(arr[0].equals("1:1대화")){	// 1:1대화/msg/receiver
			//자신에게 자신이 적은 메시지 출력
			guest.sendMsg(arr[0] + "/" + arr[1] + "/" + guest.alias + "/" + arr[2]);
			//상대방에게 자신이 적은 메시지 전송
			server.broadcastonlysomeone(arr[0] + "/" + arr[1] + "/" + guest.alias + "/" + arr[2]);
		}
		//방에서 대화하는 작업 수행
		else if(arr[0].equals("방대화")){		// 방대화/msg/fontName/fontSize/fontColor/toUser/roomname
			//방에 접속한 모든 유저에게 메시지 전송
			if(arr[5].equals("모두에게")){
				server.broadcastRoom(arr[0] + "/" + "[" + guest.alias + "] " + arr[1] + "/" + arr[2] + "/" + 
						arr[3] + "/" + arr[4], arr[6]);
			}
			//자신에게 귓속말을 했을 때 수
			else if(arr[5].equals(guest.alias)){
				guest.sendMsg("Error/자신에게 귓속말을 할 수 없습니다.");
			}
			else
				//선택한 유저에게 귓속말 전송
				server.broadcastRoomonlysomeone("방귓속말/" + arr[1] + "/" + guest.alias + "/" + arr[5], arr[6]);
		}
		//초대하기 위해 대기실 유저 리스트 얻는 작업
		else if(arr[0].equals("대기방유저목록")){
			server.guestListForInvite(guest, arr[1]);
		}
		//초대했을 경우 수락 여부 묻는 작업
		else if(arr[0].equals("초대")){	// 초대/roomname/selectedId
			server.broadcastonlysomeone(arr[0] + "/초대가 왔습니다. 수락하시겠습니까?/" + arr[1] + "/" + arr[2]);
		}
		//초대 수락했을 경우 방에 입장하는 작업 수행
		else if(arr[0].equals("초대수락")){	// 초대수락/roomname/selectedId
			joinRoom(arr[1]);
		}
		//방에서 퇴장했을 때 하는 작업
		else if(arr[0].equals("Out")){ // out/roomname/null
			//퇴장한 유저가 방장인 경우 수행
			if(guest.isCaptain){
				//손님으로 변경
				guest.isCaptain = false;
				//방에 유저가 남아있는 경우 작업
				if(server.getRoomguestNumber(arr[1]) > 1){
					//방장 다음 유저를 얻음
					String nextcaptain = server.getNextGuest(arr[1]);
					//얻어온 유저에게 방장 권한 부여
					server.giveGrant(nextcaptain, arr[1]);
					//방의 모든  유저에게 메시지 전송
					server.broadcastRoom("방대화/[" + nextcaptain + "]님이 방장이 되었습니다.", arr[1]);
				}
			}
			//자신을 방 유저 목록에서 제거 
			server.removeRoomguest(guest, arr[1]);
			//남아 있는 유저가 없는 경우 수행
			if(server.getRoomguestNumber(arr[1]) == 0){
				//방 목록에서 방 제거
				server.removeRoom(arr[1]);
			}
			else{
				//선택한 방의 정보 얻어옴
				String[] roomInfo = server.getRoomInfo(arr[1]);
				//변경된 정보를 갱신하는 작업
				server.updateRoomInfo(arr[1], roomInfo[0], roomInfo[1], server.getRoomguestNumber(arr[1]));
			}
			//대기실 유저 목록에 자신 추가
			server.addGuest(guest);
			//방의 모든 유저에게 퇴장 메시지 전송
			server.broadcastRoom("방퇴장/" + guest.alias, arr[1]);
			//방의 유저리스트에 방 유저 목록 출력
			server.broadcastRoomguestlist(arr[1]);
			//대기실 유저 목록을 대기실 유저리스트에 출력
			server.broadcastGuestlist();
			//방 목록을 대기실 방 리스트에 출력
			server.broadcastRoomlist();
			//대기실을 보여주기위한 Flag정보 전송
			guest.sendMsg("방퇴장완료/");
		}
		//방장넘기는 작업 수행
		else if(arr[0].equals("방장넘기기")){	// 방장넘기기/roomname/selectedId
			//선택한 유저가 자신인 경우 수행
			if(guest.alias.equals(arr[2])){
				guest.sendMsg("Error/자신에게 방장을 넘길 수 없습니다.");
			}
			else{
				//선택한 유저에게 방장 권한 부여
				server.giveGrant(arr[2], arr[1]);
				//방의 모든 유저에게 메시지 전송
				server.broadcastRoom("방대화/[" + arr[2] + "]님이 방장이 되었습니다.", arr[1]);
			}
		}
		//강퇴 작업 수행
		else if(arr[0].equals("강퇴")){	// 강퇴/roomname/selectedId
			//선택한 유저가 자신인 경우 수행
			if(guest.alias.equals(arr[2]))
				guest.sendMsg("Error/자신을 강퇴 할 수 없습니다.");
			else
				//강퇴당한 유저에게 메시지 전송
				server.broadcastRoomonlysomeone(arr[0] + "/강퇴되었습니다./" + arr[1] + "/" + arr[2], arr[1]);
		}
		//대기실 방 리스트에서 방에 접속한 유저 정보 보는 작업
		else if(arr[0].equals("방정보")){
			server.roomGuestList(guest, arr[1]);
		}
		//프로그램에 접속한 모든 유저를 얻어오는 작업
		else if(arr[0].equals("전체유저")){ // 전체유저/타입
			//접속한 모든 유저 얻어옴
			String s = server.getallconnectedguest();
			//메시지 전송
			guest.sendMsg(msg + s);
		}
		//파일 전송하기 위한 작업
		else if(arr[0].equals("파일전송")){	// 파일전송/selectedId
			String IPaddr = guest.s.getInetAddress().getHostAddress();
			server.broadcastonlysomeone("파일전송수락/" + IPaddr + "/" + guest.alias + "/" + arr[1]);
		}
		else if(arr[0].equals("파일전송수락여부")){ // 파일전송수락여부/false/sender/receiver
			server.broadcastonlysomeone("파일전송수락여부/" + arr[1] + "/" + arr[3] + "/" + arr[2]);
		}
		//학생수정화면 콤보박스 세팅을 위한 작업
		else if(arr[0].equals("학생수정화면콤보박스")){
			String idAndName = DBWork.getIdAndName();
			guest.sendMsg("학생수정화면콤보박스세팅" + idAndName);
		}
		//선택된 사람 정보 표시 ----- 사람정보표시/" + id + "/" + userName + "/" + pcNumber
		else if(arr[0].equals("사람정보표시")){
			String pcNumber = "";
			for(int i=0; i<server.guestlist.size(); i++){
				if((server.guestlist.get(i).id).equals(arr[1])){
					pcNumber = server.guestlist.get(i).pcNumber;
				}
			}
			if(arr[2].equals("없음")){
				String name = DBWork.getName(arr[1]);
				String result = DBWork.getOneManProperty(arr[1], name);
				guest.sendMsg("사람정보리스트표시" + "/" + "PC 번호 : " + pcNumber + result);
			}
			else{
				String result = DBWork.getOneManProperty(arr[1], arr[2]);
				guest.sendMsg("사람정보리스트표시" + "/" + "PC 번호 : " +pcNumber + result);
			}
		}
		//교수 클라이언트 삭제 콤보박스 설정
		else if(arr[0].equals("학생삭제화면콤보박스")){
			String result = DBWork.getAllIdAndName();
			guest.sendMsg("학생삭제화면콤보박스세팅" + result);
		}
		//교수 클라이언트 수정 콤보박스 설정
		else if(arr[0].equals("학생수정화면콤보박스")){
			String result = DBWork.getAllIdAndName();
			guest.sendMsg("학생수정화면콤보박스세팅" + result);
		}
		//교수 클라이언트 콤보박스 클릭시				"회원수정정보가져오기/id-name
		else if(arr[0].equals("회원수정정보가져오기")){
			StringTokenizer stz = new StringTokenizer(arr[1], "-");
			String[] temp = new String[stz.countTokens()];
			for(int i = 0; stz.hasMoreTokens(); i++){
				temp[i] = stz.nextToken();
			}
			String result = DBWork.getAllProperty(temp[0], temp[1]);

			guest.sendMsg("회원수정정보가져오기완료" + result);
		}
		////교수 클라이언트 콤보박스 클릭시				"회원삭제정보가져오기/id-name
		else if(arr[0].equals("회원삭제정보가져오기")){
			StringTokenizer stz = new StringTokenizer(arr[1], "-");
			String[] temp = new String[stz.countTokens()];
			for(int i = 0; stz.hasMoreTokens(); i++){
				temp[i] = stz.nextToken();
			}
			String result = DBWork.getAllProperty(temp[0], temp[1]);

			guest.sendMsg("회원삭제정보가져오기완료" + result);
		}
		//회원정보수정 확인버튼
		else if(arr[0].equals("회원정보수정")){
			boolean isDelete = DBWork.updateInformation(arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], 
					arr[7], arr[8], arr[9], arr[10]);
			if(isDelete){
				guest.sendMsg("회원정보수정완료/성공");
			}
			else{
				guest.sendMsg("회원정보수정완료/실패");
			}
		}
		//회원정보삭제 확인버튼
		else if(arr[0].equals("회원정보삭제")){
			boolean isDelete = DBWork.deleteInformation(arr[1]);
			if(isDelete){
				guest.sendMsg("회원정보삭제완료/성공");
			}
			else{
				guest.sendMsg("회원정보삭제완료/실패");
			}
		}
		//학생검색
		else if(arr[0].equals("학생검색")){
			String result = null; 
			result = DBWork.getAllPropertyByName(arr[1]);

			if(result != null){
				guest.sendMsg("학생검색완료" + result + "/성공");
			}
			else{
				guest.sendMsg("학생검색완료" + result + "/실패");
			}
		}
		//학생검색메뉴아이템
		else if(arr[0].equals("학생검색메뉴아이템")){
			String result = null; 
			result = DBWork.getAllPropertyByName(arr[1]);

			if(result != null){
				guest.sendMsg("학생검색메뉴아이템완료" + result + "/성공");
			}
			else{
				guest.sendMsg("학생검색메뉴아이템완료" + result + "/실패");
			}
		}
		//연속화면해제요청		연속화면해제요청 / 선택된사람
		else if(arr[0].equals("연속화면해제요청")){
			//자신에게 연속화면해제요청 보냈을 때 수행
			if(arr[1].equals(guest.alias))
				guest.sendMsg("Error/자신에게 연속화면해제 요청을 할 수 없습니다.");
			else
				//선택한 유저에게 연속화면해제요청 보내는 메세지 전송
				System.out.println("[서버] 선택한 접속자에게 원격제어 요청 보냄");
			server.broadcastonlysomeone(arr[0] + "/" + arr[1] + "/" + guest.alias + "/" + arr[1]);
		}
		//프레임창 보이기
		else if(arr[0].equals("프레임창보이기")){
			server.broadcastProfessor("프레임창보이기/");
		}
	}

	//방에 입장하는 작업 수행
	private void joinRoom(String roomname){
		//방 유저 목록에 자신을 추가, 입장 제한 인원을 넘었을 경우 수행
		if(server.addRoomguest(guest, roomname)){
			return;
		}
		//대기실 유저 목록에서 자신 제거
		server.removeGuest(guest);
		//선택한 방의 정보 얻어옴
		String[] roomInfo = server.getRoomInfo(roomname);
		//입장하는데 필요한 정보 전송
		guest.sendMsg("JoinRoom/" + roomInfo[0] + "/" + roomname + "/" + server.getRoomguestNumber(roomname) + "/" + roomInfo[1]); 
		//변경된 정보를 갱신하는 작업
		server.updateRoomInfo(roomname, roomInfo[0], roomInfo[1], server.getRoomguestNumber(roomname));
		//방의 모든 유저에게 입장 메시지 출력
		server.broadcastRoom("방입장/" + "[" + guest.alias + "]", roomname);
		//대기실의 방 리스트에 방 목록 출력
		server.broadcastRoomlist();
		//방 유저 목록을 방 유저 리스트에 출력
		server.broadcastRoomguestlist(roomname);
		//대기실 유저 목록을 대기실 유저 리스트에 출
		server.broadcastGuestlist();
	}
}