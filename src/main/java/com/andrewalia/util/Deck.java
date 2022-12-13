package com.andrewalia.util;

import it.unimi.dsi.util.XoRoShiRo128PlusRandom;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.apache.commons.collections4.list.UnmodifiableList;

public class Deck {

    private final List<Card> cards;
    private final Random random = new XoRoShiRo128PlusRandom();
    public static final List<Card> FULL_DECK;
    /**
     * <strong>Please don't modify this array...</strong>
     */
    public static final Card[] FULL_DECK_ARRAY;

    static {
        LinkedList<Card> TEMP_FULL_DECK = new LinkedList<>();
        FULL_DECK_ARRAY = new Card[52];
        int i = 0;
        for (Suit suit : new Suit[] { Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS, Suit.SPADES }) {
            for (Rank rank : new Rank[] {
                Rank.ACE,
                Rank.KING,
                Rank.QUEEN,
                Rank.JACK,
                Rank.TEN,
                Rank.NINE,
                Rank.EIGHT,
                Rank.SEVEN,
                Rank.SIX,
                Rank.FIVE,
                Rank.FOUR,
                Rank.THREE,
                Rank.TWO,
            }) {
                final Card card = new Card(rank, suit);
                TEMP_FULL_DECK.add(card);
                FULL_DECK_ARRAY[i++] = card;
            }
        }

        FULL_DECK = UnmodifiableList.unmodifiableList(TEMP_FULL_DECK);
    }

    public Deck() {
        cards = new LinkedList<Card>();
        //make a deep copy of the full deck
        for (Card card : FULL_DECK) {
            cards.add(new Card(card.getRank(), card.getSuit()));
        }
    }

    public void shuffle() {
        Collections.shuffle(cards, random);
    }

    public Card deal() {
        return cards.remove(0);
    }

    public Card[] deal(int n) {
        Card[] cards = new Card[n];
        for (int i = 0; i < n; i++) {
            cards[i] = deal();
        }
        return cards;
    }

    public Card peek() {
        return cards.get(0);
    }

    public int size() {
        return cards.size();
    }

    @Override
    public String toString() {
        return cards.toString();
    }

    public void add(Card card) {
        cards.add(card);
    }

    public void remove(Card card) {
        cards.remove(card);
    }

    public static void main(String[] args) {
        Deck deck = new Deck();
        System.out.println(deck.size());
    }

    public Card get(int i) {
        return cards.get(i);
    }


}
