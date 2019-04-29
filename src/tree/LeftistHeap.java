package tree;

import java.util.Comparator;
import java.util.Iterator;

/**
 * <p>This class is an implementation of a queue.</p>
 * <p>The implementation is based on a leftist heap.</p>
 * <p>An instance of this class can contain any type of elements that could be ordered
 * via comparator or in a natural order.</p>
 * <p>This class offers an almost O(log(n)) time performance
 * on add/peek/poll operations.</p>
 * <p><strong>Note that this implementation is not synchronized.</strong></p>
 * @param <T> type of element to be stored in this queue.
 */
public class LeftistHeap<T> implements Heap<T> {
    /**
     * The root of this leftist heap.
     */
    private Node root;
    /**
     * The number of elements in this leftist heap.
     */
    private int size;

    /**
     * The comparator or null if this leftist heap orders the elements in a natural order.
     */
    private final Comparator<T> comparator;

    /**
     * Creates a leftist heap without any comparator.
     */
    public LeftistHeap() {
        this(null);
    }
    /**
     *
     * Creates a leftist heap with the specified comparator
     * @param comparator the comparator that will be used to order the elements in this leftist heap.
     */
    public LeftistHeap(Comparator<T> comparator) {
        this.root = null;
        this.comparator = comparator;
    }

    /**
     * Appends the specified element to the end of this queue.
     * @param value element to be appended.
     * @throws NullPointerException if the element is null.
     */
    @Override
    public void add(T value) {
        if (value == null)
            throw new NullPointerException();

        if (root == null) {
            root = new Node(value);
            size = 1;
            return;
        }

        root = merge(root, new Node(value));
        size++;
    }

    /**
     * Returns the value of the element at the beginning of the queue without removing it.
     * @return value of the element at the beginning of the queue
     * or null if this queue is empty.
     */
    @Override
    public T peek() {
        if (size == 0)
            return null;

        return root.value;
    }

    /**
     * Returns the value of the element at the beginning of the queue removing it.
     * @return value of the element at the beginning of the queue
     * or null if this queue is empty.
     */
    @Override
    public T poll() {
        if (size == 0)
            return null;

        T result = root.value;

        root = merge(root.L, root.R);
        size--;

        return result;
    }

    /**
     * Merges this leftist heap with another leftist heap.
     * Returns a new leftist heap as a result.
     * @param anotherHeap specified leftist heap.
     * @throws NullPointerException if the specified leftist heap is null.
     */
    public void mergeWith(LeftistHeap anotherHeap) {
        if (anotherHeap == null)
            throw new NullPointerException();

        root = merge(root, (Node)anotherHeap.root);
        size += anotherHeap.size;
    }

    /**
     * Returns the size of this queue.
     * @return the size of this queue.
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Returns true if this queue contains no elements.
     * @return tree if this queue contains no elements, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all the elements from this queue.
     * This queue will be empty after this call returns.
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns an array containing all the elements from this queue.
     * The order of the elements in the array does not match their order in the queue.
     * @return an array containing all the elements from this queue.
     */
    @Override
    public Object[] toArray() {
        return Trees.<T>toArray(root, size);
    }

    /**
     * Returns an array containing all if the elements from this queue.
     * The order of the elements in the array does not match their order in the queue.
     * @param a the array into which the elements from this queue are to
     * be stored, if it is big enough, otherwise, a new array of the
     * same type is allocated.
     * @return an array containing all of the elements from this queue.
     */
    @Override
    public <T2> T2[] toArray(T2[] a) {
        return Trees.<T2>toArray(a, root, size);
    }

    /**
     * Returns a string representation of this queue.
     * The order of the elements in the string does not match their order in the queue.
     * @return string representation of this queue.
     */
    @Override
    public String toString() {
        return Trees.<T>toString(root, size);
    }

    /**
     * Returns an iterator over the elements from this queue.
     * @return an iterator over the elements from this queue.
     */
    @Override
    public Iterator<T> iterator() {
        return Trees.<T>getIterator(root, size);
    }

    /**
     * Starts the process of merging two specified leftist heaps into one.
     * Calls <strong>mergeAsComparable</strong> or <strong>mergeWithComparator</strong>
     * depending on the presence of a comparator in this leftist heap.
     * @param L first leftist heap to be merged.
     * @param R second leftist heap to be merged.
     * @return new leftist heap which contains all the elements from two that were merged.
     */
    private Node merge(Node L, Node R) {
        if (comparator == null)
            return mergeAsComparable(L, R);
        else
            return mergeWithComparator(L, R);
    }

    /**
     * Merges two specified leftist heaps into one without using the comparator.
     * @param L first leftist heap to be merged.
     * @param R second leftist heap to be merged.
     * @return new leftist heap which contains all the elements from two that were merged.
     */
    private Node mergeAsComparable(Node L, Node R) {
        if (L == null) return R;
        if (R == null) return L;

        Node N = null;

        Comparable<? super T> leftValue = (Comparable<? super T>) L.value;
        T rightValue = (T) R.value;

        if (leftValue.compareTo(rightValue) <= 0) {
            N = L;
            N.R = mergeAsComparable(L.R, R);
        } else {
            N = R;
            N.L = mergeAsComparable(L, R.L);
        }

        N.checkChildren();
        N.update();

        return N;
    }

    /**
     * Merges two specified leftist heaps into one using the comparator.
     * @param L first leftist heap to be merged.
     * @param R second leftist heap to be merged.
     * @return new leftist heap which contains all the elements from two that were merged.
     */
    private Node mergeWithComparator(Node L, Node R) {
        if (L == null) return R;
        if (R == null) return L;

        Node N = null;

        T leftValue = (T) L.value;
        T rightValue = (T) R.value;

        if (comparator.compare(leftValue, rightValue) <= 0) {
            N = L;
            N.R = mergeWithComparator(L.R, R);
        } else {
            N = R;
            N.L = mergeWithComparator(L, R.L);
        }

        N.checkChildren();
        N.update();

        return N;
    }

    /**
     * A node of this leftist heap.
     * Actually each node can be considered as a leftist heap.
     */
    private final class Node extends Trees.Node<T> {

        /**
         * Value stored in this node.
         */
        private final T value;
        /**
         * Left child.
         */
        private Node L;
        /**
         * Right child.
         */
        private Node R;

        /**
         * Distance from this node to the nearest absent child.
         */
        private int d = 0;

        /**
         * Creates a new node with the specified value.
         * @param value value to be stored in the node.
         */
        Node(T value) {
            this.value = value;
            this.L = null;
            this.R = null;
            this.d = 1;
        }

        /**
         * Checks that the value of the parameter d of the right child
         * is less than that of the left child.
         * If this is not the case, it changes the children in places.
         */
        void checkChildren() {
            if (R == null) return;

            if (L == null || R.d > L.d) {
                Node temp = L;
                L = R;
                R = temp;
            }
        }

        /**
         * Updates the value of the parameter d for this node.
         */
        void update() {
            int dR = R == null ? 0 : R.d;
            int dL = L == null ? 0 : L.d;

            d = Math.min(dL, dR) + 1;
        }

        /**
         * Returns the value stored.
         * @return value stored.
         */
        @Override
        T getValue() {
            return value;
        }

        /**
         * Returns left child.
         * @return left child.
         */
        @Override
        Node getL() {
            return L;
        }

        /**
         * Returns right child.
         * @return right child.
         */
        @Override
        Node getR() {
            return R;
        }
    }
}
