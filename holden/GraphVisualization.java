import java.util.*;
import java.awt.*;
import javax.swing.*;

public class GraphVisualization extends JFrame {
    Graph graph;
    Map<Vertex, Coordinate> map;
    int size = 800;
    int dotRadius = 5;

    // helper function for constructor; assigns coordinates to vertices
    private void assignCoordinates(Graph graph) {
        int n = graph.getVertexCount();
        double r = size/3;
        Coordinate shift = new Coordinate(size/2, size/2);
        int i = 0;
        double tau = Math.PI*2;
        for (Vertex v: graph.getVertices()) {
            Coordinate coordinate = new Coordinate(r * Math.cos(i*tau/n), r * Math.sin(i*tau/n));
            coordinate.add(shift);
            map.put(v, coordinate);
            i++;
        }
    }

    // swaps coordinates two vertices
    private void swap(Vertex v1, Vertex v2) {
        Coordinate p1 = map.get(v1);
        Coordinate p2 = map.get(v2);
        map.put(v1, p2);
        map.put(v2, p1);
    }

    // optimizes locations for vertices
    private void optimize() {
        Set<Vertex> vertices = graph.getVertices();
        while (true) {
            boolean changed = false;
            for (Vertex v1: vertices) {
                for (Vertex v2: vertices) {
                    double oldSum = 0;
                    double newSum = 0;
                    for (Vertex u: graph.getNeighbors(v1)) {
                        if (u != v2) {
                            oldSum += Coordinate.distance(map.get(v1), map.get(u));
                        }
                    }
                    for (Vertex u: graph.getNeighbors(v2)) {
                        if (u != v1) {
                            oldSum += Coordinate.distance(map.get(v2), map.get(u));
                        }
                    }
                    for (Vertex u: graph.getNeighbors(v1)) {
                        if (u != v2) {
                            newSum += Coordinate.distance(map.get(v2), map.get(u));
                        }
                    }
                    for (Vertex u: graph.getNeighbors(v2)) {
                        if (u != v1) {
                            newSum += Coordinate.distance(map.get(v1), map.get(u));
                        }
                    }
                    if (newSum < oldSum) {
                        swap(v1, v2);
                        changed = true;
                    }
                }
            }
            if (!changed) {
                break;
            }
        }
    }

    private double coulomb(double r) {
        return -r/10000;
    }

    private double hooke(double r) {
        return (50-r)/10000;
    }

    private Coordinate force(Vertex v) {
        Set<Vertex> neighbors = graph.getNeighbors(v);
        Coordinate totalForce = new Coordinate(0, 0);
        for (Vertex u: graph.getVertices()) {
            Coordinate force = Coordinate.subtract(map.get(v), map.get(u));
            double distance = force.getNorm();
            if (neighbors.contains(u)) {
                force.scale(hooke(distance));
            } else {
                force.scale(coulomb(distance));
            }
            totalForce.add(force);
        }
        return totalForce;
    }

    private void adjust() {
        for (int counter = 0; counter < 1000; counter++) {
            for (Vertex v: graph.getVertices()) {
                map.get(v).add(force(v));
            }
        }
    }

    // constructs graph visualization given graph
    public GraphVisualization(Graph graph) {
        super(graph.getName());
        this.graph = graph;
        this.map = new HashMap<Vertex, Coordinate>();
        assignCoordinates(graph);
        setSize(size, size);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        optimize();
        adjust();
    }

    // draws dot
    void drawDot(Graphics2D g2d, Coordinate p) {
        g2d.fillOval((int) p.getX()-dotRadius, (int) p.getY()-dotRadius, 2*dotRadius, 2*dotRadius);
    }

    // draws line
    void drawLine(Graphics2D g2d, Coordinate p1, Coordinate p2) {
        g2d.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
    }
    
    // adds vertices and edges to drawing
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Edge e: graph.getEdges()) {
            Vertex v1 = e.getFirstVertex();
            Vertex v2 = e.getSecondVertex();
            drawLine(g2d, map.get(v1), map.get(v2));
        }
        for (Vertex v: graph.getVertices()) {
            drawDot(g2d, map.get(v));
        }
    }

    // displays drawing
    public void display() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
    }
}