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

    public boolean movePossibleTableau(Card movingCard, Card receivingCardTableau){
        if(movingCard.getSuit() == HEARTS || movingCard.getSuit() == DIAMONDS){
            if(receivingCardTableau.getSuit() == SPADES || receivingCardTableau.getSuit() == CLUBS){
               movingCard.setMovable(true);
               return true;
            }else{
                movingCard.setMovable(false);
                return false;
            }
            //return(receivingCardTableau.getSuit() == SPADES || receivingCardTableau.getSuit() == CLUBS);

        }else if(movingCard.getSuit() == SPADES || movingCard.getSuit() == CLUBS){
            if(receivingCardTableau.getSuit() == HEARTS || receivingCardTableau.getSuit() == DIAMONDS){
                movingCard.setMovable(true);
                return true;
            }else{
                movingCard.setMovable(false);
                return false;
            }
            //return (receivingCardTableau.getSuit() == HEARTS || movingCard.getSuit() == DIAMONDS);
        }
        return false;
    }

    public boolean movePossibleFoundations(Card movingCard, Card receivingCardFoundations){
        if(movingCard.getSuit() == receivingCardFoundations.getSuit()){
            movingCard.setMovable(true);
            return true;
        }else{
            movingCard.setMovable(false);
            return false;
        }

        //return (movingCard.getSuit() == receivingCardFoundations.getSuit());
    }

    public void suggestMove(){}

    public void checkPossibleMoves(){
        for(LinkedList<Card> cardList : tableau){
            if(!cardList.isEmpty()){
                for(LinkedList<Card> otherCardListTableau : tableau){
                       if(movePossibleTableau(cardList.peek(), otherCardListTableau.peek())){
                           cardList.peek().addMove(otherCardListTableau.peek());
                       }
                    }
                for(LinkedList<Card> otherCardListFoundations : foundations){
                    if(movePossibleFoundations(cardList.peek(), otherCardListFoundations.peek())){
                        cardList.peek().addMove(otherCardListFoundations.peek());
                    }
                }

            }
        }
    }



}
