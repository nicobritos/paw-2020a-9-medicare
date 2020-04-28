package ar.edu.itba.paw.webapp.transformer;

import ar.edu.itba.paw.models.Locality;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LocalityTransformer extends GenericTransformer<Locality> {
    @Override
    public Map<String, ?> transform(Locality locality) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", locality.getId());
        map.put("name", locality.getName());
        return map;
    }
}
