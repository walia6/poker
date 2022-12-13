package com.andrewalia.simulator.permutations;

import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

//import org.apache.commons.math3.util.Combinations;

import com.andrewalia.simulator.permutations.thread.ComparingThread;
import com.andrewalia.simulator.permutations.util.ShowdownResultsMap;

import it.unimi.dsi.util.SplitMix64Random;



public class Main {
    public static void main(int threadCount, int iterations) throws InterruptedException {
        // new Combinations(Deck.FULL_DECK.size(), 9).iterator();
        //get start time
        double startTime = System.currentTimeMillis();
        final Iterator<int[]> iterator = new Iterator<int[]>() {
            private int i = 0;
            Random random = new SplitMix64Random();
            @Override
            public boolean hasNext() {
                return i++ < iterations;
            }

            @Override
            public int[] next() {
                return random.ints(0,52).distinct().limit(9).toArray();
            }
            
        };
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
