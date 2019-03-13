package trees;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Treap {

    private class Node {
        final int x;
        final double y;

        Node L;
        Node R;

        Node(int x) {
            this.x = x;
            this.y = Math.random();
            this.L = null;
            this.R = null;
        }

        Node(int x, Node L, Node R) {
            this.x = x;
            this.y = Math.random();
            this.L = L;
            this.R = R;
        }

        Node(int x, double y, Node L, Node R) {
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

    private Node root;
    private int size;

    public Treap() {
        root = null;
    }

    private Node merge(Node L, Node R) {
        if (L == null) return R;
        if (R == null) return L;

        if (L.y > R.y) {
            return new Node(L.x, L.y, L.L, merge(L.R, R));
        } else {
            return new Node(R.x, R.y, merge(L, R.L), R.R);
        }
    }

    private PairOfNodes split(Node N, int x) {
        if (N == null) {
            return new PairOfNodes(null, null);
        }

        Node L;
        Node R;
        Node newNode = null;

        if (N.x <= x) {
            if (N.R == null) {
                R = null;
            } else {
                PairOfNodes pair = split(N.R, x);
                newNode = pair.getL();
                R = pair.getR();
            }
            L = new Node(N.x, N.y, N.L, newNode);
        } else {
            if (N.L == null) {
                L = null;
            } else {
                PairOfNodes pair = split(N.L, x);
                L = pair.getL();
                newNode = pair.getR();
            }
            R = new Node(N.x, N.y, newNode, N.R);
        }
        return new PairOfNodes(L, R);
    }

    public void add(int x) {
        if (root == null) {
            size = 1;
            root = new Node(x);
            return;
        }

        if (contains(x)) {
            return;
        }
        size++;

        Node L, R;
        Node M = new Node(x);

        PairOfNodes pair = split(root, x);
        L = pair.getL();
        R = pair.getR();

        root = merge(merge(L, M), R);
    }

    public void remove(int x) {
        Node L, R;

        PairOfNodes pair = split(root, x - 1);
        L = pair.getL();
        R = pair.getR();

        pair = split(R, x);
        R = pair.getR();

        if (pair.getL() != null) {
            size--;
        }

        root = merge(L, R);
    }

    public boolean contains(int x) {
        Node L, M, R;

        PairOfNodes pair = split(root, x - 1);
        L = pair.getL();
        R = pair.getR();

        pair = split(R, x);
        M = pair.getL();

        root = merge(L, R);

        return M != null;
    }

    public int size() {
        return this.size();
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
        List<Integer> ar = new ArrayList<Integer>();
        print(root, ar);
        return ar.stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "[", "]"));
    }

    private void print(Node N, List<Integer> ar) {
        if (N.L != null) {
            print(N.L, ar);
        }
        ar.add(N.x);
        if (N.R != null) {
            print(N.R, ar);
        }
    }
}