package tree;

import java.util.ArrayList;
import java.util.Comparator;

public class LeftistHeap<T> {

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

    public void add(T value) {
        if (root == null) {
            root = new Node(value);
            size = 1;
            return;
        }

        root = merge(root, new Node(value));
        size++;
    }

    public T peek() {
        if (size == 0)
            return null;

        return root.value;
    }

    public T poll() {
        if (size == 0)
            return null;

        T result = root.value;

        root = merge(root.L, root.R);

        size--;
        return result;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public Object[] toArray() {
        if (size == 0)
            return new Object[0];

        ArrayList<T> ar = new ArrayList<>(size);
        walkInOrder(root, ar);

        return ar.toArray();
    }

    public void merge(LeftistHeap anotherHeap) {
        if (anotherHeap == null)
            throw new IllegalArgumentException();

        root = merge(root, anotherHeap.root);
        size += anotherHeap.size;
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

    private void walkInOrder(Node N, ArrayList<T> ar) {
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
}
