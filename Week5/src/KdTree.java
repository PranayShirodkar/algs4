import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Comparator;

public class KdTree {

    private static final int DIMENSIONS = 2;
    private Node root;
    private int size;

    private class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // for drawing
        private Node lb;              // the left/bottom subtree
        private Node rt;              // the right/top subtree

        Node(Point2D p, RectHV rect) {
            this.p = new Point2D(p.x(), p.y());
            this.rect = rect;
        }
        public void draw(int dim) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(this.p.x(), this.p.y());
            switch(dim) {
                case 0:
                    StdDraw.setPenColor(StdDraw.RED);
                    StdDraw.setPenRadius();
                    StdDraw.line(p.x(), rect.ymin(), p.x(), rect.ymax());
                    break;
                case 1:
                    StdDraw.setPenColor(StdDraw.BLUE);
                    StdDraw.setPenRadius();
                    StdDraw.line(rect.xmin(), p.y(), rect.xmax(), p.y());
                    break;
                default:
            }
        }
    }

    public KdTree() { // construct an empty set of points

    }
    public boolean isEmpty() { // is the set empty?
        return root == null;
    }
    public int size() { // number of points in the set
        return this.size;
    }
    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException("null point!");
        root = insert(root, p, 0, new RectHV(0, 0, 1, 1));
    }
    private Node insert(Node node, Point2D p, int dim, RectHV rect) { // add the point to the set (if it is not already in the set)
        if (node == null) {
            this.size++;
            return new Node(p, rect);
        }
        Comparator<Point2D> c = null;
        RectHV subRect = null;
        int cmp;
        switch(dim) {
            case 0:
                c = Point2D.X_ORDER;
                cmp = c.compare(p, node.p);
                if      (cmp < 0  && node.lb != null) subRect = node.lb.rect;
                else if (cmp >= 0 && node.rt != null) subRect = node.rt.rect;
                else if (cmp < 0)                     subRect = new RectHV(rect.xmin(), rect.ymin(), node.p.x(), rect.ymax());
                else                                  subRect = new RectHV(node.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                break;
            case 1:
                c = Point2D.Y_ORDER;
                cmp = c.compare(p, node.p);
                if      (cmp < 0  && node.lb != null) subRect = node.lb.rect;
                else if (cmp >= 0 && node.rt != null) subRect = node.rt.rect;
                else if (cmp < 0)                     subRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.p.y());
                else                                  subRect = new RectHV(rect.xmin(), node.p.y(), rect.xmax(), rect.ymax());
                break;
            default:
                throw new RuntimeException("Error! We are confined to 2D points!");
        }
        if      (cmp < 0) {
            node.lb = insert(node.lb, p, (dim+1) % DIMENSIONS, subRect);
        }
        else if (cmp > 0 || !p.equals(node.p)) {
            // Case 1: cmp > 0
            // Case 2: cmp == 0 && !p.equals(node.p)
            // if we are in Case 2, the inserted point p has the same value
            // for the compared coordinate but it is not identical to node.p
            node.rt = insert(node.rt, p, (dim+1) % DIMENSIONS, subRect);
        }
        return node;
    }
    public boolean contains(Point2D p) { // does the set contain point p?
        if (p == null) throw new IllegalArgumentException("null point!");
        Node node = root;
        int dim = 0;
        while (node != null) {
            Comparator<Point2D> c = null;
            switch(dim) {
                case 0:
                    c = Point2D.X_ORDER;
                    break;
                case 1:
                    c = Point2D.Y_ORDER;
                    break;
                default:
                    throw new RuntimeException("Error! We are confined to 2D points!");
            }
            int cmp = c.compare(p, node.p);
            if      (cmp < 0) node = node.lb;
            else if (cmp > 0 || !p.equals(node.p)) node = node.rt;
            else return true; // we found it
            dim = (dim+1) % DIMENSIONS; // not found yet, keep going
        }
        return false;
    }
    public void draw() { // draw all points to standard draw
        draw(root, 0);
    }
    private void draw(Node node, int dim) {
        if (node == null) return;
        draw(node.lb, (dim+1) % DIMENSIONS);
        node.draw(dim);
        draw(node.rt, (dim+1) % DIMENSIONS);
    }
    public Iterable<Point2D> range(RectHV rect) { // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException("null rectangle!");
        if (isEmpty()) return null;
        ArrayList<Point2D> pointsInRect = new ArrayList<Point2D>();
        range(root, rect, pointsInRect);
        return pointsInRect;
    }
    private void range(Node node, RectHV rect, ArrayList<Point2D> pointsInRect) { // all points that are inside the rectangle (or on the boundary)
        if (node == null) return;
        if (node.lb != null && rect.intersects(node.lb.rect)) range(node.lb, rect, pointsInRect);
        if (rect.contains(node.p)) {
            pointsInRect.add(node.p);
        }
        if (node.rt != null && rect.intersects(node.rt.rect)) range(node.rt, rect, pointsInRect);
    }
    public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException("null point!");
        if (isEmpty()) return null;
        return nearest(root, p, root.p, 0);
    }
    private Point2D nearest(Node node, Point2D p, Point2D nearestPoint, int dim) {
        if (node.p.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) nearestPoint = node.p;
        Comparator<Point2D> c = null;
        switch(dim) {
            case 0:
                c = Point2D.X_ORDER;
                break;
            case 1:
                c = Point2D.Y_ORDER;
                break;
            default:
                throw new RuntimeException("Error! We are confined to 2D points!");
        }
        int cmp = c.compare(p, node.p);
        if      (cmp < 0) {
            if (node.lb != null && node.lb.rect.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) {
                Point2D potentialNearest = nearest(node.lb, p, nearestPoint, (dim+1) % DIMENSIONS);
                if (potentialNearest.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) nearestPoint = potentialNearest;
            }
            if (node.rt != null && node.rt.rect.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) {
                Point2D potentialNearest = nearest(node.rt, p, nearestPoint, (dim+1) % DIMENSIONS);
                if (potentialNearest.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) nearestPoint = potentialNearest;
            }
        }
        else if (cmp > 0 || !p.equals(node.p)) {
            if (node.rt != null && node.rt.rect.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) {
                Point2D potentialNearest = nearest(node.rt, p, nearestPoint, (dim+1) % DIMENSIONS);
                if (potentialNearest.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) nearestPoint = potentialNearest;
            }
            if (node.lb != null && node.lb.rect.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) {
                Point2D potentialNearest = nearest(node.lb, p, nearestPoint, (dim+1) % DIMENSIONS);
                if (potentialNearest.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) nearestPoint = potentialNearest;
            }
        }
        return nearestPoint;
    }
    public static void main(String[] args) { // unit testing of the methods (optional)
    }
}
