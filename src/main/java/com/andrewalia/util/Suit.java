package com.andrewalia.util;

public class Suit implements Comparable<Suit> {
    
    public final static Suit HEARTS = new Suit("Hearts");
    public final static Suit DIAMONDS = new Suit("Diamonds");
    public final static Suit CLUBS = new Suit("Clubs");
    public final static Suit SPADES = new Suit("Spades");
    public final static Suit[] SUITS = new Suit[]{HEARTS, DIAMONDS, CLUBS, SPADES};

    private final String longName;
    private final char shortName;

    public Suit(String longName) {
        this.longName = longName;
        shortName = longName.charAt(0);
    }

    public String getLongName() {
        return longName;
    }

    public char getShortName() {
        return shortName;
    }
    
    @Override
    public String toString() {
        return longName;
    }

    @Override
    public int compareTo(Suit o) {
        return shortName - o.shortName;
    }

    public static Suit[] values() {
        return null;
    }

    static public Suit valueOf(char shortName) {
        for (Suit suit : new Suit[] {HEARTS, DIAMONDS, CLUBS, SPADES}) {
            if (suit.getShortName() == shortName) {
                return suit;
            }
        }
        throw new IllegalArgumentException("No suit with short name " + shortName);
    }
}
 