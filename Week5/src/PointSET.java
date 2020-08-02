import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {
    private final SET<Point2D> points;

    public PointSET() { // construct an empty set of points
        points = new SET<Point2D>();
    }
    public boolean isEmpty() { // is the set empty?
        return points.isEmpty();
    }
    public int size() { // number of points in the set
        return points.size();
    }
    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException("null point!");
        points.add(p);
    }
    public boolean contains(Point2D p) { // does the set contain point p?
        if (p == null) throw new IllegalArgumentException("null point!");
        return points.contains(p);
    }
    public void draw() { // draw all points to standard draw
        for (Point2D point : points) {
            point.draw();
        }
    }
    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException("null rectangle!");
        ArrayList<Point2D> pointsInRect = new ArrayList<Point2D>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                pointsInRect.add(point);
            }
        }
        return pointsInRect;
    }
    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException("null point!");
        if (points.isEmpty()) return null;
        Point2D nearestPoint = points.max();
        for (Point2D point : points) {
            if (point.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) {
                nearestPoint = point;
            }
        }
        return new Point2D(nearestPoint.x(), nearestPoint.y());
    }

    public static void main(String[] args) { // unit testing of the methods (optional)
        // unit test code
    }
}
