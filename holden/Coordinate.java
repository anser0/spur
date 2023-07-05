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
}
