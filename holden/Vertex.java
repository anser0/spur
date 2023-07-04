public class Vertex {
    private String label;   // string to identify vertex

    // returns label
    public String getLabel() {
        return label;
    }

    // constructor
    public Vertex(String label) {
        this.label = label;
    }

    // construct unlabeled vertex
    public Vertex() {
        this.label = "null";
    }

    // determines if this vertex is incident to e
    public boolean incident(edge e) {
        return e.incident(this);
    }

    // returns label
    public String toString() {
        return label;
    }
}