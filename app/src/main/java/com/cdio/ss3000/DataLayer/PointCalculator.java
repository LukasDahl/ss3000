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
    private final int UNKNOWN_PILE_SIZE_POINTS = 2;
    private final int ONELOWER = 1; //Hvis der er et kort der har værdi 1 lavere end det kort vi tjekker
    private final int MOVABLEPILE = 8; //Hvis et træk resulterer i at en bunke kan rykkes over, så der frigøres ukendte kort.
    private final int MAX_HIGHERVAL_FOUNDATION = 3;


    private Card checkMovesWaste(Card card,/* ArrayList<Card> knownCards, */State state) {
        ArrayList<Card> tempList = new ArrayList<>();
        tempList.add(card);
        int freeTableauPiles = 0;

        //If card is king, check if there is an opposite color king which would be a better move.
        //If the alternative king is better, the current king will get points set to -1 so it doesn't get chosen as a move.
        if(card.getValue() == KING && restOfTableau(card, state) == 0){
            for(ArrayList<Card> tableauPile : state.tableau){
                if(tableauPile.isEmpty()){
                    freeTableauPiles++;
                }
            }
            if(freeTableauPiles < 2){
                for(Card mCard : state.stock){
                    if(mCard.getSuit() != Suit.UNKNOWN && mCard.getValue() == 13 && mCard.isRed() != card.isRed() && restOfTableau(mCard, state) > 0){
                        card.clearMoves();
                        card.setPoints(-1);
                        return card;
                    }
                }
                for(Card mCard : state.waste){
                    if(mCard.getSuit() != Suit.UNKNOWN && mCard.getValue() == 13 && mCard.isRed() != card.isRed() && restOfTableau(mCard, state) > 0){
                        card.clearMoves();
                        card.setPoints(-1);
                        return card;
                    }
                }
            }
        }

        Card bestCard = checkMoves(tempList, card, state);
        bestCard.setPoints(bestCard.getPoints() + restOfTableau(card, state));
        //else bestCard.setPoints(bestCard.getPoints() + lowerCardsInPile(card, knownCards));

        return bestCard;
    }

    private Card checkMoves(ArrayList<Card> column, Card card, State state) {
        int temp_points = 0;
        //If card is an ace
        Card ace = checkAce(card);
        if (ace != null) {
            ace.clearMoves();
            return ace;
        }
        //Do all other moves
        Card newCard = checkCards(column, card, state);
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

        System.out.println(card.toMovesString());


        //Return the supplied card with the single best move
        return card;
    }

    private Card checkCards(ArrayList<Card> column, Card card, State state) {
        boolean hasFoundItem = false;
        int bonus = addUnknownPileSizePoints(column, card);//Add the unknown pile size points
        for (ArrayList<Card> move : card.getMoves()) {
            int temp_points = bonus;
            hasFoundItem = false;
            //Empty spot in Tableau
            if (move.isEmpty() && card.getValue() == KING) {
                hasFoundItem = true;
                if (column.indexOf(card) != 0 || (!state.waste.isEmpty() && state.waste.get(state.waste.size() - 1).compareTo(card) == 0)) {
                        temp_points += TABLEAU_EMPTY_POINTS;
                }
                card.setPoints(temp_points);
                // return card;
            }/* else */ else if (move.get(move.size() - 1).getSuit() == card.getSuit()) {//This will be a move to foundation
                //Checking if we have empty piles in foundations

                temp_points += checkFoundations(move, state.foundations);


            }/* else */ else if (move.get(move.size() - 1).getValue() > 0 && move.get(move.size() - 1).getValue() < 14 && move.get(move.size() - 1).isRed() != card.isRed()) {//Any other card in the tableau


                if (column.get(0).equals(card) || column.get(column.indexOf(card) - 1).getSuit() == Suit.UNKNOWN) {
                    temp_points += TABLEAU_POINTS;
                }

                //TODO: now there is never enough points to ever suggest splitting. Give points to splitting piles in special cases ***Moves have been added to all cards in waste pile and stock pile***
                else {
                    for (ArrayList<Card> moveOfParent : column.get(column.indexOf(card) - 1).getMoves()) {

                        if (moveOfParent.get(moveOfParent.size() - 1).getSuit() == column.get(column.indexOf(card) - 1).getSuit()) {
                            temp_points += checkFoundations(moveOfParent, state.foundations) - TABLEAU_SPLIT_POINTS;
                            break;
                        }
                    }
                }

                //Add points for each underlying face down card
            }


            if (!hasFoundItem) {
                move.get(move.size() - 1).setPoints(temp_points);
            }
        }
        int highestPoint = -1;
        Card newCard = new Card();
        for (ArrayList<Card> list : card.getMoves()) {
            if (list.isEmpty()) {
                if (highestPoint < card.getPoints()) {
                    highestPoint = card.getPoints();
                    newCard = card;
                }
            } else {
                if (highestPoint < list.get(list.size() - 1).getPoints()) {
                    highestPoint = list.get(list.size() - 1).getPoints();
                    newCard = list.get(list.size() - 1);
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

    int addUnknownPileSizePoints(ArrayList<Card> column, Card card) {
        if (column.isEmpty() || column.indexOf(card) == 0)
            return 0;
        if (column.size() == 1 || column.get(column.indexOf(card) - 1).getSuit() != Suit.UNKNOWN)
            return 0;
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
        bl_points = sublist.size() * UNKNOWN_PILE_SIZE_POINTS;
        return bl_points;
    }

    //TODO: Make a better version of this, you fool!
   /* int lowerCardsInPile(Card card, ArrayList<Card> knownCards) {
        int bestHeuristic = 0;
        boolean pointGiven = false;

        for (Card mCard : knownCards) {
            if (mCard.getValue() == card.getValue() - 1 && mCard.isRed() != card.isRed() && !pointGiven) {
                bestHeuristic += ONELOWER;
                pointGiven = true;
            }
        }

        return bestHeuristic;
    }*/

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


    public Card getBestMove(ArrayList<Card> column, Card card, State state) {
        return checkMoves(column, card, state);
    }

    public Card getBestMoveWaste(Card card,/* ArrayList<Card> knownCards,*/ State state) {
        return checkMovesWaste(card,/* knownCards, */state);
    }

    public int checkFoundations(ArrayList<Card> move, ArrayList<Card>[] foundations) {
        boolean emptyPiles = false;
        int lowestCardVal = 15;
        for (ArrayList<Card> pile : foundations) {
            if (pile.isEmpty()) emptyPiles = true;
            else {
                if (pile.get(pile.size() - 1).getValue() < lowestCardVal)
                    lowestCardVal = pile.get(pile.size() - 1).getValue();
            }
        }
        //Ensuring we don't just endlessly adds cards to one foundation pile, and none to the other
        //TODO: If there is nothing else you can do, this should still be suggested.
        if (move.get(move.size() - 1).getValue() < MAX_HIGHERVAL_FOUNDATION) {
            return FOUNDATION_POINTS;
        } else if (!emptyPiles && move.get(move.size() - 1).getValue() < lowestCardVal + MAX_HIGHERVAL_FOUNDATION) {
            return FOUNDATION_POINTS;
        } else
            return 0;
    }
}
