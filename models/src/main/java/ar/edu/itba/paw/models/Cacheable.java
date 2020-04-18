package ar.edu.itba.paw.models;

public abstract class Cacheable {
    private boolean cached;

    public boolean isCached() {
        return this.cached;
    }
}
