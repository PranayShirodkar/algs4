import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;

public class Board {

    private int[] board; // use a 1-d array
    private final int n; // size of board
    private int hamDist; // hamming distance
    private int manDist; // manhattan distance

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException("No tiles!");
        if (tiles.length < 2) throw new IllegalArgumentException("Not enough tiles!");
        for (int[] row : tiles) {
            if (row == null) throw new IllegalArgumentException("No tiles!");
            if (tiles.length != row.length) throw new IllegalArgumentException("Board is not square!");
        }
        n = tiles.length;
        board = new int[n*n+1];
        int[] checkTilesBoard = new int[n*n];
        hamDist = 0;
        manDist = 0;
        for (int a = 0; a < n; a++) {
            for (int b = 0; b < n; b++) {
                if (tiles[a][b] < 0) throw new IllegalArgumentException("out of range!");
                if (tiles[a][b] >= n*n) throw new IllegalArgumentException("out of range!");
                int i = a*n + b + 1; // convert 2d current index (a,b) to 1d current index (i)
                board[i] = tiles[a][b];
                checkTilesBoard[tiles[a][b]] = tiles[a][b];
                if (tiles[a][b] == 0) {
                    board[0] = i; // store the location of the blank tile
                }
                if ((board[i] != 0) && (board[i] != i)) {
                    hamDist++;
                    int c = (board[i]-1)/n;
                    int d = (board[i]-1) % n; // convert 1d value board[i] to 2d goal index (c,d)
                    // use difference between current index (a,b) and goal index (c,d) for Manhattan distance
                    manDist += Math.abs(a - c) + Math.abs(b - d);
                }
            }
        }
        for (int i = 0; i < checkTilesBoard.length; i++) {
            // When number x is found in tiles, checkTilesBoard[x] was set to x.
            // Hence, if every number exists, every number should match its index.
            // If there is a mismatch, it means some number was missed, also implying that some number is duplicated
            if (checkTilesBoard[i] != i) throw new IllegalArgumentException("Tile set is incorrect!");
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder(n + "\n");
        for (int i = 1; i < board.length; i++) {
            s.append(String.format("%2d ", board[i]));
            if (i % n == 0) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hamDist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manDist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manDist == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) return false;
        for (int i = 0; i < board.length; i++) {
            if (this.board[i] != that.board[i]) {
                return false;
            }
        }
        return true;
    }

    private int blankLocation() {
        return board[0];
    }

    private void swapByIndex(int a, int b) {
        int temp = board[a];
        board[a] = board[b];
        board[b] = temp;
    }
    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighborsArr = new ArrayList<>(4);
        int currBlankR = (blankLocation()-1)/n;
        int currBlankC = (blankLocation()-1) % n; // convert 1d location of blank to 2d index (currBlankR, currBlankC)
        for (int i = -1; i < 2; i++) { // i = -1, 0, 1
            for (int j = -1; j < 2; j++) { // j = -1, 0, 1
                int distSqr = i * i + j * j;
                // distSqr = 0: itself, ignore it
                // distSqr = 1: left, right, up or down of current location, check it
                // distSqr = 2: diagonal of current location, ignore it
                if (distSqr != 1) continue;
                int nextBlankR = currBlankR + i;
                int nextBlankC = currBlankC + j;
                if (nextBlankR < 0 || nextBlankR > (n - 1) || nextBlankC < 0 || nextBlankC > (n - 1)) continue;
                // if we are here, we found a valid next blank location (nextBlankR, nextBlankC)
                // 1. copy 1D board into 2D tiles
                int[][] tiles = new int[n][n];
                for (int p = 0; p < n; p++) {
                    for (int q = 0; q < n; q++) {
                        tiles[p][q] = board[p*n+q+1];
                    }
                }
                // 2. move the blank location
                tiles[currBlankR][currBlankC] = tiles[nextBlankR][nextBlankC];
                tiles[nextBlankR][nextBlankC] = 0;
                // 3. use tiles to construct a new Board
                Board neighbor = new Board(tiles);
                // 4. add new Board to iterable Board list
                neighborsArr.add(neighbor);

            }
        }
        neighborsArr.trimToSize();
        return neighborsArr;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // chose 2 indices around the blank (this is arbitary, it does not need to be around the blank to be a twin)
        int indexA = blankLocation() - 1;
        int indexB = blankLocation() + 1;
        if (blankLocation() == 1) indexA += 3;
        if (blankLocation() == n*n) indexB -= 3;
        swapByIndex(indexA, indexB);
        int[][] tiles = new int[n][n];
        for (int p = 0; p < n; p++) {
            for (int q = 0; q < n; q++) {
                tiles[p][q] = board[p*n+q+1];
            }
        }
        swapByIndex(indexA, indexB); // swap it back after copying to tiles
        return new Board(tiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {

        // for each command-line argument
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Board initial = new Board(tiles);
            StdOut.println(initial.toString());
//            for (Board board : initial.neighbors())
//                StdOut.println(board);
//            StdOut.println(initial.twin());

        }
    }

}
