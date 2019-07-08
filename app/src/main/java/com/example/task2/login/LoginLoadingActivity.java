package com.example.task2.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.example.task2.LoadingActivity;
import com.example.task2.MainActivity;
import com.example.task2.R;

public class LoginLoadingActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_loginloading);
    startLoading();

  }

  private void startLoading() {
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        Intent intent = new Intent(LoginLoadingActivity.this.getApplicationContext(), MainActivity.class);
        String global_id = getIntent().getStringExtra("global_id");
        intent.putExtra("global_id", global_id);
        startActivity(intent);
        LoginLoadingActivity.this.finish();
      }
    }, 1000);
  }
}