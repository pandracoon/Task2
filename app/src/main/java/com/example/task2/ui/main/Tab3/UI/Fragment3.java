package com.example.task2.ui.main.Tab3.UI;

import static com.example.task2.MainActivity.getContextOfApplication;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.task2.R;
import com.example.task2.Retrofit.IMyService;
import com.example.task2.Retrofit.RetrofitClient;
import com.example.task2.ui.main.Tab3.Data.LocationData;
import com.example.task2.ui.main.Tab3.Data.OptionList;
import com.example.task2.ui.main.Tab3.Data.Restaurant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Retrofit;

;

public class Fragment3 extends Fragment implements OnMapReadyCallback {

  MapView mapView;
  EditText searchText;
  ImageButton searchbutton;
  ArrayList<Restaurant> restaurantList;
  OptionList optionList;

  public final static int ADD_REQUEST = 1;
  public final static int LIST_REQUEST = 2;
  public static final int SEARCH_REQUEST = 3;
  public static final int MODIFY_REQUEST = 4;
  public static final int INFO_REQUEST = 5;
  public static final int RECOM_REQUEST = 6;

  private Animation fab_open, fab_close;
  private Boolean isFabOpen = false;
  private FloatingActionButton fab, fab1, fab2, fab3;
  
  private CompositeDisposable compositeDisposable = new CompositeDisposable();
  private IMyService iMyService;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.layout_fragment3, container, false);
  
    Retrofit retrofitClient = RetrofitClient.getInstance();
    iMyService = retrofitClient.create(IMyService.class);

    mapView = (MapView) view.findViewById(R.id.map_view);
    mapView.getMapAsync(this);

    restaurantList = new ArrayList<Restaurant>(); //TODO DB에서 데이터를 불러와서 Restaurant 객체들을 채워야 할 곳.
    optionList = new OptionList();
  
    downloadData("1");

    fab_open = AnimationUtils.loadAnimation(getContextOfApplication(), R.anim.fab_open);
    fab_close = AnimationUtils.loadAnimation(getContextOfApplication(), R.anim.fab_close);

    fab = view.findViewById(R.id.timetableBtn);
    fab1 = view.findViewById(R.id.recommendFab);
    fab2 = view.findViewById(R.id.listFab);
    fab3 = view.findViewById(R.id.addFab);

    OnClickListener fabOnClickListener = new OnClickListener() {
      @Override
      public void onClick(View view) {
        int id = view.getId();
        switch (id) {
          case R.id.timetableBtn:
            anim();
            break;
          case R.id.addFab:
            anim();
            Intent addIntent = new Intent(getContextOfApplication(), ResAddActivity.class);
            addIntent.putExtra("restaurantList", restaurantList);
            addIntent.putExtra("optionList", optionList);
            startActivityForResult(addIntent, ADD_REQUEST);
            break;
          case R.id.listFab:
            anim();
            Intent listIntent = new Intent(getContextOfApplication(), ResListSearchActivity.class);
            listIntent.putExtra("restaurantList", restaurantList);
            listIntent.putExtra("optionList", optionList);
            startActivityForResult(listIntent, LIST_REQUEST);
            break;
          case R.id.recommendFab:
            anim();
            Intent recommendIntent = new Intent(getContextOfApplication(), ResRecomActivity.class);
            recommendIntent.putExtra("restaurantList", restaurantList);
            recommendIntent.putExtra("optionList", optionList);
            startActivityForResult(recommendIntent, RECOM_REQUEST);
            break;
        }

      }

      public void anim() {

        if (isFabOpen) {
          fab1.startAnimation(fab_close);
          fab2.startAnimation(fab_close);
          fab3.startAnimation(fab_close);
          fab1.setClickable(false);
          fab2.setClickable(false);
          fab3.setClickable(false);
          isFabOpen = false;
        } else {
          fab1.startAnimation(fab_open);
          fab2.startAnimation(fab_open);
          fab3.startAnimation(fab_open);
          fab1.setClickable(true);
          fab2.setClickable(true);
          fab3.setClickable(true);
          isFabOpen = true;
        }
      }
    };

    fab.setOnClickListener(fabOnClickListener);
    fab1.setOnClickListener(fabOnClickListener);
    fab2.setOnClickListener(fabOnClickListener);
    fab3.setOnClickListener(fabOnClickListener);

    FloatingActionButton timeTableButton = (FloatingActionButton) view.findViewById(R.id.realTimeTableButton);
    timeTableButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getContextOfApplication(), TimeTableActivity.class);
        startActivity(intent);
      }
    });

    return view;
  }

  @Override
  public void onMapReady(@NonNull NaverMap naverMap) {

    UiSettings uiSettings = naverMap.getUiSettings();
    uiSettings.setZoomControlEnabled(false);

    LatLng basicLocation = new LatLng(36.3703641, 127.3626828);
    CameraPosition cameraPosition = new CameraPosition(basicLocation, 14);
    naverMap.setCameraPosition(cameraPosition);

    naverMap.setMinZoom(14);

    LatLng NELimitLocation = new LatLng(36.3763389, 127.3707596);
    LatLng SWLimitLocation = new LatLng(36.3631224, 127.354996);
    naverMap.setExtent(new LatLngBounds(SWLimitLocation, NELimitLocation));

    final LocationData locationData = new LocationData();
    locationData.setData();
    final Marker[] stationMarker = new Marker[locationData.getStationLocSize()];

    for (int i = 0; i < locationData.getStationLocSize(); i++) {
      stationMarker[i] = new Marker();
      stationMarker[i].setPosition(locationData.stationLocList.get(i));
      stationMarker[i].setMap(naverMap);
      stationMarker[i].setCaptionText(locationData.stationNameList.get(i));
    }

    naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
      @Override
      public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
        for (int i = 0; i < locationData.getStationLocSize(); i++) {
          if (stationMarker[i].getInfoWindow() != null) {
            stationMarker[i].getInfoWindow().close();
          }
        }
      }
    });

    Overlay.OnClickListener listener = new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull Overlay overlay) {
        Marker marker = (Marker) overlay;

        final String stationName = marker.getCaptionText();
        final int stationNum = locationData.stationNameList.indexOf(stationName);

        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getContext()) {

          @NonNull
          @Override
          public CharSequence getText(@NonNull InfoWindow infoWindow) {
            String latestTime[] = locationData.getLatestTime(stationNum);
            String infoString = "1st : " + latestTime[0] + "\n2nd : " + latestTime[1];

            return infoString;
          }
        });

        if (marker.getInfoWindow() == null) {
          // 현재 마커에 정보 창이 열려있지 않을 경우 엶
          infoWindow.open(marker);
        }
        return true;

      }
    };

    for (int i = 0; i < locationData.getStationLocSize(); i++) {
      stationMarker[i].setOnClickListener(listener);
    }

    PathOverlay path = new PathOverlay();
    path.setCoords(locationData.pathLocList);
    path.setPatternImage(OverlayImage.fromResource(R.drawable.arrow));
    path.setColor(Color.BLUE);
    path.setWidth(20);
    path.setPatternInterval(50);
    path.setMap(naverMap);


  }

  @Override
  public void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  public void onStop() {
    compositeDisposable.clear();
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mapView.onDestroy();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @RequiresApi(api = VERSION_CODES.KITKAT)
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) { //TODO 정보 업데이트 시켜줘야
    super.onActivityResult(requestCode, resultCode, intent);

    switch (requestCode) {
      case ADD_REQUEST:
        restaurantList = (ArrayList<Restaurant>) intent.getSerializableExtra("restaurantList");
        optionList = (OptionList) intent.getSerializableExtra("optionList");
        uploadData("1", serialize(restaurantList), serialize(optionList));
        break;
      case LIST_REQUEST:
        restaurantList = (ArrayList<Restaurant>) intent.getSerializableExtra("restaurantList");
        optionList = (OptionList) intent.getSerializableExtra("optionList");
        uploadData("1", serialize(restaurantList), serialize(optionList));
        break;
      case RECOM_REQUEST:
        restaurantList = (ArrayList<Restaurant>) intent.getSerializableExtra("restaurantList");
        optionList = (OptionList) intent.getSerializableExtra("optionList");
        uploadData("1", serialize(restaurantList), serialize(optionList));
    }
  }
  
  @TargetApi(VERSION_CODES.O)
  @RequiresApi(api = VERSION_CODES.KITKAT)
  public String serialize(Object object) {
    byte[] serializedObject;
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
        oos.writeObject(object);
        serializedObject = baos.toByteArray();
        return Base64.getEncoder().encodeToString(serializedObject);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  @RequiresApi(api = VERSION_CODES.O)
  public Object deserialize(String string) {
    byte[] serializedObject = Base64.getDecoder().decode(string);
    try(ByteArrayInputStream bais = new ByteArrayInputStream(serializedObject)) {
      try(ObjectInputStream ois = new ObjectInputStream(bais)) {
        Object object = ois.readObject();
        return object;
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        return null;
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  private void uploadData(String user_id, String restaurant_list, String option_list) {
    compositeDisposable.add(iMyService.uploadRestaurant(user_id, restaurant_list, option_list)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<String>() {
          @Override
          public void accept(String response) throws Exception {
            //
          }
        }));
  }
  
  private void downloadData(String user_id) {
    compositeDisposable.add(iMyService.downloadRestaurant(user_id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<String>() {
          @RequiresApi(api = VERSION_CODES.O)
          @Override
          public void accept(String response) throws Exception {
            if(isJSONValid(response)) {
              JSONArray jArray = new JSONArray(response);
              for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                String restaurantString = jObject.getString("restaurant_list");
                String optionString = jObject.getString("option_list");
                if(deserialize(restaurantString) != null && deserialize(optionString) != null) {
                  restaurantList = (ArrayList<Restaurant>) deserialize(restaurantString);
                  optionList = (OptionList) deserialize(optionString);
                }
              }
            }
          }
        }));
  }
  
  public boolean isJSONValid(String test) {
    try {
      new JSONObject(test);
    } catch (JSONException ex) {
      // edited, to include @Arthur's comment
      // e.g. in case JSONArray is valid as well...
      try {
        new JSONArray(test);
      } catch (JSONException ex1) {
        return false;
      }
    }
    return true;
  }
}

