package com.cdio.ss3000;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WonGameActivity extends AppCompatActivity implements View.OnClickListener {
    EditText enterName;
    Button save, mainMenu, newGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won_game);

        enterName = findViewById(R.id.enterNameET);
        enterName.setHint("Your Name:");

        save = findViewById(R.id.saveBtn);
        save.setOnClickListener(this);

        mainMenu = findViewById(R.id.mainMenuBtn);
        mainMenu.setOnClickListener(this);

        newGame = findViewById(R.id.newGameBtn);
        newGame.setOnClickListener(this);

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