package ar.edu.itba.paw.persistence.utils.proxy;

import ar.edu.itba.paw.models.GenericModel;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public abstract class ProxyHelper {
    public static <M extends GenericModel<M, I>, I> M copyInstance(M instance) {
        if (!isProxied(instance))
            return null;

        ProxiedModel<M, I> proxiedModel = new ProxiedModel<>(getAsProxiedModel(instance));
        ProxyFactory factory = new ProxyFactory(proxiedModel.getTarget());
        factory.addAdvice(proxiedModel);
        return (M) factory.getProxy();
    }

    public static <M extends GenericModel<M, I>, I> M createInstance(M instance) {
        ProxyFactory factory = new ProxyFactory(instance);
        factory.addAdvice(new ProxiedModel<>(instance));
        return (M) factory.getProxy();
    }

    public static <M extends GenericModel<M, I>, I> Set<GenericModel<Object, Object>> getPreviousCollection(M model, Field field) {
        Set<GenericModel<Object, Object>> collection;

        try {
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(model.getClass(), "advisors");
            if (propertyDescriptor != null) {
                collection = ((ProxiedModelCollection) (((Advisor[]) propertyDescriptor.getReadMethod().invoke(model))[0].getAdvice())).getPreviousModels(field);
                if (collection != null) {
                    return collection;
                }
            }

            return new HashSet<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    public static <M extends GenericModel<M, I>, I> void setPreviousCollection(M model, Field field, Set<GenericModel<Object, Object>> models) {
        try {
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(model.getClass(), "advisors");
            if (propertyDescriptor != null) {
                ((ProxiedModelCollection) (((Advisor[]) propertyDescriptor.getReadMethod().invoke(model))[0].getAdvice())).setPreviousCollection(field, models);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <M extends GenericModel<M, I>, I> void setField(M model, Field field, Object value) {
        ProxiedModel<M, I> proxiedModel = getAsProxiedModel(model);
        if (proxiedModel != null) {
            proxiedModel.setField(field, value);
        }
    }

    public static <M extends GenericModel<M, I>, I> boolean isProxied(M model) {
        return BeanUtils.getPropertyDescriptor(model.getClass(), "advisors") != null;
    }

    private static <M extends GenericModel<M, I>, I> ProxiedModel<M, I> getAsProxiedModel(M proxiedModel) {
        try {
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(proxiedModel.getClass(), "advisors");
            if (propertyDescriptor == null)
                return null;

            return (ProxiedModel<M, I>) ((Advisor[]) propertyDescriptor.getReadMethod().invoke(proxiedModel))[0].getAdvice();
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }
}
