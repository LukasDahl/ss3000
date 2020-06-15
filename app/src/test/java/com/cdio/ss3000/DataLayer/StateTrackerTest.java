package com.cdio.ss3000.DataLayer;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class StateTrackerTest {

    @Test
    public void showTopCardTest() {//Tests the print function of the showTopCard method usually just used for debugging tests
        System.out.println("showTopCardTest: Started");
        StateTracker stateTracker = new StateTracker();
        stateTracker.showTopCard();
        System.out.println("showTopCardTest: OK");
    }

    @Test
    public void updateStateTest(){
        System.out.println("updateStateTest: Started");
        StateTracker stateTracker = new StateTracker();
        //stateTracker.showTopCard();
        //---- Create required fields
        ArrayList<Card>[] foundation = new ArrayList[4];
        ArrayList<Card>[] tableau = new ArrayList[7];
        ArrayList<Card> stock = new ArrayList<>();
        ArrayList<Card> waste = new ArrayList<>();

        //Create list of cards to be added (Heart 5)
        ArrayList<Card> cardList = new ArrayList<>();
        Card card = new Card(5, Suit.HEARTS, true);
        cardList.add(card);
        cardList.add(card);
        //Add heart of 5 to end of all columns
        for(int i = 0; i < 7; i++){
            tableau[i] = cardList;
        }
        //Create new state from fields
        State newState = new State(foundation, tableau, stock, waste);
        stateTracker.updateState(newState);//Update the state

        ArrayList<Card> cardList2 = new ArrayList<>();//Create second cardlist to be added on top of previous cards
        //Set previous top card to no longer be movable
        card.setMovable(false);
        //Add card to inputState to simulate being the face-up card furthest "back"
        cardList2.add(card);
        //Add new card to be the next front face-up card
        cardList2.add(new Card(4, Suit.CLUBS, true));
        //Save the card list to the tableau slot 1
        newState.tableau[1] = cardList2;
        //Create new state from new set of tableau fields
        State stateUpdated = new State(newState.foundations, newState.tableau, newState.stock, newState.waste);
        //Update state
        stateTracker.updateState(stateUpdated);
        assertEquals(stateUpdated.tableau[1], newState.tableau[1]);//Testing for the card to be added
        stateUpdated.tableau[1] = cardList;//Change the previously updated tableau slot to the former (cardlist)
        stateTracker.updateState(stateUpdated);//Update state again
        assertEquals(stateUpdated.tableau[1].get(1), card);//Testing for the card to be removed
        System.out.println("updateStateTest: OK");
    }

    @Test
    public void foundationTest(){
        System.out.println("foundationTest: Started");
        StateTracker stateTracker = new StateTracker();
        ArrayList<Card>[] foundation = new ArrayList[4];
        //Double aces for the highest/lowest card of the tableau
        Card ace_heart = new Card(1, Suit.HEARTS, true);
        ArrayList<Card> cardlist = new ArrayList<>();
        cardlist.add(ace_heart);
        cardlist.add(ace_heart);
        //Add cards to the foundation
        ArrayList<Card> addToFoundation1 = new ArrayList<>();
        ArrayList<Card> addToFoundation2 = new ArrayList<>();
        ArrayList<Card> addToFoundation3 = new ArrayList<>();
        ArrayList<Card> addToFoundation4 = new ArrayList<>();
        addToFoundation1.add(new Card(1, Suit.HEARTS, true));
        addToFoundation2.add(new Card(3, Suit.DIAMONDS, true));
        addToFoundation3.add(new Card(5, Suit.SPADES, true));
        addToFoundation4.add(new Card(7, Suit.CLUBS, true));

        foundation[0] = addToFoundation1;
        foundation[1] = addToFoundation2;
        foundation[2] = addToFoundation3;
        foundation[3] = addToFoundation4;
        //Tableau
        ArrayList<Card>[] tableau = new ArrayList[7];
        for(int i = 0; i < 7; i++){
            tableau[i] = cardlist;
        }
        //Show change
        State nextState = new State(foundation, tableau, null, null);
        stateTracker.updateState(nextState);
        stateTracker.showTopCard();
        System.out.println("foundationTest: OK");
    }

    @Test
    public void fullStateTest(){
        //TODO: Try with a full on board with a move.
        //Create a full board state
    }
}