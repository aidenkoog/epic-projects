package com.example.taxiclient;

import beans.Operation;
import beans.UserInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class RegisterUserFormActivity extends Activity implements OnCheckedChangeListener
{

	
	EditText register_user_nickname;
	EditText register_user_dept;
	TelephonyManager telManager;
	SocketManager socket;
	final static public String prefFileName = "user_info";
	final static public String prefUserNickname = "user_nickname";
	final static public String prefUserDept = "user_dept";
	final static public String prefSex = "user_sex";
	
	RadioGroup radio;
	String sexValue="";
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        
        telManager = (TelephonyManager)this.getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        socket = SocketManager.getInstance();
        
        setContentView(R.layout.register_user_form);
        
        radio=(RadioGroup)findViewById(R.id.sex);
        radio.setOnCheckedChangeListener(this);
        DialogNotice();
    }
	@Override
	public void onCheckedChanged(RadioGroup group,int checkedId){
		if(checkedId == R.id.man)
			sexValue = "남";
		else if(checkedId ==R.id.woman)
			sexValue = "여";
	}
	public void DialogNotice() {
		 AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		 alt_bld.setMessage("\n ** 정확하게 기입하십시오.\n ** 성별을 허위로 작성할경우 즉시 퇴장조치됩니다.\n"
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
		 alert.setTitle("주의사항");
		 alert.setIcon(R.drawable.ic_launcher);
		 alert.show();
	 }
	
	public void onClickedRegisterBtn(View v){
		String phoneId = telManager.getLine1Number();
		register_user_nickname = (EditText) findViewById(R.id.register_user_nickname);
		register_user_dept = (EditText) findViewById(R.id.register_user_dept);
		
		if(register_user_nickname.getText().toString().equals("")){
			Toast.makeText(getApplicationContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
		}else if(register_user_dept.getText().toString().equals("")){
			Toast.makeText(getApplicationContext(), "학과를 입력해주세요", Toast.LENGTH_SHORT).show();
		}else if(sexValue.equals("")){
			Toast.makeText(getApplicationContext(), "성별을 선택해주세요", Toast.LENGTH_SHORT).show();
		}
		else{
			String nickname = register_user_nickname.getText().toString();
			String dept = register_user_dept.getText().toString();
			
			
			nickname=nickname+"( "+sexValue+" )";
			UserInfo info = new UserInfo(Operation.OP_ADD_USER);
			info.setPhoneNumber(phoneId);
			info.setNickname(nickname);
			info.setDept(dept);
			info.setsexValue(sexValue);
			storeUserInfo(info); // 쉐어드프리퍼런스에 저장
			
			try{
				socket.writeObj(info);
			}catch(Exception e){
				e.printStackTrace();
			}
			Intent intent = new Intent(getApplicationContext(), RoomListActivity.class);
		    startActivity(intent);
		    finish();
			
		}
		
	}
	
	private void storeUserInfo(UserInfo info){
		SharedPreferences pref = getSharedPreferences(RegisterUserFormActivity.prefFileName, MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(RegisterUserFormActivity.prefUserNickname, info.getNickname());
		editor.putString(RegisterUserFormActivity.prefUserDept, info.getDept());
		editor.putString(RegisterUserFormActivity.prefSex, info.getsexValue());
		editor.commit();
	}
	
	public void onClickedOutBtn(View v){
		onBackPressed();
	}
	
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
	}
	
}
