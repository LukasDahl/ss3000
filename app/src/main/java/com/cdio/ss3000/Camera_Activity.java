package com.cdio.ss3000;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Bundle;


public class Camera_Activity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera_);

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
      if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
        // this device has a camera
        return true;
      } else {
        // no camera on this device
        return false;
      }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
      Camera c = null;
      try {
        c = Camera.open(); // attempt to get a Camera instance
      }
      catch (Exception e){
        // Camera is not available (in use or does not exist)
      }
      return c; // returns null if camera is unavailable
    }
  }
}
