package com.cdio.ss3000;

public class Highscore implements Comparable {
  private String name;
  private int score;

  public Highscore(String name, int score) {
    this.name = name;
    this.score = score;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  @Override
  public int compareTo(Object o) {
    return 0;
  }
}
