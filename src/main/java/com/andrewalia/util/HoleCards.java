package com.andrewalia.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HoleCards{
    private final Rank lowRank;
    private final Rank highRank;
    private final boolean suited;
    private final static Map<Set<Card>,HoleCards> H_MAP = new HashMap<>();

    static {
        for (Card card : Deck.FULL_DECK) {
            for (Card oCard : Deck.FULL_DECK) {
                if (card == oCard) {
                    continue;
                }
                Set<Card> set = new HashSet<>();
                set.add(card);
                set.add(oCard);
                H_MAP.put(set, new HoleCards(card, oCard));
            }
        }
    }

    public HoleCards(Card card1, Card card2) {
        if (card1.getRank().compareTo(card2.getRank()) < 0) {
            this.lowRank = card1.getRank();
            this.highRank = card2.getRank();
        } else {
            this.lowRank = card2.getRank();
            this.highRank = card1.getRank();
        }
        this.suited = card1.getSuit().equals(card2.getSuit());
    }

    static public HoleCards valueOf(Card card0, Card card1) {
        Set<Card> set = new HashSet<>();
        set.add(card0);
        set.add(card1);
        return H_MAP.get(set);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HoleCards)) {
            return false;
        }
        HoleCards other = (HoleCards) o;
        if (this.lowRank.equals(other.lowRank) && this.highRank.equals(other.highRank) && this.suited == other.suited) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        //72o = 7-2 offsuit, 72s = 7-2 suited, AKo = Ace King offsuit, AKs = Ace King suited, TT = Ten Ten
        if (lowRank.equals(highRank)) {
            return String.valueOf(lowRank.getShortName()) + String.valueOf(lowRank.getShortName());
        } else if (suited) {
            return String.valueOf(highRank.getShortName()) + String.valueOf(lowRank.getShortName()) + "s";
        } else {
            return String.valueOf(highRank.getShortName()) + String.valueOf(lowRank.getShortName()) + "o";
        }
    }

    @Override
    public int hashCode() {
        return lowRank.hashCode() + highRank.hashCode()*13 + (suited ? 1 : 0);
    }
}