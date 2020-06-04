package com.cdio.ss3000.DataLayer;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class State {
    public LinkedList<Card>[] foundations = new LinkedList[4], tableau = new LinkedList[7];
    public LinkedList<Card> stock, waste;

    public State(LinkedList<Card>[] foundations, LinkedList<Card>[] tableau, LinkedList<Card> stock, LinkedList<Card> waste) {
        this.foundations = foundations;
        this.tableau = tableau;
        this.stock = stock;
        this.waste = waste;
    }

    public LinkedList<Card> getMovableCardsTableau(){
            LinkedList<Card> movableCardsTableau = null;

        for(LinkedList<Card> tab : tableau){
            movableCardsTableau.add(tab.peek());
        }

       return movableCardsTableau;
    }

    public Card getMovableCardWaste(){
        return waste.peek();
    }

    public LinkedList<Card> getMovableCardsFoundations(){
        LinkedList<Card> movableCardsFoundations = null;

        for(LinkedList<Card> found : foundations){
            movableCardsFoundations.add(found.peek());
        }

        return movableCardsFoundations;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        LinkedList<Card>[] foundations = new LinkedList[4];
        foundations[0] = new LinkedList<>();
        foundations[1] = new LinkedList<>();
        foundations[2] = new LinkedList<>();
        foundations[3] = new LinkedList<>();
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

        LinkedList<Card>[] tableau = new LinkedList[7];
        tableau[0] = new LinkedList<>();
        tableau[1] = new LinkedList<>();
        tableau[2] = new LinkedList<>();
        tableau[3] = new LinkedList<>();
        tableau[4] = new LinkedList<>();
        tableau[5] = new LinkedList<>();
        tableau[6] = new LinkedList<>();
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

        LinkedList<Card> stock = new LinkedList<>();
        for (Card card: this.stock){
            stock.add((Card)card.clone());
        }

        LinkedList<Card> waste = new LinkedList<>();
        for (Card card: this.waste){
            waste.add((Card)card.clone());
        }

        State newState = new State(foundations, tableau, stock, waste);
        return newState;
    }
}
