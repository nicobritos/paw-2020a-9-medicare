package ar.edu.itba.paw.webapp.transformer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class GenericTransformer<T> {
    public abstract Map<String, ?> transform(T t);

    public Collection<Map<String, ?>> transform(Collection<T> ts) {
        List<Map<String, ?>> list = new LinkedList<>();
        for (T t : ts) {
            list.add(this.transform(t));
        }
        return list;
    }
}
