package com.cdio.ss3000.DataLayer;

import android.widget.ArrayAdapter;

import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @Author Flotfyr27 - https://github.com/Flotfyr27
 */
public class PointCalculatorTest {
    PointCalculator PC = new PointCalculator();
    @Test
    public void CardIsAceTest(){
        System.out.println("\n");
        System.out.println("TEST: CardIsAceTest");
        //Build our hand of an ace of spades
        Card card = new Card(1, Suit.SPADES, true);
        //Setup column of cards
        ArrayList<Card> column = new ArrayList<>();
        column.add(new Card());
        column.add(card);
        //Create an empty move list as aces always go straight to foundation
        ArrayList<Card> moves = new ArrayList<>();
        card.addMove(moves);
        //Calculate best move
        Card returnCard = PC.getBestMove(column, card);
        //Return card should be the ace it self
        System.out.println("Expected result: " + card.toString());
        System.out.println("Actual result:   " + returnCard.toString());
        assertEquals(card.getValue(), returnCard.getValue());
        assertEquals(card.getSuit(), returnCard.getSuit());
        assertEquals(100, returnCard.getPoints());

    }

    @Test
    public void CardIsInTableau(){
        System.out.println("\n");
        System.out.println("TEST: CardIsInTableau");
        //Setting up our hand
        Card card = new Card(3, Suit.SPADES, true); //our card
        //Setup column of cards
        ArrayList<Card> column = new ArrayList<>();
        column.add(new Card());
        column.add(card);
        //Setup move
        ArrayList<Card> moves = new ArrayList<>();
        Card moveToCard = new Card(4, Suit.HEARTS, true);//Card to move our card to
        moves.add(moveToCard);
        card.addMove(moves);//Adding possible moves to our card
        Card returnCard = PC.getBestMove(column, card);//Calculate the best move receive that card as return
        //test if the 4 of hearts is calculated to be the best move
        System.out.println("Expected result: " + moveToCard.toString());
        System.out.println("Actual result:   " + returnCard.getMoves().get(0).get(0).toString());
        assertEquals(moveToCard.getValue(), ((Card)returnCard.getMoves().get(0).get(0)).getValue());
        assertEquals(moveToCard.getSuit(), ((Card)returnCard.getMoves().get(0).get(0)).getSuit());
        assertEquals(4, ((Card)returnCard.getMoves().get(0).get(0)).getPoints());
    }

    @Test
    public void CardIsKingToEmptySpaceTableau(){
        System.out.println("\n");
        System.out.println("TEST: CardIsKingToEmptySpaceTableau");
        //Setup of hand
        Card card = new Card(13, Suit.HEARTS, true); //King of hearts
        //Prepare possible moves
        ArrayList<Card> moves = new ArrayList<>();
        card.addMove(moves);//Add empty move
        //Setup column of cards
        ArrayList<Card> column = new ArrayList<>();
        column.add(new Card());
        column.add(card);
        //Get best move
        Card returnCard = PC.getBestMove(column, card);
        //Test to see if the returned card is a king of hearts
        System.out.println("Expected result: " + card.toString());
        System.out.println("Actual result:   " + returnCard.toString());
        assertEquals(card.getValue(), returnCard.getValue());
        assertEquals(card.getSuit(), returnCard.getSuit());
        assertEquals(5, returnCard.getPoints());
    }

    @Test
    public void CardIsKingToFoundationWithEmptySlotTableau(){
        System.out.println("\n");
        System.out.println("TEST: CardIsKingToFoundationWithEmptySlotTableau");
        //Setup of hand
        Card king = new Card(13, Suit.HEARTS, true);
        //prepare possible moves
        ArrayList<Card> foundation = new ArrayList<>();
        Card queen = new Card(12, Suit.HEARTS, true);
        queen.setPlacedInFoundation(true);
        foundation.add(queen);
        ArrayList<Card> tableauSpace = new ArrayList<>();
        //Add moves to king
        king.addMove(foundation);
        king.addMove(tableauSpace);
        //Setup column of cards
        ArrayList<Card> column = new ArrayList<>();
        column.add(new Card());
        column.add(king);
        //Check the move
        Card bestmove = PC.getBestMove(column, king);
        //Best move should be to move to the queen in the foundation
        System.out.println("Expected result: " + queen.toString());
        System.out.println("Actual result:   " + ((Card)bestmove.getMoves().get(0).get(0)).toString());
        //Check to see if we have received the correct card
        assertEquals(queen.getValue(), ((Card)bestmove.getMoves().get(0).get(0)).getValue());
        assertEquals(queen.getSuit(), ((Card)bestmove.getMoves().get(0).get(0)).getSuit());
        assertEquals(12, ((Card)bestmove.getMoves().get(0).get(0)).getPoints());

    }

    @Test
    public void BaseLinePointTest(){
        System.out.println("\n");
        System.out.println("TEST: BaseLinePointTest");
        //Setup hand
        Card card1 = new Card(6, Suit.HEARTS, true);
        ArrayList<Card> deck = new ArrayList<>();
        //If deck only has a single card
        deck.add(card1);
        int bonus = PC.addBaseLinePoints(deck, card1);//Calculate bonus if card1 is the only card in the stack
        System.out.println("Expected (Actual): 0 (" + bonus + ")");
        assertEquals(0, bonus);//test -> should be 0

        //Remove single card from deck
        deck.clear();


        //Fill column (deck) with cards
        for(int i = 0; i < 7; i++){
            deck.add(new Card());
        }
        //Add hand to deck
        deck.add(card1);
        //Check value of bonus points
        bonus = PC.addBaseLinePoints(deck, card1);
        System.out.println("Expected (Actual): 14 (" + bonus + ")");
        assertEquals(14, bonus);//test it

        //Add a new card to the deck (so two face up cards exist)
        Card card2 = new Card(5, Suit.CLUBS, true);
        deck.add(card2);
        bonus = PC.addBaseLinePoints(deck, card2);//Calc bonus points with the top face-up card
        System.out.println("Expected (Actual): 0 (" + bonus + ")");
        assertEquals(0, bonus);

        //Add a new card to the deck (so three face up cards exist, one of which is an illegal move)
        Card card3 = new Card(4, Suit.CLUBS, true);
        deck.add(card3);//This is an illegal move
        bonus = PC.addBaseLinePoints(deck, card3);//Calc bonus points with the top face-up card
        System.out.println("Expected (Actual): 0 (" + bonus + ")");
        assertEquals(0, bonus);

    }
    ArrayList<Card>[] foundations = new ArrayList[4], tableau = new ArrayList[7];

    ArrayList<Card> waste = new ArrayList<>(), stock= new ArrayList<>();
    State state = new State(foundations, tableau, stock, waste);
    GameControl gameControl;
    Card hearts2 = new Card(2, Suit.HEARTS, true);
    Card hearts3 = new Card(3, Suit.HEARTS, true);
    Card clubs4 = new Card(4, Suit.CLUBS, true);
    Card spade13 = new Card(13, Suit.SPADES, true);
    @Test
    public void restOfTableauTest() {
        for (int i = 0; i < foundations.length; i++) {
            foundations[i] = new ArrayList<>();
        }
        for (int i = 0; i < tableau.length; i++) {
            tableau[i] = new ArrayList<>();
            tableau[i].add(new Card());
        }
        state.tableau[2].add(hearts3);
        state.tableau[4].add(clubs4);
        state.foundations[1].add(hearts2);
        gameControl = new GameControl(state);
        assertEquals(5, PC.restOfTableau(hearts3,state));
    }
}