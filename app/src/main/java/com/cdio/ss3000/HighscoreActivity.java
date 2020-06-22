package com.cdio.ss3000;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HighscoreActivity extends AppCompatActivity {

  String[] hsNameList = new String[10];
  int[] hsScoreList = new int[10];
  ArrayList<Highscore> scores;
  private SharedPreferences sharedPreferences;
  Gson gson;


  SharedPreferences prefName, prefScore;

  RecyclerView recyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_highscore);
    gson = new Gson();
    recyclerView = findViewById(R.id.recyclerView);
    sharedPreferences = getSharedPreferences("scores", MODE_PRIVATE);
    prefName = getSharedPreferences("highscoreName", Context.MODE_PRIVATE);
    prefScore = getSharedPreferences("highscoreScore", Context.MODE_PRIVATE);

    String json = sharedPreferences.getString("scores", "");
    Type listOfScoresType = new TypeToken<ArrayList<Highscore>>(){}.getType();
    scores = gson.fromJson(json, listOfScoresType);
    recyclerView.setAdapter(adapter);
  }

  RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View itemView = getLayoutInflater().inflate(R.layout.highscore_element, parent, false);
      return new RecyclerView.ViewHolder(itemView){};
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      TextView hsNumber = holder.itemView.findViewById(R.id.hsNumber);
      TextView hsName = holder.itemView.findViewById(R.id.hsName);
      TextView hsScore = holder.itemView.findViewById(R.id.hsScore);

      hsNumber.setText("" + (position+1) + ".");
      hsName.setText(" " + scores.get(position).getName());
      hsScore.setText(" " + scores.get(position).getScore());
    }

    @Override
    public int getItemCount() {
      return scores.size();
    }
  };
}
