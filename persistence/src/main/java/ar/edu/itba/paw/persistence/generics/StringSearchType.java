package ar.edu.itba.paw.persistence.generics;

import java.util.function.BiFunction;
import java.util.function.Function;

public enum StringSearchType {
    EQUALS(s -> s, String::equals),
    PREFIX(s -> '%' + s, String::startsWith),
    SUFFIX(s -> s + '%', String::endsWith),
    CONTAINS(s -> '%' + s + '%', String::contains);

    private Function<String, String> transformer;
    private BiFunction<String, String, Boolean> operator;

    StringSearchType(Function<String, String> transformer, BiFunction<String, String, Boolean> operator) {
        this.transformer = transformer;
        this.operator = operator;
    }

    public String transform(String s) {
        return this.transformer.apply(s);
    }

    public boolean operate(String s1, String s2) {
        return this.operator.apply(s1, s2);
    }
}
