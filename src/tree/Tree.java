package tree;

public interface Tree<T> {
    int getSize();
    boolean isEmpty();
    void clear();
    Object[] toArray();
}
