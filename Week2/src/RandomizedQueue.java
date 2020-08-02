import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] data; // queue of items
    private int n;       // position where next item is to be enqueued. Also number of items currently in the queue

    // construct an empty randomized queue
    public RandomizedQueue() {
        data = (Item[]) new Object[1];
        n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    private void resize(int capacity) {
        Item[] tempData = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            tempData[i] = data[i];
        }
        data = tempData;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Cannot enqueue null item! Check before adding!");
        if (data.length == n) {
            n = data.length;
            resize(n*2);
        }
        data[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (n == 0) throw new NoSuchElementException("The queue is empty! Check before removing!");
        // select a random Item to dequeue
        int dequeueIndex = StdRandom.uniform(n);
        Item dequeueItem = data[dequeueIndex];
        n--;
        // move most recent element to the position of the random element
        data[dequeueIndex] = data[n];
        data[n] = null;
        if (n > 0 && n == (data.length/4)) resize(data.length/2);
        return dequeueItem;

    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (n == 0) throw new NoSuchElementException("The queue is empty! Nothing to sample!");
        // select a random Item to sample
        return data[StdRandom.uniform(n)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() { return new RandomIterator(); }

    private class ArrayIterator implements Iterator<Item> {
        private int curr = 0;
        public boolean hasNext() { return curr < n; }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("Iterator reached the end! Check before calling next!");
            return data[curr++];
        }
    }

    private class RandomIterator implements Iterator<Item> {
        private Item[] randomOrder;
        private int curr;
        private RandomIterator() {
            curr = 0;
            randomOrder = (Item[]) new Object[n];
            for (int i = 0; i < n; i++) {
                randomOrder[i] = data[i];
            }
            StdRandom.shuffle(randomOrder);
        }
        public boolean hasNext() { return curr < randomOrder.length; }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("Iterator reached the end! Check before calling next!");
            return randomOrder[curr++];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {

        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<Integer>();
        while (!StdIn.isEmpty()) {
            int val = StdIn.readInt();
            if (val > 0) {
                randomizedQueue.enqueue(val);
            }
            else if (val == 0 && !randomizedQueue.isEmpty()) {
                StdOut.println(randomizedQueue.dequeue());
                for (Integer elem : randomizedQueue) {
                    StdOut.print(elem + " ");
                }
            }
            else if (val == -1 && randomizedQueue.size() != 0) {
                StdOut.println(randomizedQueue.sample());
            }
            else if (val == -2) {
                for (Integer elem : randomizedQueue) {
                    StdOut.print(elem + " ");
                }
                StdOut.println();
            }
            else if (val == -3) {
                for (Integer elem : randomizedQueue) {
                    StdOut.println(elem);
                    for (Integer elem2 : randomizedQueue) {
                        StdOut.print(elem2 + " ");
                    }
                    StdOut.println();
                }
            }
        }
    }

}
