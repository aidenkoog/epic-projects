package com.example.taxiclient;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import beans.ChatMessage;
import beans.Operation;
import beans.UserList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BBBGame extends Activity implements OnClickListener {
	Button[] btn = new Button[4];
	Button btn_mix, btn_open;
	TextView[] tv = new TextView[4];
	TextView tv_result;
	int[] a = new int[4];
	String[] user = new String[4];
	int numberofpeople;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbbgame);


		//		Intent intent = getIntent();
		//		Integer number = intent.getIntExtra("number", 0);
		//		String n = number.toString();

		btn[0] = (Button)findViewById(R.id.btn1);
		btn[1] = (Button)findViewById(R.id.btn2);
		btn[2] = (Button)findViewById(R.id.btn3);
		btn[3] = (Button)findViewById(R.id.btn4);
		btn_mix = (Button)findViewById(R.id.btn_mix);
		btn_open = (Button)findViewById(R.id.btn_open);
		for(int i=0; i<btn.length; i++)
			btn[i].setOnClickListener(this);
		btn_mix.setOnClickListener(this);
		btn_open.setOnClickListener(this);

		tv[0] = (TextView)findViewById(R.id.tv1);
		tv[1] = (TextView)findViewById(R.id.tv2);
		tv[2] = (TextView)findViewById(R.id.tv3);
		tv[3] = (TextView)findViewById(R.id.tv4);
		tv_result = (TextView)findViewById(R.id.tv_result);

		Intent intent = getIntent();
		String aa = intent.getStringExtra("aa");
		user[0] = intent.getStringExtra("user1");
		user[1]= intent.getStringExtra("user2");
		user[2] = intent.getStringExtra("user3");
		user[3]= intent.getStringExtra("user4");

		numberofpeople = Integer.parseInt(aa);
		setArray(a, numberofpeople );
		SetVisible(10, false);
	}

	//username 세팅
	public void SetUserId(int aa){
		for(int i=0; i<4; i++)
			btn[i].setVisibility(Button.INVISIBLE);
		for(int i=0; i<aa; i++)
		{
			btn[i].setVisibility(Button.VISIBLE);
			btn[i].setText(""+user[i]);
		}
	}
	//버튼눌렀을때 TextView 보이기함수
	public void SetVisible(int i, boolean b){
		if(b){
			if(i==10) {
				for(int j=0; j<4; j++) 
					tv[j].setVisibility(TextView.VISIBLE);
			}else
				tv[i].setVisibility(TextView.VISIBLE);
		}else{
			if(i==10) {
				for(int j=0; j<4; j++) 
					tv[j].setVisibility(TextView.INVISIBLE);
			}else
				tv[i].setVisibility(TextView.INVISIBLE);			
		}

	}
	@Override
	public void onClick(View v){
		Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		vibe.vibrate(100);
		switch ( v.getId() ) 
		{
		case R.id.btn_mix:  //섞는버튼
		{
			SetUserId( numberofpeople );
			setArray(a, numberofpeople);
			startGame(a, numberofpeople);
			SetVisible(10, false);


			break;
		}
		case R.id.btn_open:  //전체오픈
		{
			for(int i=0;i<numberofpeople; i++)
				SetVisible(i, true);

			break;
		}
		case R.id.btn1:  //1번
		{
			SetVisible(10, false);
			SetVisible(0, true);

			break;
		}
		case R.id.btn2:  //2번
		{
			SetVisible(10, false);
			SetVisible(1, true);
			break;
		}
		case R.id.btn3:  //3번
		{
			SetVisible(10, false);
			SetVisible(2, true);
			break;
		}
		case R.id.btn4:  //4번
		{
			SetVisible(10, false);
			SetVisible(3, true);
			break;
		}
		}
	}

	public void setArray(int array[],int number) {
		boolean duplicated = false;
		Random  rand = new Random();
		for (int i=0; i<number; i++)  {
			do {
				duplicated = false;  // 중복되지 않았다고 가정
				array[i] = rand.nextInt(number); // 숫자하나 추출한 후
				for (int j=0; j<i; j++)      // 앞의 숫자들과 비교하여
					if (array[i]==array[j])  // 같은 것이 존재하면
						duplicated = true;   // 중복되었다고 체크함
			} while(duplicated);
		}
		//숫사세팅됨
		for(int i=0; i<number;i++){
			tv[i].setText(Integer.toString(array[i])); 
		}

	}
	public void startGame(int array[],int number){
		boolean boola=true;
		int i=0;
		switch (number) {
		case 1:
			tv[0].setText("4500원");
			tv[0].setTextColor(Color.parseColor("#ffff00"));
			break;
		case 2:
		{
			do {
				// 중복되지 않았다고 가정
				if(tv[i].getText().equals("0")) {
					tv[i].setText("2000원");
					tv[i].setTextColor(Color.parseColor("#aaaaaa"));
					i++;
				}else if(tv[i].getText().equals("1")) {
					tv[i].setText("2500원");
					tv[i].setTextColor(Color.parseColor("#ffff00"));
					i++;
				}
				if(i==2)
					boola = false;
				// 중복되었다고 체크함
			} while(boola);
			break;
		}
		case 3:
		{
			do {
				// 중복되지 않았다고 가정
				if(tv[i].getText().equals("0")) {
					tv[i].setText("1000원");
					tv[i].setTextColor(Color.parseColor("#aaaaaa"));
					i++;
				}else if(tv[i].getText().equals("1")) {
					tv[i].setText("1500원");
					tv[i].setTextColor(Color.parseColor("#ff00ff"));
					i++;
				}else if(tv[i].getText().equals("2")) {
					tv[i].setText("2000원");
					tv[i].setTextColor(Color.parseColor("#ffff00"));
					i++;
				}
				if(i==3)
					boola = false;
				// 중복되었다고 체크함
			} while(boola);
			break;
		}
		case 4:
		{
			do {
				// 중복되지 않았다고 가정
				if(tv[i].getText().equals("0")) {
					tv[i].setText("1000원");
					tv[i].setTextColor(Color.parseColor("#999999"));
					i++;
				}else if(tv[i].getText().equals("1")) {
					tv[i].setText("1000원");
					tv[i].setTextColor(Color.parseColor("#999999"));
					i++;
				}else if(tv[i].getText().equals("2")) {
					tv[i].setText("1000원");
					tv[i].setTextColor(Color.parseColor("#999999"));
					i++;
				}else if(tv[i].getText().equals("3")) {
					tv[i].setText("1500원");
					tv[i].setTextColor(Color.parseColor("#ffff00"));
					i++;
				}
				if(i==4)
					boola = false;
				// 중복되었다고 체크함
			} while(boola);
			break;
		}
		default:
			break;
		}

	}
	//	public void startGame(int array[],int i,boolean b){
	//		if(tv[i].getText().equals("0"))
	//		{
	//			tv[i].setText("0000");
	//		}else if(tv[i].getText().equals("1"))
	//		{
	//			tv[i].setText("1000");
	//		}else if(tv[i].getText().equals("2"))
	//		{
	//			tv[i].setText("2000");
	//		}else if(tv[i].getText().equals("3"))
	//		{
	//			tv[i].setText("3000");
	//		}
	//		tv[i].setVisibility(TextView.VISIBLE);
	//	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bbbgame, menu);
		return true;
	}

}
