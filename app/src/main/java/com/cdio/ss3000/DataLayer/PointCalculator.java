package com.cdio.ss3000.DataLayer;

import java.util.ArrayList;

/**
 * @Author Flotfyr27 - https://github.com/Flotfyr27
 */
public class PointCalculator {
    private final int ACE_POINTS = 100;
    private final int TABLEAU_EMPTY_POINTS = 6;
    private final int TABLEAU_POINTS = 3;
    private final int TABLEAU_SPLIT_POINTS = 1;
    private final int FOUNDATION_POINTS = 10;
    private final int ACE = 1;
    private final int KING = 13;
    private final int BASELINE = 2;
    private final int ONELOWER = 1; //Hvis der er et kort der har værdi 1 lavere end det kort vi tjekker
    private final int MOVABLEPILE = 5; //Hvis et træk resulterer i at en bunke kan rykkes over, så der frigøres ukendte kort.


    private Card checkMovesWaste(Card card, ArrayList<Card> knownCards, State state) {
        ArrayList<Card> tempList = new ArrayList<>();
        tempList.add(card);

        Card bestCard = checkMoves(tempList, card);
        if (restOfTableau(card, state) != 0)
            bestCard.setPoints(bestCard.getPoints() + restOfTableau(card, state));
        else bestCard.setPoints(bestCard.getPoints() + lowerCardsInPile(card, knownCards));

        return bestCard;
    }

    private Card checkMoves(ArrayList<Card> column, Card card) {
        int temp_points = 0;
        //If card is an ace
        Card ace = checkAce(card);
        if (ace != null) {
            ace.clearMoves();
            return ace;
        }
        //Do all other moves
        Card newCard = checkCards(column, card);
        //Remove all previous moves from the supplied card
        card.clearMoves();
        //Save the new card (best move) to the list of moves (as the only one)
        ArrayList<Card> moves = new ArrayList<>();
        moves.add(newCard);
        //Add the list of moves to the supplied card
        card.addMove(moves);


        //points that are assigned to the card in moves, is copied to the moving card
        if (card.getPoints() == 0) {
            card.setPoints(moves.get(0).getPoints());
        }


        //Return the supplied card with the single best move
        return card;
    }

    private Card    checkCards(ArrayList<Card> column, Card card) {
        boolean hasFoundItem = false;
        int bonus = addBaseLinePoints(column, card);//Add the baseline points
        for (ArrayList<Card> move : card.getMoves()) {
            int temp_points = bonus;
            hasFoundItem = false;
            //Empty spot in Tableau
            if (move.isEmpty() && card.getValue() == KING) {
                hasFoundItem = true;
                temp_points += TABLEAU_EMPTY_POINTS;
                card.setPoints(temp_points);
                // return card;
            } else if (move.get(move.size() - 1).getSuit() == card.getSuit()) {//This will be a move to foundation
                temp_points += FOUNDATION_POINTS;
            } else if (move.get(move.size() - 1).getValue() > 0 && move.get(move.size() - 1).getValue() < 14) {//Any other card in the tableau
                if(column.get(column.size()-1).equals(card)){
                    temp_points += TABLEAU_POINTS;
                }
                //TODO: now there is never enough points to ever suggest splitting. Give points to splitting piles in special cases
                else temp_points += TABLEAU_SPLIT_POINTS;

                //Add points for each underlying face down card
            }


            if (!hasFoundItem) {
                move.get(move.size() - 1).setPoints(temp_points);
            }
        }
        int highestPoint = 0;
        Card newCard = new Card();
        for (ArrayList<Card> list : card.getMoves()) {
            if (list.isEmpty()) {
                if (highestPoint < card.getPoints()) {
                    highestPoint = card.getPoints();
                    newCard = card;
                }
            } else {
                if (highestPoint < list.get(list.size()-1).getPoints()) {
                    highestPoint = list.get(list.size()-1).getPoints();
                    newCard = list.get(list.size()-1);
                }
            }
        }
        return newCard;
    }

    private Card checkAce(Card card) {
        if (card.getValue() == ACE && !card.getPlacedInFoundation()) {
            card.setPoints(ACE_POINTS);
            return card;
        } else {
            return null;
        }
    }

    int addBaseLinePoints(ArrayList<Card> column, Card card) {
        if (column.size() == 1 || column.get(column.indexOf(card)-1).getSuit() != Suit.UNKNOWN) return 0;
        //Save points and index of card
        int bl_points;
        int index = column.indexOf(card);
        //Create a sublist from the column
        ArrayList<Card> sublist = new ArrayList<>();
        for (int i = 0; i < index; i++) {
            sublist.add(column.get(i));
        }
        //Determine points based on size of sublist
        if (column.get(index - 1).getSuit() != Suit.UNKNOWN) return 0;
        bl_points = sublist.size() * BASELINE;
        return bl_points;
    }

    int lowerCardsInPile(Card card, ArrayList<Card> knownCards) {
        int bestHeuristic = 0;
        boolean pointGiven = false;

        for (Card mCard : knownCards) {
            if (mCard.getValue() == card.getValue() - 1 && mCard.isRed() != card.isRed() && !pointGiven) {
                bestHeuristic += ONELOWER;
                pointGiven = true;
            }
        }

        return bestHeuristic;
    }

    int restOfTableau(Card card, State state) {
        int bestHeuristic = 0;
        boolean pointGiven = false;
        boolean isInTableau = false;

        for (ArrayList<Card> tableauPile : state.tableau) {
            for (Card mCard : tableauPile) {
                if (mCard.getValue() == card.getValue() && mCard.getSuit() == card.getSuit())
                    isInTableau = true;
                if (mCard.getValue() != 0 && !pointGiven) {
                    if (mCard.getValue() == card.getValue() - 1 && mCard.isRed() != card.isRed()) {
                        bestHeuristic += MOVABLEPILE;
                        pointGiven = true;
                    }
                    break;
                }

            }
            // if(isInTableau && pointGiven) bestHeuristic *= 2;
        }

        return bestHeuristic;
    }


    public Card getBestMove(ArrayList<Card> column, Card card) {
        return checkMoves(column, card);
    }

    public Card getBestMoveWaste(Card card, ArrayList<Card> knownCards, State state) {
        return checkMovesWaste(card, knownCards, state);
    }
}
