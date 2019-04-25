package tree;

public interface Heap<T> extends Tree<T> {
    void add(T value);
    T peek();
    T poll();
}
