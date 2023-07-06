import java.util.*;

public class Graph {
    private Set<Vertex> vertices;                   // set of vertices
    private Set<Edge> edges;                        // set of edges
    private int vertexCount;                        // number of vertices
    private int edgeCount;                          // number of edges
    private Map<Vertex, Set<Vertex>> neighbors;     // neighbors for each vertex
    private Map<Vertex, Integer> eccentricities;    // eccentricities for each vertex; initialized later
    private String name = "unnamed graph";          // name

    // returns vertex set
    public Set<Vertex> getVertices() {
        return vertices;
    }

    // returns vertex count
    public int getVertexCount() {
        return vertexCount;
    }

    // returns edges
    public Set<Edge> getEdges() {
        return edges;
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
            int degree = getNeighbors(vertex).size();
            if (degree > maxDegree) {
                maxDegree = degree;
            }
        }
        return maxDegree;
    }

    // returns set of neighbors of a vertex
    public Set<Vertex> getNeighbors(Vertex v) {
        return neighbors.get(v);
    }

    public int degree(Vertex v) {
        return getNeighbors(v).size();
    }

    // checks if two vertices are connected by an edge
    public boolean adjacent(Vertex u, Vertex v) {
        return getNeighbors(u).contains(v);
    }

    // sets name
    public void setName(String newName) {
        this.name = newName;
    }

    // returns name
    public String getName() {
        return name;
    }

    // constructor: edges connect vertices
    public Graph(Set<Vertex> vertices, Set<Edge> edges) {
        this.vertices = vertices;
        this.vertexCount = vertices.size();
        this.neighbors = new HashMap<Vertex, Set<Vertex>>();
        for (Vertex v: vertices) {
            neighbors.put(v, new HashSet<Vertex>());
        }
        for (Edge e: edges) {
            Vertex v1 = e.getFirstVertex();
            Vertex v2 = e.getSecondVertex();
            getNeighbors(v1).add(v2);
            getNeighbors(v2).add(v1);
        }
        this.edges = new HashSet<Edge>();
        for (Vertex u: vertices) {
            for (Vertex v: getNeighbors(u)) {
                if (u.getID() < v.getID()) {
                    this.edges.add(new Edge(u, v));
                }
            }
        }
        this.edgeCount = edges.size();
        this.eccentricities = new HashMap<Vertex, Integer>();
    }

    private int getEccentricity(Vertex startingVertex) {
        if (eccentricities.containsKey(startingVertex)) {
            return eccentricities.get(startingVertex);
        }
        ArrayList<Set<Vertex>> layers = new ArrayList<Set<Vertex>>();
        layers.add(new HashSet<Vertex>());
        layers.get(0).add(startingVertex);
        layers.add(getNeighbors(startingVertex));
        while (layers.get(layers.size() - 1).size() > 0) {
            Set<Vertex> currentLayer = layers.get(layers.size() - 1);
            Set<Vertex> previousLayer = layers.get(layers.size() - 2);
            Set<Vertex> nextLayer = new HashSet<Vertex>();
            for (Vertex v: currentLayer) {
                for (Vertex n: getNeighbors(v)) {
                    if (!currentLayer.contains(n) && !previousLayer.contains(n)) {
                        nextLayer.add(n);
                    }
                }
            }
            layers.add(nextLayer);
        }
        int eccentricity = layers.size() - 2;
        eccentricities.put(startingVertex, eccentricity);
        return eccentricity;
    }

    // returns the center of the connected graph
    public Set<Vertex> getCenter() {
        int radius = vertexCount;
        Set<Vertex> center = new HashSet<Vertex>();
        for (Vertex v: vertices) {
            int maximumDistance = getEccentricity(v);
            if (maximumDistance < radius) {
                radius = maximumDistance;
                center = new HashSet<Vertex>();
            }
            if (maximumDistance == radius) {
                center.add(v);
            }
        }
        return center;
    }

    // returns the radius of the largest component in a connected graph
    public int getRadius() {
        Set<Vertex> center = getCenter();
        Vertex v = center.iterator().next();
        return getEccentricity(v);
    }

    // returns the perimeter of the connected graph
    public Set<Vertex> getPerimeter() {
        int diameter = 0;
        Set<Vertex> perimeter = new HashSet<Vertex>();
        for (Vertex v: vertices) {
            int maximumDistance = getEccentricity(v);
            if (maximumDistance > diameter) {
                diameter = maximumDistance;
                perimeter = new HashSet<Vertex>();
            }
            if (maximumDistance == diameter) {
                perimeter.add(v);
            }
        }
        return perimeter;
    }
    // returns the diameter of the largest component in a connected graph
    public int getDiameter() {
        Set<Vertex> perimeter = getPerimeter();
        Vertex v = perimeter.iterator().next();
        return getEccentricity(v);
    }

    // returns string containing vertex set and edge set
    public String toString() {
        String s = "name: " + getName() + ", ";
        s += "vertices: {" + Tools.join(vertices) + "}, ";
        s += "edges: {" + Tools.join(edges) + "}";
        return s;
    }
}
