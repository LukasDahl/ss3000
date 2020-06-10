package com.cdio.ss3000;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.pranavpandey.android.dynamic.utils.DynamicUnitUtils;

import java.util.ArrayList;


public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

  boolean tester;
  ImageView image;
  Button capture;
  Camera camera;
  FrameLayout framelayout;
  ShowCamera showCamera;
  public Bitmap[] bmArray = new Bitmap[21];
  //ArrayList<Bitmap> bmArray = new ArrayList<Bitmap>();
  Bitmap bm = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);
    framelayout = (FrameLayout)findViewById(R.id.frameLayout);


    //FrameLayout framelayout;

    tester = false;

    capture = findViewById(R.id.captureBtn);
    capture.setOnClickListener(this);

    image = findViewById(R.id.testiv);
    image.setImageBitmap(null);

    //opens the camera
    camera = Camera.open();

    showCamera = new ShowCamera(this, camera);
    framelayout.addView(showCamera);

    capture.bringToFront();
  }

  public @NonNull static Bitmap createBitmapFromView(@NonNull View view, int width, int height) {
    if (width > 0 && height > 0) {
      view.measure(View.MeasureSpec.makeMeasureSpec(DynamicUnitUtils
                      .convertDpToPixels(width), View.MeasureSpec.EXACTLY),
              View.MeasureSpec.makeMeasureSpec(DynamicUnitUtils
                      .convertDpToPixels(height), View.MeasureSpec.EXACTLY));
    }
    view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

    Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
            view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Drawable background = view.getBackground();

    if (background != null) {
      background.draw(canvas);
    }
    view.draw(canvas);

    return bitmap;
  }

  @Override
  public void onClick(View v) {
    if(v == capture){
        framelayout = (FrameLayout) findViewById(R.id.frameLayout);

        for(int i=0; i<10; i++){
        Bitmap bitmap = createBitmapFromView(framelayout, 0, 0);
        bmArray[i] = bitmap;
        image.setImageBitmap(bitmap);
        image.bringToFront();
        }

        //image.setImageBitmap(bm);
      System.out.println(bmArray[0]);
    }
  }
}
