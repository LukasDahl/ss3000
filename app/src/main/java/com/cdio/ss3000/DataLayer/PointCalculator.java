package com.cdio.ss3000.DataLayer;

import java.util.ArrayList;

/**
 * @Author Flotfyr27 - https://github.com/Flotfyr27
 */
public class PointCalculator {
    private final int ACE_POINTS = 100;
    private final int TABLEAU_EMPTY_POINTS = 3;
    private final int TABLEAU_POINTS = 2;
    private final int FOUNDATION_POINTS = 10;
    private final int ACE = 1;
    private final int KING = 13;
    private final int BASELINE = 2;

    private Card checkMoves(Card card){
        int temp_points = 0;
        //If card is an ace
        Card ace = checkAce(card);
        if(ace != null){
            return ace;
        }
        //Do all other moves
        Card newCard = checkCards(card);
        //Remove all previous moves from the supplied card
        card.clearMoves();
        //Save the new card (best move) to the list of moves (as the only one)
        ArrayList<Card> moves = new ArrayList<>();
        moves.add(newCard);
        //Add the list of moves to the supplied card
        card.addMove(moves);
        //Return the supplied card with the single best move
        return card;
    }

    private Card checkCards(Card card){
        boolean hasFoundItem = false;
        for(ArrayList<Card> move : card.getMoves()){
            int temp_points = 0;
            hasFoundItem = false;
            //Empty spot in Tableau
            if(move.isEmpty() && card.getValue() == KING){
                hasFoundItem = true;
                temp_points += TABLEAU_EMPTY_POINTS;
                card.setPoints(temp_points);
               // return card;
            }else if(move.get(move.size()-1).getSuit() == card.getSuit()){//This will be a move to foundation
                temp_points += FOUNDATION_POINTS;
            }else if(move.get(move.size()-1).getValue() > 0 && move.get(move.size()-1).getValue() < 14){            //Any other card in the tableau
                temp_points += TABLEAU_POINTS;
                //Add points for each underlying face down card
            }
            if(!hasFoundItem) {
                move.get(move.size() - 1).setPoints(temp_points);
            }
        }
            int highestPoint = 0;
            Card newCard = new Card();
        for(ArrayList<Card> list : card.getMoves()){
            if(list.isEmpty()){
                if(highestPoint < card.getPoints()){
                    highestPoint = card.getPoints();
                    newCard = card;
                }
            }else{
                if(highestPoint < list.get(0).getPoints()){
                    highestPoint = list.get(0).getPoints();
                    newCard = list.get(0);
                }
            }
        }
            return newCard;
    }

    private Card checkAce(Card card){
        if(card.getValue() == ACE && !card.getPlacedInFoundation()){
            card.setPoints(ACE_POINTS);
            return card;
        }else{
            return null;
        }
    }
    /*
    TODO: Add points for the amount of face-down cards
     */

    int addBaseLinePoints(ArrayList<Card> column, Card card){
        if(column.size() == 1) return 0;
        //Save points and index of card
        int bl_points;
        int index = column.indexOf(card);
        //Create a sublist from the column
        ArrayList<Card> sublist = new ArrayList<>();
        for(int i = 0; i < index; i++){
            sublist.add(column.get(i));
        }
        //Determine points based on size of sublist
        if(column.get(index-1).getSuit() != Suit.UNKNOWN) return 0;
        bl_points = sublist.size()*BASELINE;
        return bl_points;
    }


    public Card getBestMove(Card card){
        return checkMoves(card);
    }
}
