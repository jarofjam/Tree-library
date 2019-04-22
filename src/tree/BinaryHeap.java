package tree;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class BinaryHeap<T> {

    private Object[] list;
    private int size = 0;

    private static final int DEFAULT_INITIAL_CAPACITY = 12;
    private Comparator<? super T> comparator;

    public BinaryHeap() {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }

    public BinaryHeap(int initialCapacity) {
        this(initialCapacity, null);
    }

    public BinaryHeap(Comparator<? super T> comparator) {
        this(DEFAULT_INITIAL_CAPACITY, comparator);
    }

    public BinaryHeap(int initialCapacity, Comparator<? super T> comparator) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException();

        list = new Object[initialCapacity];
        this.comparator = comparator;
    }

    public BinaryHeap(Collection<? extends T> collection) {
        this.size = collection.size();
        list = Arrays.copyOf(collection.toArray(), size);
        comparator = null;

        for (int i = size / 2; i >= 0; i--)
            siftDown(i);
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(T value) {
        if (size == list.length)
            increaseCapacity();

        list[size++] = value;

        siftUp(size - 1);
    }

    public T peek() {
        if (size == 0)
            return null;

        return (T) list[0];
    }

    public T poll() {
        if (size == 0)
            return null;

        T result = (T) list[0];

        list[0] = list[--size];
        siftDown(0);

        return result;
    }

    public Object[] toArray() {
        return Arrays.copyOf(list, size);
    }

    private void increaseCapacity() {
        int oldCapacity = list.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;

        list = Arrays.copyOf(list, newCapacity);
    }

    private void siftDown(int i) {
        if (comparator == null)
            siftDownAsComparable(i);
        else
            siftDownWithComparator(i);
    }

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

    private void siftUp(int i) {
        if (comparator == null)
            siftUpAsComparable(i);
        else
            siftUpWithComparator(i);
    }

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

    private void swap(int i, int j) {
        Object c = list[i];
        list[i] = list[j];
        list[j] = c;
    }

}