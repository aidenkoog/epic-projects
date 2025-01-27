package com.example.taxiclient;




import android.os.Bundle;
import android.view.Window;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        startActivity(new Intent(this, IntroActivity.class));
        finish();
    }
}
