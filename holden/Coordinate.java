public class Coordinate {
    private double x;  // x-coordinate
    private double y;  // y-coordinate

    // constructor: (first, second)
    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // returns x-coordinate
    public double getX() {
        return x;
    }

    // returns y-coordinate
    public double getY() {
        return y;
    }

    public double getNorm() {
        return Math.sqrt(x*x+y*y);
    }

    // adds another coordinate (with vector addition)
    public void add(Coordinate c) {
        x += c.getX();
        y += c.getY();
    }

    // returns the sum of two coordinates
    public static Coordinate add(Coordinate p1, Coordinate p2) {
        return new Coordinate(p1.getX()+p2.getX(), p1.getY()+p2.getY());
    }

    // returns the difference of two coordinates
    public static Coordinate subtract(Coordinate p1, Coordinate p2) {
        return new Coordinate(p1.getX()-p2.getX(), p1.getY()-p2.getY());
    }

    // subtracts another coordinate (with vector subtraction)
    public void subtract(Coordinate c) {
        x -= c.getX();
        y -= c.getY();
    }

    // scales coordinate
    public void scale(double r) {
        x *= r;
        y *= r;
    }

    // determines if two coordinates are equal
    public boolean equals(Object o) {
        if (!(o instanceof Coordinate)) {
            return false;
        }
        Coordinate other = (Coordinate) o;
        return x == other.getX() && y == other.getY();
    }

    // outputs coordinate pair as string (x, y)
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public static double distance(Coordinate p1, Coordinate p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        return Math.sqrt(dx*dx + dy*dy);
    }
}
