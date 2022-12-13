package com.andrewalia;

import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import com.andrewalia.util.Card;
import com.andrewalia.util.Deck;
import com.andrewalia.util.FiveCardHand;
import com.andrewalia.util.HandType;


/**
 * Hello world!
 */
public final class Main {

    private Main() {}

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        int[][] handCounts = { { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } };
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int iterations = 25989600;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-t")) {
                availableProcessors = Integer.parseInt(args[i + 1]);
            } else if (args[i].equals("-i")) {
                iterations = Integer.parseInt(args[i + 1]);
            } else if (args[i].equals("-p")) {
                com.andrewalia.simulator.permutations.Main.main(availableProcessors);
                return;
            }
        }
        final int perThreadIterations = iterations / availableProcessors;

        randomFiveCardHistogram(handCounts, availableProcessors, perThreadIterations);
    }

    private static void randomFiveCardHistogram(int[][] handCounts, int availableProcessors, final int perThreadIterations) {
        Runnable runnable = () -> {
            int[] personalHandCounts = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            Random random = new XoRoShiRo128PlusRandom();
            for (int i = 0; i < perThreadIterations; i++) {
                //make an array of 5 card
                Card[] cards = new Card[5];
                //for each index of cards, set the value to a random card from FULL_DECK, making sure that the card is not already in the array
                for (int j = 0; j < cards.length; j++) {
                    Card card;
                    do card = Deck.FULL_DECK.get((int) (random.nextDouble() * Deck.FULL_DECK.size())); while (
                        ArrayUtils.contains(cards, card)
                    );
                    cards[j] = card;
                }

                FiveCardHand hand = new FiveCardHand(cards);
                if (HandType.ROYAL_FLUSH.test(hand)) personalHandCounts[0]++; else if (
                    HandType.STRAIGHT_FLUSH.test(hand)
                ) personalHandCounts[1]++; else if (HandType.FOUR_OF_A_KIND.test(hand)) personalHandCounts[2]++; else if (
                    HandType.FULL_HOUSE.test(hand)
                ) personalHandCounts[3]++; else if (HandType.FLUSH.test(hand)) personalHandCounts[4]++; else if (
                    HandType.STRAIGHT.test(hand)
                ) personalHandCounts[5]++; else if (HandType.THREE_OF_A_KIND.test(hand)) personalHandCounts[6]++; else if (
                    HandType.TWO_PAIR.test(hand)
                ) personalHandCounts[7]++; else if (
                    HandType.PAIR.test(hand)
                ) personalHandCounts[8]++; else personalHandCounts[9]++;
            }

            for (int i = 0; i < handCounts.length; i++) synchronized (handCounts[i]) {
                handCounts[i][0] += personalHandCounts[i];
            }
        };

        long begin = System.currentTimeMillis();

        Thread[] threads = new Thread[availableProcessors];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(runnable);
            threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("----------------------------------------------------");
        System.out.println("\tTime: " + (end - begin) + "ms");
        System.out.println("\tThreads: " + availableProcessors + "\n");

        System.out.println("\tRoyal Flushes: " + handCounts[0][0]);
        System.out.println("\tStraight Flushes: " + handCounts[1][0]);
        System.out.println("\tFour of a Kind: " + handCounts[2][0]);
        System.out.println("\tFull Houses: " + handCounts[3][0]);
        System.out.println("\tFlushes: " + handCounts[4][0]);
        System.out.println("\tStraights: " + handCounts[5][0]);
        System.out.println("\tThree of a Kind: " + handCounts[6][0]);
        System.out.println("\tTwo Pair: " + handCounts[7][0]);
        System.out.println("\tPairs: " + handCounts[8][0]);
        System.out.println("\tHigh Card: " + handCounts[9][0]);
        System.out.println();
    }
}
