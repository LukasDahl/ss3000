package com.cdio.ss3000.DataLayer;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PointCalculatorTest {
    PointCalculator PC = new PointCalculator();
    @Test
    public void CardIsAceTest(){
        System.out.println("TEST: CardIsAceTest");
        //Build our hand of an ace of spades
        Card card = new Card(1, Suit.SPADES, true);
        //Create an empty move list as aces always go straight to foundation
        ArrayList<Card> moves = new ArrayList<>();
        card.addMove(moves);
        //Calculate best move
        Card returnCard = PC.getBestMove(card);
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
        ArrayList<Card> moves = new ArrayList<>();
        Card moveToCard = new Card(4, Suit.HEARTS, true);//Card to move our card to
        moves.add(moveToCard);
        card.addMove(moves);//Adding possible moves to our card
        Card returnCard = PC.getBestMove(card);//Calculate the best move receive that card as return
        //test if the 4 of hearts is calculated to be the best move
        System.out.println("Expected result: " + moveToCard.toString());
        System.out.println("Actual result:   " + returnCard.getMoves().get(0).get(0).toString());
        assertEquals(moveToCard.getValue(), ((Card)returnCard.getMoves().get(0).get(0)).getValue());
        assertEquals(moveToCard.getSuit(), ((Card)returnCard.getMoves().get(0).get(0)).getSuit());
        assertEquals(2, ((Card)returnCard.getMoves().get(0).get(0)).getPoints());
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
        //Get best move
        Card returnCard = PC.getBestMove(card);
        //Test to see if the returned card is a king of hearts
        System.out.println("Expected result: " + card.toString());
        System.out.println("Actual result:   " + returnCard.toString());
        assertEquals(card.getValue(), returnCard.getValue());
        assertEquals(card.getSuit(), returnCard.getSuit());
        assertEquals(3, returnCard.getPoints());
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
        //Check the move
        Card bestmove = PC.getBestMove(king);
        //Best move should be to move to the queen in the foundation
        System.out.println("Expected result: " + queen.toString());
        System.out.println("Actual result:   " + ((Card)bestmove.getMoves().get(0).get(0)).toString());
        //Check to see if we have received the correct card
        assertEquals(queen.getValue(), ((Card)bestmove.getMoves().get(0).get(0)).getValue());
        assertEquals(queen.getSuit(), ((Card)bestmove.getMoves().get(0).get(0)).getSuit());
        assertEquals(10, ((Card)bestmove.getMoves().get(0).get(0)).getPoints());

    }
}