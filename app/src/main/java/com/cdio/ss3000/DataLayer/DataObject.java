package com.cdio.ss3000.DataLayer;

import android.graphics.Bitmap;

public class DataObject {

  private static DataObject instance = null;
  private Bitmap bitmap = null;
  public static DataObject getInstance(){
    if (instance == null){
      instance = new DataObject();
    }
    return instance;
  }

  private DataObject(){

  }

  public Bitmap getBitmap(){
    return bitmap;
  }
  public void setBitmap(Bitmap bitmap){
    this.bitmap = bitmap;
  }
}
