package tree;

import java.util.Comparator;
import java.util.Iterator;

public class Treap<T> implements Tree<T> {

    private Node root;
    private int size;

    private final Comparator<T> comparator;

    public Treap() {
        this(null);
    }

    public Treap(Comparator<T> comparator) {
        root = null;
        this.comparator = comparator;
    }

    public void add(T x) {
        if (contains(x))
            return;

        size++;

        Node L, M, R;

        PairOfNodes pair = leftSplit(root, x);
        L = pair.getL();
        R = pair.getR();

        M = new Node(x);

        root = merge(merge(L, M), R);
    }

    public void remove(T x) {
        Node L, M, R;

        PairOfNodes pair = rightSplit(root, x);
        L = pair.getL();
        R = pair.getR();

        pair = leftSplit(R, x);
        M = pair.getL();
        R = pair.getR();

        if (M != null)
            size--;

        root = merge(L, R);
    }

    public boolean contains(T x) {
        Node L, M, R;

        PairOfNodes pair = rightSplit(root, x);
        L = pair.getL();
        R = pair.getR();

        pair = leftSplit(R, x);
        M = pair.getL();

        root = merge(L, R);

        return M != null;
    }

    @Override
    public int getSize() {
        return this.size;
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
        if (L == null) return R;
        if (R == null) return L;

        if (L.y > R.y)
            return new Node(L.x, L.y, L.L, merge(L.R, R));
        else
            return new Node(R.x, R.y, merge(L, R.L), R.R);

    }

    private PairOfNodes leftSplit(Node N, T x) {
        if (N == null)
            return new PairOfNodes(null, null);

        Node L;
        Node R;
        Node newNode = null;

        boolean compareResult;
        if (comparator == null) {
            Comparable<? super T> value = (Comparable<? super T>) N.x;

            compareResult = value.compareTo(x) <= 0;
        } else {
            compareResult = comparator.compare(N.x, x) <= 0;
        }

        if (compareResult) {
            if (N.R == null) {
                R = null;
            } else {
                PairOfNodes pair = leftSplit(N.R, x);
                newNode = pair.getL();
                R = pair.getR();
            }
            L = new Node(N.x, N.y, N.L, newNode);
        } else {
            if (N.L == null) {
                L = null;
            } else {
                PairOfNodes pair = leftSplit(N.L, x);
                L = pair.getL();
                newNode = pair.getR();
            }
            R = new Node(N.x, N.y, newNode, N.R);
        }

        return new PairOfNodes(L, R);
    }

    private PairOfNodes rightSplit(Node N, T x) {
        if (N == null)
            return new PairOfNodes(null, null);

        Node L;
        Node R;
        Node newNode = null;

        boolean compareResult;
        if (comparator == null) {
            Comparable<? super T> value = (Comparable<? super T>) N.x;

            compareResult = value.compareTo(x) < 0;
        } else {
            compareResult = comparator.compare(N.x, x) < 0;
        }

        if (compareResult) {
            if (N.R == null) {
                R = null;
            } else {
                PairOfNodes pair = rightSplit(N.R, x);
                newNode = pair.getL();
                R = pair.getR();
            }
            L = new Node(N.x, N.y, N.L, newNode);
        } else {
            if (N.L == null) {
                L = null;
            } else {
                PairOfNodes pair = rightSplit(N.L, x);
                L = pair.getL();
                newNode = pair.getR();
            }
            R = new Node(N.x, N.y, newNode, N.R);
        }

        return new PairOfNodes(L, R);
    }

    private final class Node extends Trees.Node<T> {
        T x;
        double y;

        Node L;
        Node R;

        Node(T x) {
            this(x, Math.random(), null, null);
        }

        Node(T x, Node L, Node R) {
            this(x, Math.random(), L, R);
        }

        Node(T x, double y, Node L, Node R) {
            this.x = x;
            this.y = y;
            this.L = L;
            this.R = R;
        }

        @Override
        T getValue() {
            return x;
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

    private final class PairOfNodes {
        private Node L;
        private Node R;

        Node getL() {
            return L;
        }

        Node getR() {
            return R;
        }

        PairOfNodes(Node L, Node R) {
            this.L = L;
            this.R = R;
        }
    }
}
