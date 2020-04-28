package ar.edu.itba.paw.persistence.utils.proxy;

import ar.edu.itba.paw.models.GenericModel;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;

public abstract class ProxyHelper {
    public static <M extends GenericModel<M, I>, I> M copyInstance(M instance) {
        if (!isProxied(instance))
            return null;

        Enhancer enhancer = new Enhancer();
        ProxiedModel<M, I> proxiedModel = new ProxiedModel<>(getAsProxiedModel(instance));
        enhancer.setSuperclass(proxiedModel.getTarget().getClass());
        enhancer.setCallback(proxiedModel.getMethodInterceptor());
        return (M) enhancer.create();
    }

    public static <M extends GenericModel<M, I>, I> M createInstance(M instance) {
        if (isProxied(instance))
            return copyInstance(instance);

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(instance.getClass());
        ProxiedModel<M, I> proxiedModel = new ProxiedModel<>(instance);
        enhancer.setCallback(proxiedModel.getMethodInterceptor());
        return (M) enhancer.create();
    }

    public static <M extends GenericModel<M, I>, I> Collection<GenericModel<Object, Object>> getPreviousCollection(M model, Field field) {
        ProxiedModel<M, I> proxiedModel = getAsProxiedModel(model);
        if (proxiedModel != null)
            return proxiedModel.getPreviousModels(field);
        return new LinkedList<>();
    }

    public static <M extends GenericModel<M, I>, I> void setPreviousCollection(M model, Field field, Collection<GenericModel<Object, Object>> models) {
        ProxiedModel<M, I> proxiedModel = getAsProxiedModel(model);
        if (proxiedModel != null)
            proxiedModel.setPreviousCollection(field, models);
    }

    public static <M extends GenericModel<M, I>, I> void setField(M model, Field field, Object value) {
        ProxiedModel<M, I> proxiedModel = getAsProxiedModel(model);
        if (proxiedModel != null) {
            proxiedModel.setField(field, value);
        }
    }

    public static <M extends GenericModel<M, I>, I> boolean isProxied(M model) {
        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(model.getClass(), "callbacks");
        return propertyDescriptor != null && propertyDescriptor.getReadMethod() != null;
    }

    private static <M extends GenericModel<M, I>, I> ProxiedModel<M, I> getAsProxiedModel(M proxiedModel) {
        try {
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(proxiedModel.getClass(), "callbacks");
            if (propertyDescriptor == null)
                return null;

            MethodInterceptor methodInterceptor = ((MethodInterceptor) ((Callback[]) propertyDescriptor.getReadMethod().invoke(proxiedModel))[0]);
            try {
                return (ProxiedModel<M, I>) methodInterceptor.intercept(null, null, null, null);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
        return null;
    }
}
