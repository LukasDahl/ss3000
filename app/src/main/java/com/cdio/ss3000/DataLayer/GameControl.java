package com.cdio.ss3000.DataLayer;

import com.cdio.ss3000.DataLayer.Card;
import com.cdio.ss3000.DataLayer.State;

import java.util.LinkedList;
import java.util.List;

import static com.cdio.ss3000.DataLayer.Suit.*;

public class GameControl {

    private LinkedList<Card>[] foundations = new LinkedList[4], tableau = new LinkedList[7];
    private LinkedList<Card> stock, waste;
    private State state;

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
    public boolean movePossibleTableau(Card movingCard, Card receivingCardTableau){
        if(movingCard.getSuit() == HEARTS || movingCard.getSuit() == DIAMONDS){
            if(receivingCardTableau.getSuit() == SPADES || receivingCardTableau.getSuit() == CLUBS){
                if(movingCard.getValue()+1 == receivingCardTableau.getValue()){
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
                if(movingCard.getValue()+1 == receivingCardTableau.getValue()){
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
    public boolean movePossibleFoundations(Card movingCard, Card receivingCardFoundations){
        if(movingCard.getSuit() == receivingCardFoundations.getSuit()){
            if(movingCard.getValue()-1 == receivingCardFoundations.getValue()){
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
    public boolean movePossibleEmptyStackTableau(Card movingCard){
        if(movingCard.getValue() == 13){
            return true;
        }else return false;
    }
    //Checks if card is an Ace
    public boolean movePossibleEmptyStackFoundation(Card movingCard){
        if(movingCard.getValue() == 1){
            return true;
        }else return false;
    }

    public void suggestMove(){}

    /*
    This finds all possible moves
    Each possible move is added to the specific card
     */
    public void checkPossibleMoves(){
        //Possible moves from tableau
        for(LinkedList<Card> cardList : tableau){
            if(!cardList.isEmpty()){
                for(LinkedList<Card> otherCardListTableau : tableau){
                       if(movePossibleTableau(cardList.peek(), otherCardListTableau.peek())){
                           cardList.peek().addMove(otherCardListTableau.peek());
                       }
                    }
                for(LinkedList<Card> cardListFoundations : foundations){
                    if(movePossibleFoundations(cardList.peek(), cardListFoundations.peek())){
                        cardList.peek().addMove(cardListFoundations.peek());
                    }
                }

                for(LinkedList<Card> otherCardlistTableau : tableau){
                    if(otherCardlistTableau.isEmpty()){
                        if(movePossibleEmptyStackTableau(cardList.peek())){
                            cardList.peek().addMoveToEmptySpace(otherCardlistTableau);
                        }
                    }

                }

                for(LinkedList<Card> cardListFoundations : foundations){
                    if(cardListFoundations.isEmpty()){
                        if(movePossibleEmptyStackFoundation(cardList.peek())){
                            cardList.peek().addMoveToEmptySpace(cardListFoundations);
                        }
                    }
                }
                //possible moves from waste pile
                if(movePossibleTableau(waste.peek(), cardList.peek())){
                    waste.peek().addMove(cardList.peek());
                }

                if(movePossibleEmptyStackTableau(waste.peek())){
                    waste.peek().addMoveToEmptySpace(cardList);
                }

                for(LinkedList<Card> cardListFoundations: foundations){
                    if(movePossibleFoundations(waste.peek(), cardListFoundations.peek())){
                        waste.peek().addMove(cardListFoundations.peek());
                    }
                    if(movePossibleEmptyStackFoundation(waste.peek())){
                        waste.peek().addMoveToEmptySpace(cardListFoundations);
                    }

                }





            }
        }

        //Possible moves from foundations
        for(LinkedList<Card> cardList : foundations){
            for(LinkedList<Card> cardListTableau : tableau){
                if(movePossibleFoundations(cardList.peek(), cardListTableau.peek())){
                    cardList.peek().addMove(cardListTableau.peek());
                }

                if(movePossibleEmptyStackTableau(cardList.peek())){
                    cardList.peek().addMoveToEmptySpace(cardListTableau);
                }
            }
        }
    }



}
