import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class MyResizingArrayBasedStack<Item> implements Iterable<Item> {

    private Item[] data;  // store of elements
    private int n;       // current number of elements

    public MyResizingArrayBasedStack() {
        data = (Item[]) new Object[1];
        n = 0;
    }

    public void push(Item elem) {
        // double the size of data array if it is full
        if (n == data.length) resize(n*2);
        data[n++] = elem;
    }

    public Item pop() {
        if (n == 0) throw new RuntimeException("The stack is empty! Check before popping!");
        Item topElem = data[--n];
        data[n] = null;
        if ( n > 0 && n == (data.length/4)) resize(data.length/2);
        return topElem;
    }

    private void resize(int size) {
        Item[] tempData = (Item[]) new Object[size];
        for (int i = 0; i < n; i++) {
            tempData[i] = data[i];
        }
        data = tempData;
    }

    public boolean isEmpty() {
        return (n == 0);
    }

    public Iterator<Item> iterator() { return new ReverseArrayIterator(); }

    private class ReverseArrayIterator implements Iterator<Item> {

        private int current = n;
        public boolean hasNext() { return current > 0; }
        public Item next() { return data[--current]; }

    }

    public static void main(String[] args) {
        // Create a stack of ints. Input ints 0 and above will get pushed onto the stack.
        // Negative input ints will pop the value at the top of the stack.
        // When there is no more input data, use an iterator to print all elements in the stack (starting from the top).
        MyResizingArrayBasedStack<Integer> stack = new MyResizingArrayBasedStack<Integer>();
        while (!StdIn.isEmpty()) {
            int elem = StdIn.readInt();
            if ((elem < 0) && (!stack.isEmpty())) {
                StdOut.println("out:" + stack.pop());
            }
            else if (elem >= 0) {
                stack.push(elem);
            }
        }
        StdOut.print("From the top: ");
        for (Integer elem : stack) {
            StdOut.print(elem + " ");
        }
        StdOut.println();
    }

}
