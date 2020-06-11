package com.cdio.ss3000;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.service.media.CameraPrewarmService;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.cdio.ss3000.R;

import java.io.IOException;

public class Camera1Activity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera1);
  }

  private boolean isCameraInitialized;

  private Camera mCamera = null;

  private static SurfaceHolder myHolder;

  private static CameraPreview mPreview;

  private FrameLayout preview;

  private static OrientationEventListener orientationEventListener = null;

  private static boolean fM;

  @Override
  protected void onResume(){
    super.onResume();

    if(!isCameraInitialized){
      mCamera = Camera.open();
      mPreview = new CameraPreview(this, mCamera);
      preview = findViewById(R.id.camera_preview);
      preview.addView(mPreview);
      rotateCamera();
      orientationEventListener = new OrientationEventListener(this) {
        @Override
        public void onOrientationChanged(int orientation) {
          rotateCamera();
        }
      };
      orientationEventListener.enable();
      preview.setOnLongClickListener(new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v){
          if(whichCamera){
            if(fM){
              p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            else{
              p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            try{
              mCamera.setParameters(p);
            }catch (Exception e){

            }
            fM = !fM;
          }
          return true;
        }
      });
    }
  }

  private static int rotation;

  private static boolean whichCamera = true;

  private static Camera.Parameters p;

  private void rotateCamera(){
    if(mCamera != null){
      rotation = this.getWindowManager().getDefaultDisplay().getRotation();
      if(rotation == 0){
        rotation = 90;
      }
      else if(rotation == 1){
        rotation = 0;
      }
      else if(rotation == 2){
        rotation = 270;
      }
      else{
        rotation = 180;
      }
      mCamera.setDisplayOrientation(rotation);
      if(!whichCamera){
        if(rotation == 90){
          rotation = 270;
        }
        else if(rotation == 270){
          rotation = 90;
        }
      }
      p = mCamera.getParameters();
      p.setRotation(rotation);
      mCamera.setParameters(p);
    }
  }

  private static class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
    private static SurfaceHolder mHolder;
    private static Camera mCamera;

    private CameraPreview(Context context, Camera camera){
      super(context);
      mCamera = camera;
      mHolder = getHolder();
      mHolder.addCallback(this);
      mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder){
      myHolder = holder;
      try {
        mCamera.setPreviewDisplay(holder);
        mCamera.startPreview();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public void surfaceDestroyed(SurfaceHolder holder){

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){

    }
  }
}
