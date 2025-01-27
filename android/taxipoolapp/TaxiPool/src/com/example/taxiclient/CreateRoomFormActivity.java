package com.example.taxiclient;

import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import beans.Operation;
import beans.RoomInfo;

public class CreateRoomFormActivity extends Activity{

	SocketManager socket;
	RoomInfo room;
    Spinner departure;
    Spinner destination;
    Button time;
    EditText content;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    int number = 3;
    
    private String[] destinationList = {"학교정문", "기숙사", "가장동", "버스터미널", "미스터피자", "관광호텔", "동영빌딩"};
    final Calendar c = Calendar.getInstance();
    
    private int mHour;
	private int mMinute;
	String amPm;
	
	protected void onCreate(Bundle savedInstanceState) {
    
	   super.onCreate(savedInstanceState);
       this.requestWindowFeature(Window.FEATURE_NO_TITLE);
       setContentView(R.layout.roomform);
       socket = SocketManager.getInstance();
       
       
       ArrayAdapter<String> destinationAdapter;
       
       destinationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, destinationList);
       destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

       departure = (Spinner) findViewById(R.id.departure);
       destination = (Spinner) findViewById(R.id.destination);
       time = (Button) findViewById(R.id.time);
       content = (EditText) findViewById(R.id.content);
       rb1 = (RadioButton) findViewById(R.id.man2);
       rb2 = (RadioButton) findViewById(R.id.man3);
       rb3 = (RadioButton) findViewById(R.id.man4);
       rb1.setOnClickListener(radioListener);
       rb2.setOnClickListener(radioListener);
       rb3.setOnClickListener(radioListener);
       
       departure.setAdapter(destinationAdapter);
       departure.setSelection(0);
       destination.setAdapter(destinationAdapter);
       destination.setSelection(3);
       
       mHour = c.get(Calendar.HOUR_OF_DAY);
       mMinute = c.get(Calendar.MINUTE);

       time.setText(new StringBuilder()
		.append(mHour).append("시")
		.append(pad(mMinute)).append("분"));
       
       time.setOnClickListener(new View.OnClickListener() {
    	   @Override
    	   public void onClick(View v) {
    		   new TimePickerDialog(CreateRoomFormActivity.this, mTimeSetListener, mHour, mMinute, false).show();
    	   }
       });
       
       
       
       
	}
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker arg0, int hourOfDay, int minute) {
			Calendar tmpTime = Calendar.getInstance();
			tmpTime.set(Calendar.HOUR, hourOfDay);
			tmpTime.set(Calendar.MINUTE, minute);
			
//			if(tmpTime.get(Calendar.AM_PM) == Calendar.AM){
//				amPm = "오전 ";
//			}else if(tmpTime.get(Calendar.AM_PM) == Calendar.PM){
//				amPm = "오후 ";
//			}
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay();
		}
	};
	private void updateDisplay() {
		time.setText(new StringBuilder()
//		.append(amPm).append(" ")
		.append(mHour).append("시")
		.append(pad(mMinute)).append("분"));
	}
	private static String pad(int c) {
	    // TODO Auto-generated method stub
	    if(c >= 10){
	        return String.valueOf(c);
	    }else
	        return "0" + String.valueOf(c);
	}
	
	private OnClickListener radioListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			RadioButton rb = (RadioButton) v;
			if(rb.getText().equals("2명")) number = 2;
			else if(rb.getText().equals("3명")) number = 3;
			else if(rb.getText().equals("4명")) number = 4;
		}
	};

	public void onClickedCreateBtn(View target){
		TelephonyManager telManager = (TelephonyManager)this.getSystemService(getApplicationContext().TELEPHONY_SERVICE);
		String phoneId = telManager.getLine1Number();
		   
		if(content.getText().toString().equals("")){
			System.out.println("내용을 입력해 주세요");
			Toast.makeText(getApplicationContext(), "내용을 입력해 주세요", Toast.LENGTH_SHORT).show();
		}else{
			room = new RoomInfo(Operation.OP_CREATE_ROOM);
			room.setNumberOfPeople(number);
			room.setDeparture(departure.getSelectedItem().toString());
			room.setDestination(destination.getSelectedItem().toString());
			room.setTime(time.getText().toString());
			room.setContents(content.getText().toString());
			room.setOwnerUserPhoneNumber(phoneId);
			
			
			Toast.makeText(getApplicationContext(), "Create Room", Toast.LENGTH_SHORT).show();
			try {
				System.out.println(room.toString());
				socket.writeObj(room); //이부분 바꿔야됨 
				startActivity(new Intent(this, ChatRoomActivity.class));
				finish();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		
	}
	
	public void onClickedCancelBtn(View target){
		onBackPressed();
	}
	
	public void onBackPressed(){
		super.onBackPressed();
		//작성중인 폼 정보가 날라갑니다. 정말 나가시겠습니까? 다이얼로그 표시
    }
	
}
