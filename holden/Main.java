import java.util.*;
import Element;

public class Main {
    public static void main(String[] args) {
        int[] dimensions = new int[] {5, 4};
        SelfDualPoset[] chains = new SelfDualPoset[dimensions.length];
        for (int i = 0; i < dimensions.length; i++) {
            chains[i] = SelfDualPoset.chain(dimensions[i]);
        }
        SelfDualPoset p = SelfDualPoset.product(chains);
        Graph g = p.generateFlipGraph();
        System.out.println(g.getCenter());
        // System.out.println(g.getRadius());
        // GraphVisualization drawing = new GraphVisualization(g);
        // drawing.display();
    }
}