package com.example.taxiclient;

import java.io.IOException;

import beans.Operation;
import beans.UserInfo;

import com.example.taxiclient.service.MyService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.AlteredCharSequence;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

public class IntroActivity extends Activity implements Runnable{

	 boolean isConnected = false;
	 Handler handler;

	 SocketManager socket;
	private String userNickname;
	private String userDept;
	 
	 private static Toast toast;
	 
	 protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.intro);
	        
	        toast = Toast.makeText(getApplicationContext(), "서버의 응답을 기다리고 있습니다. wait", Toast.LENGTH_SHORT);
	        
	        CDialog.showLoading(this);
	        handler = new Handler();
	        
	        (new Thread(this)).start();
	 }
	 
	 public static void cancelToast(){
		 
		 toast.cancel();
	 }
	 
	 
	 private boolean isWifiConnected(){
			ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connMgr != null){
				NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
				if ( (networkInfo != null) && (networkInfo.isAvailable() == true) ){
					if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
						if ( (networkInfo.getState() == State.CONNECTED) 
								|| (networkInfo.getState() == State.CONNECTING) ){
							return true;
						}
					}
				}
			}
			
			return false;
	 }
	 
	 @Override
	 public void run(){
		 
		 try{
			 ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			 boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
			 
		     if(isWifiConnected() || is3g){
		    	 handler.post(new Runnable() {
					@Override
					public void run() {
						toast.show();
					}
		    	 });
		    	 
		    	 socket = SocketManager.getInstance();
		    	 socket.connect();
		    	 
				 isConnected = true;

		    	 if(!MyService.isRunning())
						startService(new Intent(getApplicationContext(), MyService.class));
		    	 
		     }else {
		    	 isConnected = false;

		    	 handler.post(new Runnable() {
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "인터넷 연결상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
						AlertDialog alert = new AlertDialog.Builder(IntroActivity.this).create();
						alert.setMessage("어플리케이션을 종료합니다.");
						alert.setButton("확인", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								CDialog.hideLoading();
								dialog.dismiss();
								finish();
							}
						});
						alert.show();
					}
				 });
		     }
			 
		 }catch(IOException ie){
			 isConnected = false;
			   handler.post(new Runnable() {
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "서버연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
						AlertDialog alert = new AlertDialog.Builder(IntroActivity.this).create();
						alert.setMessage("어플리케이션을 종료합니다.");
						alert.setButton("확인", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								CDialog.hideLoading();
								dialog.dismiss();
								finish();
							}
						});
						alert.show();
					}
				 });
		 }catch(Exception e){
		 }finally{
		 }
		 if(isConnected){
			 // connect success
			 CDialog.hideLoading();
			 
			 if(!isRegisteredUser()){
				 Intent intent = new Intent(getApplicationContext(), RegisterUserFormActivity.class);
			     startActivity(intent);
			     finish();
			 }else {
				 
				 getUserInfoFromPref();
		    	 TelephonyManager telManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		    	 String phoneId = telManager.getLine1Number();
		    	 UserInfo info = new UserInfo(Operation.OP_FIRST_CONNECTION);
		    	 info.setDept(userDept);
		    	 info.setNickname(userNickname);
		    	 info.setPhoneNumber(phoneId);
		    	 try {
					socket.writeObj(info);
				} catch (IOException e) {
					e.printStackTrace();
				}
				 Intent intent = new Intent(getApplicationContext(), RoomListActivity.class);
			     startActivity(intent);
			     finish();
			 }
		 }else{
			 //connect fail
		 }
	 }
	 
	 public void getUserInfoFromPref(){
			SharedPreferences pref = getSharedPreferences(RegisterUserFormActivity.prefFileName, MODE_PRIVATE);
			userNickname = pref.getString(RegisterUserFormActivity.prefUserNickname, "");
			userDept = pref.getString(RegisterUserFormActivity.prefUserDept, "");
		}
	 
	 public boolean isRegisteredUser(){
		 SharedPreferences pref = getSharedPreferences(RegisterUserFormActivity.prefFileName , MODE_PRIVATE);
		 
		 userNickname = pref.getString(RegisterUserFormActivity.prefUserNickname, "");
		 System.out.println("저장된 유저 닉네임은 =" + userNickname);
		 if(userNickname.equals(""))return false;
		 else return true;
	 }
	 
}
