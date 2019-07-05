package com.example.task2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.task2.login.LoginActivity;
import com.example.task2.login.LoginLoadingActivity;
import com.facebook.login.LoginManager;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.example.task2.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

  public static Context contextOfApplication;

  public static Context getContextOfApplication() {
    return contextOfApplication;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    String email = getIntent().getStringExtra("email");

    contextOfApplication = getApplicationContext();

    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this,
        getSupportFragmentManager());

    ViewPager viewPager = findViewById(R.id.view_pager);
    viewPager.setAdapter(sectionsPagerAdapter);

    TabLayout tabs = findViewById(R.id.tabs);
    tabs.setupWithViewPager(viewPager);



  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    super.onOptionsItemSelected(item);

    if (item.getItemId() == R.id.logout_menu) {
      LoginManager.getInstance().logOut();
      Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
      startActivity(loginIntent);
    }
    return true;
  }

}