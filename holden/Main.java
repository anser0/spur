import java.util.*;

public class Main {
    public static void main(String[] args) {
<<<<<<< HEAD
        int[] dimensions = new int[] {4, 5};
=======
        SelfDualPoset p = chainProduct(2, 2, 2, 4);
        Graph g = p.generateFlipGraph();
        System.out.println(g.getVertexCount());
    }

    public static SelfDualPoset chainProduct(int... dimensions) {
>>>>>>> f933280513a3d943e2d147827c3d928aac77809b
        SelfDualPoset[] chains = new SelfDualPoset[dimensions.length];
        for (int i = 0; i < dimensions.length; i++) {
            chains[i] = SelfDualPoset.chain(dimensions[i]);
        }
        return SelfDualPoset.product(chains);
    }

    public static void createImage(int[] dimensions) {
        SelfDualPoset p = chainProduct(dimensions);
        Graph g = p.generateFlipGraph();
<<<<<<< HEAD
        // GraphVisualization drawing = new GraphVisualization(g);
        // drawing.display();
        System.out.println(g.getCenter());
        System.out.println(g.getRadius());
        System.out.println(g.getVertexCount());
=======
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
>>>>>>> f933280513a3d943e2d147827c3d928aac77809b
    }
}