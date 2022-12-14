package com.andrewalia.simulator.permutations;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.math3.util.Combinations;

//import org.apache.commons.math3.util.Combinations;

import com.andrewalia.simulator.permutations.thread.ComparingThread;
import com.andrewalia.simulator.permutations.util.ShowdownResultsMap;
import com.andrewalia.util.Card;
import com.andrewalia.util.Deck;

import it.unimi.dsi.util.SplitMix64Random;



public class Main {
    private static final class IteratorImplementation implements Iterator<int[]> {
        private final int iterations;
        private int i = 0;

        private final Iterator<int[]> holeCardsIterator = new Combinations(52, 2).iterator();
        private Iterator<int[]> boardCardsIterator = new Combinations(50, 5).iterator();
        private Iterator<int[]> opponentHoleCardsIterator = new Combinations(45, 2).iterator();
        private final List<Card> deck = Arrays.asList(Deck.FULL_DECK_ARRAY);
        private final List<Integer> deckIndexes;
        private int[] boardCards = new int[5];
        private int[] opponentHoleCards = new int[2];
        private int[] holeCards = new int[2];
        Random random = new SplitMix64Random();
        int[] cardIndexes = new int[9];

        private IteratorImplementation(int iterations) {
            this.iterations = iterations;
            deckIndexes = new java.util.ArrayList<Integer>(deck.size());
            for (int i = 0; i < 52; i++) {
                deckIndexes.add(i);
            }
        }

        @Override
        public boolean hasNext() {
            if (!opponentHoleCardsIterator.hasNext()) {
                if (!boardCardsIterator.hasNext()) {
                    if (!holeCardsIterator.hasNext()) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public int[] next() {
            if (i++ > iterations) {
                throw new IllegalStateException("Exceeded iterations");
            }
            if (!opponentHoleCardsIterator.hasNext()) {
                if (!boardCardsIterator.hasNext()) {
                    if (!holeCardsIterator.hasNext()) {
                        throw new IllegalStateException("No more combinations");
                    }
                    deckIndexes.add(holeCards[0]);
                    deckIndexes.add(holeCards[1]);
                    holeCards = holeCardsIterator.next();
                    deckIndexes.add(holeCards[0]);
                    deckIndexes.add(holeCards[1]);
                    deckIndexes.add(boardCards[0]);
                    deckIndexes.add(boardCards[1]);
                    deckIndexes.add(boardCards[2]);
                    deckIndexes.add(boardCards[3]);
                    deckIndexes.add(boardCards[4]);
                    cardIndexes[0] = holeCards[0];
                    cardIndexes[1] = holeCards[1];
                    deckIndexes.remove(cardIndexes[0]);
                    deckIndexes.remove(cardIndexes[1]);
                    boardCardsIterator = new Combinations(50, 5).iterator();
                }
                boardboardCardsIterator.next();
                deckIndexes.add(boardCards[0]);
                deckIndexes.add(boardCards[1]);
                deckIndexes.add(boardCards[2]);
                deckIndexes.add(boardCards[3]);
                deckIndexes.add(boardCards[4]);
                cardIndexes[2] = boardCards[deckIndexes.remove(0)];
                cardIndexes[3] = boardCards[deckIndexes.remove(1)];
                cardIndexes[4] = boardCards[deckIndexes.remove(2)];
                cardIndexes[5] = boardCards[deckIndexes.remove(3)];
                cardIndexes[6] = boardCards[deckIndexes.remove(4)];
                opponentHoleCardsIterator = new Combinations(45, 2).iterator();
            }
            opponentHoleCards = opponentHoleCardsIterator.next();

            
            cardIndexes[0] = holeCards[0];
            cardIndexes[1] = holeCards[1];
            deckIndexes.remove(cardIndexes[0]);
            deckIndexes.remove(cardIndexes[1]);
            cardIndexes[2] = boardCards[0];
            cardIndexes[3] = boardCards[1];
            cardIndexes[4] = boardCards[2];
            cardIndexes[5] = boardCards[3];
            cardIndexes[6] = boardCards[4];
            cardIndexes[7] = opponentHoleCards[0];
            cardIndexes[8] = opponentHoleCards[1];

            return cardIndexes;
        }
    }

    public static void main(int threadCount, int iterations) throws InterruptedException {
        // new Combinations(Deck.FULL_DECK.size(), 9).iterator();
        //get start time
        double startTime = System.currentTimeMillis();
        final Iterator<int[]> iterator = new IteratorImplementation(iterations);
        final Thread[] threads = new Thread[threadCount];
        final AtomicLong permutations = new AtomicLong(0);
        final ShowdownResultsMap globalShowdownResultsMap = new ShowdownResultsMap();

        System.out.println("Starting " + threadCount + " threads");

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new ComparingThread(iterator, permutations, globalShowdownResultsMap));
            threads[i].start();
        }
        Timer timer = new Timer();
        //use a timer to print the showdown results every 10 seconds
        timer.schedule(new TimerTask() { @Override public void run() {
                System.out.println("Permutations: " + permutations.get());
                System.out.println("Showdown results:\n" + globalShowdownResultsMap.toString(10));
        }}, 10000, 10000);

        for (Thread thread : threads) {
            thread.join();
        }

        timer.cancel();

        System.out.println("Permutations: " + permutations.get());
        System.out.println("Showdown results:\n" + globalShowdownResultsMap);

        //print the time it took to run
        double endTime = System.currentTimeMillis();
        double totalTime = endTime - startTime;
        System.out.println("\nTotal time: " + totalTime + "ms");
    }
}
