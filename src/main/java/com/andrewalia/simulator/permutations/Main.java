package com.andrewalia.simulator.permutations;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

//import org.apache.commons.math3.util.Combinations;

import com.andrewalia.simulator.permutations.thread.ComparingThread;
import com.andrewalia.simulator.permutations.thread.ProducerThread;
import com.andrewalia.simulator.permutations.util.ShowdownResultsMap;




public class Main {
    
    private static final double PRODUCER_COMPARING_RATIO = 2.5;

    public static void main(int threadCount, int iterations) throws InterruptedException {
        final double startTime = System.currentTimeMillis();
        final Queue<int[]> queue = new ArrayDeque<>();
        final AtomicLong permutations = new AtomicLong(0);
        final ShowdownResultsMap globalShowdownResultsMap = new ShowdownResultsMap();

        final int producingThreadCount = (int) (threadCount / PRODUCER_COMPARING_RATIO);
        final int comparingThreadCount = threadCount - producingThreadCount;

        ProducerThread.setTotalThreadCount(producingThreadCount);

        final Thread[] producerThreads = new Thread[producingThreadCount];
        final Thread[] comparingThreads = new Thread[comparingThreadCount];

        
        System.out.println("Starting " + producingThreadCount + " producer threads and " + comparingThreadCount + " comparing threads.");

        for (int i = 0; i < producingThreadCount; i++) {
            producerThreads[i] = new Thread(new ProducerThread(queue));
            //set name to ProducerThread-1, ProducerThread-2, etc
            producerThreads[i].setName(ProducerThread.class.getSimpleName() + "-" + (i + 1));
        }

        for (int i = 0; i < comparingThreadCount; i++) {
            comparingThreads[i] = new Thread(new ComparingThread(queue, permutations, globalShowdownResultsMap));
            //set name to ComparingThread-1, ComparingThread-2, etc
            comparingThreads[i].setName(ComparingThread.class.getSimpleName() + "-" + (i + 1));
        }




        for (Thread thread : comparingThreads) {
            thread.start();
        }

        for (Thread thread : producerThreads) {
            thread.start();
        }





        Timer timer = new Timer();
        //use a timer to print the showdown results every 10 seconds
        timer.schedule(new TimerTask() { @Override public void run() {
                System.out.println("Permutations: " + permutations.get());
                System.out.println("Showdown results:\n" + globalShowdownResultsMap.toString());
                System.out.println("Queue size: " + queue.size());
        }}, 10000, 10000);

        for (Thread thread : producerThreads) {
            thread.join();
        }

        for (Thread thread : comparingThreads) {
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
