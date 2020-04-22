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

public class HighscoreActivity extends AppCompatActivity {

  String[] hsNameList = new String[10];
  int[] hsScoreList = new int[10];

  SharedPreferences prefName, prefScore;

  RecyclerView recyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    recyclerView = findViewById(R.id.recyclerView);

    prefName = getSharedPreferences("highscoreName", Context.MODE_PRIVATE);
    prefScore = getSharedPreferences("highscoreScore", Context.MODE_PRIVATE);

    for (int i = 0; i < 10; i++){
      hsNameList[i] = prefName.getString(""+i, "Null");
      hsScoreList[i] = prefScore.getInt(""+i, 0);
    }

    SharedPreferences.Editor nameEditor = prefName.edit();
    SharedPreferences.Editor scoreEditor = prefScore.edit();

    recyclerView.setAdapter(adapter);

    setContentView(R.layout.activity_highscore);

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

      hsNumber.setText("" + position + ".");
      hsName.setText("" + hsNameList[position]);
      hsScore.setText("" + hsScoreList[position]);
    }

    @Override
    public int getItemCount() {
      return hsNameList.length;
    }
  };
}
