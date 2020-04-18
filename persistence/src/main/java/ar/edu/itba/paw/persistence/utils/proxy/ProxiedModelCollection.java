package ar.edu.itba.paw.persistence.utils.proxy;

import ar.edu.itba.paw.models.GenericModel;

import java.lang.reflect.Field;
import java.util.Set;

public interface ProxiedModelCollection {
    Set<GenericModel<Object, Object>> getPreviousModels(Field field);

    void setPreviousCollection(Field field, Set<GenericModel<Object, Object>> models);
}
