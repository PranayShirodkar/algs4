import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;

public class Solver {
    private boolean solvable;
    private LinkedList<Board> solution;

    private class SearchNode implements Comparable<SearchNode> {
        Board board;
        SearchNode prevNode;
        int moves;
        int manDist;

        private SearchNode(Board board, SearchNode prevNode) {
            this.board = board;
            this.prevNode = prevNode;
            if (prevNode == null) {
                this.moves = 0;
            } else {
                this.moves = prevNode.moves + 1;
            }
            this.manDist = board.manhattan();
        }

        public int compareTo(SearchNode that) { // compares the Manhattan priority function of 2 search nodes
            int result = Integer.compare(this.moves + this.manDist, that.moves + that.manDist);
            if (result == 0) result = Integer.compare(this.manDist, that.manDist);
            return result;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("No board!");
        solution = new LinkedList<>();
        Board initialTwin = initial.twin();

        // create minPQ structure
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>();
        SearchNode root = new SearchNode(initial, null);
        SearchNode rootTwin = new SearchNode(initialTwin, null);
        pq.insert(root);
        pqTwin.insert(rootTwin);
        SearchNode minNode;
        SearchNode minNodeTwin;

        // dequeue element from minPQ which has the smallest priority function value
        // enqueue its neighbors (ignore the neighbor if neighbor is equal to minNode's prevNode; we dont go backwards)
        // repeat until goalBoard is dequeued
        while (true) {
            minNode = pq.delMin();
            minNodeTwin = pqTwin.delMin();
            if (minNode.board.isGoal()) {
                solvable = true;
                break; // solution found
            }
            else if (minNodeTwin.board.isGoal()) {
                solvable =  false;
                break; // solution found
            }
            for (Board neighbor : minNode.board.neighbors()) {
                if ((minNode.prevNode == null) ||
                   (minNode.prevNode != null && !neighbor.equals(minNode.prevNode.board))) {
                    SearchNode nextSearchNode = new SearchNode(neighbor, minNode);
                    pq.insert(nextSearchNode);
                }
            }
            for (Board neighbor : minNodeTwin.board.neighbors()) {
                if ((minNodeTwin.prevNode == null) ||
                    (minNodeTwin.prevNode != null && !neighbor.equals(minNodeTwin.prevNode.board))) {
                    SearchNode nextSearchNode = new SearchNode(neighbor, minNodeTwin);
                    pqTwin.insert(nextSearchNode);
                }
            }
        }
        if (!solvable) return;
        while (minNode != null) {
            solution.addFirst(minNode.board);
            SearchNode tempNode = minNode.prevNode;
            minNode.prevNode = null;
            minNode = tempNode;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) return null;
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {

        // read in the board specified in the filename
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println(initial);
            StdOut.println("No solution possible");
        }
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
