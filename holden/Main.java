import java.util.*;
import Element;

public class Main {
    public static void main(String[] args) {
        int a = 3;
        int b = 4;
        int c = 5;
        SelfDualPoset p = SelfDualPoset.product(SelfDualPoset.chain(a), SelfDualPoset.chain(b), SelfDualPoset.chain(c));
        Graph g = p.generateFlipGraph();

        System.out.println("Poset: [" + a + "]*[" + b + "]*[" + c + "]");
        System.out.println("Vertices: " + g.getVertexCount());
        System.out.println("Edges: " + g.getEdgeCount());
        System.out.println("Average degree: " + g.getAverageDegree());
        System.out.println("Maximum degree: " + g.getMaximumDegree());
        System.out.println("Radius: " + g.getRadius());
        System.out.println("Diameter: " + g.getDiameter());

        GraphVisualization drawing = new GraphVisualization(g);
        drawing.draw();
    }
}