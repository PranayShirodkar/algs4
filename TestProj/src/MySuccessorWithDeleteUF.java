import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/*PROBLEM:
Successor with delete. Given a set of n integers S = { 0, 1, ... , n-1 } and a sequence of requests of the following form:

        Remove x from S
        Find the successor of x: the smallest y in S such that y>=x.
        design a data type so that all operations (except construction) take logarithmic time or better in the worst case.
*/

public class MySuccessorWithDeleteUF {

    private int[] left;           // left[i] = left of node i
    private int[] right;          // right[i] = right of node i

    public MySuccessorWithDeleteUF(int n) {
        if (n < 0) throw new IllegalArgumentException();
        left = new int[n];
        right = new int[n];
        // initialize every node as a root node, left is itself
        for (int i = 0; i < n; i++) {
            right[i] = i+1;
            left[i] = i-1;
        }
        left[0] = 0;
        right[n-1] = n-1;
    }

    private void validate(int a) {
        if (a < 0 || a >= left.length || left[a] == -1 || right[a] == -1) {
            throw new IllegalArgumentException("index " + a + "is not in range or this number has already been removed");
        }
    }

    public void print() {
        StdOut.print("left:  ");
        for (int i = 0; i < left.length; i++) {
            StdOut.print(left[i] + " ");
        }
        StdOut.println();
        StdOut.print("right: ");
        for (int i = 0; i < right.length; i++) {
            StdOut.print(right[i] + " ");
        }
        StdOut.println();
    }

    public int remove(int x) {
        validate(x);
        int xLeft = left[x];
        int xRight = right[x];
        int successor = xLeft;
        right[xLeft] = xRight;
        left[xRight] = xLeft;
        left[x] = right[x] = -1;
        return successor;
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        MySuccessorWithDeleteUF uf = new MySuccessorWithDeleteUF(n);
        uf.print();
        while (!StdIn.isEmpty()) {
            int a = 0;
            a = StdIn.readInt();
            int successor = uf.remove(a);
            StdOut.println("successor: " + successor);
            uf.print();
        }
    }

}
