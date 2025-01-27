package com.example.taxiclient;

import java.util.ArrayList;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import beans.ChatMessage;

public class ChatRoomAdapter extends ArrayAdapter<ChatMessage>{

	private Context context;
	private int resource;
	private ArrayList<ChatMessage> arr;
	private LayoutInflater inflater;
	
	
	
	public ChatRoomAdapter(Context context, int resource, int textViewResourceId, ArrayList<ChatMessage> chatList) {
		super(context, resource, textViewResourceId, chatList);
		this.context = context;
		this.resource = resource;
		this.arr = chatList;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		TelephonyManager telManager = (TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE);
		String phoneId = telManager.getLine1Number();
		
		ChatMessage msg = arr.get(position);
		if(msg.getUserName().equals("TaxiPool")){
			convertView = inflater.inflate(R.layout.chatroom_list_item_3, null);
		}else if(msg.getUserPhoneNumber().equals(phoneId)){
			convertView = inflater.inflate(R.layout.chatroom_list_item_2, null);
		}else{
			convertView = inflater.inflate(R.layout.chatroom_list_item_1, null);
		}

		if(msg != null){
			if(msg.getUserName().equals("TaxiPool")){
				TextView msgView = (TextView) convertView.findViewById(R.id.chatroom_sys_msg);
				msgView.setText(msg.getMsg());
				System.out.println(msg.getMsg());
			}else if(msg.getUserPhoneNumber().equals(phoneId)){
				TextView msgView = (TextView) convertView.findViewById(R.id.chatroom_list_2_msg);
				TextView timeView = (TextView) convertView.findViewById(R.id.chatroom_list_2_time);
				msgView.setText(msg.getMsg());
				timeView.setText(msg.getTime());

			}else{
				TextView msgView = (TextView) convertView.findViewById(R.id.chatroom_list_1_msg);
				TextView timeView = (TextView) convertView.findViewById(R.id.chatroom_list_1_time);
				TextView nameView = (TextView) convertView.findViewById(R.id.chatroom_list_1_name);
				
				msgView.setText(msg.getMsg());
				timeView.setText(msg.getTime());
				nameView.setText(msg.getUserName());
			}
		}
		return convertView;
	}
}
