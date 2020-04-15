package com.cdio.ss3000;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class HighscoreActivity extends AppCompatActivity {

  String[] hsNameList = new String[10];
  int[] hsScoreList = new int[10];

  SharedPreferences prefName, prefScore;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    prefName = getSharedPreferences("highscoreName", Context.MODE_PRIVATE);
    prefScore = getSharedPreferences("highscoreScore", Context.MODE_PRIVATE);

    for (int i = 0; i < 10; i++){
      hsNameList[i] = prefName.getString(""+i, "Null");
      hsScoreList[i] = prefScore.getInt(""+i, 0);
    }

    SharedPreferences.Editor nameEditor = prefName.edit();
    SharedPreferences.Editor scoreEditor = prefScore.edit();

    setContentView(R.layout.activity_highscore);
  }
}
