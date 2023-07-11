import java.util.*;

public class Main {
    public static void main(String[] args) {
        SelfDualPoset p = chainProduct(2, 2, 2, 4);
        Graph g = p.generateFlipGraph();
        System.out.println(g.getVertexCount());
    }

    public static SelfDualPoset chainProduct(int... dimensions) {
        SelfDualPoset[] chains = new SelfDualPoset[dimensions.length];
        for (int i = 0; i < dimensions.length; i++) {
            chains[i] = SelfDualPoset.chain(dimensions[i]);
        }
        return SelfDualPoset.product(chains);
    }

    public static void createImage(int[] dimensions) {
        SelfDualPoset p = chainProduct(dimensions);
        Graph g = p.generateFlipGraph();
        GraphVisualization drawing = new GraphVisualization(g);
        String filename = "chain";
        filename += dimensions.length + "d";
        for (int d: dimensions) {
            filename += d;
            filename += "t";
        }
        filename = filename.substring(0, filename.length() - 1);
        System.out.println("Creating " + filename);
        drawing.save(filename);
    }
}