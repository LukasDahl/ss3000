package com.cdio.ss3000.DataLayer;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PointCalculatorTest {
    PointCalculator PC = new PointCalculator();
    @Test
    public void CardIsAceTest(){
        //Build our hand of an ace of spades
        Card card = new Card(1, Suit.SPADES, true);
        //Create an empty move list as aces always go straight to foundation
        ArrayList<Card> moves = new ArrayList<>();
        card.addMove(moves);
        //Calculate best move
        Card returnCard = PC.getBestMove(card);
        //Return card should be the ace it self
        assertEquals(card.getValue(), returnCard.getValue());
        assertEquals(card.getSuit(), returnCard.getSuit());
        assertEquals(100, returnCard.getPoints());

    }

    @Test
    public void CardIsInTableau(){
        //Setting up our hand
        Card card = new Card(3, Suit.SPADES, true); //our card
        ArrayList<Card> moves = new ArrayList<>();
        Card moveToCard = new Card(4, Suit.HEARTS, true);//Card to move our card to
        moves.add(moveToCard);
        card.addMove(moves);//Adding possible moves to our card
        Card returnCard = PC.getBestMove(card);//Calculate the best move receive that card as return
        //test if the 4 of hearts is calculated to be the best move
        assertEquals(moveToCard.getValue(), ((Card)returnCard.getMoves().get(0).get(0)).getValue());
        assertEquals(moveToCard.getSuit(), ((Card)returnCard.getMoves().get(0).get(0)).getSuit());
        assertEquals(2, ((Card)returnCard.getMoves().get(0).get(0)).getPoints());
    }

    @Test
    public void CardIsKingToEmptySpaceTableau(){
        //Setup of hand
        Card card = new Card(13, Suit.HEARTS, true); //King of hearts
        //Prepare possible moves
        ArrayList<Card> moves = new ArrayList<>();
        card.addMove(moves);//Add empty move
        //Get best move
        Card returnCard = PC.getBestMove(card);
        //Test to see if the returned card is a king of hearts
        System.out.println("Card value: " + card.getValue() + "\t\t Returned value: " + returnCard.getValue());
        assertEquals(card.getValue(), returnCard.getValue());
        System.out.println("Card suit: " + card.getSuit().name() + "\t Returned suit: " + returnCard.getSuit().name());
        assertEquals(card.getSuit(), returnCard.getSuit());
        System.out.println("Card points: " + 3 + "\t\t Returned points: " + returnCard.getPoints());
        assertEquals(3, returnCard.getPoints());
    }

}