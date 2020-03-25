package com.cdio.ss3000;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button start, help;
    TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = findViewById(R.id.startBtn);
        start.setOnClickListener(this);

        help.findViewById(R.id.helpBtn);
        help.setOnClickListener(this);

        header.findViewById(R.id.headerTv);



    }

    @Override
    public void onClick(View v) {

        if(v == start){
            Intent i = new Intent(this,CameraActivity.class);
            this.startActivity(i);
        }else if (v == help){
            //Intent i = new Intent(this,)
        }
    }
}


