import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class MyQuickUnionUF {

    private int componentCount;   // number of components
    private int[] parent;         // parent[i] = parent of node i

    public MyQuickUnionUF(int n) {
        if (n < 0) throw new IllegalArgumentException();
        componentCount = n;
        parent = new int[n];
        // initialize every node as a root node, parent is itself
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    public int componentCount() {
        return componentCount;
    }

    private int root(int a) {
        validate(a);
        while (a != parent[a]) {
            a = parent[a];
        }
        return a;
    }

    private void validate(int a) {
        if (a < 0 || a >= parent.length) {
            throw new IllegalArgumentException("index " + a + "is not in range");
        }
    }

    public boolean isConnected(int a, int b) {
        return root(a) == root(b);
    }

    public void union(int a, int b) {
        int aRoot = root(a);
        int bRoot = root(b);
        if (aRoot == bRoot) return;
        parent[aRoot] = bRoot;
        componentCount--;
    }

    public void print() {
        for (int i = 0; i < parent.length; i++) {
            StdOut.print(parent[i] + " ");
        }
        StdOut.println();
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        MyQuickUnionUF uf = new MyQuickUnionUF(n);
        while (!StdIn.isEmpty()) {
            int a = 0, b = 0;
            a = StdIn.readInt();
            b = StdIn.readInt();
            uf.union(a, b);
        }
        StdOut.println(uf.componentCount() + " components");
    }

}
