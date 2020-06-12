package com.cdio.ss3000;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cdio.ss3000.Vision.ComputerVision;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button start, help, highscore;
    TextView header, group;
    ImageView king, play, questionMark, dtu;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        start = findViewById(R.id.startBtn);
        start.setOnClickListener(this);

        help = findViewById(R.id.helpBtnn);
        help.setOnClickListener(this);

        highscore = findViewById(R.id.highscoreBtn);
        highscore.setOnClickListener(this);

        header = findViewById(R.id.headerTv);
        group = findViewById(R.id.groupNameTv);

        king = findViewById(R.id.kingIv);
        play = findViewById(R.id.playIv);
        questionMark = findViewById(R.id.helpIv);
        dtu =findViewById(R.id.dtuLogoIv);




    }

    // Checks if app has camera permission, and asks for it, if it doesn't.
    @Override   //Gotten from: https://stackoverflow.com/questions/38552144/how-get-permission-for-camera-in-android-specifically-marshmallow
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {

        if(v == start){
            Intent i = new Intent(this, CameraActivity.class);
            this.startActivity(i);
        }else if (v == help){
            Intent i = new Intent(this, HelpActivity.class);
            this.startActivity(i);
        }
        else if (v == highscore){
            Intent i = new Intent(this, HighscoreActivity.class);
            this.startActivity(i);
        }
    }
}


