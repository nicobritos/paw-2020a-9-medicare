package ar.edu.itba.paw.persistence.utils;

public class JDBCArgumentValue {
    private final String argument;
    private final Object value;

    public JDBCArgumentValue(String argument, Object value) {
        this.argument = ":" + argument;
        this.value = value;
    }

    public String getArgument() {
        return this.argument;
    }

    public Object getValue() {
        return this.value;
    }
}
