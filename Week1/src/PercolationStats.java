/* *****************************************************************************
 *  Name: Pranay Shirodkar
 *  Date: 20th April 2020
 *  Description: Percolation Assignment, Week 1
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int trials;
    private double mean;
    private double stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0) throw new IllegalArgumentException("n is out of bounds");
        if (trials <= 0) throw new IllegalArgumentException("trials is out of bounds");
        this.trials = trials;
        this.mean = 0;
        this.stddev = 0;
        double[] percThreshold = new double[trials];
        for (int i = 0; i < percThreshold.length; i++) {
            percThreshold[i] = performSimulation(n);
        }
        mean = StdStats.mean(percThreshold);
        stddev = StdStats.stddev(percThreshold);

    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - ((1.96 * stddev) / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + ((1.96 * stddev) / Math.sqrt(trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        if (n <= 0) throw new IllegalArgumentException("n is out of bounds");
        if (trials <= 0) throw new IllegalArgumentException("trials is out of bounds");
        PercolationStats percStats = new PercolationStats(n, trials);
        StdOut.printf("%-23s = %.16f\n", "mean", percStats.mean());
        StdOut.printf("%-23s = %.16f\n", "stddev", percStats.stddev());
        StdOut.printf("%-23s = [%.16f, %.16f]\n", "95% confidence interval",
                      percStats.confidenceLo(),
                      percStats.confidenceHi());
    }

    private double performSimulation(int n) {
        Percolation perc = new Percolation(n);
        int[] shuffledArray = new int[n * n]; // shuffledArray[i] = node number
        for (int i = 0; i < shuffledArray.length; i++) {
            shuffledArray[i] = i;
        }
        StdRandom.shuffle(shuffledArray);
        // if n = 3, example shuffledArray = [4,6,2,7,1,8,3,5,0]
        // with this randomized array, convert node number to (row, col)
        // open (row, col) in shuffledArray order
        for (int i = 0; i < shuffledArray.length; i++) {
            int row = (shuffledArray[i] / n) + 1;
            int col = (shuffledArray[i] % n) + 1;
            perc.open(row, col);
            if (perc.percolates()) {
                break;
            }
        }
        return (double) perc.numberOfOpenSites() / (n * n);
    }
}
