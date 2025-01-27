package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.ChatMessage;
import beans.RoomInfo;
import beans.UserInfo;

public class DBWork {
	
	public boolean initRoomInfo(Connection conn){
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = "update room_info set is_activated = 0";
		try{
			pstmt = conn.prepareStatement(sql);
			result = pstmt.executeUpdate();
			if(result != 0) return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				pstmt.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return false;
		
	}
	
	public int selectRoomState(Connection conn, RoomInfo info){
		int result = -1;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select is_activated from room_info where id=?";
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(info.getRoomId()));
			rs = pstmt.executeQuery();
			if(rs.next()) result = rs.getInt(1);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				pstmt.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		
		return result;
	}
	
	public ArrayList<RoomInfo> selectActivatedRoomInfoList(Connection conn){
		ArrayList<RoomInfo> al = new ArrayList<RoomInfo>();
		RoomInfo info = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select id, departure, destination, depart_time, contents, number_of_people, owner_user_phone_number, is_activated, inserted_datetime, current_number_of_people from room_info where is_activated = 1 order by inserted_datetime desc";
		try{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				info = new RoomInfo();
				info.setRoomId(rs.getInt("id")+"");
				info.setDeparture(rs.getString("departure"));
				info.setDestination(rs.getString("destination"));
				info.setTime(rs.getString("depart_time"));
				info.setContents(rs.getString("contents"));
				info.setNumberOfPeople(rs.getInt("number_of_people"));
				info.setOwnerUserPhoneNumber(rs.getString("owner_user_phone_number"));
				info.setIsActivated(rs.getInt("is_activated"));
				info.setCurrentNumberOfPeople(rs.getInt("current_number_of_people"));
				al.add(info);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				pstmt.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return al;
	}
	
	
	public boolean inActivationRoom(Connection conn, RoomInfo info){
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = "update room_info set is_activated = 0 where id = ?";
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(info.getRoomId()));
			result = pstmt.executeUpdate();
			if(result != 0) return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				pstmt.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return false;
	}
	public int selectMaxNumberOfPeopleInRoomInfo(Connection conn, RoomInfo info){
		int result = -1;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select number_of_people from room_info where id=?";
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(info.getRoomId()));
			rs = pstmt.executeQuery();
			if(rs.next()) result = rs.getInt(1);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				pstmt.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		
		return result;
	}
	
	public int selectCurrNumberOfPeopleInRoomInfo(Connection conn, RoomInfo info){
		int result = -1;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select current_number_of_people from room_info where id=?";
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(info.getRoomId()));
			rs = pstmt.executeQuery();
			if(rs.next()) result = rs.getInt(1);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				pstmt.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		
		return result;
	}
	
	public boolean insertCurrNumberOfPeopleInRoomInfo(Connection conn, RoomInfo info, int value){
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = "update room_info set current_number_of_people = ? where id = ? ";
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, value);
			pstmt.setInt(2, Integer.parseInt(info.getRoomId()));
			result = pstmt.executeUpdate();
			if(result != 0) return true;
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				pstmt.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean insertChatMessageLog(Connection conn, ChatMessage msg){
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = "insert into chat_message_log(user_phone_number, room_id, msg_contents, msg_time) values(?,?,?, now())";
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, msg.getUserPhoneNumber());
			pstmt.setInt(2, Integer.parseInt(msg.getRoomId()));
			pstmt.setString(3, msg.getMsg());
			result = pstmt.executeUpdate();
			if(result != 0)	return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				pstmt.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		
		
		return false;
		
	}
	
	public boolean insertUserInfo(Connection conn, UserInfo info){
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = "insert into user_info(phone_number, nickname, dept) values(?,?,?)";
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, info.getPhoneNumber());
			pstmt.setString(2, info.getNickname());
			pstmt.setString(3, info.getDept());
			result = pstmt.executeUpdate();
			if(result != 0) return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				pstmt.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean insertRoomInfo(Connection conn, RoomInfo info){
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = "insert into room_info(departure, destination, depart_time, contents, number_of_people, owner_user_phone_number, is_activated, inserted_datetime, current_number_of_people) values (?,?,?,?,?,?,?,now(), 1)";
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, info.getDeparture());
			pstmt.setString(2, info.getDestination());
			pstmt.setString(3, info.getTime());
			pstmt.setString(4, info.getContents());
			pstmt.setInt(5, info.getNumberOfPeople());
			//pstmt.setInt(6, info.getOwnerUserPhoneNumber()); //이부분은 받은 폰번호로 디비검색해서 유저아이디 받을것!
			pstmt.setString(6, info.getOwnerUserPhoneNumber());
			pstmt.setInt(7, 1);
			result = pstmt.executeUpdate();
			
			
			if(result != 0) return true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public int selecLastRoomId(Connection conn){
		int id = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select max(id) from room_info";
		try{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) id = rs.getInt(1);
			return ++id;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				pstmt.close();
				rs.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		return -1;
		
	}

}
