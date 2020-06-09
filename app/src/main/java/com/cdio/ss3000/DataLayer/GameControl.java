package com.cdio.ss3000.DataLayer;

import android.graphics.Point;

import java.util.LinkedList;

import static com.cdio.ss3000.DataLayer.Suit.*;

public class GameControl {

    /*
    private LinkedList<Card>[] foundations = new LinkedList[4], tableau = new LinkedList[7];
    private LinkedList<Card> stock, waste;
     */
    private State state;
    private PointCalculator pointCalculator;
    private Card emptyStackTableau = new Card(-1, UNKNOWN, false);
    private Card emptyStackFoundation = new Card(-2, UNKNOWN, false);

    public GameControl(){
    }

    public Object getState() {
        try {
            return state.clone();

            } catch (CloneNotSupportedException e) {
            System.out.println("Error in cloning state");
            return state;
    }
    }

    public void setState(State state){
        this.state = state;
    }

    //Checks if a card can be placed on another specific card
    public boolean moveToTableauPossible(Card movingCard, Card receivingCardTableau){
        if(movingCard.getSuit() == HEARTS || movingCard.getSuit() == DIAMONDS){
            if(receivingCardTableau.getSuit() == SPADES || receivingCardTableau.getSuit() == CLUBS){
                if(movingCard.getValue() == receivingCardTableau.getValue() -1){
                    //movingCard.setMovable(true);
                    return true;
                }
            }else{
                //movingCard.setMovable(false);
                return false;
            }
            //return(receivingCardTableau.getSuit() == SPADES || receivingCardTableau.getSuit() == CLUBS);

        }else if(movingCard.getSuit() == SPADES || movingCard.getSuit() == CLUBS){
            if(receivingCardTableau.getSuit() == HEARTS || receivingCardTableau.getSuit() == DIAMONDS){
                if(movingCard.getValue() == receivingCardTableau.getValue() -1){
                   // movingCard.setMovable(true);
                    return true;
                }
            }else{
                //movingCard.setMovable(false);
                return false;
            }
            //return (receivingCardTableau.getSuit() == HEARTS || movingCard.getSuit() == DIAMONDS);
        }
        return false;
    }
    //Checks if card can be moved to foundations
    public boolean moveToFoundationPossible(Card movingCard, Card receivingCardFoundations){
        if(movingCard.getSuit() == receivingCardFoundations.getSuit()){
            if(movingCard.getValue() == receivingCardFoundations.getValue()+1){
                //movingCard.setMovable(true);
                return true;
            }else return false;
        }else{
            //movingCard.setMovable(false);
            return false;
        }

        //return (movingCard.getSuit() == receivingCardFoundations.getSuit());
    }
    //Checks if card is a King
    public boolean moveToEmptySpaceTableauPossible(Card movingCard){
        if(movingCard.getValue() == 13){
            return true;
        }else return false;
    }
    //Checks if card is an Ace
    public boolean moveToEmptyFoundationPossible(Card movingCard){
            if(movingCard.getValue() == 1){
            return true;
        }else return false;
    }


    /*
    This finds all possible moves
    Each possible move is added to the specific card
     */
    public void checkPossibleMoves(){


        state = (State)getState();


        //Possible moves from tableau
        for(LinkedList<Card> cardList : state.tableau){
            if(!cardList.isEmpty()){
                for(LinkedList<Card> otherCardListTableau : state.tableau){
                       if(moveToTableauPossible(cardList.peek(), otherCardListTableau.peek())){
                           cardList.peek().addMove(otherCardListTableau.peek());
                       }
                    }
                for(LinkedList<Card> cardListFoundations : state.foundations){
                    if(moveToFoundationPossible(cardList.peek(), cardListFoundations.peek())){
                        cardList.peek().addMove(cardListFoundations.peek());
                    }
                }

                for(LinkedList<Card> otherCardlistTableau : state.tableau){
                    if(otherCardlistTableau.isEmpty()){
                        if(moveToEmptySpaceTableauPossible(cardList.peek())){
                            cardList.peek().addMove(emptyStackTableau);
                        }
                    }

                }

                for(LinkedList<Card> cardListFoundations : state.foundations){
                    if(cardListFoundations.isEmpty()){
                        if(moveToEmptyFoundationPossible(cardList.peek())){
                            cardList.peek().addMove(emptyStackFoundation);
                        }
                    }
                }

                //possible moves from waste pile
                if(moveToTableauPossible(state.waste.peek(), cardList.peek())){
                    state.waste.peek().addMove(cardList.peek());
                }

                if(moveToEmptySpaceTableauPossible(state.waste.peek())){
                    state.waste.peek().addMove(emptyStackTableau);
                }

            }
        }


        //possible moves from waste pile
        for(LinkedList<Card> cardListFoundations: state.foundations){
            if(moveToFoundationPossible(state.waste.peek(), cardListFoundations.peek())){
                state.waste.peek().addMove(cardListFoundations.peek());
            }
            if(moveToEmptyFoundationPossible(state.waste.peek())){
                state.waste.peek().addMove(emptyStackFoundation);
            }

        }


        //Possible moves from foundations
        for(LinkedList<Card> cardList : state.foundations){
            for(LinkedList<Card> cardListTableau : state.tableau){
                if(moveToTableauPossible(cardList.peek(), cardListTableau.peek())){
                    cardList.peek().addMove(cardListTableau.peek());
                }

                if(moveToEmptySpaceTableauPossible(cardList.peek())){
                    cardList.peek().addMove(emptyStackTableau);
                }
            }
        }
    }

/*
    public void suggestMove(){
        LinkedList<Card> bestMoveCards;
        for()
    }
*/
}
