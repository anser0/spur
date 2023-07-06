import java.util.*;

public class Main {
    public static void main(String[] args) {
        // int maxVolume = 64;
        // for (int a = 2; a <= maxVolume/2; a++) {
        //     for (int b = a; b <= maxVolume/2; b++) {
        //         if (a*b % 2 == 1 || a*b > maxVolume) {
        //             continue;
        //         }
        //         createImage(new int[] {a, b});
        //     }
        // }
        // for (int a = 2; a <= maxVolume/4; a++) {
        //     for (int b = a; b <= maxVolume/4; b++) {
        //         for (int c = b; c <= maxVolume/4; c++) {
        //             if (a*b*c % 2 == 1 || a*b*c > maxVolume) {
        //                 continue;
        //             }
        //             createImage(new int[] {a, b, c});
        //         }
        //     }
        // }
        for (int a = 2; a <= 8; a++) {
            for (int b = a; b <= 8; b++) {
                for (int c = b; c <= 8; c++) {
                    for (int d = c; d <= 8; d++) {
                        if (a*b*c*d % 2 == 1 || a*b*c*d > 64) {
                            continue;
                        }
                        createImage(new int[] {a, b, c, d});
                    }
                }
            }
        }
        createImage(new int[] {2, 2, 2, 2, 2});
        createImage(new int[] {2, 2, 2, 2, 3});
        createImage(new int[] {2, 2, 2, 2, 4});
        createImage(new int[] {2, 2, 2, 2, 2, 2});
    }

    public static void createImage(int[] dimensions) {
        SelfDualPoset[] chains = new SelfDualPoset[dimensions.length];
        for (int i = 0; i < dimensions.length; i++) {
            chains[i] = SelfDualPoset.chain(dimensions[i]);
        }
        SelfDualPoset p = SelfDualPoset.product(chains);
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