package com.example.taxiclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class PopUpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON );
		

		Bundle extras = getIntent().getExtras();
		String msg = extras.getString("chatMsg");
		String name = extras.getString("chatUserName");
		
		
		Button btnGoView = (Button) findViewById(R.id.popupGoView);
		Button btnCancel = (Button) findViewById(R.id.popupCancel);
		
		TextView msgView = (TextView) findViewById(R.id.popupMsg);
		TextView nameView = (TextView) findViewById(R.id.popupName);
		
		msgView.setText(msg);
		nameView.setText(name);
		
		
		btnGoView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), ChatRoomActivity.class);
        		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK 
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP 
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        		startActivity(intent);
				finish();
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
