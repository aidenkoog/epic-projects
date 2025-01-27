package com.example.taxiclient;

import java.util.ArrayList;
import beans.RoomInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RoomListAdapter extends ArrayAdapter<RoomInfo>{

	private Context context;
	private int resource;
	private ArrayList<RoomInfo> arr;
	private LayoutInflater inflater;
	
	public RoomListAdapter(Context context, int resource, int textViewResourceId, ArrayList<RoomInfo> roomList) {
		super(context, resource, textViewResourceId, roomList);
		this.context = context;
		this.resource = resource;
		this.arr = roomList;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		RoomInfo info = arr.get(position);
		if(convertView == null){
			convertView = inflater.inflate(resource, null);
		}
		if(info != null){
			TextView departure = (TextView) convertView.findViewById(R.id.room_list_row_departure);
			TextView destination = (TextView) convertView.findViewById(R.id.room_list_row_destination);
			TextView time = (TextView) convertView.findViewById(R.id.room_list_row_time);
			TextView numberOfPeople = (TextView) convertView.findViewById(R.id.room_list_row_numberOfPeople);
			
			departure.setText(info.getDeparture());
			destination.setText(info.getDestination());
			time.setText("출발 시간 : "+info.getTime());
			numberOfPeople.setText("(" + info.getCurrentNumberOfPeople() +"/"+info.getNumberOfPeople()+"명)");
			System.out.println(info.getCurrentNumberOfPeople());
		}
		return convertView;
		
	}

}
