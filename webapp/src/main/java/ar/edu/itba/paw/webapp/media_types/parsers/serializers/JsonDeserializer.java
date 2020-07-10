package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.Collection;
import java.util.LinkedList;

public abstract class JsonDeserializer<T> {
    public abstract T fromJson(JsonNode object);

    public Collection<T> fromJsonArray(ArrayNode jsonArray) {
        Collection<T> collection = new LinkedList<>();
        for (Object o : jsonArray) {
            try {
                collection.add((T) o);
            } catch (ClassCastException e) {
                collection.add(this.fromJson((JsonNode) o));
            }
        }
        return collection;
    }
}
