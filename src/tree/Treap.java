package tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Treap<T extends Comparable<? super T>> {

    private Node root;
    private int size;

    public Treap() {
        root = null;
    }

    public void add(T x) {
        if (root == null) {
            size = 1;
            root = new Node(x);
            return;
        }

        if (contains(x))
            return;

        size++;

        Node L, R;
        Node M = new Node(x);

        PairOfNodes pair = leftSplit(root, x);
        L = pair.getL();
        R = pair.getR();

        root = merge(merge(L, M), R);
    }

    public void remove(T x) {
        Node L, R;

        PairOfNodes pair = rightSplit(root, x);
        L = pair.getL();
        R = pair.getR();

        pair = leftSplit(R, x);
        R = pair.getR();

        if (pair.getL() != null)
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

    public int getSize() {
        return this.size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (root == null) {
            return "[]";
        }
        List<T> ar = new ArrayList<T>();
        walkInOrder(root, ar);
        return ar.stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "[", "]"));
    }

    private void walkInOrder(Node N, List<T> ar) {
        if (N.L != null)
            walkInOrder(N.L, ar);

        ar.add(N.x);

        if (N.R != null)
            walkInOrder(N.R, ar);

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

        if (N.x.compareTo(x) <= 0) {
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

        if (N.x.compareTo(x) < 0) {
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

    private class Node {
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
