public class Vertex {
    private String label;   // string to identify vertex
    private int id;
    private static int idCounter = 0;

    // returns label
    public String getLabel() {
        return label;
    }

    public int getID() {
        return id;
    }

    // constructor
    public Vertex(String label) {
        this.label = label;
        this.id = idCounter;
        this.idCounter++;
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