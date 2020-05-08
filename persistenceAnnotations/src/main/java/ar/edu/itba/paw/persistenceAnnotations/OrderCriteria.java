package ar.edu.itba.paw.persistenceAnnotations;

public enum OrderCriteria {
    ASC(" ASC "),
    DESC(" DESC ");

    private String criteria;

    OrderCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getCriteria() {
        return this.criteria;
    }

    @Override
    public String toString() {
        return this.criteria;
    }
}
