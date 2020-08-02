import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.NoSuchElementException;

public class Permutation {
    public static void main(String[] args) {
        if (args.length != 1) throw new NoSuchElementException("The queue is empty! Nothing to sample!");
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<String>();

        // normal method with randomizedQueue of size n
//        while (!StdIn.isEmpty()) {
//            randomizedQueue.enqueue(StdIn.readString());
//        }
//        for (int i = 0; i < k; i++) {
//            StdOut.println(randomizedQueue.dequeue());
//        }

        // special method with randomizedQueue of size at most k
        for (int i = 0; i < k; i++) {
            randomizedQueue.enqueue(StdIn.readString());
        }
        int elemCount = k;
        while (!StdIn.isEmpty()) {
            String temp = StdIn.readString();
            elemCount++;
            boolean p = StdRandom.bernoulli((double)k/(elemCount));
            if (p) {
                randomizedQueue.dequeue();
                randomizedQueue.enqueue(temp);
            }
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(randomizedQueue.dequeue());
        }
    }
}
