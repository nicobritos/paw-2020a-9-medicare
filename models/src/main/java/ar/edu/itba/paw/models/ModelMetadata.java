package ar.edu.itba.paw.models;

import java.util.Objects;

public class ModelMetadata {
    private final Integer count;
    private final Integer min;
    private final Integer max;

    public ModelMetadata() {
        this.count = this.min = this.max = null;
    }

    public ModelMetadata(Integer count, Integer min, Integer max) {
        this.count = count;
        this.min = min;
        this.max = max;
    }

    public Integer getMin() {
        return this.min;
    }

    public Integer getMax() {
        return this.max;
    }

    public Integer getCount() {
        return this.count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModelMetadata)) return false;
        ModelMetadata that = (ModelMetadata) o;
        return Objects.equals(this.getCount(), that.getCount()) &&
                Objects.equals(this.getMin(), that.getMin()) &&
                Objects.equals(this.getMax(), that.getMax());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getCount(), this.getMin(), this.getMax());
    }
}
