package ar.edu.itba.paw.persistence.utils;

public class Pair<T, R> {
    private T left;
    private R right;

    public Pair(T left, R right) {
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return this.left;
    }

    public R getRight() {
        return this.right;
    }
}
