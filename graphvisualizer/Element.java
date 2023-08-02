public class Element {
    private String label;   // string to identify element

    // constructor
    public Element(String label) {
        this.label = label;
    }

    // construct unlabeled element
    public Element() {
        this.label = "null";
    }

    // prints label
    public String toString() {
        return label;
    }
}