package com.andrewalia.util;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.commons.lang3.ArrayUtils;

public enum HandType{
    HIGH_CARD(
        (fiveCardHand) -> true,
        (fiveCardHand) -> ArrayUtils.reverse(fiveCardHand.getCards())
    ),
    PAIR(
        (fiveCardHand) -> {
            Card[] cards = fiveCardHand.getCards();
            return (
                cards[0].getRank() == cards[1].getRank() ||
                cards[1].getRank() == cards[2].getRank() ||
                cards[2].getRank() == cards[3].getRank() ||
                cards[3].getRank() == cards[4].getRank()
            );
        },
        (fiveCardHand) -> {
            //get the most frequent rank using a for loop
            Card[] cards = fiveCardHand.getCards();
            Rank mostFrequentRank = null;
            int mostFrequentRankCount = 0;
            for (int i = 0; i < cards.length; i++) {
                int count = 0;
                for (int j = 0; j < cards.length; j++) {
                    if (cards[i].getRank() == cards[j].getRank()) {
                        count++;
                    }
                }
                if (count > mostFrequentRankCount) {
                    mostFrequentRank = cards[i].getRank();
                    mostFrequentRankCount = count;
                }
            }
            

            Card[] threeCards = new Card[3];
            Card[] pair = new Card[2];
            int threeCardsIndex = 0;
            for (int i = 0; i < cards.length; i++) {
                if (cards[i].getRank() == mostFrequentRank) {
                    pair[i - threeCardsIndex] = cards[i];
                    continue;
                }
                threeCards[threeCardsIndex] = cards[i];
                threeCardsIndex++;
            }

            ArrayUtils.reverse(threeCards);
            cards[0] = pair[0];
            cards[1] = pair[1];
            cards[2] = threeCards[0];
            cards[3] = threeCards[1];
            cards[4] = threeCards[2];
            return;
        }

    ),
    TWO_PAIR(
        (fiveCardHand) -> {
            Card[] cards = fiveCardHand.getCards();
            return (
                    cards[0].getRank() == cards[1].getRank() &&
                    cards[2].getRank() == cards[3].getRank()
                ) || (
                    cards[0].getRank() == cards[1].getRank() &&
                    cards[3].getRank() == cards[4].getRank()
                ) || (
                    cards[1].getRank() == cards[2].getRank() &&
                    cards[3].getRank() == cards[4].getRank()
                );
        },
        (fiveCardHand) -> {
            Card[] cards = fiveCardHand.getCards();
            if (cards[2].getRank() == cards[3].getRank()) {
                Card temp = cards[0];
                cards[0] = cards[2];
                cards[2] = cards[1];
                cards[1] = cards[3];
                cards[3] = temp;
                return;
            }
            if (cards[0].getRank() == cards[1].getRank()) {
                Card temp = cards[0];
                cards[0] = cards[3];
                cards[3] = cards[1];
                cards[1] = cards[4];
                cards[4] = cards[2];
                cards[2] = temp;
                return;
            }
            Card temp = cards[0];
            cards[0] = cards[3];
            cards[3] = cards[1];
            cards[1] = cards[4];
            cards[4] = temp;
            return;
        }
    ),
    THREE_OF_A_KIND(
        (fiveCardHand) -> {
            Card[] cards = fiveCardHand.getCards();
            return (
                cards[0].getRank() == cards[1].getRank() &&
                cards[1].getRank() == cards[2].getRank()
            ) || (
                cards[1].getRank() == cards[2].getRank() &&
                cards[2].getRank() == cards[3].getRank()
            ) || (
                cards[2].getRank() == cards[3].getRank() &&
                cards[3].getRank() == cards[4].getRank()
            );
        },
        (fiveCardHand) -> {
            Card[] cards = fiveCardHand.getCards();
            if (cards[0].getRank() == cards[1].getRank() && cards[1].getRank() == cards[2].getRank()) {
                //first 3 are good, check if we need to swap the last 2
                if (cards[3].getRank().getValue() < cards[4].getRank().getValue()) {
                    Card temp = cards[3];
                    cards[3] = cards[4];
                    cards[4] = temp;
                }
                return;
            }
            if (cards[2].getRank() == cards[4].getRank()) {
                Card temp = cards[0];
                cards[0] = cards[3];
                cards[3] = cards[1];
                cards[1] = cards[4];
                cards[4] = temp;
                //first 3 are good, check if we need to swap the last 2
                if (cards[3].getRank().getValue() < cards[4].getRank().getValue()) {
                    temp = cards[3];
                    cards[3] = cards[4];
                    cards[4] = temp;
                }
                return;
            }
            Card temp = cards[0];
            cards[0] = cards[3];
            cards[3] = temp;
            if (cards[3].getRank().getValue() < cards[4].getRank().getValue()) {
                temp = cards[3];
                cards[3] = cards[4];
                cards[4] = temp;
            }
            return;
        }
    ),
    STRAIGHT(
        (fiveCardHand) -> {
            Card[] cards = fiveCardHand.getCards();
            // Special case for A2345 straight
            //REMEMBER, ACE IS SORTED TO THE END OF THE ARRAY
            if (
                cards[4].getRank() == Rank.ACE &&
                cards[0].getRank() == Rank.TWO &&
                cards[1].getRank() == Rank.THREE &&
                cards[2].getRank() == Rank.FOUR &&
                cards[3].getRank() == Rank.FIVE
            ) {
                return true;
            }
            for (int i = 1; i < cards.length; i++) {
                if (cards[i].getRank().getValue() != cards[i - 1].getRank().getValue() + 1) {
                    return false;
                }
            }
            return true;
        },
        (fiveCardHand) -> ArrayUtils.reverse(fiveCardHand.getCards())
    ),
    FLUSH(
        (fiveCardHand) -> {
            for (int i = 1; i < fiveCardHand.getCards().length; i++) {
                if (fiveCardHand.getCards()[i].getSuit() != fiveCardHand.getCards()[i - 1].getSuit()) {
                    return false;
                }
            }
            return true;
        },
        (fiveCardHand) -> ArrayUtils.reverse(fiveCardHand.getCards())
    ),
    FULL_HOUSE(
        (fiveCardHand) -> {
            Card[] cards = fiveCardHand.getCards();
            return
                (cards[0].getRank() == cards[1].getRank()
                && cards[1].getRank() == cards[2].getRank()
                && cards[3].getRank() == cards[4].getRank() 
            ) || (
                cards[0].getRank() == cards[1].getRank()
                && cards[2].getRank() == cards[3].getRank()
                && cards[3].getRank() == cards[4].getRank());
        },
        (fiveCardHand) -> {
            Card[] cards = fiveCardHand.getCards();
            if (cards[1].getRank() != cards[2].getRank()) {
                Card temp = cards[0];
                cards[0] = cards[3];
                cards[3] = cards[1];
                cards[1] = cards[4];
                cards[4] = temp;
            }
            return;
        }
    ),
    FOUR_OF_A_KIND(
        (fiveCardHand) -> {
            Card[] cards = fiveCardHand.getCards();
            return
                    (cards[0].getRank() == cards[1].getRank() &&
                    cards[1].getRank() == cards[2].getRank() &&
                    cards[2].getRank() == cards[3].getRank()
                ) || (
                    cards[1].getRank() == cards[2].getRank() &&
                    cards[2].getRank() == cards[3].getRank() &&
                    cards[3].getRank() == cards[4].getRank());
        },
        (fiveCardHand) -> {
            Card[] cards = fiveCardHand.getCards();
            if (cards[0].getRank() == cards[1].getRank()) {
                //do nothing
            } else {
                Card temp = cards[0];
                cards[0] = cards[1];
                cards[1] = cards[2];
                cards[2] = cards[3];
                cards[3] = cards[4];
                cards[4] = temp;
            }
            return;
        }

    ),
    STRAIGHT_FLUSH(
        (fiveCardHand) -> {
            return HandType.STRAIGHT.test(fiveCardHand)
                && HandType.FLUSH.test(fiveCardHand);
        },
        (fiveCardHand) -> {
            Card[] cards = fiveCardHand.getCards();
            ArrayUtils.reverse(cards);
            // if first card ace, shift array 1 left
            if (cards[0].getRank() == Rank.ACE) {
                Card temp = cards[0];
                cards[0] = cards[1];
                cards[1] = cards[2];
                cards[2] = cards[3];
                cards[3] = cards[4];
                cards[4] = temp;
            }
            return;
        }


        
    ),
    ROYAL_FLUSH(
        (fiveCardHand) -> {
            return HandType.STRAIGHT_FLUSH.test(fiveCardHand)
                && fiveCardHand.getCards()[0].getRank() == Rank.TEN;
        },
        (fiveCardHand) -> ArrayUtils.reverse(fiveCardHand.getCards())
    );

    private Predicate<FiveCardHand> predicate;
    private Consumer<FiveCardHand> rearranger;
    
    //constructor
    HandType(Predicate<FiveCardHand> predicate, Consumer<FiveCardHand> rearranger) {
        this.predicate = predicate;
        this.rearranger = rearranger;
    }

    public boolean test(FiveCardHand hand) {
        Arrays.sort(hand.getCards());
        return predicate.test(hand);
    }

    public void rearrange(FiveCardHand hand) {
        rearranger.accept(hand);
    }

    static public HandType[] getRoyalFlushFirstHandTypes(){
        return new HandType[] {
            HandType.ROYAL_FLUSH,
            HandType.STRAIGHT_FLUSH,
            HandType.FOUR_OF_A_KIND,
            HandType.FULL_HOUSE,
            HandType.FLUSH,
            HandType.STRAIGHT,
            HandType.THREE_OF_A_KIND,
            HandType.TWO_PAIR,
            HandType.PAIR,
            HandType.HIGH_CARD
        };
    }

    static public HandType getBestHandType(FiveCardHand hand) {
        for (HandType handType : HandType.getRoyalFlushFirstHandTypes()) {
            if (handType.test(hand)) {
                return handType;
            }
        }
        return null;
    }
}
