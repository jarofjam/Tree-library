package tree;

import java.util.Comparator;

public class LeftistHeap<T> extends TreeStructured<T> implements Heap<T> {

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

        return root.getValue();
    }

    @Override
    public T poll() {
        if (size == 0)
            return null;

        T result = root.getValue();

        root = merge(root.getL(), root.getR());
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

    public void merge(LeftistHeap anotherHeap) {
        if (anotherHeap == null)
            throw new IllegalArgumentException();

        root = merge(root, anotherHeap.root);
        size += anotherHeap.size;
    }

    private Node merge(AbstractNode abstractNode1, AbstractNode abstractNode2) {
        Node L = (Node) abstractNode1;
        Node R = (Node) abstractNode2;

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

    private final class Node extends AbstractNode {

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
