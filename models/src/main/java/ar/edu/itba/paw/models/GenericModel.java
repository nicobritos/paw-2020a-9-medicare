package ar.edu.itba.paw.models;

import java.util.Objects;

/**
 * This class provides a generic implementation of a model.
 * @param <I>
 */
public abstract class GenericModel<M, I> extends Cacheable<M> {
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
        if (o == null || this.getClass() != o.getClass()) return false;
        GenericModel<?, ?> that = (GenericModel<?, ?>) o;
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
