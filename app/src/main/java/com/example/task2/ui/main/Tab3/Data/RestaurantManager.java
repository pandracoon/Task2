package com.example.task2.ui.main.Tab3.Data;


import android.util.Log;
import java.util.ArrayList;


public class RestaurantManager {

  public static final int CLEAR = -1;
  public static final int DELETE_NEEDED = -2;
  public static final int MODIFIED = -3;

  public static int addRestaurant(ArrayList<Restaurant> restaurantList,
      ArrayList<Boolean> optionStateList, OptionList optionList, String name, String location) {

    Restaurant restaurant = new Restaurant(name, location);

    for (int i = 0; i < optionList.getList(OptionList.TYPE).size(); i++) {
      if (optionStateList.get(i)) {
        restaurant.addOption(optionList.getList(OptionList.TYPE).get(i));
      }
    }

    for (int i = 0; i < optionList.getList(OptionList.COST).size(); i++) {
      if (optionStateList.get(i + 5)) {
        restaurant.addOption(optionList.getList(OptionList.COST).get(i));
      }
    }

    for (int i = 0; i < optionList.getList(OptionList.NUM).size(); i++) {
      if (optionStateList.get(i + 10)) {
        restaurant.addOption(optionList.getList(OptionList.NUM).get(i));
      }
    }

    if (!optionList.getList(OptionList.LOC).contains(location)) {
      optionList.addLocation(location);
    }

    int deleteIndex = isNameContained(restaurant, name, restaurantList);

    if (deleteIndex != RestaurantManager.CLEAR) {
      return deleteIndex;
    }

    restaurantList.add(restaurant);

    return RestaurantManager.CLEAR;
  }

  public static ArrayList<Restaurant> searchRestaurant(ArrayList<Restaurant> restaurantList,
      OptionList optionList,
      ArrayList<Boolean> optionStateList) {
    ArrayList<Restaurant> filteredList = (ArrayList<Restaurant>) restaurantList.clone();

    filteredList = filterList(optionList.getList(OptionList.TYPE), filteredList,
        optionStateList, OptionList.TYPE);
    filteredList = filterList(optionList.getList(OptionList.COST), filteredList,
        optionStateList, OptionList.COST);
    filteredList = filterList(optionList.getList(OptionList.NUM), filteredList,
        optionStateList, OptionList.NUM);
    filteredList = filterList(optionList.getList(OptionList.LOC), filteredList,
        optionStateList, OptionList.LOC);

//    String[] searchResultNameList = new String[filteredList.size()];
//    for (int i = 0; i < filteredList.size(); i++) {
//      searchResultNameList[i] = filteredList.get(i).getName();
//    }

    return filteredList;
  }

  public static int deleteRestaurant(Restaurant restaurant, ArrayList<Restaurant> restaurantList,
      OptionList optionList) {
    String name = restaurant.getName();
    int index = 0;
    for (int i = 0; i < restaurantList.size(); i++) {
      if (name.equals(restaurantList.get(i).getName())) {
        index = i;
        break;
      }
    }

    restaurantList.remove(index);
    for (int i = 0; i < restaurantList.size(); i++) {
      if (restaurantList.get(i).getLocation().equals(restaurant.getLocation())) {

        break;
      }
      if (i == restaurantList.size() - 1) {
        optionList.getList(OptionList.LOC).remove(restaurant.getLocation());

        return 2;
      }
    }

    if (restaurantList.size() == 0) {
      optionList.getList(OptionList.LOC).remove(restaurant.getLocation());

      return 1;
    }

    return 0;
  }

  public static int[] modifyRestaurant(Restaurant restaurant, OptionList optionList,
      ArrayList<Restaurant> restaurantList, ArrayList<Boolean> optionStateList, String name,
      String location) {

    int index;
    int state;
    int[] returnValue = new int[2];
    int deleteIndex = RestaurantManager.CLEAR;

    if (!name.equals(restaurant.getName())) {
      deleteIndex = isNameContained(null, name, restaurantList);
    }

    if (deleteIndex != RestaurantManager.CLEAR) {
      index = deleteIndex;
      state = DELETE_NEEDED;

      returnValue[0] = index;
      returnValue[1] = state;
      return returnValue;
    }

    deleteRestaurant(restaurant, restaurantList, optionList);
    addRestaurant(restaurantList, optionStateList, optionList, name, location);

    index = restaurantList.size() - 1;
    state = MODIFIED;
    returnValue[0] = index;
    returnValue[1] = state;

    return returnValue;

  }

  public static String recommendRestaurant(ArrayList<Restaurant> restaurantList,
      OptionList optionList, ArrayList<Boolean> optionStateList) {

    ArrayList<Restaurant> searchResList = searchRestaurant(restaurantList, optionList,
        optionStateList);

    String[] restaurantNameArray = new String[searchResList.size()];
    for (int i = 0; i < searchResList.size(); i++) {
      restaurantNameArray[i] = searchResList.get(i).getName();
    }

    if (restaurantNameArray.length == 0) {
      return "결과 없음";
    }

    int recommendIndex = (int) (Math.random() * restaurantNameArray.length);
    return restaurantNameArray[recommendIndex];
  }


  private static ArrayList<Restaurant> filterList(ArrayList<String> optionList,
      ArrayList<Restaurant> restaurantList, ArrayList<Boolean> optionStateList, int listNum) {
    ArrayList<Restaurant> filteredList = new ArrayList<Restaurant>();

    for (int i = 0; i < optionList.size(); i++) {
      if (optionStateList.get(i + 5 * listNum)) {
        break;
      }
      if (i == optionList.size() - 1) {
        return restaurantList;
      }
    }

    if (listNum == OptionList.LOC) {
      for (Restaurant restaurant : restaurantList) {
        for (int j = 0; j < optionList.size(); j++) {
          if (!optionStateList.get(j + 5 * listNum)) {
            continue;
          }
          if (restaurant.getLocation().equals(optionList.get(j))) {
            filteredList.add(restaurant);
            break;
          }
        }
      }
    } else {
      for (Restaurant restaurant : restaurantList) {
        for (int j = 0; j < optionList.size(); j++) {
          if (!optionStateList.get(j + 5 * listNum)) {
            continue;
          }
          if (restaurant.getOptionList().contains(optionList.get(j))) {
            filteredList.add(restaurant);
            break;
          }
        }
      }
    }
    return filteredList;
  }

  public static int isNameContained(Restaurant restaurant, String name,
      ArrayList<Restaurant> restaurantList) {

    if (restaurantList.size() == 0) {
      return RestaurantManager.CLEAR;
    }

    for (int i = 0; i < restaurantList.size(); i++) {

      if (restaurantList.get(i).getName().equals(name)) {
        return i;
      }
    }
    return RestaurantManager.CLEAR;
  }
}
