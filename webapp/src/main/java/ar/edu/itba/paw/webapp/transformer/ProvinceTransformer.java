package ar.edu.itba.paw.webapp.transformer;

import ar.edu.itba.paw.models.Province;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProvinceTransformer extends GenericTransformer<Province> {
    @Override
    public Map<String, ?> transform(Province province) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", province.getId());
        map.put("name", province.getName());
        return map;
    }
}
