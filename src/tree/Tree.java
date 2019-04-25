package tree;

public interface Tree<T> extends Iterable<T> {
    int getSize();
    boolean isEmpty();
    void clear();
    Object[] toArray();
}
