package com.cdio.ss3000;

import androidx.appcompat.app.AppCompatActivity;

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
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won_game);

        enterName = findViewById(R.id.enterNameET);
        enterName.setHint("Your Name:");

        save = findViewById(R.id.saveBtn);
        save.setOnClickListener(this);

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
                //Score person = new Score(enterName.getText().toString(),udregnScore());
                //Logik.highScoreList.add(person);

               // gemData();

                //Gør sådan så knappen kun kan trykkes på én gang. Da brugeren ellers ville
                // kunne gemme samme score flere gange
                save.setEnabled(false);

                //gemmer keyboardet når gem knap trykkes
                enterName.onEditorAction(EditorInfo.IME_ACTION_DONE);
                enterName.setEnabled(false);


                Toast.makeText(this, "Highscore saved!", Toast.LENGTH_SHORT).show();

            }
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