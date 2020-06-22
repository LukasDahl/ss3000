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
    public void checkPossibleMoves() {
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

                //possible moves from waste pile
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

                //possible moves from stock pile
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


        //possible moves from waste pile
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


        //Possible moves from foundations
        for (ArrayList<Card> cardList : state.foundations) {
            if (cardList.size() > 0) {
                for (ArrayList<Card> cardListTableau : state.tableau) {
                    if (!cardListTableau.isEmpty()) {
                        if (moveToTableauPossible(cardList.get(cardList.size() - 1), cardListTableau.get(cardListTableau.size() - 1))) {
                            //cardList.get(cardList.size()-1).addMove(cardListTableau.get(cardListTableau.size()-1));
                            ArrayList<Card> newMove = new ArrayList<>();
                            for (Card cardFromMove : cardListTableau) {
                                try {
                                    newMove.add((Card) cardFromMove.clone());
                                } catch (CloneNotSupportedException e) {
                                    e.printStackTrace();
                                }
                            }
                            cardList.get(cardList.size() - 1).addMove(newMove);
                        }
                        if (moveToEmptySpaceTableauPossible(cardList.get(cardList.size() - 1))) {
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
                                    (cardList.get(cardList.size() - 1)).addMove(newMove);
                                }
                            }

                        }
                    }
                }
            }
        }
    }


    public void updateState(State newState) {
        System.out.println(newState);
        stateTracker.updateState(newState, lastMove);
        state = stateTracker.getBoard();
    }

    public Card run() {

        System.out.println("In run()");
        System.out.println(state.toString());

        ArrayList<Card> cardPointList = new ArrayList<>();
        Card _cardHighestValue = new Card();
        checkPossibleMoves();

        for (ArrayList<Card> tableauColumn : state.tableau) {
            for (Card card : tableauColumn) {
                if (!card.getMoves().isEmpty()) {
                    Card cardWithBestMove = pointCalculator.getBestMove(tableauColumn, card, state);
                    cardPointList.add(cardWithBestMove);
                }
            }
        }
        //TODO piles.knownCards --> state.waste
        if (!state.waste.isEmpty() && !state.waste.get(state.waste.size() - 1).getMoves().isEmpty())
            cardPointList.add(pointCalculator.getBestMoveWaste(state.waste.get(state.waste.size() - 1),/* state.waste,*/ state));
        for (ArrayList<Card> cards : state.foundations) {
            if (cards.size() > 0 && !cards.get(cards.size() - 1).getMoves().isEmpty())
                cardPointList.add(cards.get(cards.size() - 1));
        }

        if (!state.waste.isEmpty() || !state.stock.isEmpty()) {
            stockCard.setPoints(TURN_CARD_POINTS);
            cardPointList.add(stockCard);

        }

        System.out.println("---------------\nCard point list\n---------------");
        for (Card card : cardPointList) {
            System.out.println(card.toMovesString());
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
