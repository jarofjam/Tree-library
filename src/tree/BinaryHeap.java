package tree;

import java.util.Arrays;
import java.util.Collection;

public class BinaryHeap<T extends Comparable<? super T> > {

    private Object[] list;
    private int size = 0;

    private static final int DEFAULT_INITIAL_CAPACITY = 12;

    public BinaryHeap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public BinaryHeap(int initialCapacity) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException();

        list = new Object[initialCapacity];
    }

    public BinaryHeap(Collection<? extends T> input) {
        this.size = input.size();
        list = Arrays.copyOf(input.toArray(), size);

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

        list[size] = value;
        size++;

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

        Object result =  list[0];

        list[0] = list[size - 1];
        size--;
        siftDown(0);

        return (T) result;
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
        while (2 * i + 1 < size) {
            int leftChild = 2 * i + 1;
            int rightChild = 2 * i + 2;
            int j = leftChild;

            if (rightChild < size && ((T) list[rightChild]).compareTo((T) list[leftChild]) < 0)
                j = rightChild;

            if (((T) list[i]).compareTo((T) list[j]) <= 0)
                break;

            swap(i, j);
            i = j;
        }
    }

    private void siftUp(int i) {
        while (i > 0 && ((T) list[i]).compareTo((T) list[(i - 1) / 2]) < 0) {
            swap(i, (i - 1) / 2);
            i = (i - 1) / 2;
        }
    }

    private void swap(int i, int j) {
        Object c = list[i];
        list[i] = list[j];
        list[j] = c;
    }

}
