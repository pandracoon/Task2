package com.example.task2.ui.main.Tab3.UI;

import static com.example.task2.ui.main.Tab3.UI.Fragment3.SEARCH_REQUEST;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.VoiceInteractor.PickOptionRequest.Option;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.task2.R;
import com.example.task2.ui.main.Tab3.Data.OptionList;
import com.example.task2.ui.main.Tab3.Data.Restaurant;
import java.util.ArrayList;

public class ResListSearchActivity extends Activity {

  private CheckBox[] typeCheckBox = new CheckBox[5];
  private CheckBox[] costCheckBox = new CheckBox[5];
  private CheckBox[] numOfPeopleCheckBox = new CheckBox[5];
  private CheckBox[] locationCheckBox;
  private OptionList optionList;
  private ArrayList<Restaurant> restaurantList;
  private ArrayList<Boolean> optionStateList = new ArrayList<Boolean>();
  Button searchButton, listAllButton;
  LinearLayout locationLayout;
  int state;


  public static final int CONDITIONED = 1;
  public static final int UNCONDITIONED = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_restaurant_list_search);

    optionList = (OptionList) getIntent().getSerializableExtra("optionList");
    restaurantList = (ArrayList<Restaurant>) getIntent().getSerializableExtra("restaurantList");

    typeCheckBox[0] = (CheckBox) findViewById(R.id.typeCheckBox1S);
    typeCheckBox[1] = (CheckBox) findViewById(R.id.typeCheckBox2S);
    typeCheckBox[2] = (CheckBox) findViewById(R.id.typeCheckBox3S);
    typeCheckBox[3] = (CheckBox) findViewById(R.id.typeCheckBox4S);
    typeCheckBox[4] = (CheckBox) findViewById(R.id.typeCheckBox5S);

    costCheckBox[0] = (CheckBox) findViewById(R.id.costCheckBox1S);
    costCheckBox[1] = (CheckBox) findViewById(R.id.costCheckBox2S);
    costCheckBox[2] = (CheckBox) findViewById(R.id.costCheckBox3S);
    costCheckBox[3] = (CheckBox) findViewById(R.id.costCheckBox4S);
    costCheckBox[4] = (CheckBox) findViewById(R.id.costCheckBox5S);

    numOfPeopleCheckBox[0] = (CheckBox) findViewById(R.id.numOfPeopleCheckBox1S);
    numOfPeopleCheckBox[1] = (CheckBox) findViewById(R.id.numOfPeopleCheckBox2S);
    numOfPeopleCheckBox[2] = (CheckBox) findViewById(R.id.numOfPeopleCheckBox3S);
    numOfPeopleCheckBox[3] = (CheckBox) findViewById(R.id.numOfPeopleCheckBox4S);
    numOfPeopleCheckBox[4] = (CheckBox) findViewById(R.id.numOfPeopleCheckBox5S);

    locationLayout = findViewById(R.id.locationLinearLayout);

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

    searchButton = (Button) findViewById(R.id.searchButton);
    listAllButton = (Button) findViewById(R.id.listAllButton);

    searchButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        optionStateList = getOptionStateList();
        for (int i = 0; i < optionStateList.size(); i++) {
          if (optionStateList.get(i)) {
            state = CONDITIONED;
            break;
          }
          state = UNCONDITIONED;
        }

        if (state == UNCONDITIONED) {
          Toast.makeText(getApplicationContext(), "최소 한 개 이상의 옵션을 선택해야 합니다.", Toast.LENGTH_SHORT)
              .show();
          return;
        }
        Intent intent = new Intent(getApplicationContext(), ResListActivity.class);
        intent.putExtra("restaurantList", restaurantList);
        intent.putExtra("optionList", optionList);
        intent.putExtra("optionStateList", optionStateList);
        intent.putExtra("state", CONDITIONED);
        startActivityForResult(intent, SEARCH_REQUEST);
      }
    });

    listAllButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ResListActivity.class);
        intent.putExtra("restaurantList", restaurantList);
        intent.putExtra("optionList", optionList);
        intent.putExtra("state", UNCONDITIONED);
        startActivityForResult(intent, SEARCH_REQUEST);
      }
    });

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);

    switch (requestCode) {
      case SEARCH_REQUEST:
        restaurantList = (ArrayList<Restaurant>) intent.getSerializableExtra("restaurantList");
        optionList = (OptionList) intent.getSerializableExtra("optionList");

        locationLayout.removeAllViews();

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
        break;
    }
  }

  @Override
  public void onBackPressed() {
    Intent backIntent = new Intent();
    backIntent.putExtra("restaurantList", restaurantList);
    backIntent.putExtra("optionList", optionList);
    setResult(RESULT_OK, backIntent);
    finish();
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

}
