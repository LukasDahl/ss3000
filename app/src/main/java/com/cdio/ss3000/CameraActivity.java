package com.cdio.ss3000;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;


public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

  ImageView image;
  Button capture;
  Camera camera;
  FrameLayout framelayout;
  ShowCamera showCamera;
  ArrayList<Bitmap> bmArray = new ArrayList<Bitmap>();
  Bitmap bm = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);
    framelayout = (FrameLayout)findViewById(R.id.frameLayout);


    //FrameLayout framelayout;

    capture = findViewById(R.id.captureBtn);
    capture.setOnClickListener(this);

    image = findViewById(R.id.testiv);
    image.setImageBitmap(null);

    //opens the camera
    camera = Camera.open();

    showCamera = new ShowCamera(this, camera);
    framelayout.addView(showCamera);
  }

  @Override
  public void onClick(View v) {
    if(v == capture){

      for(int i=0; i<10; i++){
        framelayout = (FrameLayout)findViewById(R.id.frameLayout);
        framelayout.setDrawingCacheEnabled(true);
        framelayout.buildDrawingCache();
        bm = framelayout.getDrawingCache();
        bmArray.add(bm);
        framelayout.destroyDrawingCache();
        System.out.println("Array er: "+bmArray.isEmpty());

      }
      image.setImageBitmap(bmArray.get(2));
    }
  }
}
