import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
        // handle error cases
        if (points == null) throw new IllegalArgumentException("No points!");
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException("null point!");
        }

        // prepare data
        Point[] localPoints = points.clone();
        Arrays.sort(localPoints); // so that we know every time we find 4 collinear points, index i & m are the end points
        segments = new ArrayList<>(points.length*points.length);

        // handle duplicate points error
        for (int i = 0; i < localPoints.length-1; i++) {
            if (localPoints[i].compareTo(localPoints[i+1]) == 0) { throw new IllegalArgumentException("duplicate point!"); }
        }

        // handle case where there are definitely no line segments to be found
        if (points.length < 4) {
            return;
        }

        // finds all line segments containing 4 points
        for (int i = 0; i < localPoints.length; i++) {
            for (int j = i + 1; j < localPoints.length; j++) {
                for (int k = j + 1; k < localPoints.length; k++) {
                    for (int m = k + 1; m < localPoints.length; m++) {
                        if ((localPoints[i].slopeTo(localPoints[j]) == localPoints[i].slopeTo(localPoints[k])) &&
                            (localPoints[i].slopeTo(localPoints[k]) == localPoints[i].slopeTo(localPoints[m]))) {
                            segments.add(new LineSegment(localPoints[i], localPoints[m]));
                        }
                    }
                }
            }
        }
        segments.trimToSize();
    }

    public int numberOfSegments() {       // the number of line segments
        return segments.size();
    }
    public LineSegment[] segments() {               // the line segments
        return segments.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
