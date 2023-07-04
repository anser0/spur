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
        Set<Vertex> vertices = new HashSet<Vertex>();
        Set<Edge> edges = new HashSet<Edge>();
        Map<Vertex, SelfDualOrderIdeal> map = new HashMap<Vertex, SelfDualOrderIdeal>();
        Vertex startingVertex = new Vertex(startingOrderIdeal.toString());
        vertices.add(startingVertex);
        map.put(startingVertex, startingOrderIdeal);
        ArrayList<Vertex> todo = new ArrayList<Vertex>();
        todo.add(startingVertex);
        while (todo.size() > 0) {
            Vertex currentVertex = todo.get(0);
            SelfDualOrderIdeal currentIdeal = map.get(currentVertex);
            Set<SelfDualOrderIdeal> neighbors = currentIdeal.neighbors();
            for (SelfDualOrderIdeal ideal: neighbors) {
                boolean alreadyExists = false;
                for (Vertex v: vertices) {
                    if (map.get(v).equals(ideal)) {
                        alreadyExists = true;
                        edges.add(new Edge(currentVertex, v));
                        break;
                    }
                }
                if (!alreadyExists) {
                    Vertex newVertex = new Vertex(ideal.toString());
                    vertices.add(newVertex);
                    map.put(newVertex, ideal);
                    edges.add(new Edge(currentVertex, newVertex));
                    todo.add(newVertex);
                }
            }
            todo.remove(0);
        }
        return new Graph(vertices, edges);
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
        return new SelfDualPoset(elements, relations, bijection);
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
        return new SelfDualPoset(elements, relations, bijection);
    }
}