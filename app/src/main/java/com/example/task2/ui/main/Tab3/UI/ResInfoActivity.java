package com.example.task2.ui.main.Tab3.UI;

import static com.example.task2.ui.main.Tab3.UI.Fragment3.MODIFY_REQUEST;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ChangedPackages;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.task2.R;
import com.example.task2.ui.main.Tab3.Data.OptionList;
import com.example.task2.ui.main.Tab3.Data.Restaurant;
import com.example.task2.ui.main.Tab3.Data.RestaurantManager;
import java.util.ArrayList;

public class ResInfoActivity extends Activity {

  ArrayList<Restaurant> restaurantList;
  OptionList optionList;
  Restaurant restaurant;
  TextView titleTextView;
  TextView locationTextView;
  ListView listView;
  Button deleteButton, modifyButton;
  ArrayAdapter adapter;
  boolean isDataChanged;
  String[] optionNameArray;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_restaurant_info);

    isDataChanged = false;

    restaurantList = (ArrayList<Restaurant>) getIntent().getSerializableExtra("restaurantList");
    optionList = (OptionList) getIntent().getSerializableExtra("optionList");
    restaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");

    titleTextView = (TextView) findViewById(R.id.titleTextView);
    titleTextView.setText("  " + restaurant.getName());

    locationTextView = (TextView) findViewById(R.id.locationTextView);
    locationTextView.setText(restaurant.getLocation());

    optionNameArray = new String[restaurant.getOptionList().size()];
    for (int i = 0; i < restaurant.getOptionList().size(); i++) {
      optionNameArray[i] = restaurant.getOptionList().get(i);
    }

    adapter = new ArrayAdapter(getApplicationContext(),
        android.R.layout.simple_list_item_1, optionNameArray);

    listView = (ListView) findViewById(R.id.infoListView);
    listView.setAdapter(adapter);

    modifyButton = (Button) findViewById(R.id.modifyButton);
    deleteButton = (Button) findViewById(R.id.deleteButton);

    modifyButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ResModifyActivity.class);
        intent.putExtra("restaurantList", restaurantList);
        intent.putExtra("optionList", optionList);
        intent.putExtra("restaurant", restaurant);
        startActivityForResult(intent, MODIFY_REQUEST);

      }
    });

    deleteButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ResInfoActivity.this);

        String message = restaurant.getName() + "을 삭제하시겠습니까?";

        builder.setTitle("식당 삭제").setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int id) {
            Toast.makeText(getApplicationContext(), "삭제됩니다", Toast.LENGTH_SHORT).show();
 RestaurantManager
                .deleteRestaurant(restaurant, restaurantList, optionList);

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
      }
    });


  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);

    if (requestCode == MODIFY_REQUEST) {
      if (resultCode == RESULT_OK) {
        restaurantList = (ArrayList<Restaurant>) intent.getSerializableExtra("restaurantList");
        optionList = (OptionList) intent.getSerializableExtra("optionList");
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");

        titleTextView.setText(restaurant.getName());
        locationTextView.setText(restaurant.getLocation());

        optionNameArray = new String[restaurant.getOptionList().size()];
        for (int i = 0; i < restaurant.getOptionList().size(); i++) {
          optionNameArray[i] = restaurant.getOptionList().get(i);
        }

        adapter = new ArrayAdapter(getApplicationContext(),
            android.R.layout.simple_list_item_1, optionNameArray);

        listView.setAdapter(adapter);

        isDataChanged = true;

      } else {
        restaurantList = (ArrayList<Restaurant>) intent.getSerializableExtra("restaurantList");
        optionList = (OptionList) intent.getSerializableExtra("optionList");
        isDataChanged = false;
      }
    }
  }

  @Override
  public void onBackPressed() {

    Intent backIntent = new Intent();
    backIntent.putExtra("restaurantList", restaurantList);
    backIntent.putExtra("optionList", optionList);

    if (isDataChanged) {
      setResult(RESULT_OK, backIntent);
    } else {
      setResult(RESULT_CANCELED, backIntent);
    }

    finish();
  }
}
