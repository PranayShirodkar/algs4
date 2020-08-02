import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        // handle error cases
        if (points == null) throw new IllegalArgumentException("No points!");
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException("null point!");
        }

        // prepare data
        Point[] localPoints = points.clone();
        Point[] slopeOrderedPoints = points.clone();
        Arrays.sort(localPoints);
        Arrays.sort(slopeOrderedPoints);
        segments = new ArrayList<>(points.length*points.length);

        // handle duplicate points error
        for (int i = 0; i < localPoints.length-1; i++) {
            if (localPoints[i].compareTo(localPoints[i+1]) == 0) { throw new IllegalArgumentException("duplicate point!"); }
        }

        // handle case where there are definitely no line segments to be found
        if (points.length < 4) {
            return;
        }

        // finds all line segments containing 4 or more points
        for (Point origin : localPoints) { // for every point, sort all the points in slope order wrt the origin
            Comparator<Point> c = origin.slopeOrder();
            Arrays.sort(slopeOrderedPoints, c); // origin will always be the same as origin
            double currSlope = origin.slopeTo(slopeOrderedPoints[1]);
            double newSlope = 0;
            int collinearPointsFound = 0;
            int currIndex = 1;
            while (currIndex != slopeOrderedPoints.length) {
                newSlope = origin.slopeTo(slopeOrderedPoints[currIndex]);
                if (currSlope == newSlope) {
                    // found a point collinear with origin
                    collinearPointsFound++;
                }
                else {
                    if (collinearPointsFound >= 3) {
                        addLineSegment(slopeOrderedPoints, collinearPointsFound, currIndex-collinearPointsFound);
                    }
                    currSlope = newSlope;
                    collinearPointsFound = 1;
                }
                currIndex++;
            }
            if (collinearPointsFound >= 3) {
                addLineSegment(slopeOrderedPoints, collinearPointsFound, currIndex-collinearPointsFound);
            }
        }
        segments.trimToSize();
    }

    private void addLineSegment(Point[] slopeOrderedPoints, int collinearPointsFound, int startIndex) {
        Point origin = slopeOrderedPoints[0];
        // sort the collinear points in slopeOrderedPoints
        Arrays.sort(slopeOrderedPoints, startIndex, startIndex + collinearPointsFound);
        if (origin.compareTo(slopeOrderedPoints[startIndex]) < 0) {
            // check if origin is the minimum point of the collinear points
            // if yes, add the line segment
            // (if we always add line segment without checking this, we get duplicate segments)
            segments.add(new LineSegment(origin, slopeOrderedPoints[startIndex + collinearPointsFound-1]));
        }
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdOut.println(collinear.numberOfSegments());
        StdDraw.show();
    }
}
