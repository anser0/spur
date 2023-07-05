import java.util.*;
import Poset;

public class SelfDualPoset extends Poset {
    private final Integer[] bijection;  // maps element indices to the indices of their duals

    // returns the dual of an element
    public Element dual(Element e) {
        return elements[bijection[getIndex(e)]];
    }

    // helper function for constructor; checks that the given bijection is indeed an isomorphism on its dual
    private Boolean verify() {
        for (int a = 0; a < cardinality; a++) {
            if (bijection[bijection[a]] != a) {
                return false;
            }
        }
        for (int a = 0; a < cardinality; a++) {
            for (int b = 0; b < cardinality; b++) {
                if (compare(a, b) && !compare(bijection[b], bijection[a])) {
                    return false;
                }
            }
        }
        return true;
    }

    // constructor: relations relate elements, and bijection gives the isomorphism to the dual
    public SelfDualPoset(Set<Element> elements, Set<Pair<Element>> relations, Map<Element, Element> bijection) {
        super(elements, relations);
        this.bijection = new Integer[cardinality];
        for (Element e : elements) {
            this.bijection[getIndex(e)] = getIndex(bijection.get(e));
        }
        if (!verify()) {
            throw new Error("bijection is not self-dual");
        }
    }

    // generates a self-dual order ideal, if possible
    private SelfDualOrderIdeal generateSelfDualOrderIdeal() {
        for (Element e: elements) {
            if (dual(e) == e) {
                throw new Error("self-dual poset has no self-dual order ideals");
            }
        }
        Set<Element> generators = new HashSet<Element>();
        for (Element e: elements) {
            if (compareeq(dual(e), e)) {
                continue;
            }
            boolean ignore = false;
            for (Element g: generators) {
                if (compareeq(e, g) || compareeq(dual(e), g)) {
                    ignore = true;
                    break;
                }
            }
            if (!ignore) {
                generators.add(e);
            }
        }
        return new SelfDualOrderIdeal(this, generators);
    }

    // generates the flip graph on self-dual order ideals
    public Graph generateFlipGraph() {
        SelfDualOrderIdeal startingOrderIdeal = generateSelfDualOrderIdeal();
        Vertex startingVertex = new Vertex(startingOrderIdeal.toString());
        Map<Vertex, SelfDualOrderIdeal> map = new HashMap<Vertex, SelfDualOrderIdeal>();
        ArrayList<Set<Vertex>> layers = new ArrayList<Set<Vertex>>();
        Set<Edge> edges = new HashSet<Edge>();
        layers.add(new HashSet<Vertex>());
        layers.get(0).add(startingVertex);
        map.put(startingVertex, startingOrderIdeal);
        layers.add(new HashSet<Vertex>());
        for (SelfDualOrderIdeal neighborIdeal: startingOrderIdeal.neighbors()) {
            Vertex neighborVertex = new Vertex(neighborIdeal.toString());
            layers.get(1).add(neighborVertex);
            map.put(neighborVertex, neighborIdeal);
            edges.add(new Edge(startingVertex, neighborVertex));
        }
        while (layers.get(layers.size()-1).size() > 0) {
            Set<Vertex> previousLayer = layers.get(layers.size()-2);
            Set<Vertex> currentLayer = layers.get(layers.size()-1);
            Set<Vertex> nextLayer = new HashSet<Vertex>();
            for (Vertex vertex: currentLayer) {
                SelfDualOrderIdeal orderIdeal = map.get(vertex);
                for (SelfDualOrderIdeal neighborIdeal: orderIdeal.neighbors()) {
                    boolean alreadyExists = false;
                    for (Vertex v: previousLayer) {
                        if (map.get(v).equals(neighborIdeal)) {
                            alreadyExists = true;
                        }
                        if (alreadyExists) {
                            break;
                        }
                    }
                    for (Vertex v: currentLayer) {
                        if (map.get(v).equals(neighborIdeal)) {
                            alreadyExists = true;
                            edges.add(new Edge(vertex, v));
                        }
                        if (alreadyExists) {
                            break;
                        }
                    }
                    for (Vertex v: nextLayer) {
                        if (map.get(v).equals(neighborIdeal)) {
                            alreadyExists = true;
                            edges.add(new Edge(vertex, v));
                        }
                        if (alreadyExists) {
                            break;
                        }
                    }
                    if (!alreadyExists) {
                        Vertex newVertex = new Vertex(neighborIdeal.toString());
                        nextLayer.add(newVertex);
                        map.put(newVertex, neighborIdeal);
                        edges.add(new Edge(vertex, newVertex));
                    }
                }
            }
            layers.add(nextLayer);
        }
        Set<Vertex> vertices = new HashSet<Vertex>();
        for (Set<Vertex> layer: layers) {
            for (Vertex v: layer) {
                vertices.add(v);
            }
        }
        Graph result = new Graph(vertices, edges);
        result.setName("flip graph on " + getName());
        return result;
    }

    // prints elements, covering relation, and dual map
    public String toString() {
        String s = super.toString();
        ArrayList<String> bijectionStrings = new ArrayList<String>();
        for (int i = 0; i < cardinality; i++) {
            bijectionStrings.add(elements[i].toString() + "->" + elements[bijection[i]].toString());
        }
        s += ", dual map: {" + Tools.join(bijectionStrings) + "}";
        return s;
    }

    // constructs a chain of length n
    public static SelfDualPoset chain(int n) {
        if (n <= 0) {
            throw new Error("chain length must be positive");
        }
        Element[] elementArray = new Element[n];
        Set<Pair<Element>> relations = new HashSet();
        for (int i = 0; i < n; i++) {
            elementArray[i] = new Element(Integer.toString(i+1));
        }
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                relations.add(new Pair<Element>(elementArray[i], elementArray[j]));
            }
        }
        Map<Element, Element> bijection = new HashMap<Element, Element>();
        for (int i = 0; i < n; i++) {
            bijection.put(elementArray[i], elementArray[n-1-i]);
        }
        Set<Element> elements = new HashSet<Element>();
        for (Element element: elementArray) {
            elements.add(element);
        }
        SelfDualPoset result = new SelfDualPoset(elements, relations, bijection);
        result.setName("[" + n + "]");
        return result;
    }

    // helper function for product; maps elements of a Cartesian product to their duals
    private static Map<Element, Element> createBijection(SelfDualPoset[] selfDualPosets, Map<Element, ArrayList<Element>> map) {
        int dim = selfDualPosets.length;
        Set<Element> elements = map.keySet();
        Map<Element, Element> bijection = new HashMap<Element, Element>();
        for (Element a: elements) {
            for (Element b: elements) {
                boolean dual = true;
                for (int d = 0; d < dim; d++) {
                    if (selfDualPosets[d].dual(map.get(a).get(d)) != map.get(b).get(d)) {
                        dual = false;
                        break;
                    }
                }
                if (dual) {
                    bijection.put(a, b);
                }
            }
        }
        return bijection;
    }

    // product of posets
    public static SelfDualPoset product(SelfDualPoset... selfDualPosets) {
        Map<Element, ArrayList<Element>> map = Poset.createMap(selfDualPosets);
        Set<Element> elements = map.keySet();
        Set<Pair<Element>> relations = Poset.createRelations(selfDualPosets, map);
        Map<Element, Element> bijection = createBijection(selfDualPosets, map);
        SelfDualPoset result = new SelfDualPoset(elements, relations, bijection);
        ArrayList<String> posetNames = new ArrayList<String>();
        for (SelfDualPoset selfDualPoset: selfDualPosets) {
            posetNames.add(selfDualPoset.getName());
        }
        result.setName(String.join("*", posetNames));
        return result;
    }
}