package com.andrewalia.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.math3.util.Combinations;

public class NCardHand implements Comparable<NCardHand> {

    private final Card[] cards;

    private FiveCardHand bestFiveCardHand = null;

    public NCardHand(Card[] cards) {
        this.cards = Arrays.copyOf(cards, cards.length);
    }

    public FiveCardHand getBestFiveCardHand() {
        if (bestFiveCardHand == null) {
            bestFiveCardHand = new FiveCardHand((Card[]) Collections.max(getAllFiveCardHands()).toArray());
        }
        return bestFiveCardHand;
    }

    public Collection<FiveCardHand> getAllFiveCardHands() {
        // can be more than seven cards
        if (cards.length < 5) {
            throw new IllegalArgumentException("Not enough cards");
        }

        Combinations fiveCardHandIndexCombinations = new Combinations(cards.length, 5);
        Collection<FiveCardHand> fiveCardHandsCombinations = new LinkedList<>();
        for (int[] is : fiveCardHandIndexCombinations) {
            final Card[] fiveCardArray = new Card[5];
            for (int i = 0; i < 5; i++) {
                fiveCardArray[i] = cards[is[i]];
            }
            fiveCardHandsCombinations.add(new FiveCardHand(fiveCardArray));
        }

        return fiveCardHandsCombinations;
    }

    public static void main(String[] args) {
        //make  adeck
        //make an NCardHand from all cards in Deck.FULL_DECK
        NCardHand hand = new NCardHand(Deck.FULL_DECK.toArray(new Card[0]));
        //get all possible five card hands
        Collection<FiveCardHand> allFiveCardHands = hand.getAllFiveCardHands();
        //print the size of the collectioin
        System.out.println(allFiveCardHands.size());
        //make a key,value map of handtype and default count of zero
        Map<HandType, Integer> handTypeCount = new EnumMap<>(HandType.class);
        //iterate through all five card hands

        //intiialzie the map with default zero
        for (HandType handType : HandType.values()) {
            handTypeCount.put(handType, 0);
        }
        for (FiveCardHand fiveCardHand : allFiveCardHands) {
            handTypeCount.put(HandType.getBestHandType(fiveCardHand), handTypeCount.get(HandType.getBestHandType(fiveCardHand)) + 1);
        }

        //print the map iteratively
        for (HandType handType : HandType.values()) {
            System.out.println(handType + " " + handTypeCount.get(handType));
        }
        

    }

    @Override
    public int compareTo(NCardHand o) {
        // compare the best five card hands
        return getBestFiveCardHand().compareTo(o.getBestFiveCardHand());
    }

    @Override
    public String toString() {
        // each card separated by a space (Shortname)
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card.getShortName());
            sb.append(" ");
        }
        return sb.toString();
    }
}
