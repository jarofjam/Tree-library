package tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Utility class</p>
 * <p>Implements common methods for working with trees stored as
 * multiple nodes with no more than two children each.</p>
 * <p>Contains a nested class that describes the required
 * interface of the nodes that Trees works with.</p>
 */
final class Trees {
    /**
     * Returns an array containing all the elements from the tree.
     * @param root root of the tree given.
     * @param size size of the tree given.
     * @param <T> type of elements stored in the tree given.
     * @return an array containing all the elements from the tree.
     */
    static <T> Object[] toArray(Node root, int size) {
        if (size == 0)
            return new Object[0];

        List<T> ar = new ArrayList<>(size);
        walkInOrder(root, ar);

        return ar.toArray();
    }

    /**
     * Returns an array containing all the elements from the tree.
     * @param root root of the tree given.
     * @param size size of the tree given.
     * @param <T> type of elements stored in the tree given.
     * @return an array containing all the elements from the tree.
     */
    static <T> T[] toArray(T[] a, Node root, int size) {
        List<T> ar = new ArrayList<>(size);
        walkInOrder(root, ar);

        return ar.<T>toArray(a);
    }

    /**
     * Returns a string representation of the tree.
     * @param root root of the tree given.
     * @param size size of the tree given.
     * @param <T> type of elements stored in the tree given.
     * @return string representation of the tree.
     */
    static <T> String toString(Node root, int size) {
        if (root == null) {
            return "[]";
        }
        List<T> ar = new ArrayList<>(size);
        walkInOrder(root, ar);
        return ar.stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "[", "]"));
    }

    /**
     * Returns an iterator over the elements from the tree.
     * @param root root of the tree given.
     * @param size size of the tree given.
     * @param <T> type of elements stored in the tree given.
     * @return an iterator over the elements from the tree.
     */
    static <T> Iterator<T> getIterator(Node root, int size) {
        return new Iterator<T>() {
            private List<T> list;
            private int cursor;

            {
                list = new ArrayList<>(size);
                walkInOrder(root, list);
                cursor = 0;
            }

            @Override
            public boolean hasNext() {
                return cursor != size;
            }

            @Override
            public T next() {
                return list.get(cursor++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Puts all elements from the tree into one list.
     * @param N the node currently viewed.
     * @param ar the list into which elements are placed.
     * @param <T> type of elements stored in the tree given.
     */
    private static <T> void walkInOrder(Node N, List<T> ar) {
        if (N == null)
            return;

        if (N.getL() != null)
            walkInOrder(N.getL(), ar);

        ar.add((T) N.getValue());

        if (N.getR() != null)
            walkInOrder(N.getR(), ar);

    }

    /**
     * Class that describes the required
     * interface of the nodes that Trees works with.
     * @param <T> type of elements stored in the node.
     */
    static abstract class Node<T> {
        /**
         * returns the value stored in the node.
         * @return the value stored in the node.
         */
        abstract T getValue();

        /**
         * Return the node left child.
         * @return the node left child.
         */
        abstract Node getL();

        /**
         * Return the node right child.
         * @return the node right child.
         */
        abstract Node getR();
    }
}
