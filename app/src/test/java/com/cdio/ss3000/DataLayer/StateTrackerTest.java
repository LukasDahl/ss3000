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
        ArrayList<Card> cardList2 = new ArrayList<>();
        card.setMovable(false);
        cardList2.add(card);
        cardList2.add(new Card(4, Suit.CLUBS, true));
        newState.tableau[1] = cardList2;
        State stateUpdated = new State(newState.foundations, newState.tableau, newState.stock, newState.waste);
        stateTracker.updateState(stateUpdated);
        assertEquals(stateUpdated.tableau[1], newState.tableau[1]);//Testing for the card to be added
        stateUpdated.tableau[1] = cardList;
        stateTracker.updateState(stateUpdated);
        assertEquals(stateUpdated.tableau[1].get(1), card);//Testing for the card to be removed

    }

    @Test
    public void foundationTest(){
        StateTracker stateTracker = new StateTracker();
        ArrayList<Card>[] foundation = new ArrayList[4];
        //Add two cards to the hearts foundation
        ArrayList<Card> foundation1 = new ArrayList<>();
        Card f1 = new Card(1, Suit.HEARTS, false);
        Card f2 = new Card(2, Suit.HEARTS, true);
        foundation1.add(f1);
        foundation1.add(f2);
        foundation[0] = foundation1;
        //Show change

//        stateTracker.showTopCard();
    }

    @Test
    public void fullStateTest(){
        //TODO: Try with a full on board with a move.
        //Create a full board state
    }
}