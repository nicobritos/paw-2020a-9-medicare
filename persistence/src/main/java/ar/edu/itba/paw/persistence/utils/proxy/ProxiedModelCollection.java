package ar.edu.itba.paw.persistence.utils.proxy;

import ar.edu.itba.paw.models.GenericModel;

import java.lang.reflect.Field;
import java.util.Collection;

public interface ProxiedModelCollection {
    Collection<GenericModel<Object, Object>> getPreviousModels(Field field);

    void setPreviousCollection(Field field, Collection<GenericModel<Object, Object>> models);
}
