public class CoordinatePair {
    private double x;  // x-coordinate
    private double y;  // y-coordinate

    // constructor: (first, second)
    public CoordinatePair(double x, double y) {
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

    // sets x-coordinate
    public void setX(double x) {
        this.x = x;
    }

    // sets y-coordinate
    public void setY(double y) {
        this.y = y;
    }

    public double getNorm() {
        return Math.sqrt(x*x+y*y);
    }

    // adds another coordinate pair (with coordinate pair addition)
    public void add(CoordinatePair c) {
        x += c.getX();
        y += c.getY();
    }

    // returns the sum of two coordinate pairs
    public static CoordinatePair add(CoordinatePair p1, CoordinatePair p2) {
        return new CoordinatePair(p1.getX()+p2.getX(), p1.getY()+p2.getY());
    }

    // returns the difference of two coordinate pairs
    public static CoordinatePair subtract(CoordinatePair p1, CoordinatePair p2) {
        return new CoordinatePair(p1.getX()-p2.getX(), p1.getY()-p2.getY());
    }

    // subtracts another coordinate pair
    public void subtract(CoordinatePair c) {
        x -= c.getX();
        y -= c.getY();
    }

    // scales coordinate pair
    public void scale(double r) {
        x *= r;
        y *= r;
    }

    public void normalize() {
        scale(1.0/getNorm());
    }

    // determines if two coordinate pairs are equal
    public boolean equals(Object o) {
        if (!(o instanceof CoordinatePair)) {
            return false;
        }
        CoordinatePair other = (CoordinatePair) o;
        return x == other.getX() && y == other.getY();
    }

    // outputs coordinate pair as string (x, y)
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public static double distance(CoordinatePair p1, CoordinatePair p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        return Math.sqrt(dx*dx + dy*dy);
    }
}
