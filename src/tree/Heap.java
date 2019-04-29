package tree;

/**
 * <p>Common interface for all heaps in this package.
 * Contains all common for heaps methods, clarifying the features
 * of working with heaps as with trees.</p>
 * <p>Each heap in this package implements this interface.</p>
 * @param <T> type of elements to be stored in the heap.
 */
public interface Heap<T> extends Tree<T> {
    /**
     * Adds the specified element to the heap.
     * @param value element to be stored.
     */
    void add(T value);

    /**
     * Returns the value of the top element in the heap without removing it.
     * @return the value of the top element in the heap or null if the heap is empty.
     */
    T peek();

    /**
     * Returns the top element in the heap removing it.
     * @return the top element in the heap or null if the heap is empty.
     */
    T poll();
}
