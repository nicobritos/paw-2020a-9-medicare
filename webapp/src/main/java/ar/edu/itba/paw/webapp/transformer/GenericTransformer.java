package ar.edu.itba.paw.webapp.transformer;

import java.util.*;

public abstract class GenericTransformer<T> {
    public abstract Map<String, ?> transform(T t);

    public Collection<Map<String, ?>> transform(Collection<T> ts) {
        List<Map<String, ?>> list = new LinkedList<>();
        for (T t : ts) {
            list.add(this.transform(t));
        }
        return list;
    }

    public Map<String, ?> transformMap(Collection<T> ts) {
        return new HashMap<>();
    }
}
