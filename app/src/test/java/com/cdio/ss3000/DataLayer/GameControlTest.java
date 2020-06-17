package com.cdio.ss3000.DataLayer;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameControlTest {
    ArrayList<Card>[] foundations = new ArrayList[4], tableau = new ArrayList[7];

    ArrayList<Card> waste = new ArrayList<>(), stock= new ArrayList<>();
    State state = new State(foundations, tableau, stock, waste);
    GameControl gameControl;
    Card hearts2 = new Card(2, Suit.HEARTS, true);
    Card hearts3 = new Card(3, Suit.HEARTS, true);
    Card clubs4 = new Card(4, Suit.CLUBS, true);
    Card spade13 = new Card(13, Suit.SPADES, true);
    @Test
    public void checkPossibleMovesTest() {
        for (int i = 0; i < foundations.length; i++) {
            foundations[i] = new ArrayList<>();
        }
        for (int i = 0; i < tableau.length; i++) {
            tableau[i] = new ArrayList<>();
        }
        state.tableau[2].add(hearts3);
        state.tableau[4].add(clubs4);
        state.foundations[1].add(hearts2);
        gameControl = new GameControl(state);
        gameControl.checkPossibleMoves();
        assertEquals(true, hearts3.equals(state.tableau[2].get(state.tableau[2].size()-1)));
        //assertEquals(2,hearts3.getMoves().size());
        assertEquals(2, state.tableau[2].get(state.tableau[2].size()-1).getMoves().size());
    }
    @Test
    public void moveToTableauPossibleTest(){
        gameControl = new GameControl(state);
        assertEquals(true, gameControl.moveToTableauPossible(hearts3, clubs4));
        assertEquals(false, gameControl.moveToTableauPossible(hearts3,hearts2));
    }

    @Test
    public void moveToFoundationPossibleTest(){
        gameControl = new GameControl(state);
        assertEquals(false, gameControl.moveToFoundationPossible(hearts3, clubs4));
        assertEquals(true, gameControl.moveToFoundationPossible(hearts3,hearts2));

    }

    @Test
    public void testRun(){
        for (int i = 0; i < foundations.length; i++) {
            foundations[i] = new ArrayList<>();
        }
        for (int i = 0; i < tableau.length; i++) {
            tableau[i] = new ArrayList<>();
        }
        state.tableau[2].add(hearts3);
        state.tableau[4].add(clubs4);
        state.foundations[1].add(hearts2);
        gameControl = new GameControl(state);
        assertEquals(hearts3, gameControl.run());
    }

    @Test
    public void kingHasMoveTest(){
        for (int i = 0; i < state.tableau.length; i++) {
            state.tableau[i] = new ArrayList<>();
            }
        for (int i = 0; i < state.foundations.length; i++) {
            state.foundations[i] = new ArrayList<>();
        }
        state.tableau[2].add(spade13);
        gameControl = new GameControl(state);
        gameControl.checkPossibleMoves();
        assertEquals(6, state.tableau[2].get(state.tableau[2].size()-1).getMoves().size());
    }

    @Test
    public void value14Test(){
        for (int i = 0; i < tableau.length; i++) {
            tableau[i] = new ArrayList<>();
        }
        for (int i = 0; i < foundations.length; i++) {
            foundations[i] = new ArrayList<>();
        }
        Card hearts14 = new Card(14,Suit.HEARTS, true);
        state.tableau[2].add(hearts14);
        state.tableau[5].add(spade13);
        gameControl = new GameControl(state);
        gameControl.checkPossibleMoves();
        assertEquals(1, state.tableau[2].get(state.tableau[2].size()-1).getMoves().size());

    }

    @Test
    public void allUnknownTest(){
        for (int i = 0; i < foundations.length; i++) {
            foundations[i] = new ArrayList<>();
            foundations[i].add(new Card());
        }
        for (int i = 0; i < tableau.length; i++) {
            tableau[i] = new ArrayList<>();
            tableau[i].add(new Card());
        }
        gameControl = new GameControl(state);
        gameControl.checkPossibleMoves();
        assertEquals(0, state.tableau[0].get(state.tableau[0].size()-1).getMoves().size());
        assertEquals(0, state.tableau[1].get(state.tableau[1].size()-1).getMoves().size());
        assertEquals(0, state.tableau[2].get(state.tableau[2].size()-1).getMoves().size());
        assertEquals(0, state.tableau[3].get(state.tableau[3].size()-1).getMoves().size());
        assertEquals(0, state.tableau[4].get(state.tableau[4].size()-1).getMoves().size());
        assertEquals(0, state.tableau[5].get(state.tableau[5].size()-1).getMoves().size());
        assertEquals(0, state.tableau[6].get(state.tableau[6].size()-1).getMoves().size());
    }

}