package com.example.taxiclient.service;


import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.example.taxiclient.SocketManager;

public class MyService extends Service {
	
	private static boolean isRunning = false;
	
	ArrayList<Messenger> mClients = new ArrayList<Messenger>();
	int mValue = 0;
	public static final int MSG_REGISTER_CLIENT = 1;
	public static final int MSG_UNREGISTER_CLIENT = 2;
	public static final int MSG_SET_INT_VALUE = 3;
	public static final int MSG_SET_STRING_VALUE = 4;
	public static final int MSG_SET_OBJECT_VALUE = 5;
    
    final Messenger mMessenger = new Messenger(new IncomingHandler()); // Target we publish for clients to send messages to IncomingHandler.
    
    MessageReceiver mr;
	SocketManager socket;
	Object obj;
	
    @SuppressLint("HandlerLeak")
	class IncomingHandler extends Handler{
    	@Override
    	public void handleMessage(Message msg){
    		switch(msg.what){
    		case MSG_REGISTER_CLIENT:
    			mClients.add(msg.replyTo);
    			break;
    		case MSG_UNREGISTER_CLIENT:
    			mClients.remove(msg.replyTo);
    			break;
    		default:
    			super.handleMessage(msg);
    				
    		}
    	}
    }
    class MessageReceiver extends Thread{
    	@Override
    	public void run(){
    		try {
				while((obj = socket.readObj()) != null){
					System.out.println("Receive Object In MyService :" + obj);
					sendMessageToUI(obj);
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
    	}
    }
    
    private void sendMessageToUI(Object obj){
    	for(int i=mClients.size()-1; i>=0; i--){
    		try{
    			mClients.get(i).send(Message.obtain(null, MSG_SET_OBJECT_VALUE, obj));
    		}catch(Exception e){
    			mClients.remove(i);
    		}
    	}
    	
    }
    
    
	@Override
	public void onCreate(){
		super.onCreate();
		isRunning = true;
		socket = SocketManager.getInstance();
		mr = new MessageReceiver();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(mr != null)
			mr.interrupt();
		
		isRunning = false;
		socket.socketClose();
	}
	
	public static boolean isRunning(){
		return isRunning;
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.i("MyService", "Received start id " + startId + ": " + intent);
		if(!mr.isAlive())
			mr.start();
        return START_STICKY; // run until explicitly stopped.
    }
	
	@Override
	public IBinder onBind(Intent intent) {
		
		return mMessenger.getBinder();
	}

}
