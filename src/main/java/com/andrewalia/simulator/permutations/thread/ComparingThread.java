package com.andrewalia.simulator.permutations.thread;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

import org.javatuples.Triplet;

import com.andrewalia.simulator.permutations.util.ShowdownResultsMap;
import com.andrewalia.util.Card;
import com.andrewalia.util.Deck;
import com.andrewalia.util.HoleCards;
import com.andrewalia.util.NCardHand;

public class ComparingThread implements Runnable {

    private static final int REPORTING_INTERVAL = 1000000;
    private final Iterator<int[]> iterator;
    private final ShowdownResultsMap localShowdownResultsMap;
    private final AtomicLong permutations;
    private final ShowdownResultsMap globalShowdownResultsMap;
    private long reports;

    public ComparingThread(Iterator<int[]> iterator, AtomicLong permutations, ShowdownResultsMap globalShowdownResultsMap) {
        this.iterator = iterator;
        this.permutations = permutations;
        this.localShowdownResultsMap = new ShowdownResultsMap();
        this.reports = 0;
        this.globalShowdownResultsMap = globalShowdownResultsMap;
    }

    @Override
    public void run() {
        int[] cardIndexes;
        while (true) {
            synchronized(iterator) {
                if (!iterator.hasNext()) {
                    report();
                    break;
                }
                cardIndexes = iterator.next();
            }

            compareAndUpdateLocalShowdownResultsMap(cardIndexes);

            if (permutations.getAndIncrement() > (reports + 1 ) * REPORTING_INTERVAL) {
                report();
            }
        }
    }

    private void compareAndUpdateLocalShowdownResultsMap(int[] cardIndexes) {

        final HoleCards holeCards = HoleCards.valueOf(Deck.FULL_DECK.get(cardIndexes[0]), Deck.FULL_DECK.get(cardIndexes[1]));

        if (!localShowdownResultsMap.containsKey(holeCards)) {
            localShowdownResultsMap.put(holeCards, new Triplet<Long[],Long[],Long[]>(new Long[] {0L}, new Long[] {0L}, new Long[] {0L}));
        }

        final int comparison =
                new NCardHand( new Card[] {
                    Deck.FULL_DECK_ARRAY[cardIndexes[0]],
                    Deck.FULL_DECK_ARRAY[cardIndexes[1]],
                    Deck.FULL_DECK_ARRAY[cardIndexes[2]],
                    Deck.FULL_DECK_ARRAY[cardIndexes[3]],
                    Deck.FULL_DECK_ARRAY[cardIndexes[4]],
                    Deck.FULL_DECK_ARRAY[cardIndexes[5]],
                    Deck.FULL_DECK_ARRAY[cardIndexes[6]]})
            .compareTo(
                new NCardHand( new Card[] {
                    Deck.FULL_DECK_ARRAY[cardIndexes[7]],
                    Deck.FULL_DECK_ARRAY[cardIndexes[8]],
                    Deck.FULL_DECK_ARRAY[cardIndexes[2]],
                    Deck.FULL_DECK_ARRAY[cardIndexes[3]],
                    Deck.FULL_DECK_ARRAY[cardIndexes[4]],
                    Deck.FULL_DECK_ARRAY[cardIndexes[5]],
                    Deck.FULL_DECK_ARRAY[cardIndexes[6]]}));

        final Triplet<Long[],Long[],Long[]> localShowdownResults = localShowdownResultsMap.get(holeCards);
      
        if (comparison > 0) { // if the holeCards win
            localShowdownResults.getValue0()[0] = (localShowdownResultsMap.get(holeCards).getValue0()[0] + 1);
        } else if (comparison < 0) { // loss
            localShowdownResults.getValue1()[0] = (localShowdownResultsMap.get(holeCards).getValue1()[0] + 1);
        } else { // tie
            localShowdownResults.getValue2()[0] = (localShowdownResultsMap.get(holeCards).getValue2()[0] + 1);
        }
    }

    private void report(){
        synchronized (globalShowdownResultsMap) {
            for (final HoleCards holeCards : localShowdownResultsMap.keySet()) {
                if (!(globalShowdownResultsMap.containsKey(holeCards))) {
                    globalShowdownResultsMap.put(holeCards, new Triplet<Long[],Long[],Long[]>(new Long[] {0L}, new Long[] {0L}, new Long[] {0L}));
                }
                final Triplet<Long[],Long[],Long[]> localShowdownResults = localShowdownResultsMap.get(holeCards);
                final Triplet<Long[],Long[],Long[]> globalShowdownResults = globalShowdownResultsMap.get(holeCards);

                globalShowdownResults.getValue0()[0] = (globalShowdownResults.getValue0()[0] + localShowdownResults.getValue0()[0]);
                localShowdownResults.getValue0()[0] = 0L;

                globalShowdownResults.getValue1()[0] = (globalShowdownResults.getValue1()[0] + localShowdownResults.getValue1()[0]);
                localShowdownResults.getValue1()[0] = 0L;

                globalShowdownResults.getValue2()[0] = (globalShowdownResults.getValue2()[0] + localShowdownResults.getValue2()[0]);
                localShowdownResults.getValue2()[0] = 0L;
            }
        }
        reports++;
    }

    
}