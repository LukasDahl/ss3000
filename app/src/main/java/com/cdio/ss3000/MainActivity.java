package com.cdio.ss3000;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button start, help;
    TextView header, group;
    ImageView king, play, questionMark, dtu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = findViewById(R.id.startBtn);
        start.setOnClickListener(this);

        help = findViewById(R.id.helpBtnn);
        help.setOnClickListener(this);

        header = findViewById(R.id.headerTv);
        group = findViewById(R.id.groupNameTv);

        king = findViewById(R.id.kingIv);
        play = findViewById(R.id.playIv);
        questionMark = findViewById(R.id.helpIv);
        dtu =findViewById(R.id.dtuLogoIv);




    }

    @Override
    public void onClick(View v) {

        if(v == start){
            Intent i = new Intent(this,Camera_Activity.class);
            this.startActivity(i);
        }else if (v == help){
            Intent i = new Intent(this, HelpActivity.class);
            this.startActivity(i);
        }
    }
}


