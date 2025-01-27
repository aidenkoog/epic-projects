package com.example.taxiclient;

import java.io.IOException;
import java.util.ArrayList;

import com.example.taxiclient.service.MyService;
import beans.Operation;
import beans.RoomInfo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class RoomListActivity extends Activity {
	private static final int DIALOG_YES_NO_MESSAGE = 1;
	
	RoomInfo roomToEnter;
	
	SocketManager socket;
	ArrayList<RoomInfo> al;
	Handler handler;
	Object obj;
	ArrayAdapter<RoomInfo> adapter;
	
	ListView listView;
	
	boolean mIsBound;
	Messenger mService = null;
	final Messenger mMessenger = new Messenger(new IncomingHandler());
	
	@SuppressLint("HandlerLeak")
	class IncomingHandler extends Handler{
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MyService.MSG_SET_OBJECT_VALUE:
				if(msg.obj instanceof ArrayList){
					al = (ArrayList<RoomInfo>)msg.obj;
					handler.post(new Runnable() {
						@Override
						public void run() {
							//adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, al);
							adapter = new RoomListAdapter(getApplicationContext(), R.layout.room_list_row, R.id.room_list_row_departure, al);
							listView.setAdapter(adapter);
							listView.setOnItemClickListener(listViewListener);
							adapter.notifyDataSetChanged();
						}
					});
				}else if(msg.obj instanceof Operation){
					Operation op = (Operation) msg.obj;
					switch(op.getState()){
					case Operation.OP_APPROVE_ENTER_ROOM:
						startActivity(new Intent(getApplicationContext(), ChatRoomActivity.class));
						break;
						
					case Operation.OP_REJECT_ENTER_ROOM:
						Toast.makeText(getApplicationContext(), "인원이 가득찼습니다.", Toast.LENGTH_LONG).show();
						break;
						
					case Operation.OP_NOT_EXIST_ROOM:
						Toast.makeText(getApplicationContext(), "존재하지 않는 방입니다.", Toast.LENGTH_LONG).show();
						break;
					}
					
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
	
	
	private void requireRoomListToServer(){
		try {
			socket.writeObj(new Operation(Operation.OP_PRINT_ROOM_LIST));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
	
	private void enterRoom(RoomInfo info){
		try{
			socket.writeObj(info);
		}catch(Exception e){
			
		}
	}
	
	private OnItemClickListener listViewListener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos,
				long id) {
			RoomInfo info = (RoomInfo)adapterView.getAdapter().getItem(pos);
			System.out.println("info : "+info);
			//System.out.println(((TextView)clickedView).getText().toString() + " is selected.");
			System.out.println("clicked view = " + clickedView);
			roomToEnter = new RoomInfo(Operation.OP_ENTER_ROOM);
			roomToEnter.setRoomId(info.getRoomId());
			showDialog(DIALOG_YES_NO_MESSAGE);
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.roomlist);
		handler = new Handler();
		socket = SocketManager.getInstance();
		
		listView = (ListView) findViewById(R.id.listView);
		
		if(!MyService.isRunning())
			startService(new Intent(getApplicationContext(), MyService.class));
		if(!mIsBound)
			doBindService();
		CDialog.showLoading(this);
		IntroActivity.cancelToast();
		Toast.makeText(getApplicationContext(), "채팅방 목록을 가져오고 있습니다.", Toast.LENGTH_SHORT).show();
		new Thread(){
			public void run(){
				try{
					Thread.sleep(2500);
					requireRoomListToServer();
					CDialog.hideLoading();
				}catch(Exception e){}
			}
		}.start();
		DialogNotice();
	}
	//공지사항
	public void DialogNotice() {
		 AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		 alt_bld.setMessage("\n1. 이 어플은 택시를 함께타기위한 어플입니다.\n	 불미스러운 일을 만들지 않도록 합니다.\n" +
		 		"2. 시간을 준수합니다.(3분전에는 나와주세요).\n" +
				"3. 무리한 에약을 하지 않습니다.\n" +
				"4. 펑크내지 맙시다.\n" +
				"5. 다른사람들에게 불편을 줄 경우 \n	 불의를 입을수 있습니다.\n"
		 		).setCancelable(
				 false).setPositiveButton("동의하겠습니다.", 
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
		 AlertDialog alert = alt_bld.create();
		 alert.setTitle("공지사항");
		 alert.setIcon(R.drawable.taxi_icon);
		 alert.show();
	 }
	@Override
	protected Dialog onCreateDialog(int id){
		switch(id){
		case DIALOG_YES_NO_MESSAGE:
			//다이얼로그
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("방정보");
			builder.setMessage("방에 입장하시겠습니까잉~?");
			builder.setCancelable(false);
			builder.setPositiveButton("입장!", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					System.out.println("방입장 명령!");
					enterRoom(roomToEnter);
				}
			});
			builder.setNegativeButton("나가기", new DialogInterface.OnClickListener() {
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
	
	public void createRoom(View target){
		startActivity(new Intent(this, CreateRoomFormActivity.class));
	}
	
	public void onBackPressed(){
    	super.onBackPressed();
    }
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		try{
			doUnbindService();
			stopService(new Intent(getApplicationContext(), MyService.class));
		}catch(Throwable t){
			System.out.println(t);
		}
	}
	@Override
	public void onResume(){
		super.onResume();
		requireRoomListToServer();
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.roomlist, menu);
		return true;
	}
}
