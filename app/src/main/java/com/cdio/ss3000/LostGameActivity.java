package com.cdio.ss3000;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LostGameActivity extends AppCompatActivity implements View.OnClickListener {
Button mainMenu, newGame;
TextView scoreValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_game);

        mainMenu = findViewById(R.id.mainMenuBtn);
        mainMenu.setOnClickListener(this);

        newGame = findViewById(R.id.newGameBtn);
        newGame.setOnClickListener(this);

        scoreValues = findViewById(R.id.valuesTV);
        scoreValues.setText(String.format("%d", getIntent().getExtras().getInt("moves")));
    }

    @Override
    public void onClick(View v) {
        if(v==mainMenu){
            Intent i = new Intent(this, MainActivity.class);
            this.startActivity(i);
        }else if(v==newGame){
            Intent i = new Intent(this, CameraActivity.class);
            this.startActivity(i);
        }
    }
}