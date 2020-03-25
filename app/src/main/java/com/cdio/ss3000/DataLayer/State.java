package com.cdio.ss3000.DataLayer;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class State {
    List<Card>[] foundations = new List[4], tableau = new List[7];
    List<Card> stock, waste;

    public State(List<Card>[] foundations, List<Card>[] tableau, List<Card> stock, List<Card> waste) {
        this.foundations = foundations;
        this.tableau = tableau;
        this.stock = stock;
        this.waste = waste;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        List<Card>[] foundations = new List[4];
        foundations[0] = new ArrayList<>();
        foundations[1] = new ArrayList<>();
        foundations[2] = new ArrayList<>();
        foundations[3] = new ArrayList<>();
        for (Card card: this.foundations[0]){
            foundations[0].add((Card)card.clone());
        }
        for (Card card: this.foundations[1]){
            foundations[1].add((Card)card.clone());
        }
        for (Card card: this.foundations[2]){
            foundations[2].add((Card)card.clone());
        }
        for (Card card: this.foundations[3]){
            foundations[3].add((Card)card.clone());
        }

        List<Card>[] tableau = new List[7];
        tableau[0] = new ArrayList<>();
        tableau[1] = new ArrayList<>();
        tableau[2] = new ArrayList<>();
        tableau[3] = new ArrayList<>();
        tableau[4] = new ArrayList<>();
        tableau[5] = new ArrayList<>();
        tableau[6] = new ArrayList<>();
        for (Card card: this.tableau[0]){
            tableau[0].add((Card)card.clone());
        }
        for (Card card: this.tableau[1]){
            tableau[1].add((Card)card.clone());
        }
        for (Card card: this.tableau[2]){
            tableau[2].add((Card)card.clone());
        }
        for (Card card: this.tableau[3]){
            tableau[3].add((Card)card.clone());
        }
        for (Card card: this.tableau[4]){
            tableau[4].add((Card)card.clone());
        }
        for (Card card: this.tableau[5]){
            tableau[5].add((Card)card.clone());
        }
        for (Card card: this.tableau[6]){
            tableau[6].add((Card)card.clone());
        }

        List<Card> stock = new ArrayList<>();
        for (Card card: this.stock){
            stock.add((Card)card.clone());
        }

        List<Card> waste = new ArrayList<>();
        for (Card card: this.waste){
            waste.add((Card)card.clone());
        }

        State newState = new State(foundations, tableau, stock, waste);
        return newState;
    }
}
