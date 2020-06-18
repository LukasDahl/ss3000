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
    private Card stockCard = new Card(1, STOCK, true);
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
        if (movingCard.getValue() == 13) {
            return true;
        } else return false;
    }

    //Checks if card is an Ace
    public boolean moveToEmptyFoundationPossible(Card movingCard) {
        if (movingCard.getValue() == 1) {
            return true;
        } else return false;
    }


    /*
    This finds all possible moves
    Each possible move is added to the specific card
     */
    public void checkPossibleMoves() {

        int index;

        //Possible moves from tableau
        for (ArrayList<Card> cardList : state.tableau) {
            if (!cardList.isEmpty() && cardList.get(cardList.size() - 1).getSuit() != UNKNOWN) {
                for (ArrayList<Card> otherCardListTableau : state.tableau) {
                    if (!otherCardListTableau.isEmpty() && !otherCardListTableau.equals(cardList)) {
                        index = cardList.size() - 1;
                        while (cardList.get(index).getSuit() != UNKNOWN) {
                            if (moveToTableauPossible(cardList.get(index), otherCardListTableau.get(otherCardListTableau.size() - 1))) {
                                //cardList.get(cardList.size()-1).addMove(otherCardListTableau.get(otherCardListTableau.size()-1));
                                cardList.get(index).addMove(otherCardListTableau);
                            } else if (cardList.get(index).getSuit() == UNKNOWN) break;
                            index -= 1;
                            if (cardList.size() == 1) break;
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
                            cardList.get(cardList.size() - 1).addMove(cardListFoundations);
                        }
                    }
                }

                for (ArrayList<Card> otherCardlistTableau : state.tableau) {
                    if (!otherCardlistTableau.isEmpty()) {
                        if (moveToEmptySpaceTableauPossible(cardList.get(cardList.size() - 1))) {
                            // cardList.get(cardList.size()-1).addMove(emptyStackTableau);
                            cardList.get(cardList.size() - 1).addMove(otherCardlistTableau);
                        }
                    }

                }

                for (ArrayList<Card> cardListFoundations : state.foundations) {
                    if (!cardListFoundations.isEmpty()) {
                        if (moveToEmptyFoundationPossible(cardList.get(cardList.size() - 1))) {
                            //cardList.get(cardList.size()-1).addMove(emptyStackFoundation);
                            cardList.get(cardList.size() - 1).addMove(cardListFoundations);
                        }
                    }
                }

                //possible moves from waste pile
                if (!state.waste.isEmpty()) {
                    for(Card card : state.waste){
                        if (moveToTableauPossible(card, cardList.get(cardList.size() - 1))) {
                            //state.waste.get(state.waste.size()-1).addMove(cardList.get(cardList.size()-1));
                            card.addMove(cardList);
                            card.setWaste(true);
                        }


                        if (moveToEmptySpaceTableauPossible(card)) {
                            card.addMove(cardList);
                            card.setWaste(true);
                        }
                    }

                }

                //possible moves from stock pile
                if (!state.stock.isEmpty()) {
                    for(Card card : state.stock){
                        if (moveToTableauPossible(card, cardList.get(cardList.size() - 1))) {
                            //state.waste.get(state.waste.size()-1).addMove(cardList.get(cardList.size()-1));
                            card.addMove(cardList);
                            card.setWaste(true);
                        }


                        if (moveToEmptySpaceTableauPossible(card)) {
                            card.addMove(cardList);
                            card.setWaste(true);
                        }
                    }

                }

            }
        }


        //possible moves from waste pile
        if (!state.waste.isEmpty()) {
            for(Card card : state.waste){
                for (ArrayList<Card> cardListFoundations : state.foundations) {
                    if (!cardListFoundations.isEmpty()) {
                        if (moveToFoundationPossible(card, cardListFoundations.get(cardListFoundations.size() - 1))) {
                            //state.waste.get(state.waste.size()-1).addMove(cardListFoundations.get(cardListFoundations.size()-1));
                            card.addMove(cardListFoundations);
                        }
                        if (moveToEmptyFoundationPossible(card)) {
                            //state.waste.get(state.waste.size()-1).addMove(emptyStackFoundation);
                            card.addMove(cardListFoundations);
                        }
                    }

                }
            }

        }

        //possible moves from stock pile
        if (!state.stock.isEmpty()) {
            for(Card card : state.stock){
                for (ArrayList<Card> cardListFoundations : state.foundations) {
                    if (!cardListFoundations.isEmpty()) {
                        if (moveToFoundationPossible(card, cardListFoundations.get(cardListFoundations.size() - 1))) {
                            //state.waste.get(state.waste.size()-1).addMove(cardListFoundations.get(cardListFoundations.size()-1));
                            card.addMove(cardListFoundations);
                        }
                        if (moveToEmptyFoundationPossible(card)) {
                            //state.waste.get(state.waste.size()-1).addMove(emptyStackFoundation);
                            card.addMove(cardListFoundations);
                        }
                    }

                }
            }

        }


        //Possible moves from foundations
        for (ArrayList<Card> cardList : state.foundations) {
            if (cardList.size() > 0) {
                for (ArrayList<Card> cardListTableau : state.tableau) {
                    if (!cardListTableau.isEmpty()) {
                        if (moveToTableauPossible(cardList.get(cardList.size() - 1), cardListTableau.get(cardListTableau.size() - 1))) {
                            //cardList.get(cardList.size()-1).addMove(cardListTableau.get(cardListTableau.size()-1));
                            cardList.get(cardList.size() - 1).addMove(cardListTableau);
                        }

                        if (moveToEmptySpaceTableauPossible(cardList.get(cardList.size() - 1))) {
                            //cardList.get(cardList.size()-1).addMove(emptyStackTableau);
                            cardList.get(cardList.size() - 1).addMove(cardListTableau);
                        }
                    }
                }
            }
        }
    }

/*
    public void suggestMove(){
        ArrayList<Card> bestMoveCards;
        for()
    }
*/

    public void updateState(State newState) {
        System.out.println(newState);
        stateTracker.updateState(newState, lastMove);
        state = stateTracker.getBoard();
    }

    public Card run() {

        try {
            state = (State) state.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }


        System.out.println("In run()");
        System.out.println(state.toString());

        ArrayList<Card> cardPointList = new ArrayList<>();
        Card _cardHighestValue = new Card();
        checkPossibleMoves();
        for (ArrayList<Card> cards : state.tableau) {
            for (Card card : cards) {
                if (!card.getMoves().isEmpty())
                    cardPointList.add(pointCalculator.getBestMove(cards, card, state));
            }
        }
        //TODO piles.knownCards --> state.waste
        if (!state.waste.isEmpty() && !state.waste.get(state.waste.size() - 1).getMoves().isEmpty())
            cardPointList.add(pointCalculator.getBestMoveWaste(state.waste.get(state.waste.size() - 1),/* state.waste,*/ state));
        for (ArrayList<Card> cards : state.foundations) {
            if (cards.size() > 0 && !cards.get(cards.size() - 1).getMoves().isEmpty())
                cardPointList.add(cards.get(cards.size() - 1));
        }

        if(!state.waste.isEmpty() || !state.stock.isEmpty()){
            stockCard.setPoints(TURN_CARD_POINTS);
            cardPointList.add(stockCard);

        }

        for (Card card : cardPointList) {
            if (card.getPoints() > _cardHighestValue.getPoints())
                _cardHighestValue = card;
        }
/*        if (cardPointList.isEmpty())
            _cardHighestValue = new Card(1, STOCK, true);*/

        if (_cardHighestValue.getSuit() == UNKNOWN)
            _cardHighestValue = stockCard;

        lastMove = _cardHighestValue;
        return _cardHighestValue;
    }

}
