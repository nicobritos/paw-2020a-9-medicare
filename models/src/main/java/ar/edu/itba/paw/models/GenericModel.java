package ar.edu.itba.paw.models;

import java.util.Objects;

/**
 * This class provides a generic implementation of a model.
 * @param <I>
 */
public abstract class GenericModel<M, I> {
    protected I id;

    public I getId() {
        return this.id;
    }

    public void setId(I id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!this.isSameInstance(o)) return false;
        return this.getId().equals(((GenericModel<?, Object>) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    protected abstract boolean isSameInstance(Object o);
}
