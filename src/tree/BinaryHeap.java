package tree;

import java.util.Arrays;

public class BinaryHeap {

    private int[] list;
    private int size = 0;

    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    public BinaryHeap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public BinaryHeap(int initialCapacity) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException();

        list = new int[initialCapacity];
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(int value) {
        size++;
        if (size == list.length)
            increaseCapacity();

        list[size] = value;

        siftUp(size);
    }

    public int peek() {
        if (size == 0)
            return 0;

        return list[0];
    }

    public int poll() {
        if (size == 0)
            return 0;

        int result = list[0];

        list[0] = list[size];
        size--;
        siftDown(0);

        return result;
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

            if (rightChild < size && list[rightChild] < list[leftChild])
                j = rightChild;

            if (list[i] <= list[j])
                break;

            swap(i, j);
            i = j;
        }
    }

    private void siftUp(int i) {
        while (list[i] < list[(i - 1) / 2]) {
            swap(i, (i - 1) / 2);
            i = (i - 1) / 2;
        }
    }

    private void swap(int i, int j) {
        int c = list[i];
        list[i] = j;
        list[j] = c;
    }

}
