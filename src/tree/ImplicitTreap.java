package tree;

import java.util.Iterator;

public class ImplicitTreap<T> implements Tree<T> {

    private Node root;

    public ImplicitTreap() {
        root = null;
    }

    public void add(T value) {
        root = merge(root, new Node(value));
    }

    public T get(int index) {
        checkBounds(index);

        Node M, R;

        PairOfNodes pair = split(root, index);
        R = pair.getR();

        pair = split(R, 1);
        M = pair.getL();

        return M == null ? null : M.value;
    }

    public void put(int index, T value) {
        checkBounds(index);

        PairOfNodes pair = split(root, index);
        Node L = pair.getL();
        Node R = pair.getR();

        Node M = new Node(value);

        root = merge(merge(L, M), R);
    }

    public void set(int index, T value) {
        checkBounds(index);

        Node L, R;
        Node N = new Node(value);

        PairOfNodes pair = split(root, index);
        L = pair.getL();
        R = pair.getR();

        pair = split(R, 1);
        R = pair.getR();

        root = merge(merge(L, N), R);
    }

    public void remove(int index) {
        checkBounds(index);

        Node L, R;

        PairOfNodes pair = split(root, index);
        L = pair.getL();
        R = pair.getR();

        pair = split(R, 1);
        R = pair.getR();

       root = merge(L, R);
    }

    @Override
    public int getSize() {
        return sizeOf(root);
    }

    @Override
    public boolean isEmpty() {
        return sizeOf(root) == 0;
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public String toString() {
        return Trees.<T>toString(root, sizeOf(root));
    }

    @Override
    public Object[] toArray() {
        return Trees.<T>toArray(root, sizeOf(root));
    }

    @Override
    public Iterator<T> iterator() {
        return Trees.<T>getIterator(root, sizeOf(root));
    }

    private Node merge(Node L, Node R) {
        if (L == null) return R;
        if (R == null) return L;

        Node N;

        if (L.y > R.y)
            N = new Node(L.value, L.y, L.L, merge(L.R, R));
        else
            N = new Node(R.value, R.y, merge(L, R.L), R.R);

        N.updateSize();
        return N;
    }

    private PairOfNodes split(Node N, int x) {
        if (N == null)
            return new PairOfNodes(null, null);

        Node L;
        Node R;
        Node newNode = null;
        int curIndex = sizeOf(N.L) + 1;

        if (curIndex <= x) {
            if (N.R == null) {
                R = null;
            } else {
                PairOfNodes pair = split(N.R, x - curIndex);
                newNode = pair.getL();
                R = pair.getR();
            }
            L = new Node(N.value, N.y, N.L, newNode);
            L.updateSize();
        } else {
            if (N.L == null) {
                L = null;
            } else {
                PairOfNodes pair = split(N.L, x);
                L = pair.getL();
                newNode = pair.getR();
            }
            R = new Node(N.value, N.y, newNode, N.R);
            R.updateSize();
        }

        return new PairOfNodes(L, R);
    }

    private void checkBounds(int index) {
        if (root == null || index >= root.size || index < 0)
            throw new IndexOutOfBoundsException("Index: " + index + " size: " + sizeOf(root));
    }

    private int sizeOf(Node N) {
        return N == null ? 0 : N.size;
    }

    private class Node extends Trees.Node<T> {
        T value;
        double y;
        int size;

        Node L;
        Node R;

        Node(T value) {
            this(value, Math.random(), null, null);
        }

        Node(T value, Node L, Node R) {
            this(value, Math.random(), L, R);
        }

        Node(T value, double y, Node L, Node R) {
            this.value = value;
            this.y = y;
            this.L = L;
            this.R = R;
            this.size = 1;
        }

        void updateSize() {
            size = sizeOf(L) + sizeOf(R) + 1;
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

    private class PairOfNodes {
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
