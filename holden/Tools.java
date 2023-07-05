import java.util.*;

abstract public class Tools {
    private static final long startTime = System.currentTimeMillis();

    // gets number of milliseconds since program started
    public static int time() {
        return (int) (System.currentTimeMillis() - startTime);
    }

    public static void printTime() {
        printTime("");
    }

    public static void printTime(String message) {
        System.out.println(message + ": " + time() + "ms");
    }

    // converts set to ArrayList
    public static <T> ArrayList<T> createArray(Set<T> objects) {
        ArrayList<T> array = new ArrayList<T>();
        for (T object: objects) {
            array.add(object);
        }
        return array;
    }

    // returns a comma-separated string containing the string representations of a set of objects
    public static <T> String join(Set<T> objects) {
        return join(createArray(objects));
    }

    // returns a comma-separated string containing the string representations of an ArrayList of objects
    public static <T> String join(ArrayList<T> objects) {
        String output = "";
        boolean first = true;
        for (T object: objects) {
            if (!first) {
                output += ", ";
            }
            output += object.toString();
            if (first) {
                first = false;
            }
        }
        return output;
    }

    // helper function for cartesianProduct; creates every possible combination of source and addition
    private static <T> Set<ArrayList<T>> multiply(Set<ArrayList<T>> source, Set<T> addition) {
        Set<ArrayList<T>> output = new HashSet<ArrayList<T>>();
        for (ArrayList<T> list: source) {
            for (T element: addition) {
                ArrayList<T> newList = new ArrayList<T>(list);
                newList.add(element);
                output.add(newList);
            }
        }
        return output;
    }
    
    // returns a set containing all Cartesian products of an array of sets
    public static <T> Set<ArrayList<T>> cartesianProduct(Set<T>... sets) {
        ArrayList<Set<T>> list = new ArrayList<Set<T>>();
        for (Set<T> set: sets) {
            list.add(set);
        }
        return cartesianProduct(list);
    }

    // returns a set containing all Cartesian products of an ArrayList of sets
    public static <T> Set<ArrayList<T>> cartesianProduct(ArrayList<Set<T>> sets) {
        Set<ArrayList<T>> output = new HashSet<ArrayList<T>>();
        output.add(new ArrayList<T>());
        for (Set<T> set: sets) {
            output = multiply(output, set);
        }
        return output;
    }
}
