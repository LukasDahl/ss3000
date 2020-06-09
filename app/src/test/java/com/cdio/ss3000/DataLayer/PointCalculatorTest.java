package com.cdio.ss3000.DataLayer;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PointCalculatorTest {
    PointCalculator PC = new PointCalculator();
    @Test
    public void CardIsAceTest(){
        Card card = new Card(1, Suit.SPADES, true);
        ArrayList<Card> moves = new ArrayList<>();
        Card moveCard = new Card();
        card.addMove(moves);
        Card returnCard = PC.getBestMove(card);
        assertEquals(card.getValue(), returnCard.getValue());
        assertEquals(card.getSuit(), returnCard.getSuit());
        assertEquals(10, returnCard.getPoints());
    }

}