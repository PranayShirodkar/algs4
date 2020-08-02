import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first = null;
    private Node last = null;
    private int n = 0;

    // inner node class
    private class Node
    {
        Item item;
        Node next;
        Node prev;
    }
    // construct an empty deque
    public Deque() {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Cannot add null item! Check before adding!");
        Node newFirst = new Node();
        newFirst.item = item;
        if (first != null) {
            newFirst.next = first;
            first.prev = newFirst;
        }
        else {
            last = newFirst;
        }
        first = newFirst;
        n++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Cannot add null item! Check before adding!");
        Node newLast = new Node();
        newLast.item = item;
        if (last != null) {
            last.next = newLast;
            newLast.prev = last;
        }
        else {
            first = newLast;
        }
        last = newLast;
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (n == 0) throw new NoSuchElementException("The Deque is empty! Check before removing!");
        Node oldFirst = first;
        first = first.next;
        oldFirst.next = null;
        if (first != null) {
            first.prev = null;
        }
        else {
            last = null;
        }
        n--;
        return oldFirst.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (n == 0) throw new NoSuchElementException("The Deque is empty! Check before removing!");
        Node oldLast = last;
        last = last.prev;
        oldLast.prev = null;
        if (last != null) {
            last.next = null;
        }
        else {
            first = null;
        }
        n--;
        return oldLast.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() { return new DequeForwardIterator(); }

    private class DequeForwardIterator implements Iterator<Item> {
        private Node curr = first;
        public boolean hasNext() { return curr != null; }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("Iterator reached the end! Check before calling next!");
            Item item = curr.item;
            curr = curr.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        while (!StdIn.isEmpty()) {
            int elem = StdIn.readInt();
            if ((elem < 0) && (!deque.isEmpty())) {
                StdOut.println("remove first:" + deque.removeFirst());
            }
            else if ((elem == 0) && (deque.size() != 0)) {
                StdOut.println("remove last:" + deque.removeLast());
            }
            else if ((elem > 0) && (elem % 2 == 0)) {
                deque.addFirst(elem);
            }
            else if ((elem > 0) && (elem % 2 == 1)) {
                deque.addLast(elem);
            }
        }
        StdOut.print("From the front: ");
        for (Integer elem : deque) {
            StdOut.print(elem + " ");
        }
        StdOut.println();
    }

}
