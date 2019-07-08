package com.example.task2.ui.main.Tab3.UI;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import com.example.task2.R;

public class TimeTableActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_timetable);
    ImageView imageView = (ImageView) findViewById(R.id.timetable);
    imageView.setImageResource(R.drawable.time_table);
  }
}
