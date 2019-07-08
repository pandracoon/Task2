package com.example.task2.ui.main.Tab3.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.example.task2.R;
import com.example.task2.ui.main.Tab3.Data.OptionList;
import com.example.task2.ui.main.Tab3.Data.Restaurant;
import com.example.task2.ui.main.Tab3.Data.RestaurantManager;
import java.util.ArrayList;

public class ResAddActivity extends Activity {

  private EditText nameText;
  private EditText locationText;
  private CheckBox[] typeCheckBox = new CheckBox[5];
  private CheckBox[] costCheckBox = new CheckBox[5];
  private CheckBox[] numOfPeopleCheckBox = new CheckBox[5];
  private OptionList optionList;
  private ArrayList<Restaurant> restaurantList;
  private String name;
  private String location;
  private ArrayList<Boolean> optionStateList = new ArrayList<Boolean>();
  Button addButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_restaurant_add);
    optionList = (OptionList) getIntent().getSerializableExtra("optionList");
    restaurantList = (ArrayList<Restaurant>) getIntent().getSerializableExtra("restaurantList");



    nameText = (EditText) findViewById(R.id.editText3);
    locationText = (EditText) findViewById(R.id.editText4);

    typeCheckBox[0] = (CheckBox) findViewById(R.id.typeCheckBox1);
    typeCheckBox[1] = (CheckBox) findViewById(R.id.typeCheckBox2);
    typeCheckBox[2] = (CheckBox) findViewById(R.id.typeCheckBox3);
    typeCheckBox[3] = (CheckBox) findViewById(R.id.typeCheckBox4);
    typeCheckBox[4] = (CheckBox) findViewById(R.id.typeCheckBox5);

    costCheckBox[0] = (CheckBox) findViewById(R.id.costCheckBox1);
    costCheckBox[1] = (CheckBox) findViewById(R.id.costCheckBox2);
    costCheckBox[2] = (CheckBox) findViewById(R.id.costCheckBox3);
    costCheckBox[3] = (CheckBox) findViewById(R.id.costCheckBox4);
    costCheckBox[4] = (CheckBox) findViewById(R.id.costCheckBox5);

    numOfPeopleCheckBox[0] = (CheckBox) findViewById(R.id.numOfPeopleCheckBox1);
    numOfPeopleCheckBox[1] = (CheckBox) findViewById(R.id.numOfPeopleCheckBox2);
    numOfPeopleCheckBox[2] = (CheckBox) findViewById(R.id.numOfPeopleCheckBox3);
    numOfPeopleCheckBox[3] = (CheckBox) findViewById(R.id.numOfPeopleCheckBox4);
    numOfPeopleCheckBox[4] = (CheckBox) findViewById(R.id.numOfPeopleCheckBox5);

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
            name = nameText.getText().toString();
            location = locationText.getText().toString();
            optionStateList = getOptionStateList();

            if (name.isEmpty() || location.isEmpty()) {
              Toast.makeText(getApplicationContext(), "이름 또는 장소는 비울 수 없습니다.", Toast.LENGTH_SHORT)
                  .show();
              return;
            }

            final int request = RestaurantManager
                .addRestaurant(restaurantList, optionStateList, optionList, name, location);

        if (request != RestaurantManager.CLEAR) {

          AlertDialog.Builder builder = new AlertDialog.Builder(ResAddActivity.this);

          builder.setTitle("추가 실패").setMessage("같은 이름을 가진 식당이 존재합니다.\n기존 식당 데이터를 삭제하시겠습니까?");
          builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
              Toast.makeText(getApplicationContext(), "삭제됩니다", Toast.LENGTH_SHORT).show();
RestaurantManager
                  .deleteRestaurant(restaurantList.get(request), restaurantList, optionList);

              RestaurantManager
                  .addRestaurant(restaurantList, optionStateList, optionList, name, location);
              Intent backIntent = new Intent();
              backIntent.putExtra("restaurantList", restaurantList);
              backIntent.putExtra("optionList", optionList);
              setResult(RESULT_OK, backIntent);
              finish();
            }
          });

          builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
              Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }
          });

          AlertDialog dialog = builder.create();
          dialog.show();

        } else {
          Intent backIntent = new Intent();
          backIntent.putExtra("restaurantList", restaurantList);
          backIntent.putExtra("optionList", optionList);
          setResult(RESULT_OK, backIntent);
          finish();
        }
      }
    });

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

    return optionStateList;

  }
}
