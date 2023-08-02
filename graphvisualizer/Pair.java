public class Pair<T> {
    private T first;      // first element
    private T second;     // second element

    // constructor: (first, second)
    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    // returns first element of pair
    public T getFirst() {
        return first;
    }

    // returns second element of pair
    public T getSecond() {
        return second;
    }

    // determines if two pairs are equal
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair other = (Pair) o;
        return first.equals(other.getFirst()) && second.equals(other.getSecond());
    }

    // outputs pair as string (first, second)
    public String toString() {
        return "(" + first.toString() + ", " + second.toString() + ")";
    }
}
