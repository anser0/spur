import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GraphVisualization extends JFrame {
    Graph graph;                // graph structure behind depicted graph
    Map<Vertex, CoordinatePair> map;    // maps vertices to locations
    Set<Vertex> center;         // center of graph
    Set<Vertex> perimeter;      // perimeter of graph
    int size = 750;             // size of screen
    int dotRadius = 5;          // size of vertex dot
    double optimalDistance;     // desired size between vertices

    // helper function for constructor; assigns coordinate pairs to vertices
    private void assignCoordinatePairs(Graph graph) {
        int n = graph.getVertexCount();
        double r = size/3;
        CoordinatePair shift = new CoordinatePair(size/2, size/2);
        int i = 0;
        double tau = Math.PI*2;
        for (Vertex v: graph.getVertices()) {
            CoordinatePair pair = new CoordinatePair(r * Math.cos(i*tau/n), r * Math.sin(i*tau/n));
            pair.add(shift);
            map.put(v, pair);
            i++;
        }
    }

    // swaps coordinate pairs corresponding to two vertices
    private void swap(Vertex v1, Vertex v2) {
        CoordinatePair p1 = map.get(v1);
        CoordinatePair p2 = map.get(v2);
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
                            oldSum += CoordinatePair.distance(map.get(v1), map.get(u));
                        }
                    }
                    for (Vertex u: graph.getNeighbors(v2)) {
                        if (u != v1) {
                            oldSum += CoordinatePair.distance(map.get(v2), map.get(u));
                        }
                    }
                    for (Vertex u: graph.getNeighbors(v1)) {
                        if (u != v2) {
                            newSum += CoordinatePair.distance(map.get(v2), map.get(u));
                        }
                    }
                    for (Vertex u: graph.getNeighbors(v2)) {
                        if (u != v1) {
                            newSum += CoordinatePair.distance(map.get(v1), map.get(u));
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

    // "force" between vertices not sharing an edge
    private double coulomb(double r) {
        return 1/(1+Math.exp(1.0*r/optimalDistance-1));
    }

    // "force" between vertices sharing an edge
    private double hooke(double r) {
        return -Math.log(1.0 * r / optimalDistance);
    }

    // computes total force on a vertex
    private CoordinatePair force(Vertex v) {
        Set<Vertex> neighbors = graph.getNeighbors(v);
        CoordinatePair totalForce = new CoordinatePair(0, 0);
        double step = 5; // controls how fast adjust() converges to an optimal position
        for (Vertex u: graph.getVertices()) {
            if (u == v) {
                continue;
            }
            CoordinatePair force = CoordinatePair.subtract(map.get(v), map.get(u));
            double distance = force.getNorm();
            force.normalize();
            if (neighbors.contains(u)) {
                force.scale(step*hooke(distance));
            } else {
                force.scale(step*coulomb(distance));
            }
            totalForce.add(force);
        }
        return totalForce;
    }

    // adjusts locations of vertices based on forces
    private void adjust() {
        int iterations = (int) (1000 * Math.min(1, 256.0 / graph.getVertexCount())); // controls how many iterations adjust() acts for
        for (int counter = 0; counter < iterations; counter++) {
            Map<Vertex, CoordinatePair> forces = new HashMap<Vertex, CoordinatePair>();
            for (Vertex v: graph.getVertices()) {
                forces.put(v, force(v));
            }
            for (Vertex v: graph.getVertices()) {
                CoordinatePair location = map.get(v);
                location.add(forces.get(v));
                location.setX(Math.max(dotRadius, location.getX()));
                location.setY(Math.max(dotRadius, location.getY()));
                location.setX(Math.min(size-dotRadius, location.getX()));
                location.setY(Math.min(size-dotRadius, location.getY()));
            }
        }
    }

    // constructs graph visualization given graph
    public GraphVisualization(Graph graph) {
        super(graph.getName());
        this.graph = graph;
        this.map = new HashMap<Vertex, CoordinatePair>();
        this.optimalDistance = size / Math.sqrt(graph.getVertexCount()) / Math.PI;
        this.center = graph.getCenter();
        this.perimeter = graph.getPerimeter();

        assignCoordinatePairs(graph);
        setSize(size, size);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        optimize();
        adjust();
    }

    // draws dot
    private void drawDot(Graphics2D g2d, CoordinatePair p) {
        g2d.fillOval((int) p.getX()-dotRadius, (int) p.getY()-dotRadius, 2*dotRadius, 2*dotRadius);
    }

    // draws line
    private void drawLine(Graphics2D g2d, CoordinatePair p1, CoordinatePair p2) {
        g2d.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
    }

    // draws text
    private void drawText(Graphics2D g2d, String... text) {
        int fontSize = 16;
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
        for (int i = 0; i < text.length; i++) {
            g2d.drawString(text[i], fontSize, (i+2)*fontSize);
        }
    }
    
    // adds vertices and edges to drawing
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, size, size);
        g2d.setColor(Color.BLACK);
        for (Edge e: graph.getEdges()) {
            Vertex v1 = e.getFirstVertex();
            Vertex v2 = e.getSecondVertex();
            drawLine(g2d, map.get(v1), map.get(v2));
        }
        for (Vertex v: graph.getVertices()) {
            g2d.setColor(Color.BLACK);
            drawDot(g2d, map.get(v));
        }
        for (Vertex v: center) {
            g2d.setColor(Color.BLUE);
            drawDot(g2d, map.get(v));
        }
        for (Vertex v: perimeter) {
            g2d.setColor(Color.RED);
            drawDot(g2d, map.get(v));
        }
        drawText(g2d, graph.getName(),
                      "vertices: " + graph.getVertexCount(),
                      "edges: " + graph.getEdgeCount(),
                      "max degree: " + graph.getMaximumDegree(),
                      "average degree: " + String.format("%.3g%n", graph.getAverageDegree()),
                      "radius: " + graph.getRadius(),
                      "diameter: " + graph.getDiameter());
    }

    // displays image
    public void display() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
    }

    public void save() {
        save("image");
    }

    // saves image
    public void save(String filename) {
        try {
            BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = image.createGraphics();
            this.paint(graphics2D);
            String directory = Tools.getDirectory();
            ImageIO.write(image, "jpeg", new File(directory + "/images/" + filename + ".jpeg"));
        }
        catch(Exception exception) {
            throw new Error("image could not be generated");
        }
    }
}