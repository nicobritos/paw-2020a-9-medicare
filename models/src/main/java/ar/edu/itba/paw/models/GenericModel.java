package ar.edu.itba.paw.models;

public abstract class GenericModel<I> {
    protected I id;

    public I getId() {
        return this.id;
    }

    public void setId(I id) {
        this.id = id;
    }
}
