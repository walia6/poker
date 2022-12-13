package com.andrewalia.util;

public class Card implements Comparable<Card> {

    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Card)) {
            return false;
        }
        final Card other = (Card) obj;
        if (this.rank != other.rank && (this.rank == null || !this.rank.equals(other.rank))) {
            return false;
        }
        if (this.suit != other.suit && (this.suit == null || !this.suit.equals(other.suit))) {
            return false;
        }
        return true;
    }

    public String getShortName() {
        return Character.toString(rank.getShortName()) + Character.toString(suit.getShortName());
    }

    @Override
    public int compareTo(Card o) {
        int result = rank.compareTo(o.rank);
        return result;
    }

    public static Card valueOf(String shortName) {
        if (shortName.length() != 2) {
            throw new IllegalArgumentException("Invalid card name " + shortName);
        }
        Rank rank = Rank.valueOf(shortName.charAt(0));
        Suit suit = Suit.valueOf(shortName.charAt(1));
        return new Card(rank, suit);
    }
}
