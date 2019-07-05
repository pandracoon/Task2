package com.example.task2.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.example.task2.MainActivity;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import org.json.JSONObject;

public class LoginCallback implements FacebookCallback<LoginResult> {

  Context context;

  public LoginCallback(Context context) {
    this.context = context;
  }

  // 로그인 성공 시 호출 됩니다. Access Token 발급 성공.
  @Override
  public void onSuccess(LoginResult loginResult) {
    Log.e("Callback :: ", "onSuccess");
    requestMe(loginResult.getAccessToken());

  }

  // 로그인 창을 닫을 경우, 호출됩니다.
  @Override
  public void onCancel() {
    Log.e("Callback :: ", "onCancel");
  }

  // 로그인 실패 시에 호출됩니다.
  @Override
  public void onError(FacebookException error) {
    Log.e("Callback :: ", "onError : " + error.getMessage());
  }

  // 사용자 정보 요청
  public void requestMe(AccessToken token) {
    GraphRequest graphRequest = GraphRequest.newMeRequest(token,
        new GraphRequest.GraphJSONObjectCallback() {
          @Override
          public void onCompleted(JSONObject object, GraphResponse response) {
            Log.e("result", object.toString());

            try {
              String email = object.getString("name");
              Intent intent = new Intent(context, LoginLoadingActivity.class);
              intent.putExtra("email", email);
              context.startActivity(intent);
              ((Activity)context).finish();
            } catch (Exception e) {
              e.printStackTrace();
            }


          }
        });
    Bundle parameters = new Bundle();
    parameters.putString("fields", "id,name,email,gender,birthday");
    graphRequest.setParameters(parameters);
    graphRequest.executeAsync();
  }
}