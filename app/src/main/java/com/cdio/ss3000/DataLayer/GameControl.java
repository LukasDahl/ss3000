package com.cdio.ss3000.DataLayer;

import android.graphics.Point;

import java.util.ArrayList;

import static com.cdio.ss3000.DataLayer.Suit.*;

public class GameControl {

    /*
    private ArrayList<Card>[] foundations = new ArrayList[4], tableau = new ArrayList[7];
    private ArrayList<Card> stock, waste;
     */
    private State state;
    private PointCalculator pointCalculator;
    private Piles piles;
    private StateTracker stateTracker;
    private Card lastMove = null;
    private Card stockCard = new Card(1, STOCK);
    private Card wonCard = new Card(-1, STOCK);
    private Card lostCard = new Card(-2, STOCK);
    private final int TURN_CARD_POINTS = 2;
    //  private Card emptyStackTableau = new Card(-1, UNKNOWN, false);
    // private Card emptyStackFoundation = new Card(-2, UNKNOWN, false);

    public GameControl(State state) {
        this.state = state;
        stateTracker = new StateTracker();
        pointCalculator = new PointCalculator();
    }

    public State getState() {

        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    //Checks if a card can be placed on another specific card
    public boolean moveToTableauPossible(Card movingCard, Card receivingCardTableau) {
        if (movingCard.getSuit() == HEARTS || movingCard.getSuit() == DIAMONDS) {
            if (receivingCardTableau.getSuit() == SPADES || receivingCardTableau.getSuit() == CLUBS) {
                if (movingCard.getValue() == receivingCardTableau.getValue() - 1) {
                    //movingCard.setMovable(true);
                    return true;
                }
            } else {
                //movingCard.setMovable(false);
                return false;
            }
            //return(receivingCardTableau.getSuit() == SPADES || receivingCardTableau.getSuit() == CLUBS);

        } else if (movingCard.getSuit() == SPADES || movingCard.getSuit() == CLUBS) {
            if (receivingCardTableau.getSuit() == HEARTS || receivingCardTableau.getSuit() == DIAMONDS) {
                if (movingCard.getValue() == receivingCardTableau.getValue() - 1) {
                    // movingCard.setMovable(true);
                    return true;
                }
            } else {
                //movingCard.setMovable(false);
                return false;
            }
            //return (receivingCardTableau.getSuit() == HEARTS || movingCard.getSuit() == DIAMONDS);
        }
        return false;
    }

    //Checks if card can be moved to foundations
    public boolean moveToFoundationPossible(Card movingCard, Card receivingCardFoundations) {
        if (movingCard.getSuit() == receivingCardFoundations.getSuit()) {
            if (movingCard.getValue() == receivingCardFoundations.getValue() + 1) {
                //movingCard.setMovable(true);
                return true;
            } else return false;
        } else {
            //movingCard.setMovable(false);
            return false;
        }

        //return (movingCard.getSuit() == receivingCardFoundations.getSuit());
    }

    //Checks if card is a King
    public boolean moveToEmptySpaceTableauPossible(Card movingCard) {
        return movingCard.getValue() == 13;
    }

    //Checks if card is an Ace
    public boolean moveToEmptyFoundationPossible(Card movingCard) {
        return movingCard.getValue() == 1;
    }


    /*
    This finds all possible moves
    Each possible move is added to the specific card
     */
    public void checkPossibleMoves(State state) {
        //Possible moves from tableau
        for (ArrayList<Card> cardList : state.tableau) {
            if (!cardList.isEmpty() && cardList.get(cardList.size() - 1).getSuit() != UNKNOWN) {
                for (ArrayList<Card> otherCardListTableau : state.tableau) {
                    if (!otherCardListTableau.equals(cardList)) {
                        for (int index = cardList.size() - 1; index >= 0 && cardList.get(index).getSuit() != UNKNOWN; index--) {
                            if (!otherCardListTableau.isEmpty()) {
                                if (moveToTableauPossible(cardList.get(index), otherCardListTableau.get(otherCardListTableau.size() - 1))) {
                                    //cardList.get(cardList.size()-1).addMove(otherCardListTableau.get(otherCardListTableau.size()-1));
                                    ArrayList<Card> newMove = new ArrayList<>();
                                    for (Card card : otherCardListTableau) {
                                        try {
                                            newMove.add((Card) card.clone());
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    cardList.get(index).addMove(newMove);
                                }
                            } else if (moveToEmptySpaceTableauPossible(cardList.get(index))) {
                                // cardList.get(cardList.size()-1).addMove(emptyStackTableau);
                                ArrayList<Card> newMove = new ArrayList<>();
                                cardList.get(index).addMove(newMove);
                            }


                        }
                    }
                }
                    /*
                       if(moveToTableauPossible(cardList.get(cardList.size()-1), otherCardListTableau.get(cardList.size()-1))){
                           //cardList.get(cardList.size()-1).addMove(otherCardListTableau.get(otherCardListTableau.size()-1));
                           cardList.get(cardList.size()-1).addMove(otherCardListTableau);
                       }

                     */

                for (ArrayList<Card> cardListFoundations : state.foundations) {
                    if (!cardListFoundations.isEmpty()) {
                        if (moveToFoundationPossible(cardList.get(cardList.size() - 1), cardListFoundations.get(cardListFoundations.size() - 1))) {
                            //cardList.get(cardList.size()-1).addMove(cardListFoundations.get(cardListFoundations.size()-1));
                            ArrayList<Card> newMove = new ArrayList<>();
                            for (Card card : cardListFoundations) {
                                try {
                                    newMove.add((Card) card.clone());
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                            }
                            cardList.get(cardList.size() - 1).addMove(newMove);
                        }
                    }
                }

                for (ArrayList<Card> cardListFoundations : state.foundations) {
                    if (cardListFoundations.isEmpty()) {
                        if (moveToEmptyFoundationPossible(cardList.get(cardList.size() - 1))) {
                            //cardList.get(cardList.size()-1).addMove(emptyStackFoundation);
                            ArrayList<Card> newMove = new ArrayList<>();
                            cardList.get(cardList.size() - 1).addMove(newMove);
                        }
                    }
                }

                //possible moves from waste pile to tableau
                if (!state.waste.isEmpty()) {
                    for (Card card : state.waste) {
                        if (moveToTableauPossible(card, cardList.get(cardList.size() - 1))) {
                            //state.waste.get(state.waste.size()-1).addMove(cardList.get(cardList.size()-1));
                            ArrayList<Card> newMove = new ArrayList<>();
                            for (Card cardFromMove : cardList) {
                                try {
                                    newMove.add((Card) cardFromMove.clone());
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                            }
                            card.addMove(newMove);
                            card.setWaste(true);
                        }

                        if (moveToEmptySpaceTableauPossible(card)) {
                            for (ArrayList<Card> otherCardlistTableau : state.tableau) {
                                if (otherCardlistTableau.isEmpty()) {
                                    ArrayList<Card> newMove = new ArrayList<>();
                                    for (Card cardFromMove : otherCardlistTableau) {
                                        try {
                                            newMove.add((Card) cardFromMove.clone());
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    // cardList.get(cardList.size()-1).addMove(emptyStackTableau);
                                    card.addMove(newMove);
                                }
                            }

                        }
                    }

                }

                //possible moves from stock pile to tableau
                if (!state.stock.isEmpty()) {
                    for (Card card : state.stock) {
                        if (moveToTableauPossible(card, cardList.get(cardList.size() - 1))) {
                            //state.waste.get(state.waste.size()-1).addMove(cardList.get(cardList.size()-1));
                            ArrayList<Card> newMove = new ArrayList<>();
                            for (Card cardFromMove : cardList) {
                                try {
                                    newMove.add((Card) cardFromMove.clone());
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                            }
                            card.addMove(newMove);
                            card.setWaste(true);
                        }


                        if (moveToEmptySpaceTableauPossible(card)) {
                            for (ArrayList<Card> otherCardlistTableau : state.tableau) {
                                if (otherCardlistTableau.isEmpty()) {
                                    // cardList.get(cardList.size()-1).addMove(emptyStackTableau);
                                    ArrayList<Card> newMove = new ArrayList<>();
                                    for (Card cardFromMove : otherCardlistTableau) {
                                        try {
                                            newMove.add((Card) cardFromMove.clone());
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    card.addMove(newMove);
                                }
                            }

                        }
                    }

                }

            }
        }


        //possible moves from waste pile to foundations
        if (!state.waste.isEmpty()) {
            for (Card card : state.waste) {
                for (ArrayList<Card> cardListFoundations : state.foundations) {
                    if (!cardListFoundations.isEmpty()) {
                        if (moveToFoundationPossible(card, cardListFoundations.get(cardListFoundations.size() - 1))) {
                            //state.waste.get(state.waste.size()-1).addMove(cardListFoundations.get(cardListFoundations.size()-1));
                            ArrayList<Card> newMove = new ArrayList<>();
                            for (Card cardFromMove : cardListFoundations) {
                                try {
                                    newMove.add((Card) cardFromMove.clone());
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                            }
                            card.addMove(newMove);
                        }
                    } else {
                        if (moveToEmptyFoundationPossible(card)) {
                            //state.waste.get(state.waste.size()-1).addMove(emptyStackFoundation);
                            ArrayList<Card> newMove = new ArrayList<>();
                            for (Card cardFromMove : cardListFoundations) {
                                try {
                                    newMove.add((Card) cardFromMove.clone());
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                            }
                            card.addMove(newMove);
                        }
                    }

                }
            }

        }

        //possible moves from stock pile
        if (!state.stock.isEmpty()) {
            for (Card card : state.stock) {
                for (ArrayList<Card> cardListFoundations : state.foundations) {
                    if (!cardListFoundations.isEmpty()) {
                        if (moveToFoundationPossible(card, cardListFoundations.get(cardListFoundations.size() - 1))) {
                            //state.waste.get(state.waste.size()-1).addMove(cardListFoundations.get(cardListFoundations.size()-1));
                            ArrayList<Card> newMove = new ArrayList<>();
                            for (Card cardFromMove : cardListFoundations) {
                                try {
                                    newMove.add((Card) cardFromMove.clone());
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                            }
                            card.addMove(newMove);
                        }
                        if (moveToEmptyFoundationPossible(card)) {
                            //state.waste.get(state.waste.size()-1).addMove(emptyStackFoundation);
                            ArrayList<Card> newMove = new ArrayList<>();
                            for (Card cardFromMove : cardListFoundations) {
                                try {
                                    newMove.add((Card) cardFromMove.clone());
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                            }
                            card.addMove(newMove);
                        }
                    }

                }
            }

        }

    }

    public Card checkPossibleMovesFoundation() throws CloneNotSupportedException{
        //Possible moves from foundations
        State mState;
        ArrayList<Card> cardPointList = new ArrayList<>();
        Card _cardHighestValue = new Card();

        for(int i = 0; i < state.foundations.length; i++){
            if (!state.foundations[i].isEmpty()) {
                for(int j = 0; j < state.tableau.length; j++){
                    if (!state.tableau[j].isEmpty()) {
                        if (moveToTableauPossible(state.foundations[i].get(state.foundations[i].size() - 1), state.tableau[j].get(state.tableau[j].size() - 1))) {
                            mState = (State) state.clone();
                            mState.tableau[j].add(mState.foundations[i].remove(mState.foundations[i].size() - 1));
                            checkPossibleMoves(mState);

                            for (ArrayList<Card> tableauColumn : mState.tableau) {
                                for (Card card : tableauColumn) {
                                    if (!card.getMoves().isEmpty()){
                                        for(ArrayList<Card> moves : card.getMoves()){
                                            for(Card move : moves){
                                                if(move.getSuit() == card.getSuit()) break;
                                                Card cardWithBestMove = pointCalculator.getBestMove(tableauColumn, card, mState);
                                                cardPointList.add(cardWithBestMove);
                                            }
                                        }

                                    }
                                }
                            }
                            if(!mState.waste.isEmpty()){
                                for(Card mCard : mState.waste){
                                    if(!mCard.getMoves().isEmpty()){
                                        cardPointList.add(pointCalculator.getBestMoveWaste(mCard, mState));
                                    }
                                }
                            }
                            if(!mState.stock.isEmpty()){
                                for(Card mCard : mState.stock){
                                    if(!mCard.getMoves().isEmpty()){
                                        cardPointList.add(pointCalculator.getBestMoveWaste(mCard, mState));
                                    }
                                }
                            }


                            return getCard(cardPointList, _cardHighestValue);
                        }
                    }
                }
            }
        }return null;
    }



    public Status updateState(State newState) {
        System.out.println(newState);
        stateTracker.updateState(newState, lastMove);
        state = stateTracker.getBoard();
        return stateTracker.gameOver();
    }

    public Card run() {

        System.out.println("In run()");
        System.out.println(state.toString());

        ArrayList<Card> cardPointList = new ArrayList<>();
        ArrayList<Card> foundationPointList = new ArrayList<>();
        Card _cardHighestValue = new Card();
        checkPossibleMoves(state);

        for (ArrayList<Card> tableauColumn : state.tableau) {
            for (Card card : tableauColumn) {
                if (!card.getMoves().isEmpty()) {
                    Card cardWithBestMove = pointCalculator.getBestMove(tableauColumn, card, state);
                    cardPointList.add(cardWithBestMove);
                }
            }
        }
        if (!state.waste.isEmpty() && !state.waste.get(state.waste.size() - 1).getMoves().isEmpty())
            cardPointList.add(pointCalculator.getBestMoveWaste(state.waste.get(state.waste.size() - 1),/* state.waste,*/ state));
        for (ArrayList<Card> cards : state.foundations) {
            if (cards.size() > 0 && !cards.get(cards.size() - 1).getMoves().isEmpty())
                cardPointList.add(cards.get(cards.size() - 1));
        }

        if(cardPointList.isEmpty() && stateTracker.gameOver() == Status.INPROGRESS){
           // if (!state.waste.isEmpty() || !state.stock.isEmpty()) {
                stockCard.setPoints(TURN_CARD_POINTS);
                cardPointList.add(stockCard);

        }else if(cardPointList.isEmpty() && stateTracker.gameOver() == Status.WON){
            return wonCard;

        }else if(cardPointList.isEmpty() && stateTracker.gameOver() == Status.LOST){
            try {
                if (checkPossibleMovesFoundation() == null)
                    return lostCard;
                else return checkPossibleMovesFoundation();
            }catch (CloneNotSupportedException e){
                e.printStackTrace();
            }
            return lostCard;
        }


        return getCard(cardPointList, _cardHighestValue);
    }

    private Card getCard(ArrayList<Card> cardPointList, Card _cardHighestValue) {
        System.out.println("---------------\nCard point list\n---------------");
        for (Card card : cardPointList) {
        System.out.println(card.toMovesString());
        if (card.getPoints() > _cardHighestValue.getPoints())
            _cardHighestValue = card;
    }
       /* if (cardPointList.isEmpty())
            stateTracker.gameOver();*/

        if (_cardHighestValue.getSuit() == UNKNOWN)
    _cardHighestValue = stockCard;

        lastMove = _cardHighestValue;
        return _cardHighestValue;
    }


}
