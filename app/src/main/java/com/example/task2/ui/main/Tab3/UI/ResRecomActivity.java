package com.example.task2.ui.main.Tab3.UI;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.task2.R;
import com.example.task2.ui.main.Tab3.Data.OptionList;
import com.example.task2.ui.main.Tab3.Data.Restaurant;
import com.example.task2.ui.main.Tab3.Data.RestaurantManager;
import java.util.ArrayList;

public class ResRecomActivity extends Activity {

  private CheckBox[] typeCheckBox = new CheckBox[5];
  private CheckBox[] costCheckBox = new CheckBox[5];
  private CheckBox[] numOfPeopleCheckBox = new CheckBox[5];
  private CheckBox[] locationCheckBox;
  private OptionList optionList;
  private ArrayList<Restaurant> restaurantList;
  private ArrayList<Boolean> optionStateList = new ArrayList<Boolean>();
  LinearLayout locationLayout;
  TextView textView;
  Button recommendButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_restaurant_recommend);

    optionList = (OptionList) getIntent().getSerializableExtra("optionList");
    restaurantList = (ArrayList<Restaurant>) getIntent().getSerializableExtra("restaurantList");

    typeCheckBox[0] = (CheckBox) findViewById(R.id.typeCheckBox1R);
    typeCheckBox[1] = (CheckBox) findViewById(R.id.typeCheckBox2R);
    typeCheckBox[2] = (CheckBox) findViewById(R.id.typeCheckBox3R);
    typeCheckBox[3] = (CheckBox) findViewById(R.id.typeCheckBox4R);
    typeCheckBox[4] = (CheckBox) findViewById(R.id.typeCheckBox5R);

    costCheckBox[0] = (CheckBox) findViewById(R.id.costCheckBox1R);
    costCheckBox[1] = (CheckBox) findViewById(R.id.costCheckBox2R);
    costCheckBox[2] = (CheckBox) findViewById(R.id.costCheckBox3R);
    costCheckBox[3] = (CheckBox) findViewById(R.id.costCheckBox4R);
    costCheckBox[4] = (CheckBox) findViewById(R.id.costCheckBox5R);

    numOfPeopleCheckBox[0] = (CheckBox) findViewById(R.id.numOfPeopleCheckBox1R);
    numOfPeopleCheckBox[1] = (CheckBox) findViewById(R.id.numOfPeopleCheckBox2R);
    numOfPeopleCheckBox[2] = (CheckBox) findViewById(R.id.numOfPeopleCheckBox3R);
    numOfPeopleCheckBox[3] = (CheckBox) findViewById(R.id.numOfPeopleCheckBox4R);
    numOfPeopleCheckBox[4] = (CheckBox) findViewById(R.id.numOfPeopleCheckBox5R);

    locationLayout = findViewById(R.id.locationLinearLayoutR);

    locationCheckBox = new CheckBox[optionList.getList(OptionList.LOC).size()];
    for (int i = 0; i < optionList.getList(OptionList.LOC).size(); i++) {
      ArrayList<String> locationList = optionList.getList(OptionList.LOC);
      locationCheckBox[i] = new CheckBox(getApplicationContext());
      locationCheckBox[i].setText(locationList.get(i));
      LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
          LayoutParams.WRAP_CONTENT);
      layoutParams.topMargin = 5;
      layoutParams.bottomMargin = 5;
      locationCheckBox[i].setLayoutParams(layoutParams);
      locationLayout.addView(locationCheckBox[i]);
    }

    recommendButton = (Button) findViewById(R.id.recommendButton);
    recommendButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        optionStateList = getOptionStateList();
        String recommendText = RestaurantManager
            .recommendRestaurant(restaurantList, optionList, optionStateList);

        textView=(TextView)findViewById(R.id.recommendTextView);
        textView.setText(recommendText);
      }

    });

  }


  private ArrayList<Boolean> getOptionStateList() {

    ArrayList<Boolean> optionStateList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      optionStateList.add(typeCheckBox[i].isChecked());
    }
    for (int i = 0; i < 5; i++) {
      optionStateList.add(costCheckBox[i].isChecked());
    }
    for (int i = 0; i < 5; i++) {
      optionStateList.add(numOfPeopleCheckBox[i].isChecked());
    }
    for (int i = 0; i < locationCheckBox.length; i++) {
      optionStateList.add(locationCheckBox[i].isChecked());
    }

    return optionStateList;
  }


  @Override
  public void onBackPressed() {
    Intent backIntent = new Intent();
    backIntent.putExtra("restaurantList", restaurantList);
    backIntent.putExtra("optionList", optionList);
    setResult(RESULT_CANCELED, backIntent);
    finish();

  }
}
