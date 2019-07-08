package com.example.task2.ui.main.Tab3.UI;

import static com.example.task2.ui.main.Tab3.UI.Fragment3.INFO_REQUEST;
import static com.example.task2.ui.main.Tab3.UI.ResListSearchActivity.UNCONDITIONED;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.task2.R;
import com.example.task2.ui.main.Tab3.Data.OptionList;
import com.example.task2.ui.main.Tab3.Data.Restaurant;
import com.example.task2.ui.main.Tab3.Data.RestaurantManager;
import java.util.ArrayList;

public class ResListActivity extends Activity {

  ArrayList<Restaurant> restaurantList;
  ArrayList<Boolean> optionStateList;
  ArrayList<Restaurant> searchedResList;
  String[] restaurantNameArray;
  OptionList optionList;
  ListView listView;
  Button button;
  ArrayAdapter adapter;
  ArrayAdapter<String> arrayAdapter;
  int state;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_restaurant_list);

    restaurantList = (ArrayList<Restaurant>) getIntent().getSerializableExtra("restaurantList");
    optionList = (OptionList) getIntent().getSerializableExtra("optionList");
    optionStateList = (ArrayList<Boolean>) getIntent().getSerializableExtra("optionStateList");
    state = getIntent().getIntExtra("state", UNCONDITIONED);

    if (state == UNCONDITIONED) {
      restaurantNameArray = new String[restaurantList.size()];
      for (int i = 0; i < restaurantList.size(); i++) {
        restaurantNameArray[i] = restaurantList.get(i).getName();
      }
    } else {
      searchedResList = RestaurantManager
          .searchRestaurant(restaurantList, optionList, optionStateList);
      restaurantNameArray = new String[searchedResList.size()];
      for (int i = 0; i < searchedResList.size(); i++) {
        restaurantNameArray[i] = searchedResList.get(i).getName();
      }

    }

    arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
        restaurantNameArray) {
      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setTextColor(Color.BLACK);
        return view;
      }
    };

    adapter = arrayAdapter;

    listView = (ListView) findViewById(R.id.resListView);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView parent, View view, int position, long id) {
        String clickedString = (String) parent.getItemAtPosition(position);
        int selectedRestaurantIndex = RestaurantManager
            .isNameContained(null, clickedString, restaurantList);
        Restaurant selectedRestaurant = restaurantList.get(selectedRestaurantIndex);

        Intent intent = new Intent(getApplicationContext(), ResInfoActivity.class);
        intent.putExtra("restaurantList", restaurantList);
        intent.putExtra("optionList", optionList);
        intent.putExtra("restaurant", selectedRestaurant);
        startActivityForResult(intent, INFO_REQUEST);
      }
    });

    button = (Button) findViewById(R.id.reSearchButton);
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent backIntent = new Intent();
        backIntent.putExtra("restaurantList", restaurantList);
        backIntent.putExtra("optionList", optionList);
        setResult(RESULT_OK, backIntent);
        finish();
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);

    restaurantList = (ArrayList<Restaurant>) intent.getSerializableExtra("restaurantList");
    optionList = (OptionList) intent.getSerializableExtra("optionList");

    if (state == UNCONDITIONED) {
      restaurantNameArray = new String[restaurantList.size()];
      for (int i = 0; i < restaurantList.size(); i++) {
        restaurantNameArray[i] = restaurantList.get(i).getName();
      }
    } else {
      searchedResList = RestaurantManager
          .searchRestaurant(restaurantList, optionList, optionStateList);
      restaurantNameArray = new String[searchedResList.size()];
      for (int i = 0; i < searchedResList.size(); i++) {
        restaurantNameArray[i] = searchedResList.get(i).getName();
      }
    }

    arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
        restaurantNameArray) {
      @Override
      public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setTextColor(Color.BLACK);
        return view;
      }
    };

    adapter = arrayAdapter;
    listView.setAdapter(adapter);

  }

  @Override
  public void onBackPressed() {
    Intent backIntent = new Intent();
    backIntent.putExtra("restaurantList", restaurantList);
    backIntent.putExtra("optionList", optionList);
    setResult(RESULT_OK, backIntent);
    finish();
  }

}
