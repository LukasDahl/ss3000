package com.cdio.ss3000.DataLayer;

import java.util.ArrayList;

public class Piles {
    private ArrayList<Card> stock, waste;
    private ArrayList<Card> knownCards = new ArrayList<>();

    public Piles(ArrayList<Card> stock, ArrayList<Card> waste){
        this.stock = stock;
        this.waste = waste;

        if(!waste.isEmpty()) knownCards.addAll(waste);

    }

    public void addCard(Card wasteTop){
        if(!knownCards.contains(wasteTop)){
            knownCards.add(wasteTop);
        }
        waste.add(wasteTop);
        stock.remove(wasteTop);

    }

    public void removeCard(Card wasteTop){
        waste.remove(wasteTop);
        knownCards.remove(wasteTop);
    }

    public ArrayList<Card> getKnownCards(){
        return knownCards;
    }

    public void allCardsToStock(){
        stock = waste;
        waste.clear();
    }
}
