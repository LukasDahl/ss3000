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
    //  private Card emptyStackTableau = new Card(-1, UNKNOWN, false);
    // private Card emptyStackFoundation = new Card(-2, UNKNOWN, false);

    public GameControl(State state) {
        this.state = state;
        stateTracker = new StateTracker();
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
                    if (cardListFoundations.size() > 0) {
                        if (moveToFoundationPossible(cardList.get(cardList.size() - 1), cardListFoundations.get(cardListFoundations.size() - 1))) {
                            //cardList.get(cardList.size()-1).addMove(cardListFoundations.get(cardListFoundations.size()-1));
                            cardList.get(cardList.size() - 1).addMove(cardListFoundations);
                        }
                    }
                }

                for (ArrayList<Card> otherCardlistTableau : state.tableau) {
                    if (otherCardlistTableau.isEmpty()) {
                        if (moveToEmptySpaceTableauPossible(cardList.get(cardList.size() - 1))) {
                            // cardList.get(cardList.size()-1).addMove(emptyStackTableau);
                            cardList.get(cardList.size() - 1).addMove(otherCardlistTableau);
                        }
                    }

                }

                for (ArrayList<Card> cardListFoundations : state.foundations) {
                    if (cardListFoundations.isEmpty()) {
                        if (moveToEmptyFoundationPossible(cardList.get(cardList.size() - 1))) {
                            //cardList.get(cardList.size()-1).addMove(emptyStackFoundation);
                            cardList.get(cardList.size() - 1).addMove(cardListFoundations);
                        }
                    }
                }

                //possible moves from waste pile
                if (state.waste.size() > 0) {
                    if (moveToTableauPossible(state.waste.get(state.waste.size() - 1), cardList.get(cardList.size() - 1))) {
                        //state.waste.get(state.waste.size()-1).addMove(cardList.get(cardList.size()-1));
                        state.waste.get(state.waste.size() - 1).addMove(cardList);
                        state.waste.get(state.waste.size() - 1).setWaste(true);
                    }


                    if (moveToEmptySpaceTableauPossible(state.waste.get(state.waste.size() - 1))) {
                        state.waste.get(state.waste.size() - 1).addMove(cardList);
                        state.waste.get(state.waste.size() - 1).setWaste(true);
                    }
                }

            }
        }


        //possible moves from waste pile
        if (state.waste.size() > 0) {
            for (ArrayList<Card> cardListFoundations : state.foundations) {
                if (moveToFoundationPossible(state.waste.get(state.waste.size() - 1), cardListFoundations.get(cardListFoundations.size() - 1))) {
                    //state.waste.get(state.waste.size()-1).addMove(cardListFoundations.get(cardListFoundations.size()-1));
                    state.waste.get(state.waste.size() - 1).addMove(cardListFoundations);
                }
                if (moveToEmptyFoundationPossible(state.waste.get(state.waste.size() - 1))) {
                    //state.waste.get(state.waste.size()-1).addMove(emptyStackFoundation);
                    state.waste.get(state.waste.size() - 1).addMove(cardListFoundations);
                }

            }
        }


        //Possible moves from foundations
        for (ArrayList<Card> cardList : state.foundations) {
            if (cardList.size() > 0) {
                for (ArrayList<Card> cardListTableau : state.tableau) {
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

/*
    public void suggestMove(){
        ArrayList<Card> bestMoveCards;
        for()
    }
*/

    public void updateState(State newState) {
        System.out.println(newState);
        stateTracker.updateState(newState);
        state = stateTracker.getBoard();
        System.out.println(state);
    }

    public Card run() {
        pointCalculator = new PointCalculator();
        ArrayList<Card> cardPointList = new ArrayList<>();
        Card _cardHighestValue = new Card();
        checkPossibleMoves();
        for (ArrayList<Card> cards : state.tableau) {
            for (Card card : cards) {
                if (!card.getMoves().isEmpty())
                    cardPointList.add(pointCalculator.getBestMove(cards, card));
            }
        }
        if (state.waste.size() > 0 && !state.waste.get(state.waste.size() - 1).getMoves().isEmpty())
            cardPointList.add(pointCalculator.getBestMoveWaste(state.waste.get(state.waste.size() - 1), piles.getKnownCards(), state));
        for (ArrayList<Card> cards : state.foundations) {
            if (cards.size() > 0 && !cards.get(cards.size() - 1).getMoves().isEmpty())
                cardPointList.add(cards.get(cards.size() - 1));
        }
        for (Card card : cardPointList) {
            if (card.getPoints() > _cardHighestValue.getPoints())
                _cardHighestValue = card;
        }
        return _cardHighestValue;
    }

}
