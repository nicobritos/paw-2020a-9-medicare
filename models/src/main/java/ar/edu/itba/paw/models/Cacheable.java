package ar.edu.itba.paw.models;

public abstract class Cacheable<M> {
    private boolean cached;

    public boolean isCached() {
        return this.cached;
    }
}
