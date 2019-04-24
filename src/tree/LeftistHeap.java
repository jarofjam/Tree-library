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
        if (size == 0)
            return new Object[0];

        List<T> ar = new ArrayList<>(size);
        walkInOrder(root, ar);

        return ar.toArray();
    }

    public void merge(LeftistHeap anotherHeap) {
        if (anotherHeap == null)
            throw new IllegalArgumentException();

        root = merge(root, anotherHeap.root);
        size += anotherHeap.size;
    }

    @Override
    public Iterator<T> iterator() {
        return new LeftistHeapIterator();
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

    private void walkInOrder(Node N, List<T> ar) {
        if (N.L != null)
            walkInOrder(N.L, ar);

        ar.add(N.value);

        if (N.R != null)
            walkInOrder(N.R, ar);
    }

    private final class Node {

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

        Node(T value, Node L, Node R) {
            this.value = value;

            if (L.d < R.d) {
                this.R = L;
                this.L = R;
            } else {
                this.L = L;
                this.R = R;
            }

            this.d = L.d + R.d + 1;
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
            d = 1;

            if (L != null) d += L.d;
            if (R != null) d += R.d;
        }
    }

    private final class LeftistHeapIterator implements Iterator<T> {

        private ArrayList<T> content;
        int cursor = 0;

        LeftistHeapIterator() {
            content = new ArrayList<>(size);

            walkInOrder(root, content);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public T next() {
            return content.get(cursor++);
        }
    }
}
