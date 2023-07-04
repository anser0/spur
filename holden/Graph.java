import java.util.*;

public class Graph {
    private Set<Vertex> vertices;                   // set of vertices
    private Set<Edge> edges;                        // set of edges
    private int vertexCount;                        // number of vertices
    private int edgeCount;                          // number of edges
    private Map<Vertex, Set<Vertex>> neighbors;     // neighbors for each vertex

    // returns vertex set
    public Set<Vertex> getVertices() {
        return vertices;
    }

    // returns vertex count
    public int getVertexCount() {
        return vertexCount;
    }

    // returns edge count
    public int getEdgeCount() {
        return edgeCount;
    }

    // returns average degree
    public double getAverageDegree() {
        return 2.0 * getEdgeCount() / getVertexCount();
    }

    // returns maximum degree
    public int getMaximumDegree() {
        int maxDegree = 0;
        for (Vertex vertex: vertices) {
            int degree = neighbors(vertex).size();
            if (degree > maxDegree) {
                maxDegree = degree;
            }
        }
        return maxDegree;
    }

    // returns set of neighbors of a vertex
    public Set<Vertex> neighbors(Vertex v) {
        return neighbors.get(v);
    }

    // checks if two vertices are connected by an edge
    public boolean adjacent(Vertex u, Vertex v) {
        return neighbors(u).contains(v);
    }

    // constructor: edges connect vertices
    public Graph(Set<Vertex> vertices, Set<Edge> edges) {
        this.vertices = vertices;
        this.edges = new HashSet<Edge>();
        for (Edge e: edges) {
            boolean alreadyExists = false;
            for (Edge f: this.edges) {
                if (f.equals(e)) {
                    alreadyExists = true;
                    break;
                }
            }
            if (!alreadyExists) {
                this.edges.add(e);
            }
        }
        this.vertexCount = vertices.size();
        this.edgeCount = this.edges.size();
        this.neighbors = new HashMap<Vertex, Set<Vertex>>();
        for (Vertex v: vertices) {
            neighbors.put(v, new HashSet<Vertex>());
        }
        for (Edge e: edges) {
            Vertex v1 = e.getFirstVertex();
            Vertex v2 = e.getSecondVertex();
            neighbors.get(v1).add(v2);
            neighbors.get(v2).add(v1);
        }
    }

    // returns string containing vertex set and edge set
    public String toString() {
        String s = "vertices: {" + Tools.join(vertices) + "}, ";
        s += "edges: {" + Tools.join(edges) + "}";
        return s;
    }
}
