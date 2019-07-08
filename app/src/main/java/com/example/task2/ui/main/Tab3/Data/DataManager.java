package com.example.task2.ui.main.Tab3.Data;

import java.io.*;

public class DataManager {

  //
  public static void dataSave(RestaurantList restaurantList, OptionList optionList) {
    FileOutputStream fileOutputStream = null;
    ObjectOutputStream objectOutputStream = null;

    try {
      fileOutputStream = new FileOutputStream("./data.txt");
      objectOutputStream = new ObjectOutputStream(fileOutputStream);

      objectOutputStream.writeObject(restaurantList);
      objectOutputStream.writeObject(optionList);


    } catch (IOException e) {
      e.printStackTrace();
    } finally {

      if (fileOutputStream != null) {

        try {
          fileOutputStream.close();
        } catch (IOException e) {

        }

      }

      if (objectOutputStream != null) {

        try {
          objectOutputStream.close();
        } catch (IOException e) {

        }
      }
    }

  }

  public static Object[] dataLoad() {
    FileInputStream fileInputStream = null;
    ObjectInputStream objectInputStream = null;
    Object[] objects = new Object[2];
    try {
      fileInputStream = new FileInputStream("./data.txt");
      objectInputStream = new ObjectInputStream(fileInputStream);

      objects[0] = objectInputStream.readObject();
      objects[1] = objectInputStream.readObject();

    } catch (FileNotFoundException e) {
      return null;
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } finally {
      if (fileInputStream != null) {

        try {
          fileInputStream.close();
        } catch (IOException e) {

        }

      }

      if (objectInputStream != null) {

        try {
          objectInputStream.close();
        } catch (IOException e) {

        }
      }
    }

    return objects;
  }
}
