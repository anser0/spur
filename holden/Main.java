import java.util.*;

public class Main {
    public static void main(String[] args) {
        int[] dimensions = new int[] {3, 4, 5};
        SelfDualPoset[] chains = new SelfDualPoset[dimensions.length];
        for (int i = 0; i < dimensions.length; i++) {
            chains[i] = SelfDualPoset.chain(dimensions[i]);
        }
        SelfDualPoset p = SelfDualPoset.product(chains);
        Graph g = p.generateFlipGraph();
        GraphVisualization drawing = new GraphVisualization(g);
        drawing.display();
    }
}