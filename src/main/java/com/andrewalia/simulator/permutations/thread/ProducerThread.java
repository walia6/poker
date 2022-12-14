package com.andrewalia.simulator.permutations.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.math3.util.Combinations;

public class ProducerThread implements Runnable {

    private static int instanceCount = 0;
    private static int totalThreadCount = 0;
    private static boolean hasStarted = false;

    private final int instanceNumber;
    private final Queue<int[]> queue;
    private final List<Integer> availableFullDeckArrayIndexes = new ArrayList<>(52);
    private final int[] fullDeckArrayIndexes = new int[9];
    private int permutations;

    @Override
    public void run() {

        hasStarted = true;

        for (int[] holeCardIndexes : new Combinations(52, 2)) {

            fullDeckArrayIndexes[0] = holeCardIndexes[0];
            fullDeckArrayIndexes[1] = holeCardIndexes[1];

            availableFullDeckArrayIndexes.remove(holeCardIndexes[0]);
            availableFullDeckArrayIndexes.remove(holeCardIndexes[1]);

            for (int[] boardCardIndexes : new Combinations(50, 5)) {

                fullDeckArrayIndexes[2] = availableFullDeckArrayIndexes.get(boardCardIndexes[0]);
                fullDeckArrayIndexes[3] = availableFullDeckArrayIndexes.get(boardCardIndexes[1]);
                fullDeckArrayIndexes[4] = availableFullDeckArrayIndexes.get(boardCardIndexes[2]);
                fullDeckArrayIndexes[5] = availableFullDeckArrayIndexes.get(boardCardIndexes[3]);
                fullDeckArrayIndexes[6] = availableFullDeckArrayIndexes.get(boardCardIndexes[4]);
                
                availableFullDeckArrayIndexes.remove(boardCardIndexes[0]);
                availableFullDeckArrayIndexes.remove(boardCardIndexes[1]);
                availableFullDeckArrayIndexes.remove(boardCardIndexes[2]);
                availableFullDeckArrayIndexes.remove(boardCardIndexes[3]);
                availableFullDeckArrayIndexes.remove(boardCardIndexes[4]);

                for (int[] opponentHoleCardIndexes : new Combinations(45, 2)) {

                    if (permutations++ % totalThreadCount != instanceNumber) {
                        continue;
                    }

                    fullDeckArrayIndexes[7] = availableFullDeckArrayIndexes.get(opponentHoleCardIndexes[0]);
                    fullDeckArrayIndexes[8] = availableFullDeckArrayIndexes.get(opponentHoleCardIndexes[1]);

                    synchronized (queue) {
                        if (queue.size() > 10000) {
                            try {
                                queue.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        queue.add(fullDeckArrayIndexes.clone());
                        
                        queue.notifyAll();
                    }
                }

                availableFullDeckArrayIndexes.add(boardCardIndexes[0], fullDeckArrayIndexes[2]);
                availableFullDeckArrayIndexes.add(boardCardIndexes[1], fullDeckArrayIndexes[3]);
                availableFullDeckArrayIndexes.add(boardCardIndexes[2], fullDeckArrayIndexes[4]);
                availableFullDeckArrayIndexes.add(boardCardIndexes[3], fullDeckArrayIndexes[5]);
                availableFullDeckArrayIndexes.add(boardCardIndexes[4], fullDeckArrayIndexes[6]);
            }
            availableFullDeckArrayIndexes.add(holeCardIndexes[0], fullDeckArrayIndexes[0]);
            availableFullDeckArrayIndexes.add(holeCardIndexes[1], fullDeckArrayIndexes[1]);
        }
    }

    public ProducerThread(Queue<int[]> queue) {
        if (totalThreadCount == 0)
            throw new IllegalArgumentException("threadCount must be greater than 0");
        this.queue = queue;
        instanceNumber = instanceCount++;
        permutations = 0;

        for (int i = 0; i < 52; i++) {
            availableFullDeckArrayIndexes.add(i);
        }
    }

    public static void setTotalThreadCount(int totalThreadCount) {
        //must be >= 1
        if (totalThreadCount < 1)
            throw new IllegalArgumentException("totalThreadCount must be greater than 0");
        //must not have started yet
        if (hasStarted)
            throw new IllegalStateException("totalThreadCount cannot be changed after threads have started");
        ProducerThread.totalThreadCount = totalThreadCount;
    }
}
