package ar.edu.itba.paw.models;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Paginator<M extends GenericModel<?>> {
    private final int totalCount, pageSize, page, remainingPages;
    private final List<M> models;

    public Paginator(Collection<M> models, int page, int pageSize, int totalCount) {
        this.models = new LinkedList<>(models);
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.page = page;

        this.remainingPages = (int) Math.ceil((double) totalCount / (double) pageSize) - page;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public int getRemainingPages() {
        return this.remainingPages;
    }

    public List<M> getModels() {
        return this.models;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getPage() {
        return this.page;
    }
}
