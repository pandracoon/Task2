package com.example.task2.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.example.task2.R;
import com.facebook.CallbackManager;
import com.facebook.CallbackManager.Factory;
import com.facebook.login.widget.LoginButton;
import java.util.Arrays;

public class LoginActivity extends Activity {

  private Context context;
  private LoginButton facebookLoginBtn;
  private LoginCallback loginCallback;
  private CallbackManager callbackManager;


  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    context = getApplicationContext();

    callbackManager = Factory.create();
    loginCallback = new LoginCallback(this);

    facebookLoginBtn = (LoginButton) findViewById(R.id.login_button);
    facebookLoginBtn.setReadPermissions(Arrays.asList("public_profile", "email"));
    facebookLoginBtn.registerCallback(callbackManager, loginCallback);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    callbackManager.onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);

  }

  @Override
  public void onBackPressed() {
  }

}
