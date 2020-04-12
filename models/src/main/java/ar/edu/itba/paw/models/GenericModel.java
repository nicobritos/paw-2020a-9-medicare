package ar.edu.itba.paw.models;

import java.util.Objects;

public abstract class GenericModel<I> {
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
        GenericModel<?> that = (GenericModel<?>) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
