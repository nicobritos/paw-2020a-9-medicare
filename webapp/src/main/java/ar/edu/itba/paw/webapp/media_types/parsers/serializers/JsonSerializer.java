package ar.edu.itba.paw.webapp.media_types.parsers.serializers;

import org.json.JSONArray;

import java.util.Collection;

public abstract class JsonSerializer<T> {
    // Lo dejamos en Object porque podria devolver un JSONArray tranquilamente
    // y JSONObject no extiende ninguna clase o implementa alguna interfaz como
    // para diferenciarlos
    public abstract Object toJson(T t);

    public JSONArray toJsonArray(Collection<T> ts) {
        JSONArray array = new JSONArray();
        for (T t : ts) {
            array.put(this.toJson(t));
        }
        return array;
    }
}
