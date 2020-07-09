package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import org.json.JSONArray;

import java.util.Collection;
import java.util.LinkedList;

public abstract class JsonDeserializer<T> {
    public abstract T fromJson(Object object);

    public Collection<T> fromJsonArray(JSONArray jsonArray) {
        Collection<T> collection = new LinkedList<>();
        for (Object o : jsonArray) {
            collection.add(this.fromJson(o));
        }
        return collection;
    }
}
