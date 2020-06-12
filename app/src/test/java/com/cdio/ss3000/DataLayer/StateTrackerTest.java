package com.cdio.ss3000.DataLayer;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class StateTrackerTest {

    @Test
    public void showTopCardTest() {
        StateTracker stateTracker = new StateTracker();
        stateTracker.showTopCard();
    }

    @Test
    public void updateStateTest(){
        StateTracker stateTracker = new StateTracker();
        //stateTracker.showTopCard();
        ArrayList<Card>[] foundation = new ArrayList[4];
        ArrayList<Card>[] tableau = new ArrayList[7];
        ArrayList<Card> stock = new ArrayList<>();
        ArrayList<Card> waste = new ArrayList<>();

        ArrayList<Card> cardList = new ArrayList<>();
        Card card = new Card(5, Suit.HEARTS, true);
        cardList.add(card);
        cardList.add(card);
        //System.out.println(cardList);
        for(int i = 0; i < 7; i++){
            tableau[i] = cardList;
        }
        State newState = new State(foundation, tableau, stock, waste);
        stateTracker.updateState(newState);
        //stateTracker.showTopCard();
        //TODO: add actual tests that can fail/pass
        System.out.println(cardList.toString());
        System.out.println(newState.tableau[0].toString());
        ArrayList<Card> cardList2 = new ArrayList<>();
        card.setMovable(false);
        cardList2.add(card);
        cardList2.add(new Card(4, Suit.CLUBS, true));
        newState.tableau[1] = cardList2;
        State stateUpdated = new State(newState.foundations, newState.tableau, newState.stock, newState.waste);
        stateTracker.updateState(stateUpdated);
        stateTracker.showTopCard();
    }
}