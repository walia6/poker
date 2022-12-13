package com.andrewalia;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import com.andrewalia.util.Card;
import com.andrewalia.util.FiveCardHand;
import com.andrewalia.util.HandType;
import com.andrewalia.util.Rank;
import com.andrewalia.util.Suit;

@Testable
public class TestFiveCardHand {
    @Test
    public void testFiveCardHandIsRoyalFlush() {
        FiveCardHand hand = new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.JACK, Suit.HEARTS),
            new Card(Rank.QUEEN, Suit.HEARTS),
            new Card(Rank.KING, Suit.HEARTS),
            new Card(Rank.ACE, Suit.HEARTS)
        });
        assertTrue(HandType.ROYAL_FLUSH.test(hand));
    }

    @Test
    public void testFiveCardHandIsStraightFlush() {
        assertFalse(HandType.STRAIGHT_FLUSH.test(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.JACK, Suit.HEARTS),
            new Card(Rank.QUEEN, Suit.HEARTS),
            new Card(Rank.KING, Suit.HEARTS),
            new Card(Rank.ACE, Suit.DIAMONDS)
        })));


        assertTrue(HandType.STRAIGHT_FLUSH.test(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.JACK, Suit.HEARTS),
            new Card(Rank.QUEEN, Suit.HEARTS),
            new Card(Rank.KING, Suit.HEARTS),
            new Card(Rank.ACE, Suit.HEARTS)
        })));


        assertTrue(HandType.STRAIGHT_FLUSH.test(new FiveCardHand(new Card[]{
            new Card(Rank.TWO, Suit.HEARTS),
            new Card(Rank.THREE, Suit.HEARTS),
            new Card(Rank.FOUR, Suit.HEARTS),
            new Card(Rank.FIVE, Suit.HEARTS),
            new Card(Rank.SIX, Suit.HEARTS)
        })));

        assertTrue(HandType.STRAIGHT_FLUSH.test(new FiveCardHand(new Card[]{
            new Card(Rank.THREE, Suit.HEARTS),
            new Card(Rank.FOUR, Suit.HEARTS),
            new Card(Rank.FIVE, Suit.HEARTS),
            new Card(Rank.SIX, Suit.HEARTS),
            new Card(Rank.SEVEN, Suit.HEARTS)
        })));
    }

    @Test
    public void testFiveCardHandIsFourOfAKind() {
        assertFalse(HandType.FOUR_OF_A_KIND.test(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.JACK, Suit.HEARTS),
            new Card(Rank.QUEEN, Suit.HEARTS),
            new Card(Rank.KING, Suit.HEARTS),
            new Card(Rank.ACE, Suit.DIAMONDS)
        })));

        assertTrue(HandType.FOUR_OF_A_KIND.test(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.TEN, Suit.CLUBS),
            new Card(Rank.TEN, Suit.SPADES),
            new Card(Rank.ACE, Suit.HEARTS)
        })));

        assertFalse(HandType.FOUR_OF_A_KIND.test(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.TEN, Suit.CLUBS),
            new Card(Rank.ACE, Suit.HEARTS),
            new Card(Rank.ACE, Suit.DIAMONDS)
        })));
    }

    @Test
    public void testFiveCardHandIsFullHouse() {
        assertFalse(HandType.FULL_HOUSE.test(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.JACK, Suit.HEARTS),
            new Card(Rank.QUEEN, Suit.HEARTS),
            new Card(Rank.KING, Suit.HEARTS),
            new Card(Rank.ACE, Suit.DIAMONDS)
        })));

        assertTrue(HandType.FULL_HOUSE.test(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.TEN, Suit.CLUBS),
            new Card(Rank.ACE, Suit.HEARTS),
            new Card(Rank.ACE, Suit.DIAMONDS)
        })));

        assertTrue(HandType.FULL_HOUSE.test(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.TEN, Suit.CLUBS),
            new Card(Rank.ACE, Suit.HEARTS),
            new Card(Rank.ACE, Suit.DIAMONDS)
        })));
        
    }

    @Test
    public void testHighCardOnly(){
        assertTrue(new FiveCardHand(new Card[]{
            new Card(Rank.ACE, Suit.HEARTS),
            new Card(Rank.TWO, Suit.HEARTS),
            new Card(Rank.THREE, Suit.HEARTS),
            new Card(Rank.FOUR, Suit.HEARTS),
            new Card(Rank.SIX, Suit.HEARTS)
        }).compareTo(new FiveCardHand(new Card[]{
            new Card(Rank.ACE, Suit.DIAMONDS),
            new Card(Rank.TWO, Suit.DIAMONDS),
            new Card(Rank.THREE, Suit.DIAMONDS),
            new Card(Rank.FOUR, Suit.DIAMONDS),
            new Card(Rank.SEVEN, Suit.DIAMONDS)
        })) < 0);

        assertTrue(new FiveCardHand(new Card[]{
            new Card(Rank.ACE, Suit.HEARTS),
            new Card(Rank.TWO, Suit.HEARTS),
            new Card(Rank.THREE, Suit.HEARTS),
            new Card(Rank.FOUR, Suit.HEARTS),
            new Card(Rank.SIX, Suit.HEARTS)
        }).compareTo(new FiveCardHand(new Card[]{
            new Card(Rank.ACE, Suit.DIAMONDS),
            new Card(Rank.TWO, Suit.DIAMONDS),
            new Card(Rank.THREE, Suit.DIAMONDS),
            new Card(Rank.FOUR, Suit.DIAMONDS),
            new Card(Rank.SIX, Suit.DIAMONDS)
        })) == 0);

        //assert Object.equals for the last couple
        assertFalse(new FiveCardHand(new Card[]{
            new Card(Rank.ACE, Suit.HEARTS),
            new Card(Rank.TWO, Suit.HEARTS),
            new Card(Rank.THREE, Suit.HEARTS),
            new Card(Rank.FOUR, Suit.HEARTS),
            new Card(Rank.SIX, Suit.HEARTS)
        }).equals(new FiveCardHand(new Card[]{
            new Card(Rank.ACE, Suit.DIAMONDS),
            new Card(Rank.TWO, Suit.DIAMONDS),
            new Card(Rank.THREE, Suit.DIAMONDS),
            new Card(Rank.FOUR, Suit.DIAMONDS),
            new Card(Rank.SIX, Suit.DIAMONDS)
        })));


        assertTrue(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.JACK, Suit.HEARTS),
            new Card(Rank.QUEEN, Suit.HEARTS),
            new Card(Rank.KING, Suit.HEARTS),
            new Card(Rank.ACE, Suit.HEARTS)
        }).compareTo(new FiveCardHand(new Card[]{
            new Card(Rank.ACE, Suit.DIAMONDS),
            new Card(Rank.TWO, Suit.DIAMONDS),
            new Card(Rank.THREE, Suit.DIAMONDS),
            new Card(Rank.FOUR, Suit.DIAMONDS),
            new Card(Rank.FIVE, Suit.DIAMONDS)

        })) > 0);

        //confirm 10H 10D 8C 8H AS beats 10H 10D 8C 8H 9S
        assertTrue(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.EIGHT, Suit.CLUBS),
            new Card(Rank.EIGHT, Suit.HEARTS),
            new Card(Rank.ACE, Suit.SPADES)
        }).compareTo(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.EIGHT, Suit.CLUBS),
            new Card(Rank.EIGHT, Suit.HEARTS),
            new Card(Rank.NINE, Suit.SPADES)
        })) > 0);
    }

    @Test
    public void testThreeOfAKind(){
        assertTrue(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.TEN, Suit.CLUBS),
            new Card(Rank.TWO, Suit.HEARTS),
            new Card(Rank.THREE, Suit.DIAMONDS)
        }).compareTo(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.TEN, Suit.CLUBS),
            new Card(Rank.TWO, Suit.HEARTS),
            new Card(Rank.FOUR, Suit.DIAMONDS)
        })) < 0);

        assertTrue(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.TEN, Suit.CLUBS),
            new Card(Rank.ACE, Suit.HEARTS),
            new Card(Rank.TWO, Suit.DIAMONDS)
        }).compareTo(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.TEN, Suit.CLUBS),
            new Card(Rank.ACE, Suit.HEARTS),
            new Card(Rank.THREE, Suit.DIAMONDS)
        })) < 0);

        assertTrue(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.TEN, Suit.CLUBS),
            new Card(Rank.ACE, Suit.HEARTS),
            new Card(Rank.SEVEN, Suit.DIAMONDS)
        }).compareTo(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.TEN, Suit.CLUBS),
            new Card(Rank.KING, Suit.HEARTS),
            new Card(Rank.QUEEN, Suit.DIAMONDS)
        })) > 0);
    }

    @Test
    public void testPairs(){
        assertTrue(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.FOUR, Suit.CLUBS),
            new Card(Rank.TWO, Suit.HEARTS),
            new Card(Rank.THREE, Suit.DIAMONDS)
        }).compareTo(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.FOUR, Suit.CLUBS),
            new Card(Rank.TWO, Suit.HEARTS),
            new Card(Rank.FIVE, Suit.DIAMONDS)
        })) < 0);

        assertTrue(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.FOUR, Suit.CLUBS),
            new Card(Rank.ACE, Suit.HEARTS),
            new Card(Rank.TWO, Suit.DIAMONDS)
        }).compareTo(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.FOUR, Suit.CLUBS),
            new Card(Rank.ACE, Suit.HEARTS),
            new Card(Rank.THREE, Suit.DIAMONDS)
        })) < 0);

        assertTrue(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.FOUR, Suit.CLUBS),
            new Card(Rank.ACE, Suit.HEARTS),
            new Card(Rank.SEVEN, Suit.DIAMONDS)
        }).compareTo(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.TEN, Suit.DIAMONDS),
            new Card(Rank.FOUR, Suit.CLUBS),
            new Card(Rank.KING, Suit.HEARTS),
            new Card(Rank.QUEEN, Suit.DIAMONDS)
        })) > 0);

        //same tests but put the tens in different positions
        assertTrue(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.FOUR, Suit.DIAMONDS),
            new Card(Rank.TEN, Suit.CLUBS),
            new Card(Rank.TWO, Suit.HEARTS),
            new Card(Rank.THREE, Suit.DIAMONDS)
        }).compareTo(new FiveCardHand(new Card[]{
            new Card(Rank.TEN, Suit.HEARTS),
            new Card(Rank.FOUR, Suit.DIAMONDS),
            new Card(Rank.TEN, Suit.CLUBS),
            new Card(Rank.TWO, Suit.HEARTS),
            new Card(Rank.FIVE, Suit.DIAMONDS)
        })) < 0);
    }
}
