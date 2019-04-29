package tree;

/**
 * <p>Common interface for all structures in this package.
 * Contains all common methods.</p>
 * <p>Each class from this package implements this interface one way or another.</p>
 * @param <T> type of elements to be stored in the tree.
 */
public interface Tree<T> extends Iterable<T> {
    /**
     * Returns the size of the tree.
     * @return the size of the tree.
     */
    int getSize();

    /**
     * Returns true if the tree contains no elements.
     * @return tree if the tree contains no elements, false otherwise.
     */
    boolean isEmpty();

    /**
     * Removes all the elements from the tree.
     * The tree will be empty after this call returns.
     */
    void clear();

    /**
     * Returns an array containing all the elements from the tree.
     * @return an array containing all the elements from the tree.
     */
    Object[] toArray();

    /**
     * Returns an array containing all the elements from the tree.
     * @param a the array into which the elements from the tree are to
     * be stored, if it is big enough; otherwise, a new array of the
     * same type is allocated.
     * @return an array containing all of the elements from the tree.
     */
    <T> T[] toArray(T[] a);
}
