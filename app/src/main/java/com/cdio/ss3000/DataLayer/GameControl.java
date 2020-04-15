package com.cdio.ss3000.DataLayer;

import com.cdio.ss3000.DataLayer.Card;
import com.cdio.ss3000.DataLayer.State;

import java.util.LinkedList;
import java.util.List;

import static com.cdio.ss3000.DataLayer.Suit.CLUBS;
import static com.cdio.ss3000.DataLayer.Suit.DIAMONDS;
import static com.cdio.ss3000.DataLayer.Suit.HEARTS;
import static com.cdio.ss3000.DataLayer.Suit.SPADES;

public class GameControl {

    LinkedList<Card>[] foundations = new LinkedList[4], tableau = new LinkedList[7];
    LinkedList<Card> stock, waste;
    State state;

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

    public boolean movePossible(Card movingCard, Card receivingCard){
        if(movingCard.getSuit() == HEARTS || movingCard.getSuit() == DIAMONDS){
            if((receivingCard.getSuit() == SPADES || receivingCard.getSuit() == CLUBS) && movingCard.value+1 == receivingCard.value){
                return true;
            }else return false;

        }else if(movingCard.getSuit() == SPADES || movingCard.getSuit() == CLUBS){
            if((receivingCard.getSuit() == HEARTS || movingCard.getSuit() == DIAMONDS) && movingCard.value+1 == receivingCard.value){
                return true;
            }else return false;
        }
        else if(movingCard.value == 13 && receivingCard == null){
            return true;
        }
        return false;
    }

    public void suggestMove(){}

    public void checkPossibleMoves(){

    }



}
