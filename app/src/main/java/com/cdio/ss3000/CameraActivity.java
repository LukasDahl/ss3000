package com.cdio.ss3000;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.cdio.ss3000.Vision.ComputerVision;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Most of this is based or copied from: https://www.youtube.com/watch?v=u4hfZXorDAQ
public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isCameraInitialized;
    private Camera mCamera = null;
    private static SurfaceHolder myHolder;
    private static CameraPreview mPreview;
    private FrameLayout preview;
    private static OrientationEventListener orientationEventListener = null;
    private static boolean fM;
    private Button capture;
    private FrameLayout frameLayout;
    public Bitmap[] bmArray = new Bitmap[10];
    private Bitmap bm = null;
    Uri imageUri;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private boolean firstTimeOpen = true;
    private static Camera.Parameters p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        frameLayout = (FrameLayout) findViewById(R.id.camera_preview);

        capture = findViewById(R.id.captureButt);
        capture.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!firstTimeOpen)
            ((TextView) findViewById(R.id.move_text)).setText("Please stand by");
        else
            firstTimeOpen = false;

        if (!isCameraInitialized) {
            mCamera = Camera.open();
            p = mCamera.getParameters();
            p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(p);
            mPreview = new CameraPreview(this, mCamera);
            preview = findViewById(R.id.camera_preview);
            preview.addView(mPreview);
            rotateCamera();
//      orientationEventListener = new OrientationEventListener(this) {
//        @Override
//        public void onOrientationChanged(int orientation) {
//          rotateCamera();
//        }
//      };
//      orientationEventListener.enable();
            preview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (whichCamera) {
                        if (fM) {
                            p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                        } else {
                            p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                        }
                        try {
                            mCamera.setParameters(p);
                        } catch (Exception e) {

                        }
                        fM = !fM;
                    }
                    return true;
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCamera.release();
        frameLayout = (FrameLayout) findViewById(R.id.camera_preview);
        frameLayout.removeAllViews();
    }

    @Override
    public void onClick(View v) {
        if (v == capture) {
            frameLayout = (FrameLayout) findViewById(R.id.camera_preview);
            View screen = getWindow().getDecorView().getRootView();

            for (int i = 0; i < 10; i++) {
//        screen.setDrawingCacheEnabled(true);
//        Bitmap screenBitmap = Bitmap.createBitmap(screen.getDrawingCache());
//        bmArray[i] = screenBitmap;
//        screen.setDrawingCacheEnabled(false);
                dispatchTakePictureIntent();

//        Bitmap screenBitmap = Bitmap.createBitmap(frameLayout.getWidth() , frameLayout.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(screenBitmap);
//        frameLayout.draw(c);

                int x = 0;
                break;
          /*Bitmap bitmap = createBitmapFromView(framelayout, 0, 0);
          bmArray[i] = bitmap;
          image.setImageBitmap(bitmap);
          image.bringToFront();*/
            }

            //image.setImageBitmap(bm);
            //System.out.println(bmArray[0]);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                //use imageUri here to access the image
                Bitmap picture = BitmapFactory.decodeFile(currentPhotoPath);
                ComputerVision.getInstance(this).runVision(picture);
                int x = 0;
            } else if (resultCode == RESULT_CANCELED) {
            } else {
            }
            new File(currentPhotoPath).delete();
        }

    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        takePictureIntent.putExtra("android.intent.extra.quickCapture", true);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private static int rotation;

    private static boolean whichCamera = true;



    private void rotateCamera() {
        if (mCamera != null) {
            rotation = this.getWindowManager().getDefaultDisplay().getRotation();
            if (rotation == 0) {
                rotation = 90;
            } else if (rotation == 1) {
                rotation = 0;
            } else if (rotation == 2) {
                rotation = 270;
            } else {
                rotation = 180;
            }
            mCamera.setDisplayOrientation(rotation);
            if (!whichCamera) {
                if (rotation == 90) {
                    rotation = 270;
                } else if (rotation == 270) {
                    rotation = 90;
                }
            }
            p = mCamera.getParameters();
            p.setRotation(rotation);
            mCamera.setParameters(p);
        }
    }

    public void gameOver(boolean won, int moves){

        if (won){
            Intent i = new Intent(this, WonGameActivity.class);
            startActivity(i);
        }
        else{
            Intent i = new Intent(this, LostGameActivity.class);
            startActivity(i);
        }

    }


    private static class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private static SurfaceHolder mHolder;
        private static Camera mCamera;

        private CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            myHolder = holder;
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {

        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        }
    }
}
