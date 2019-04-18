package tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ImplicitTreap {

    private Node root;

    public ImplicitTreap() {
        root = null;
    }

    public void add(int c) {
        Node N = new Node(c);
        root = merge(root, N);
    }

    public int get(int index) {
        checkBounds(index);

        Node L, R;

        PairOfNodes pair = split(root, index);
        L = pair.getL();
        R = pair.getR();

        pair = split(R, 1);
        L = pair.getL();

        return L == null ? 0 : L.c;
    }

    public void put(int index, int c) {
        checkBounds(index);

        PairOfNodes pair = split(root, index);

        Node L = pair.getL();
        Node R = pair.getR();
        Node M = new Node(c);

        root = merge(merge(L, M), R);
    }

    public void set(int index, int c) {
        checkBounds(index);

        Node L, R;
        Node N = new Node(c);

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

    public int size() {
        return sizeOf(root);
    }

    public boolean isEmpty() {
        return sizeOf(root) == 0;
    }

    public void clear() {
        root = null;
    }

    @Override
    public String toString() {
        if (root == null) {
            return "[]";
        }
        List<Integer> ar = new ArrayList<Integer>();
        walkInOrder(root, ar);
        return ar.stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "[", "]"));
    }

    private void walkInOrder(Node N, List<Integer> ar) {
        if (N.L != null)
            walkInOrder(N.L, ar);

        ar.add(N.c);

        if (N.R != null)
            walkInOrder(N.R, ar);

    }

    private Node merge(Node L, Node R) {
        if (L == null) return R;
        if (R == null) return L;

        Node N;

        if (L.y > R.y)
            N = new Node(L.c, L.y, L.L, merge(L.R, R));
        else
            N = new Node(R.c, R.y, merge(L, R.L), R.R);

        N.recalcSize();
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
            L = new Node(N.c, N.y, N.L, newNode);
            L.recalcSize();
        } else {
            if (N.L == null) {
                L = null;
            } else {
                PairOfNodes pair = split(N.L, x);
                L = pair.getL();
                newNode = pair.getR();
            }
            R = new Node(N.c, N.y, newNode, N.R);
            R.recalcSize();
        }

        return new PairOfNodes(L, R);
    }

    private void checkBounds(int index) {
        if (root == null || index >= root.size || index < 0)
            throw new IndexOutOfBoundsException("Index: " + index + " size: " + sizeOf(root));
    }

    private int sizeOf(Node N) {
        if (N == null)
            return 0;

        return N.size;
    }

    private class Node {
        double y;
        int c;
        int size;

        Node L;
        Node R;

        {
            size = 1;
        }

        Node(int c) {
            this.c = c;
            this.y = Math.random();
            this.L = null;
            this.R = null;
        }

        Node(int c, Node L, Node R) {
            this(c);

            this.L = L;
            this.R = R;
        }

        Node(int c, double y, Node L, Node R) {
            this.c = c;
            this.y = y;
            this.L = L;
            this.R = R;
        }

        void recalcSize() {
            size = sizeOf(L) + sizeOf(R) + 1;
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