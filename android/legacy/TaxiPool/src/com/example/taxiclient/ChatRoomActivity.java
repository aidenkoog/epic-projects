package com.example.taxiclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.RemoteException;
import android.provider.Contacts.Intents;
import android.provider.ContactsContract.Contacts;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import beans.ChatMessage;
import beans.Operation;
import beans.RoomInfo;
import beans.UserInfo;
import beans.UserList;

import com.example.taxiclient.service.MyService;

public class ChatRoomActivity extends Activity {

	private static final int DIALOG_YES_NO_MESSAGE = 1;
	private boolean isVisible = true;
	
	SocketManager socket;
	Object obj;
	ChatMessage chatMsg;
	Handler handler;
	ArrayList<ChatMessage> al;
	ArrayAdapter<ChatMessage> adapter;
	
	RoomInfo roomInfo;
	
	String userNickname;
	String userDept;
	
	TextView countOfPeople;
	TextView nameOfPeople;
	ArrayList<String> userArrayList;
	String userListName;
	
	boolean mIsBound;
	Messenger mService = null;
	final Messenger mMessenger = new Messenger(new IncomingHandler());
	
	String amPm;
	int mHour;
	int mMinute;

	String aa="4";
	TextView test;
	String[] user= new String[4];
	@SuppressLint("HandlerLeak")
	class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MyService.MSG_SET_OBJECT_VALUE:
            	if(msg.obj instanceof ChatMessage){
            		chatMsg = (ChatMessage) msg.obj;
            		al.add(chatMsg);
            		Intent intent = new Intent(getApplicationContext(), ChatRoomActivity.class);
            		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK 
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP 
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            		//intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            		if(!isVisible){
            			CNotification.addNotification(ChatRoomActivity.this, intent, msg.what, R.drawable.taxi_icon, chatMsg.getMsg(), chatMsg.getUserName(), chatMsg.getMsg());
            		}
            		if(!((PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE)).isScreenOn()){
            			Intent popupIntent = new Intent(getApplicationContext(), PopUpActivity.class);
            			popupIntent.putExtra("chatMsg", chatMsg.getMsg());
            			popupIntent.putExtra("chatUserName", chatMsg.getUserName());
            			popupIntent.putExtra("chatTime", chatMsg.getTime());
            			popupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            			getApplication().startActivity(popupIntent);
            		}
            		if(chatMsg.getUserName().equals("TaxiPool")){
    					try {
							socket.writeObj(new Operation(Operation.OP_GET_ROOM_USER_LIST));
						} catch (IOException e) {
							e.printStackTrace();
						}
            		}
            		handler.post(new Runnable() {
						@Override
						public void run() {
							adapter.notifyDataSetChanged();
						}
					});
            	}
            	if(msg.obj instanceof UserList){
            		UserList ul = (UserList) msg.obj;
            		userArrayList = ul.getAl();
            		userListName = "";
            		for(int i=0; i< userArrayList.size(); i++){
            			userListName+="["+userArrayList.get(i)+ "] ";
            			user[i] = userArrayList.get(i);
            		}
            		nameOfPeople.setText(userListName);
            		countOfPeople.setText("(" + userArrayList.size() +"��)" );
            		aa=Integer.toString(userArrayList.size());
            		
            	}
            		
            	break;
            default:
                super.handleMessage(msg);
            }
        }
    }
	
	private ServiceConnection mConnection = new ServiceConnection(){
		@Override
		public void onServiceConnected(ComponentName className, IBinder service){
			mService = new Messenger(service);
			try{
				Message msg = Message.obtain(null, MyService.MSG_REGISTER_CLIENT);
				msg.replyTo = mMessenger;
				mService.send(msg);
			}catch(RemoteException e){
				
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mService = null;
		}
	};
	
//	private void CheckIfServiceIsRunning(){
//		if(MyService.isRunning()){
//			doBindService();
//		}
//	}
//	
//	private void sendMessageToService(){
//		if(mIsBound && (mService != null)){
//			try{
//				Message msg = Message.obtain(null, MyService.MSG_SET_INT_VALUE, 0);
//				msg.replyTo = mMessenger;
//				mService.send(msg);
//			}catch(RemoteException e){
//				
//			}
//		}
//	}
	
	void doBindService(){
		bindService(new Intent(this, MyService.class), mConnection, Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	private void doUnbindService(){
		if(mIsBound && (mService != null)){
			try{
				Message msg = Message.obtain(null, MyService.MSG_UNREGISTER_CLIENT);
				msg.replyTo = mMessenger;
				mService.send(msg);
						
			}catch(RemoteException e){
				
			}
			unbindService(mConnection);
			mIsBound = false;
		}
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		try{
			doUnbindService();
			
		}catch(Throwable t){
			System.out.println(t);
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chatroom);
		getUserInfoFromPref(); //�����۷����κ��� ��������� �ҷ�����
		handler = new Handler();
		
		countOfPeople = (TextView) findViewById(R.id.chatroom_countOfPeople);
		nameOfPeople = (TextView) findViewById(R.id.chatroom_nameOfPeople);
		countOfPeople.setText("�����ο�");
		
		
		socket = SocketManager.getInstance();
		al = new ArrayList<ChatMessage>();
		
		//adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, al);
		adapter = new ChatRoomAdapter(getApplicationContext(), 0, 0, al);
		
		ListView listView = (ListView) findViewById(R.id.chatList);
		listView.setDivider(null);
		listView.setAdapter(adapter);
		
		if(!MyService.isRunning())
			startService(new Intent(getApplicationContext(), MyService.class));
		if(!mIsBound)
			doBindService();
		
		
		new Thread(){
			public void run(){
				try{
					Thread.sleep(500);
					socket.writeObj(new Operation(Operation.OP_GET_ROOM_USER_LIST));
				}catch(Exception e){}
			}
		}.start();

	}
	
	public void escapeRoom(){
		UserInfo info;
		TelephonyManager telManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneId = telManager.getLine1Number();
		try{
			info = new UserInfo(Operation.OP_ESCAPE_ROOM);
			info.setPhoneNumber(phoneId);
			socket.writeObj(info);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*
	//���Һ����� ��Ƽ��Ƽ ȣ��
	public void gobbbGame(View v){
		Intent intentbbbgame = new Intent(ChatRoomActivity.this, BBBGame.class);
		intentbbbgame.putExtra("aa", aa);
		intentbbbgame.putExtra("user1", user[0]);
		intentbbbgame.putExtra("user2", user[1]);
		intentbbbgame.putExtra("user3", user[2]);
		intentbbbgame.putExtra("user4", user[3]);
				
		startActivity(intentbbbgame);
	}
	*/
	//ä�� �������ư �̺�Ʈ
	public void sendMsg(View target){
		EditText et = (EditText) findViewById(R.id.msgEditText);
		if(et.getText().toString().equals("")) return;
		TelephonyManager telManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneId = telManager.getLine1Number();
		ChatMessage msg;
		Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		if(c.get(Calendar.AM_PM) == Calendar.AM){
			amPm = " ����";
		}else if(c.get(Calendar.AM_PM) == Calendar.PM){
			amPm = " ����";
		}
		
		if(et.getText().toString() == null) return; // re-check!
		try{
			msg = new ChatMessage(phoneId, et.getText().toString());
			msg.setUserPhoneNumber(phoneId);
			msg.setUserName(userNickname);
			msg.setTime(new StringBuilder()
			.append(amPm).append(" ")
			.append(mHour).append(":")
			.append(pad(mMinute)).append(" ").toString());
			socket.writeObj(msg);
			et.setText("");
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private static String pad(int c) {
	    if(c >= 10){
	        return String.valueOf(c);
	    }else
	        return "0" + String.valueOf(c);
	}
	
	//��ҹ�ư�� ������ ��ȭ����ȣ��
	@Override
	protected Dialog onCreateDialog(int id){
		switch(id){
		case DIALOG_YES_NO_MESSAGE:
			//���̾�α�
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("������");
			builder.setMessage("�濡�� �����ðڽ��ϱ�?");
			builder.setCancelable(false);
			builder.setPositiveButton("������", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					outRoom();
				}
			});
			builder.setNegativeButton("���", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			return alert;
		}
		return null;
		
	}
	
	public void outRoom(){
		doUnbindService();
		escapeRoom();
		finish();
	}
	
	@SuppressWarnings("deprecation")
	public void onBackPressed(){
		showDialog(DIALOG_YES_NO_MESSAGE);
		//super.onBackPressed();
    }
	
	public void getUserInfoFromPref(){
		SharedPreferences pref = getSharedPreferences(RegisterUserFormActivity.prefFileName, MODE_PRIVATE);
		userNickname = pref.getString(RegisterUserFormActivity.prefUserNickname, "");
		userDept = pref.getString(RegisterUserFormActivity.prefUserDept, "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chatroom, menu);
		
		MenuItem item= menu.add(0,1,0,"Call:�������ý�");
		item.setIcon(R.drawable.taxi_icon);
		item.setAlphabeticShortcut('a');
		menu.add(0,2,0,"Call:õ�����ý�").setIcon(R.drawable.taxi_icon);
		
		//menu�� ���submenu �߰�
		SubMenu sub = menu.addSubMenu("���");
		sub.add(0,3,Menu.NONE, "�������ý� ���");
		sub.add(0,4,Menu.NONE, "õ�����ý� ���");
		menu.add(0,5,Menu.NONE, "���Һ�����(���񳪴���)");		
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case 1:
			startActivity(new Intent("android.intent.action.CALL",
					Uri.parse("tel:054-535-2177")));
			return true;
		case 2:
			startActivity(new Intent("android.intent.action.CALL",
					Uri.parse("tel:010-9075-9111")));
			return true;
		case 3:
		Intent intent = new Intent(Intent.ACTION_INSERT, Contacts.CONTENT_URI);
			Bundle bundle = new Bundle();
			bundle.putString(Intents.Insert.NAME, "�������ý�");
			bundle.putString(Intents.Insert.PHONE, "054-535-2177"); 
			intent.putExtras(bundle);
			startActivity(intent); 

			return true;
		case 4:
			Intent intent1 = new Intent(Intent.ACTION_INSERT, Contacts.CONTENT_URI);
			Bundle bundle1 = new Bundle();
			bundle1.putString(Intents.Insert.NAME, "õ�����ý�");
			bundle1.putString(Intents.Insert.PHONE, "010-9075-9111"); 
			intent1.putExtras(bundle1);
			startActivity(intent1); 
			return true;
		case 5:
			Intent intentbbbgame = new Intent(ChatRoomActivity.this, BBBGame.class);
			intentbbbgame.putExtra("aa", aa);
			intentbbbgame.putExtra("user1", user[0]);
			intentbbbgame.putExtra("user2", user[1]);
			intentbbbgame.putExtra("user3", user[2]);
			intentbbbgame.putExtra("user4", user[3]);
					
			startActivity(intentbbbgame);
			return true;
		}
		return false;
	}
	@Override
	protected void onResume(){
		super.onRestart();
		isVisible = true;
	}
	@Override
	protected void onPause(){
		super.onPause();
		isVisible = false;
	}
	
}
