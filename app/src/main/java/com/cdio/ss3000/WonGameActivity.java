package com.cdio.ss3000;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class WonGameActivity extends AppCompatActivity implements View.OnClickListener {
    EditText enterName;
    TextView finalScore;
    Button save, mainMenu, newGame;

    String currentName;
    int currentScore;

    String[] hsNameList = new String[10];
    int[] hsScoreList = new int[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won_game);

        enterName = findViewById(R.id.enterNameET);
        enterName.setHint("Your Name:");

        finalScore = findViewById(R.id.finalScore);

        save = findViewById(R.id.saveBtn);
        save.setOnClickListener(this);

        mainMenu = findViewById(R.id.mainMenuBtn);
        mainMenu.setOnClickListener(this);

        newGame = findViewById(R.id.newGameBtn);
        newGame.setOnClickListener(this);

        finalScore.setText(String.format("%d", getIntent().getExtras().getInt("moves")));


    }

    @Override
    public void onClick(View v) {
        if(v==save){
            //Hvis brugeren prøver at gemme uden at have indtastet et navn, så vises der
            // fejlbesked i Toast og en animation af Edittext
            if(!(enterName.getText().toString().trim().length() > 0)){
                enterName.startAnimation(shakeError());
                Toast.makeText(this, "Enter your name to save!", Toast.LENGTH_SHORT).show();
                return;
            }else{
              //Gem data her

                save.setEnabled(false);

                currentName = enterName.getText().toString();
                currentScore = getIntent().getExtras().getInt("moves");

                ArrayList<Highscore> scores;
                SharedPreferences sharedPreferences;
                Gson gson = new Gson();

                sharedPreferences = getSharedPreferences("scores", MODE_PRIVATE);

                String json = sharedPreferences.getString("scores", "");
                Type listOfScoresType = new TypeToken<ArrayList<Highscore>>(){}.getType();
                scores = gson.fromJson(json, listOfScoresType);
                scores.add(new Highscore(currentName, currentScore));

                json = gson.toJson(scores);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("scores", json);
                editor.commit();

                //gemmer keyboardet når gem knap trykkes
                enterName.onEditorAction(EditorInfo.IME_ACTION_DONE);
                enterName.setEnabled(false);

                Toast.makeText(this, "Highscore saved!", Toast.LENGTH_SHORT).show();

            }
        }else if(v == mainMenu){
            Intent i = new Intent(this, MainActivity.class);
            this.startActivity(i);

        }else if(v == newGame){
            Intent i = new Intent(this, CameraActivity.class);
            this.startActivity(i);
        }
    }


    //Ryster med edittext feltet når brugeren prøver at gemme og der intet navn er skrevet i
// feltet. Haps fra respositpory https://github.com/kajgaard/GalgeSpil
    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }
}