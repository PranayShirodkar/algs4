import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class MyQuickFindUF {

    private int componentCount;
    private int[] componentId;   // componentId[i] = component identifier of node i

    public MyQuickFindUF(int n) {
        if (n < 0) throw new IllegalArgumentException();
        componentCount = n;
        componentId = new int[n];
        for (int i = 0; i < n; i++) {
            componentId[i] = i;
        }
    }

    public int componentCount() {
        return componentCount;
    }

    private void validate(int a) {
        if (a < 0 || a >= componentId.length) {
            throw new IllegalArgumentException("index " + a + "is not in range");
        }
    }

    public boolean isConnected(int a, int b) {
        validate(a);
        validate(b);
        return componentId[a] == componentId[b];
    }

    public void union(int a, int b) {
        if (isConnected(a, b)) return;
        int aId = componentId[a];
        int bId = componentId[b];
        for (int i = 0; i < componentId.length; i++) {
            if (componentId[i] == aId) {
                componentId[i] = bId;
            }
        }
        componentCount--;
    }

    public void print() {
        for (int i = 0; i < componentId.length; i++) {
            StdOut.print(componentId[i] + " ");
        }
        StdOut.println();
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        MyQuickFindUF uf = new MyQuickFindUF(n);
        while (!StdIn.isEmpty()) {
            int a = 0, b = 0;
            a = StdIn.readInt();
            b = StdIn.readInt();
            uf.union(a, b);
        }
//        uf.print();
        StdOut.println(uf.componentCount() + " components");
    }

}
