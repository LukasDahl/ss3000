package com.cdio.ss3000.DataLayer;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class State {
    public ArrayList<Card>[] foundations = new ArrayList[4], tableau = new ArrayList[7];

    public ArrayList<Card> stock, waste;

    public State(ArrayList<Card>[] foundations, ArrayList<Card>[] tableau, ArrayList<Card> stock, ArrayList<Card> waste) {
        this.foundations = foundations;
        this.tableau = tableau;
        this.stock = stock;
        this.waste = waste;
    }

    public LinkedList<Card> getMovableCardsTableau(){
            LinkedList<Card> movableCardsTableau = null;

        for(ArrayList<Card> tab : tableau){
            movableCardsTableau.add(tab.get(tab.size()-1));
        }

       return movableCardsTableau;
    }

    public Card getMovableCardWaste(){
        return waste.get(waste.size()-1);
    }

    public LinkedList<Card> getMovableCardsFoundations(){
        LinkedList<Card> movableCardsFoundations = null;

        for(ArrayList<Card> found : foundations){
            movableCardsFoundations.add(found.get(found.size()-1));
        }

        return movableCardsFoundations;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        ArrayList<Card>[] foundations = new ArrayList[4];
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

        ArrayList<Card>[] tableau = new ArrayList[7];
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

        ArrayList<Card> stock = new ArrayList<>();
        for (Card card: this.stock){
            stock.add((Card)card.clone());
        }

       ArrayList<Card> waste = new ArrayList<>();
        for (Card card: this.waste){
            waste.add((Card)card.clone());
        }

        State newState = new State(foundations, tableau, stock, waste);
        return newState;
    }
}
