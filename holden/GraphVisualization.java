import java.util.*;

public class GraphVisualization {
    Graph graph;
    Map<Vertex, Coordinate> map;

    public GraphVisualization(Graph graph) {
        this.graph = graph;
        this.map = new HashMap<Vertex, Coordinate>();
        int n = graph.getVertexCount();
        double r = 50;
        int i = 0;
        double tau = Math.PI*2;
        for (Vertex v: graph.getVertices()) {
            Coordinate coordinate = new Coordinate(r * Math.cos(i*tau/n), r * Math.sin(i*tau/n));
            map.put(v, coordinate);
            i++;
        }
    }

    public void draw() {
        System.out.println("drawing");
    }
}
