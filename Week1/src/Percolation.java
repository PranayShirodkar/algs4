/* *****************************************************************************
 *  Name: Pranay Shirodkar
 *  Date: 20th April 2020
 *  Description: Percolation Assignment, Week 1
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int n;                     // grid has n x n sites
    private boolean[][] siteState;     // siteState[i][j] = open or close
    private int openSitesCount;        // number of sites open
    private WeightedQuickUnionUF uf;   // captures the adjacency between sites
    private WeightedQuickUnionUF uf2;  // Prevents backwash. See isFull()

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        siteState = new boolean[n][n];
        openSitesCount = 0;

        // To determine if the system percolates, we need to call
        // uf.connected() between every top site and bottom site.
        // However, by adding:
        // 1 virtual node connected to all the sites in the top row
        // 1 virtual node connected to all the sites in the bottom row
        // We can just call uf.connected() once between the 2 virtual nodes
        // to determine if the system percolates.
        uf = new WeightedQuickUnionUF((n * n) + 2);
        uf2 = new WeightedQuickUnionUF((n * n) + 2);

        // now uf contains nodes numbered from 0 to (n * n) + 1
        // make node 0 and node (n * n) + 1 the virtual nodes
        /*
        Example: if n = 4, after initialization:
        virtual node 0 is connected to node 1,2,3,4
        virtual node 17 is connected to node 13,14,15,16
        Grid (sites labelled with their node number in the uf struct):
            0
        1  2  3  4
        5  6  7  8
        9 10 11 12
       13 14 15 16
            17
         */
        for (int i = 1; i <= n; i++) {
            // connect top row to top virtual node
            uf.union(0, i);
            uf2.union(0, i);
            // connect bottom row to bottom virtual node
            // to prevent backwash, dont connect in uf2. See isFull()
            uf.union((n * n) + 1, (n * n) + 1 - i);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // validation of row and col is done in isOpen()
        if (isOpen(row, col)) return;
        openSitesCount++;
        siteState[row - 1][col - 1] = true;
        // if up down left right sites are open, then perform union with it
        connectAdjacentSites(row, col);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row);
        validate(col);
        return siteState[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // validation of row and col is done in isOpen()
        if (!isOpen(row, col)) {
            return false;
        }
        // Backwash is a false positive for isFull method and arises after
        // system percolates. In uf, site is not connected to top directly,
        // but is connected through the bottom virtual node.
        // Use uf2 instead of uf in isFull() to prevent backwash.
        // In uf2 bottom row is not connected to bottom virtual node
        return uf2.connected(0, rowColToNode(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesCount;
    }

    // does the system percolate?
    public boolean percolates() {
        if (n == 1) {
            // if n = 1, we cannot rely on the uf structure, because
            // both virtual nodes are connected to node 1, so uf.connected()
            // will always return true. Instead we use siteState
            return siteState[0][0];
        }
        else {
            return uf.connected(0, (n * n) + 1);
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation perc = new Percolation(n);
        while (!StdIn.isEmpty()) {
            int row = 0, col = 0;
            row = StdIn.readInt();
            col = StdIn.readInt();
            perc.open(row, col);
        }
        StdOut.println(perc.percolates());
    }

    // check if the row or col index requested is valid
    private void validate(int index) {
        if (index < 1 || index > n) {
            throw new IllegalArgumentException("index is out of bounds");
        }
    }

    // convert from row & col to corresponding node in the uf structure
    private int rowColToNode(int row, int col) {
        return ((row - 1) * n) + col;
    }

    // helper called by open()
    // check site left, right, up and down of (row, col)
    // if site is within bounds and site is open, connect the nodes
    private void connectAdjacentSites(int row, int col) {
        for (int i = -1; i < 2; i++) { // i = -1, 0, 1
            for (int j = -1; j < 2; j++) { // j = -1, 0, 1
                int distSqr = i * i + j * j;
                // distSqr = 1: the site is left, right, up or down, check it
                // distSqr = 2: the site is diagonally adjacent, ignore it
                // distSqr = 0: itself, ignore it
                if (distSqr != 1)
                    continue;
                if ((row + i) < 1 || (row + i) > n || (col + j) < 1 || (col + j) > n)
                    continue;
                if (!isOpen(row + i, col + j))
                    continue;
                uf.union(rowColToNode(row, col), rowColToNode(row + i, col + j));
                uf2.union(rowColToNode(row, col), rowColToNode(row + i, col + j));
            }
        }
    }
}
