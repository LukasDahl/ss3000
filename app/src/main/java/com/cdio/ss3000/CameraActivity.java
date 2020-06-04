package com.cdio.ss3000;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;


public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

  Button capture;
  Camera camera;
  FrameLayout framelayout;
  ShowCamera showCamera;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);
    framelayout = (FrameLayout)findViewById(R.id.frameLayout);

    capture = findViewById(R.id.captureBtn);
    capture.setOnClickListener(this);

    //opens the camera
    camera = Camera.open();

    showCamera = new ShowCamera(this, camera);
    framelayout.addView(showCamera);
  }

  @Override
  public void onClick(View v) {
    if(v == capture){

    }
  }
}
