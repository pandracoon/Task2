package com.example.task2.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import com.example.task2.R;
import com.facebook.CallbackManager;
import com.facebook.CallbackManager.Factory;
import com.facebook.login.widget.LoginButton;
import java.util.Arrays;

public class LoginActivity extends Activity {

  private Context context;
  private LoginButton facbookLoginBtn;
  private LoginCallback loginCallback;
  private CallbackManager callbackManager;


  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    context = getApplicationContext();

    callbackManager = Factory.create();
    loginCallback = new LoginCallback(this);

    facbookLoginBtn = (LoginButton) findViewById(R.id.login_button);
    facbookLoginBtn.setReadPermissions(Arrays.asList("public_profile", "email"));
    facbookLoginBtn.registerCallback(callbackManager, loginCallback);
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
