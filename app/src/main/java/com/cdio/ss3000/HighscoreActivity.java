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
    setContentView(R.layout.activity_highscore);

    recyclerView = findViewById(R.id.recyclerView);

    prefName = getSharedPreferences("highscoreName", Context.MODE_PRIVATE);
    prefScore = getSharedPreferences("highscoreScore", Context.MODE_PRIVATE);

    for (int i = 0; i < 10; i++){
      hsNameList[i] = prefName.getString(""+i, "Null");
      hsScoreList[i] = prefScore.getInt(""+i, 0);
    }

    SharedPreferences.Editor nameEditor = prefName.edit();
    SharedPreferences.Editor scoreEditor = prefScore.edit();

    nameEditor.putString("1", "Egon Olsen");
    nameEditor.putString("2", "Benny");
    nameEditor.putString("3", "Kaj");
    nameEditor.putString("4", "Andrea");
    nameEditor.commit();
    scoreEditor.putInt("1", 2);
    scoreEditor.putInt("2", 15);
    scoreEditor.putInt("3", 500);
    scoreEditor.putInt("4", 123);
    scoreEditor.commit();

    recyclerView.setAdapter(adapter);
    System.out.println("" + hsNameList[0]);
    System.out.println("" + hsNameList[1]);
    System.out.println("" + hsNameList[2]);
    System.out.println("" + hsNameList[3]);
    System.out.println("" + hsNameList[4]);
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
      hsName.setText(" " + hsNameList[position]);
      hsScore.setText(" " + hsScoreList[position]);
    }

    @Override
    public int getItemCount() {
      return hsNameList.length;
    }
  };
}
