import java.util.*;

public class Poset {
    protected Set<Element> elementSet;              // set of elements
    protected Element[] elements;                   // array of elements
    protected int cardinality;                      // number of elements
    protected Map<Element, Integer> map;            // maps elements to integers
    protected boolean[][] order;                    // partial order
    protected boolean[][] cover;                    // covering relation
    protected Map<Element, Set<Element>> covering;  // covering relations
    protected Map<Element, Set<Element>> covered;   // covered relations
    
    // gets index of an element
    protected int getIndex(Element e) {
        return map.get(e);
    }

    // returns set of elements
    public Set<Element> getElements() {
        return elementSet;
    }

    // returns cardinality
    public int getCardinality() {
        return cardinality;
    }

    // helper function for compare
    protected boolean compare(int a, int b) {
        return order[a][b];
    }

    // returns the truth value of a < b
    public boolean compare(Element a, Element b) {
        return compare(getIndex(a), getIndex(b));
    }

    // returns the truth value of a <= b
    public boolean compareeq(Element a, Element b) {
        if (a == b) {
            return true;
        }
        return compare(a, b);
    }

    // helper function for covers
    protected boolean covers(int a, int b) {
        return cover[a][b];
    }

    // returns the truth value of a <. b
    public boolean covers(Element a, Element b) {
        return covers(getIndex(a), getIndex(b));
    }

    // returns the set of elements that cover a
    public Set<Element> coveringElements(Element a) {
        return covering.get(a);
    }
    
    // returns the set of elements a covers
    public Set<Element> coveredElements(Element a) {
        return covered.get(a);
    }

    // helper function for constructor; verifies that relations determine a poset
    private boolean verify() {
        for (int a = 0; a < cardinality; a++) {
            if (compare(a, a)) {
                return false;
            }
        }
        for (int a = 0; a < cardinality; a++) {
            for (int b = 0; b < cardinality; b++) {
                if (compare(a, b) && compare(b, a)) {
                    return false;
                }
            }
        }
        for (int a = 0; a < cardinality; a++) {
            for (int b = 0; b < cardinality; b++) {
                for (int c = 0; c < cardinality; c++) {
                    if (compare(a, b) && compare(b, c) && !compare(a, c)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // helper function for construction; calculates covering relation
    private void createCovers() {
        this.cover = new boolean[cardinality][cardinality];
        for (int a = 0; a < cardinality; a++) {
            for (int b = 0; b < cardinality; b++) {
                if (!compare(a, b)) {
                    continue;
                }
                boolean works = true;
                for (int i = 0; i < cardinality; i++) {
                    if (compare(a, i) && compare(i, b)) {
                        works = false;
                        break;
                    }
                }
                if (works) {
                    this.cover[a][b] = true;
                }
            }
        }
        this.covering = new HashMap<Element, Set<Element>>();
        this.covered = new HashMap<Element, Set<Element>>();
        for (Element a: elements) {
            Set<Element> coveringSet = new HashSet<Element>();
            Set<Element> coveredSet = new HashSet<Element>();
            for (Element b: elements) {
                if (covers(a, b)) {
                    coveringSet.add(b);
                }
                if (covers(b, a)) {
                    coveredSet.add(b);
                }
            }
            this.covering.put(a, coveringSet);
            this.covered.put(a, coveredSet);
        }
    }

    // constructor: relations gives the partial order on elements
    public Poset(Set<Element> elements, Set<Pair<Element>> relations) {
        this.cardinality = elements.size();
        this.elementSet = elements;
        this.elements = new Element[elements.size()];
        int index = 0;
        for (Element element: elements) {
            this.elements[index] = element;
            index++;
        }

        this.map = new HashMap<Element, Integer>();
        index = 0;
        for (Element e : elements) {
            map.put(e, index);
            index++;
        }

        this.order = new boolean[cardinality][cardinality];
        for (Pair<Element> relation: relations) {
            int a = getIndex(relation.getFirst());
            int b = getIndex(relation.getSecond());
            order[a][b] = true;
        }

        if (!verify()) {
            throw new Error("relations do not determine a poset");
        }

        createCovers();
    }

    // prints elements and covering relation
    public String toString() {
        String s = "elements: ";
        s += "{" + Tools.join(elements) + "}";
        s += ", ";
        s += "covering relations: ";
        ArrayList<String> relationStrings = new ArrayList<String>();
        for (int a = 0; a < cardinality; a++) {
            for (int b = 0; b < cardinality; b++) {
                if (a != b && covers(a, b)) {
                    relationStrings.add(elements[a].toString() + "<" + elements[b].toString());
                }
            }
        }
        s += "{" + Tools.join(relationStrings) + "}";
        return s;
    }

    // creates a chain of length n
    public static Poset chain(int n) {
        return SelfDualPoset.chain(n);
    }

    // helper function for product; creates map from elements of result to ArrayLists of constituent elements
    protected static Map<Element, ArrayList<Element>> createMap(Poset[] posets) {
        ArrayList<Set<Element>> sets = new ArrayList<Set<Element>>();
        for (Poset poset: posets) {
            sets.add(poset.getElements());
        }
        Set<ArrayList<Element>> tuples = Tools.cartesianProduct(sets);
        Map<Element, ArrayList<Element>> map = new HashMap<Element, ArrayList<Element>>();
        int index = 0;
        for (ArrayList<Element> tuple: tuples) {
            Element element = new Element("(" + Tools.join(tuple) + ")");
            map.put(element, tuple);
            index += 1;
        }
        return map;
    }

    // helper function for product; creates relations between elements of result
    protected static Set<Pair<Element>> createRelations(Poset[] posets, Map<Element, ArrayList<Element>> map) {
        int dim = posets.length;
        Set<Element> elements = map.keySet();
        Set<Pair<Element>> relations = new HashSet<Pair<Element>>();
        for (Element a : elements) {
            for (Element b: elements) {
                if (a == b) {
                    continue;
                }
                boolean majorizes = true;
                for (int d = 0; d < dim; d++) {
                    if (!posets[d].compareeq(map.get(a).get(d), map.get(b).get(d))) {
                        majorizes = false;
                        break;
                    }
                }
                if (majorizes) {
                    relations.add(new Pair<Element>(a, b));
                }
            }
        }
        return relations;
    }

    // product of posets
    public static Poset product(Poset... posets) {
        Map<Element, ArrayList<Element>> map = createMap(posets);
        Set<Element> elements = map.keySet();
        Set<Pair<Element>> relations = createRelations(posets, map);
        return new Poset(elements, relations);
    }
}