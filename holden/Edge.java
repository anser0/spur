import java.util.*;

public class Edge {
    private Vertex vertex1;
    private Vertex vertex2;
    
    // returns one vertex
    public Vertex getFirstVertex() {
        return vertex1;
    }

    // returns another vertex
    public Vertex getSecondVertex() {
        return vertex2;
    }

    // returns set of two vertices
    public Set<Vertex> getVertices() {
        Set<Vertex> vertexSet = new HashSet<Vertex>();
        vertexSet.add(getFirstVertex());
        vertexSet.add(getSecondVertex());
        return vertexSet;
    }

    // constructor: constructs edge connecting two vertices
    public Edge(Vertex vertex1, Vertex vertex2) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        if (vertex1 == vertex2) {
            throw new Error("edge cannot connect vertex to itself");
        }
    }

    // checks if v is incident to this edge
    public boolean incident(Vertex v) {
        return v == vertex1 || v == vertex2;
    }

    // returns both vertices in a string
    public String toString() {
        return vertex1.toString() + "--" + vertex2.toString();
    }

    // compares vertex sets of two edges to check for equality
    public boolean equals(Object o) {
        if (!(o instanceof Edge)) {
            return false;
        }
        Edge other = (Edge) o;
        return this.getVertices().equals(other.getVertices());
    }
}
