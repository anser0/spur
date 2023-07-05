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

    // returns the distance to the farthest vertex in its connected component
    private int getMaximumDistance(Vertex startingVertex) {
        ArrayList<Set<Vertex>> layers = new ArrayList<Set<Vertex>>();
        layers.add(new HashSet<Vertex>());
        layers.get(0).add(startingVertex);
        layers.add(neighbors(startingVertex));
        while (layers.get(layers.size() - 1).size() > 0) {
            Set<Vertex> currentLayer = layers.get(layers.size() - 1);
            Set<Vertex> previousLayer = layers.get(layers.size() - 2);
            Set<Vertex> nextLayer = new HashSet<Vertex>();
            for (Vertex v: currentLayer) {
                for (Vertex n: neighbors(v)) {
                    if (!currentLayer.contains(n) && !previousLayer.contains(n)) {
                        nextLayer.add(n);
                    }
                }
            }
            layers.add(nextLayer);
        }
        return layers.size() - 2;
    }

    // returns the diameter of the largest component in a connected graph
    public int getRadius() {
        int radius = vertexCount;
        for (Vertex v: vertices) {
            int maximumDistance = getMaximumDistance(v);
            if (maximumDistance < radius) {
                radius = maximumDistance;
            }
        }
        return radius;
    }

    // returns the diameter of the largest component in a connected graph
    public int getDiameter() {
        int diameter = 0;
        for (Vertex v: vertices) {
            int maximumDistance = getMaximumDistance(v);
            if (maximumDistance > diameter) {
                diameter = maximumDistance;
            }
        }
        return diameter;
    }

    // returns string containing vertex set and edge set
    public String toString() {
        String s = "vertices: {" + Tools.join(vertices) + "}, ";
        s += "edges: {" + Tools.join(edges) + "}";
        return s;
    }
}
