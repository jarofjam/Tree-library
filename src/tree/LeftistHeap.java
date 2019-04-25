package tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class LeftistHeap<T> implements Heap<T> {

    private Node root;
    private final Comparator<T> comparator;
    private int size;

    public LeftistHeap() {
        this(null);
    }

    public LeftistHeap(Comparator<T> comparator) {
        this.root = null;
        this.comparator = comparator;
    }

    @Override
    public void add(T value) {
        if (root == null) {
            root = new Node(value);
            size = 1;
            return;
        }

        root = merge(root, new Node(value));
        size++;
    }

    @Override
    public T peek() {
        if (size == 0)
            return null;

        return root.value;
    }

    @Override
    public T poll() {
        if (size == 0)
            return null;

        T result = root.value;

        root = merge(root.L, root.R);
        size--;

        return result;
    }

    public void merge(LeftistHeap anotherHeap) {
        if (anotherHeap == null)
            throw new IllegalArgumentException();

        root = merge(root, anotherHeap.root);
        size += anotherHeap.size;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public Object[] toArray() {
        return Trees.<T>toArray(root, size);
    }

    @Override
    public String toString() {
        return Trees.<T>toString(root, size);
    }

    @Override
    public Iterator<T> iterator() {
        return Trees.<T>getIterator(root, size);
    }

    private Node merge(Node L, Node R) {
        if (comparator == null)
            return mergeAsComparable(L, R);
        else
            return mergeWithComparator(L, R);
    }

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

    private final class Node extends Trees.Node<T> {

        private final T value;
        private Node L;
        private Node R;

        private int d = 0;

        Node(T value) {
            this.value = value;
            this.L = null;
            this.R = null;
            this.d = 1;
        }

        void checkChildren() {
            if (R == null) return;

            if (L == null || R.d > L.d) {
                Node temp = L;
                L = R;
                R = temp;
            }
        }

        void update() {
            int dR = R == null ? 0 : R.d;
            int dL = L == null ? 0 : L.d;

            d = Math.min(dL, dR) + 1;
        }

        @Override
        T getValue() {
            return value;
        }

        @Override
        Node getL() {
            return L;
        }

        @Override
        Node getR() {
            return R;
        }
    }
}
