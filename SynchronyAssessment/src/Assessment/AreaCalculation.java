package Assessment;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class AreaCalculation {
	// Small epsilon value for precision comparison
    private static final double EPSILON = 1e-6;

    // Class representing a point with x and y coordinates
    static class Point {
        double x, y;

        // Constructor to initialize point
        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        // Getter method for y-coordinate
        public double getY() { return this.y; }

        // Getter method for x-coordinate
        public double getX() { return this.x; }

        // Override toString method to print point details
        @Override
        public String toString() {
            return "Point [x=" + x + ", y=" + y + "]";
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Point[] points = new Point[4];
        
        // Read the coordinates of four points 
        for (int i = 0; i < 4; i++) {
            System.out.println("Enter x and y coordinates of " + (i + 1) + " point:");
            points[i] = new Point(sc.nextDouble(), sc.nextDouble());
        }
        sc.close();

        // Sort the points
        sortPoints(points);

        // Calculate the area and determine the type of quadrilateral
        calculateArea(points);
    }

    // Sort points based on their y-coordinate and then x-coordinate
    private static void sortPoints(Point[] points) {
        Arrays.sort(points, Comparator.comparingDouble(Point::getY).thenComparingDouble(Point::getX));
        
        Point bottomLeft = points[0]; // Store the bottom-left point
        
        // Sort the remaining points based on the cross product of vectors
        Arrays.sort(points, 1, 4, (p1, p2) -> {
            double cross = (p1.x - bottomLeft.x) * (p2.y - bottomLeft.y) - 
                           (p1.y - bottomLeft.y) * (p2.x - bottomLeft.x);
            return Double.compare(cross, 0);
        });
    }

    // Calculate the area and type of quadrilateral
    private static void calculateArea(Point[] points) {
        Point a = points[0], b = points[1], c = points[2], d = points[3];

        // Calculate distances between consecutive points
        double ab = distance(a, b);
        double bc = distance(b, c);
        double cd = distance(c, d);
        double da = distance(d, a);

        // Calculate slopes between consecutive points
        double slopeab = slope(a, b);
        double slopebc = slope(b, c);
        double slopecd = slope(c, d);
        double slopeda = slope(d, a);
        double slopeac = slope(a, c);
        double slopebd = slope(b, d);

        // Default quadrilateral type is "other"
        String type = "other";
        
        // Check if the points are collinear
        if (arePointsCollinear(a, b, c, d)) {
            System.out.println(type + " " + (int)-1);
            return;
        }

        // Check different types of quadrilaterals and calculate area
        boolean parallelogram = areParallel(slopeab, slopecd) && areParallel(slopebc, slopeda) && (ab == cd) && (bc == da);
        boolean rectangle = parallelogram && (
            (slopeab == Double.POSITIVE_INFINITY && slopebc == 0) || 
            (slopebc == Double.POSITIVE_INFINITY && slopeab == 0) || 
            (Math.abs(slopeab * slopebc + 1) < 1e-6));
        boolean rhombus = parallelogram && (ab == bc && bc == cd && cd == da);
        boolean square = rectangle && rhombus;
        boolean trapezoid = areParallel(slopeab, slopecd) || areParallel(slopebc, slopeda);
        boolean kite = (ab == bc && cd == da) || (ab == da && bc == cd);

        double area = shoelace(points);

        // Set the type based on conditions
        if (square) {
            type = "Square";
        } else if (rectangle) {
            type = "Rectangle";
        } else if (rhombus) {
            type = "Rhombus";
        } else if (parallelogram) {
            type = "Parallelogram";
        } else if (kite) {
            type = "Kite";
        } else if (trapezoid) {
            type = "Trapezoid";
            // Adjust area calculation for trapezoid
            double base1 = ab;
            double base2 = cd;
            if (!areParallel(slopeab, slopecd)) {
                base1 = bc;
                base2 = da;
            }
            double height = (2 * area) / Math.abs(base1 + base2);
            area = 0.5 * (base1 + base2) * height;
        } else {
            area = -1;
        }

        // Print the result
        System.out.println(type + " " + (int)area);
    }

    // Check if two lines are parallel
    private static boolean areParallel(Double a, Double b) {
        return Math.abs(a - b) < 1e-12 || (a == Double.POSITIVE_INFINITY && b == Double.POSITIVE_INFINITY);
    }

    // Calculate the slope between two points
    private static double slope(Point a, Point b) {
        return (a.getX() - b.getX()) == 0 ? Double.POSITIVE_INFINITY : (a.getY() - b.getY()) / (a.getX() - b.getX());
    }

    // Calculate the distance between two points
    private static double distance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + (Math.pow(a.getY() - b.getY(), 2)));
    }

    // Calculate the area using the shoelace formula
    private static double shoelace(Point[] points) {
        return Math.abs(
            (points[0].x * points[1].y + points[1].x * points[2].y + points[2].x * points[3].y + points[3].x * points[0].y) -
            (points[1].x * points[0].y + points[2].x * points[1].y + points[3].x * points[2].y + points[0].x * points[3].y)
        ) / 2.0;
    }

    // Check if four points are collinear
    public static boolean arePointsCollinear(Point p1, Point p2, Point p3, Point p4) {
        return pointsCollinear(p1, p2, p3) && pointsCollinear(p1, p2, p4);
    }

    // Check if three points are collinear using the cross product approach
    private static boolean pointsCollinear(Point p1, Point p2, Point p3) {
        return (p2.y - p1.y) * (p3.x - p2.x) == (p3.y - p2.y) * (p2.x - p1.x);
    }
}
