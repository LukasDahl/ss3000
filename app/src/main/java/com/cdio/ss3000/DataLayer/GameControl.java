package com.cdio.ss3000.DataLayer;

import java.util.ArrayList;

import static com.cdio.ss3000.DataLayer.Suit.*;

public class GameControl {

    private State state;
    private PointCalculator pointCalculator;
    private StateTracker stateTracker;
    private Card lastMove = null;
    private Card stockCard = new Card(1, STOCK);
    private Card wonCard = new Card(-1, STOCK);
    private Card lostCard = new Card(-2, STOCK);
    private final int TURN_CARD_POINTS = 2;

    public GameControl(State state) {
        this.state = state;
        stateTracker = new StateTracker();
        pointCalculator = new PointCalculator();
    }

    //Checks if a card can be placed on another specific card
    public boolean moveToTableauPossible(Card movingCard, Card receivingCardTableau) {
        if (movingCard.getSuit() == HEARTS || movingCard.getSuit() == DIAMONDS) {
            if (receivingCardTableau.getSuit() == SPADES || receivingCardTableau.getSuit() == CLUBS) {
                if (movingCard.getValue() == receivingCardTableau.getValue() - 1) {
                    return true;
                }
            } else {
                return false;
            }

        } else if (movingCard.getSuit() == SPADES || movingCard.getSuit() == CLUBS) {
            if (receivingCardTableau.getSuit() == HEARTS || receivingCardTableau.getSuit() == DIAMONDS) {
                if (movingCard.getValue() == receivingCardTableau.getValue() - 1) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    //Checks if card can be moved to foundations
    public boolean moveToFoundationPossible(Card movingCard, Card receivingCardFoundations) {
        if (movingCard.getSuit() == receivingCardFoundations.getSuit()) {
            if (movingCard.getValue() == receivingCardFoundations.getValue() + 1) {
                return true;
            } else return false;
        } else {
            return false;
        }

    }

    //Checks if card is a King
    public boolean moveToEmptySpaceTableauPossible(Card movingCard) {
        return movingCard.getValue() == 13;
    }

    //Checks if card is an Ace
    public boolean moveToEmptyFoundationPossible(Card movingCard) {
        return movingCard.getValue() == 1;
    }

    public void checkPossibleMoves(State state) {
        //Possible moves from tableau
        for (ArrayList<Card> cardList : state.tableau) {
            if (!cardList.isEmpty() && cardList.get(cardList.size() - 1).getSuit() != UNKNOWN) {
                for (ArrayList<Card> otherCardListTableau : state.tableau) {
                    if (!otherCardListTableau.equals(cardList)) {
                        for (int index = cardList.size() - 1; index >= 0 && cardList.get(index).getSuit() != UNKNOWN; index--) {
                            if (!otherCardListTableau.isEmpty()) {
                                if (moveToTableauPossible(cardList.get(index), otherCardListTableau.get(otherCardListTableau.size() - 1))) {
                                    ArrayList<Card> newMove = new ArrayList<>();
                                    for (Card card : otherCardListTableau) {

                                        newMove.add((Card) card.clone());

                                    }
                                    cardList.get(index).addMove(newMove);
                                }
                            } else if (moveToEmptySpaceTableauPossible(cardList.get(index))) {
                                ArrayList<Card> newMove = new ArrayList<>();
                                cardList.get(index).addMove(newMove);
                            }


                        }
                    }
                }

                for (ArrayList<Card> cardListFoundations : state.foundations) {
                    if (!cardListFoundations.isEmpty()) {
                        if (moveToFoundationPossible(cardList.get(cardList.size() - 1), cardListFoundations.get(cardListFoundations.size() - 1))) {
                            ArrayList<Card> newMove = new ArrayList<>();
                            for (Card card : cardListFoundations) {

                                newMove.add((Card) card.clone());

                            }
                            cardList.get(cardList.size() - 1).addMove(newMove);
                        }
                    }
                }

                for (ArrayList<Card> cardListFoundations : state.foundations) {
                    if (cardListFoundations.isEmpty()) {
                        if (moveToEmptyFoundationPossible(cardList.get(cardList.size() - 1))) {
                            ArrayList<Card> newMove = new ArrayList<>();
                            cardList.get(cardList.size() - 1).addMove(newMove);
                        }
                    }
                }

                //possible moves from waste pile to tableau
                if (!state.waste.isEmpty()) {
                    for (Card card : state.waste) {
                        if (moveToTableauPossible(card, cardList.get(cardList.size() - 1))) {
                            ArrayList<Card> newMove = new ArrayList<>();
                            for (Card cardFromMove : cardList) {

                                newMove.add((Card) cardFromMove.clone());

                            }
                            card.addMove(newMove);
                            card.setWaste(true);
                        }

                        if (moveToEmptySpaceTableauPossible(card)) {
                            for (ArrayList<Card> otherCardlistTableau : state.tableau) {
                                if (otherCardlistTableau.isEmpty()) {
                                    ArrayList<Card> newMove = new ArrayList<>();
                                    for (Card cardFromMove : otherCardlistTableau) {

                                        newMove.add((Card) cardFromMove.clone());

                                    }
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
                            ArrayList<Card> newMove = new ArrayList<>();
                            for (Card cardFromMove : cardList) {

                                newMove.add((Card) cardFromMove.clone());

                            }
                            card.addMove(newMove);
                            card.setWaste(true);
                        }


                        if (moveToEmptySpaceTableauPossible(card)) {
                            for (ArrayList<Card> otherCardlistTableau : state.tableau) {
                                if (otherCardlistTableau.isEmpty()) {
                                    ArrayList<Card> newMove = new ArrayList<>();
                                    for (Card cardFromMove : otherCardlistTableau) {
                                        newMove.add((Card) cardFromMove.clone());

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
                            ArrayList<Card> newMove = new ArrayList<>();
                            for (Card cardFromMove : cardListFoundations) {

                                newMove.add((Card) cardFromMove.clone());

                            }
                            card.addMove(newMove);
                        }
                    } else {
                        if (moveToEmptyFoundationPossible(card)) {
                            ArrayList<Card> newMove = new ArrayList<>();
                            for (Card cardFromMove : cardListFoundations) {

                                newMove.add((Card) cardFromMove.clone());

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
                            ArrayList<Card> newMove = new ArrayList<>();
                            for (Card cardFromMove : cardListFoundations) {

                                    newMove.add((Card) cardFromMove.clone());

                            }
                            card.addMove(newMove);
                        }
                        if (moveToEmptyFoundationPossible(card)) {
                            ArrayList<Card> newMove = new ArrayList<>();
                            for (Card cardFromMove : cardListFoundations) {

                                    newMove.add((Card) cardFromMove.clone());

                            }
                            card.addMove(newMove);
                        }
                    }

                }
            }

        }

    }

    //Possible moves from foundations. This is only used if we have no other plausible moves, in order to "save" the win.
    public Card checkPossibleMovesFoundation()  {
        State mState;
        ArrayList<Card> cardPointList = new ArrayList<>();
        Card _cardHighestValue = new Card();

        //Creates an intermediary state for each top card in the foundation, if they can be moved to the tableau
        //Then it calculates if there are any possible moves in the new state, except for moving the card back into the foundation
        //If there are possible moves, they are compared and the best card is chosen to move down into the tableau
        for (int i = 0; i < state.foundations.length; i++) {
            if (!state.foundations[i].isEmpty()) {
                for (int j = 0; j < state.tableau.length; j++) {
                    if (!state.tableau[j].isEmpty()) {
                        if (moveToTableauPossible(state.foundations[i].get(state.foundations[i].size() - 1), state.tableau[j].get(state.tableau[j].size() - 1))) {
                            mState = (State) state.clone();
                            mState.tableau[j].add(mState.foundations[i].remove(mState.foundations[i].size() - 1));
                            checkPossibleMoves(mState);

                            for (ArrayList<Card> tableauColumn : mState.tableau) {
                                for (Card card : tableauColumn) {
                                    if (!card.getMoves().isEmpty()) {
                                        for (int k = 0; k < card.getMoves().size(); k++) {
                                            ArrayList<Card> moves = card.getMoves().get(k);
                                            for (Card move : moves) {
                                                if (move.getSuit() == card.getSuit()) break;
                                                Card cardWithBestMove = pointCalculator.getBestMove(tableauColumn, card, mState);
                                                cardPointList.add(cardWithBestMove);
                                            }
                                        }

                                    }
                                }
                            }
                            //Checks for possible moves for any card in the waste pile
                            if (!mState.waste.isEmpty()) {
                                for (Card mCard : mState.waste) {
                                    if (!mCard.getMoves().isEmpty()) {
                                        cardPointList.add(pointCalculator.getBestMoveWaste(mCard, mState));
                                    }
                                }
                            }
                            //Checks for possible moves for any card in the stock pile
                            if (!mState.stock.isEmpty()) {
                                for (Card mCard : mState.stock) {
                                    if (!mCard.getMoves().isEmpty()) {
                                        cardPointList.add(pointCalculator.getBestMoveWaste(mCard, mState));
                                    }
                                }
                            }


                            return getCard(cardPointList, _cardHighestValue);
                        }
                    }
                }
            }
        }
        return null;
    }


    public Status updateState(State newState) {
        System.out.println(newState);
        try {
            boolean status = stateTracker.updateState(newState, lastMove);
            if (!status) {
                return Status.INVALID;
            }
            state = stateTracker.getBoard();
            return Status.INPROGRESS;
        }
        catch (IndexOutOfBoundsException e){
            return Status.INVALID;
        }
    }

    //when the state has been updated by the computer vision, the game is run from here, until next move.
    public Card run() {

        ArrayList<Card> cardPointList = new ArrayList<>();
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

        Card bestCard = getCard(cardPointList, _cardHighestValue);

        if (bestCard.getSuit() == UNKNOWN) {
            bestCard = stockCard;
            stockCard.setPoints(TURN_CARD_POINTS);
        }

        lastMove = bestCard;

        if (bestCard.getSuit() == STOCK && stateTracker.gameOver()
                == Status.WON)
            return wonCard;
        if (bestCard.getSuit() == STOCK && stateTracker.gameOver() == Status.LOST) {

                if (checkPossibleMovesFoundation() == null)
                    return lostCard;

        }
        return bestCard;

    }

    //Calculates the card with the highest point score, and suggests that.
    private Card getCard(ArrayList<Card> cardPointList, Card _cardHighestValue) {
        System.out.println("---------------\nCard point list\n---------------");
        for (Card card : cardPointList) {
            System.out.println(card.toMovesString());
            if (card.getPoints() > _cardHighestValue.getPoints())
                _cardHighestValue = card;
        }

        return _cardHighestValue;
    }


}
