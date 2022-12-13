package com.andrewalia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import com.andrewalia.util.Card;
import com.andrewalia.util.FiveCardHand;
import com.andrewalia.util.HandType;
import com.andrewalia.util.NCardHand;
import com.andrewalia.util.Rank;
import com.andrewalia.util.Suit;

@Testable
public class TestHandType {
    @Test
    public void testEquals(){
        HandType handType1 = HandType.FLUSH;
        HandType handType2 = HandType.FLUSH;
        assertEquals(handType1, handType2);
    }

    @Test
    public void testCompareToEqual() {
        HandType handType1 = HandType.FLUSH;
        HandType handType2 = HandType.FLUSH;
        assertEquals(0, handType1.compareTo(handType2));
    }

    @Test
    public void testCompareToLessThan() {
        HandType handType1 = HandType.STRAIGHT;
        HandType handType2 = HandType.FLUSH;
        assertEquals(-1, handType1.compareTo(handType2));
    }

    @Test
    public void testBestHandType() {
        Card[] cards = new Card[7];
        cards[0] = new Card(Rank.ACE, Suit.CLUBS);
        cards[1] = new Card(Rank.ACE, Suit.DIAMONDS);
        cards[2] = new Card(Rank.ACE, Suit.HEARTS);
        cards[3] = new Card(Rank.ACE, Suit.SPADES);
        cards[4] = new Card(Rank.KING, Suit.CLUBS);
        cards[5] = new Card(Rank.KING, Suit.DIAMONDS);
        cards[6] = new Card(Rank.KING, Suit.HEARTS);
        NCardHand nCardHand = new NCardHand(cards);
        assertEquals(HandType.FOUR_OF_A_KIND, HandType.getBestHandType(nCardHand.getBestFiveCardHand()));
    }

    @Test
    public void testRoyalFlush(){
        assertTrue(HandType.ROYAL_FLUSH.test(new FiveCardHand(new Card[]{
            new Card(Rank.ACE, Suit.CLUBS),
            new Card(Rank.KING, Suit.CLUBS),
            new Card(Rank.QUEEN, Suit.CLUBS),
            new Card(Rank.JACK, Suit.CLUBS),
            new Card(Rank.TEN, Suit.CLUBS)
        })));

        assertTrue(!HandType.ROYAL_FLUSH.test(new FiveCardHand(new Card[]{
            new Card(Rank.ACE, Suit.DIAMONDS),
            new Card(Rank.KING, Suit.DIAMONDS),
            new Card(Rank.QUEEN, Suit.DIAMONDS),
            new Card(Rank.JACK, Suit.DIAMONDS),
            new Card(Rank.NINE, Suit.DIAMONDS)
        })));
    }

    @Test
    public void testStraight(){
        assertTrue(HandType.STRAIGHT.test(new FiveCardHand(new Card[]{
            new Card(Rank.ACE, Suit.CLUBS),
            new Card(Rank.KING, Suit.DIAMONDS),
            new Card(Rank.QUEEN, Suit.HEARTS),
            new Card(Rank.JACK, Suit.SPADES),
            new Card(Rank.TEN, Suit.CLUBS)
        })));

        assertTrue(!HandType.STRAIGHT.test(new FiveCardHand(new Card[]{
            new Card(Rank.ACE, Suit.DIAMONDS),
            new Card(Rank.KING, Suit.DIAMONDS),
            new Card(Rank.QUEEN, Suit.DIAMONDS),
            new Card(Rank.JACK, Suit.DIAMONDS),
            new Card(Rank.NINE, Suit.DIAMONDS)
        })));

        // rainbow 7 high
        assertTrue(HandType.STRAIGHT.test(new FiveCardHand(new Card[]{
            new Card(Rank.SEVEN, Suit.CLUBS),
            new Card(Rank.SIX, Suit.DIAMONDS),
            new Card(Rank.FIVE, Suit.HEARTS),
            new Card(Rank.FOUR, Suit.SPADES),
            new Card(Rank.THREE, Suit.CLUBS)
        })));
    }
}
