package tree;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>This class is an implementation of a queue.</p>
 * <p>The implementation is based on a binary heap.</p>
 * <p>An instance of this class can contain any type of elements that could be ordered
 * via comparator or in a natural order.</p>
 * <p>This class offers an almost O(log(n)) time performance
 * on add/peek/poll operations.</p>
 * <p><strong>Note that this implementation is not synchronized.</strong></p>
 * @param <T> type of element to be stored in this queue.
 */
public class BinaryHeap<T> implements Heap<T> {
    /**
     * List of the elements stored in this binary heap.
     */
    private Object[] list;
    /**
     * Size of this binary heap.
     */
    private int size = 0;

    /**
     * The default capacity of this binary heap.
     * When a binary heap is created without the specified capacity
     * it gets the default capacity.
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 12;
    /**
     * Comparator used to order the elements in this binary heap
     * or null if the elements are ordered in a natural order.
     */
    private final Comparator<? super T> comparator;

    /**
     * Creates a binary heap with a default capacity
     * and without any comparator.
     */
    public BinaryHeap() {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }

    /**
     * Creates a binary heap with the specified capacity
     * and without any comparator.
     * @param initialCapacity capacity of the binary heap.
     */
    public BinaryHeap(int initialCapacity) {
        this(initialCapacity, null);
    }

    /**
     * Creates a binary heap with the default capacity
     * and with the specified comparator.
     * @param comparator comparator that will be used to order the elements in this binary heap.
     */
    public BinaryHeap(Comparator<? super T> comparator) {
        this(DEFAULT_INITIAL_CAPACITY, comparator);
    }

    /**
     * Creates a binary heap with the specified capacity
     * and the specified comparator.
     * @param initialCapacity capacity of the binary heap.
     * @param comparator comparator that will be used to order the elements in this binary heap.
     */
    public BinaryHeap(int initialCapacity, Comparator<? super T> comparator) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException();

        list = new Object[initialCapacity];
        this.comparator = comparator;
    }

    /**
     * Creates a binary heap from the elements stored in the specified collection.
     * This binary heap will not get any comparator.
     * @param collection collection of the elements from which the binary heap will be created.
     */
    public BinaryHeap(Collection<? extends T> collection) {
        this.size = collection.size();
        list = Arrays.copyOf(collection.toArray(), size);
        comparator = null;

        for (int i = size / 2; i >= 0; i--)
            siftDown(i);
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

        if (size == list.length)
            increaseCapacity();

        list[size++] = value;

        siftUp(size - 1);
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

        return (T) list[0];
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

        T result = (T) list[0];

        list[0] = list[--size];
        siftDown(0);

        return result;
    }

    /**
     * Returns the size of this queue.
     * @return size of this queue.
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
        list = new Object[DEFAULT_INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns an array containing all the elements from this queue.
     * The order of the elements in the array does not match their order in the queue.
     * @return an array containing all the elements from this queue.
     */
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(list, size);
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
        if (a.length < size)
            return (T2[]) Arrays.copyOf(list, size, a.getClass());

        System.arraycopy(list, 0, a, 0, size);

        if (a.length > size)
            a[size] = null;

        return a;
    }

    /**
     * Returns a string representation of this queue.
     * The order of the elements in the string does not match their order in the queue.
     * @return string representation of this queue.
     */
    @Override
    public String toString() {
        return Arrays.stream(list)
                .limit(size)
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "[", "]"));
    }

    /**
     * Returns an iterator over the elements from this queue.
     * @return an iterator over the elements from this queue.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public T next() {
                if (cursor < size) {
                    return (T) list[cursor++];
                }

                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Increases the capacity of this binary heap.
     * It will be called if during the add operation the program detects
     * that there is no free space in the list to store elements.
     */
    private void increaseCapacity() {
        int oldCapacity = list.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;

        list = Arrays.copyOf(list, newCapacity);
    }

    /**
     * Starts the process of sifting down the element at the specified position.
     * Will call <strong>siftDownAsComparable</strong> or <strong>siftDownWithComparator</strong>
     * depending on the presence of a comparator in this binary heap.
     * @param i index of the element that will be sifted down.
     */
    private void siftDown(int i) {
        if (comparator == null)
            siftDownAsComparable(i);
        else
            siftDownWithComparator(i);
    }

    /**
     * Sifts down the element at the specified position.
     * Is used when this binary heap has no comparator and orders
     * its elements in a natural order.
     * @param i index of the element that will be sifted down.
     */
    private void siftDownAsComparable(int i) {
        while (2 * i + 1 < size) {
            int leftChildIndex = 2 * i + 1;
            int rightChildIndex = 2 * i + 2;
            int swapIndex = leftChildIndex;

            if (rightChildIndex < size) {
                Comparable<? super T> rightChild = (Comparable<? super T>) list[rightChildIndex];
                T leftChild = (T) list[leftChildIndex];

                if (rightChild.compareTo(leftChild) < 0)
                    swapIndex = rightChildIndex;
            }

            Comparable<? super T> element = (Comparable<? super T>) list[i];
            T swapElement = (T) list[swapIndex];

            if (element.compareTo(swapElement) <= 0)
                break;

            swap(i, swapIndex);
            i = swapIndex;
        }
    }

    /**
     * Sifts down the element at the specified position.
     * Is used when this binary heap has the comparator and orders
     * its elements using it.
     * @param i index of the element that will be sifted down.
     */
    private void siftDownWithComparator(int i) {
        while (2 * i + 1 < size) {
            int leftChildIndex = 2 * i + 1;
            int rightChildIndex = 2 * i + 2;
            int swapIndex = leftChildIndex;

            if (rightChildIndex < size) {

                T rightChild = (T) list[rightChildIndex];
                T leftChild = (T) list[leftChildIndex];

                if (comparator.compare(rightChild, leftChild) < 0)
                    swapIndex = rightChildIndex;
            }

            T element = (T) list[i];
            T swapElement = (T) list[swapIndex];

            if (comparator.compare(element, swapElement) <= 0)
                break;

            swap(i, swapIndex);
            i = swapIndex;
        }
    }

    /**
     * Starts the process of sifting the an element at the specified position.
     * Will call <strong>siftUpAsComparable</strong> or <strong>siftUpWithComparator</strong>
     * depending on the presence of a comparator in this binary heap.
     * @param i index of the element that will be sifted up.
     */
    private void siftUp(int i) {
        if (comparator == null)
            siftUpAsComparable(i);
        else
            siftUpWithComparator(i);
    }

    /**
     * Sifts up the element at the specified position.
     * Is used when this binary heap has no comparator and orders
     * its elements in a natural order.
     * @param i index of the element that will be sifted up.
     */
    private void siftUpAsComparable(int i) {
        while (i > 0) {
            int parentIndex = (i - 1) >> 1;
            Comparable<? super T> element = (Comparable<? super T>) list[i];
            T parent = (T) list[parentIndex];

            if (element.compareTo(parent) >= 0)
                break;

            swap(i, parentIndex);
            i = parentIndex;
        }
    }

    /**
     * Sifts up the element at the specified position.
     * Is used when this binary heap has the comparator and orders
     * its elements using it.
     * @param i index of the element that will be sifted up.
     */
    private void siftUpWithComparator(int i) {
        while (i > 0) {
            int parentIndex = (i - 1) >> 1;
            T element = (T) list[i];
            T parent = (T) list[parentIndex];

            if (comparator.compare(element, parent) >= 0)
                break;

            swap(i, parentIndex);
            i = parentIndex;
        }
    }

    /**
     * swaps the values of two elements at specified positions.
     * @param i index of the first element.
     * @param j index of the second element.
     */
    private void swap(int i, int j) {
        Object c = list[i];
        list[i] = list[j];
        list[j] = c;
    }
}
