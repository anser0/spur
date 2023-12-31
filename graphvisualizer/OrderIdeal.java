import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OrderIdeal {
    protected Poset poset;              // the poset the order ideal is a subset of
    protected Set<Element> peaks;       // the set of the order ideal's peaks
    protected Set<Element> elements;    // the set of elements in the order ideal (remains uninitialized until getElements() or getCardinality() is called)
    protected int cardinality;          // the number of elements in the order ideal

    // returns the poset the order ideal is a subset of
    public Poset getPoset() {
        return poset;
    }

    // returns the set of peaks in the order ideal
    public Set<Element> getPeaks() {
        return peaks;
    }

    // returns elements
    public Set<Element> getElements() {
        if (elements == null) {
            elements = new HashSet<Element>();
            for (Element e: poset.getElements()) {
                if (contains(e)) {
                    elements.add(e);
                }
            }
            this.cardinality = elements.size();
        }
        return elements;
    }

    // returns cardinality
    public int getCardinality() {
        if (elements == null) {
            getElements();
        }
        return cardinality;
    }

    // determines if e is contained in the order ideal
    public boolean contains(Element e) {
        for (Element peak: peaks) {
            if (poset.compareeq(e, peak)) {
                return true;
            }
        }
        return false;
    }

    // constructor: constructs the ideal generated by the generators, a subset of the elements of the given poset
    // the constructor does NOT construct elements or cardinality (this is for efficiency reasons)
    public OrderIdeal(Poset poset, Set<Element> generators) {
        this.poset = poset;
        this.peaks = new HashSet<Element>();
        // [TODO] this algorithm can be optimized by a factor of 2
        for (Element e: generators) {
            boolean peak = true;
            for (Element f: generators) {
                if (poset.compare(e, f)) {
                    peak = false;
                    break;
                }
            }
            if (peak) {
                peaks.add(e);
            }
        }
    }

    // outputs the set of peaks in the order ideal as a string
    public String toString() {
        String s = "<" + Tools.join(peaks) + ">";
        return s;
    }

    // checks if two order ideals are the same
    public boolean equals(Object o) {
        if (!(o instanceof OrderIdeal)) {
            return false;
        }
        OrderIdeal other = (OrderIdeal) o;
        if (this.getPoset() != other.getPoset()) {
            return false;
        }
        return this.getPeaks().equals(other.getPeaks());
    }
}
