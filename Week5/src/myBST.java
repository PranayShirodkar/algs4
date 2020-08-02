import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class myBST<Key extends Comparable<Key>, Value> {

    private Node root;

    private class Node {
        private Key key;
        private Value val;
        private Node left;          // the left subtree
        private Node right;         // the right subtree
        private int size;           // number of nodes in this subtree

        public Node(Key key, Value val) {
            this.key = key;
            this.val = val;
            this.size = 1;
        }
    }

    public myBST() { // construct an empty tree
    }

    public void put(Key key, Value val) {
        if (val == null) throw new IllegalArgumentException("value is null");
        if (key == null) throw new IllegalArgumentException("key is null");
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val);
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left = put(x.left, key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else              x.val = val;
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    public Value get(Key key) {
        Node x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if      (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else              return x.val;
        }
        return null;
    }

    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("key is null");
        put(root, key, null);
    }

    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("key is null");
        return get(key) != null;
    }

    public boolean isEmpty() { return root == null; }

    public int size() { return size(root); }

    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }

    public Iterable<Key> keys() {
        Queue<Key> q = new Queue<Key>();
        inorder(root, q);
        return q;
    }

    private void inorder(Node x, Queue<Key> q) {
        if (x == null) return;
        inorder(x.left, q);
        q.enqueue(x.key);
        inorder(x.right, q);
    }

    public Iterable<Key> range(Key lower, Key upper) {
        Queue<Key> q = new Queue<Key>();
        rangeinorder(root, q, lower, upper);
        return q;
    }

    private void rangeinorder(Node x, Queue<Key> q, Key lower, Key upper) {
        if (x == null) return;
        rangeinorder(x.left, q, lower, upper);
        if (lower.compareTo(x.key) < 0 && upper.compareTo(x.key) > 0)
            q.enqueue(x.key);
        rangeinorder(x.right, q, lower, upper);
    }

    public static void main(String[] args)
    {
        // frequency counter
        int minlen = Integer.parseInt(args[0]);
        myBST<String, Integer> st = new myBST<String, Integer>();
        while (!StdIn.isEmpty())
        {
            String word = StdIn.readString();
            if (word.length() < minlen) continue;
            if (!st.contains(word)) st.put(word, 1);
            else st.put(word, st.get(word) + 1);
        }
        String max = "";
        st.put(max, 0);
        for (String word : st.keys()) {
            StdOut.print(word + " ");
            if (st.get(word) > st.get(max))
                max = word;
        }
        StdOut.println();
        StdOut.println(max + " " + st.get(max));

        for (String word : st.range("e", "v")) {
            StdOut.print(word + " ");
        }
        StdOut.println();
    }

}
