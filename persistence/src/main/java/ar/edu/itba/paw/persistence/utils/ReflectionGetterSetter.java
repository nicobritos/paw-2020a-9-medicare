package ar.edu.itba.paw.persistence.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ReflectionGetterSetter {
    public static Map<String, ?> listValues(Object model) {
        Map<String, Object> map = new HashMap<>();

        for (Field field : model.getClass().getDeclaredFields()) {
            map.put(field.getName(), get(model, field));
        }

        return map;
    }

    public static Map<String, ?> listValues(Object model, Class<? extends Annotation> annotationClass) {
        Map<String, Object> map = new HashMap<>();

        for (Field field : model.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                map.put(field.getName(), get(model, field));
            }
        }

        return map;
    }

    public static <T extends Annotation> Map<String, ?> listValues(Object model, Class<T> annotationClass, Function<Field, String> mapper) {
        Map<String, Object> map = new HashMap<>();

        for (Field field : model.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                map.put(mapper.apply(field), get(model, field));
            }
        }

        return map;
    }

    public static <T extends Annotation> void iterateFields(Class<?> mClass, Class<T> annotationClass, Consumer<Field> consumer) {
        for (Field field : mClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                consumer.accept(field);
            }
        }
    }

    public static <T extends Annotation> void iterateValues(Object model, Class<T> annotationClass, BiConsumer<Field, Object> consumer) {
        for (Field field : model.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                consumer.accept(field, get(model, field));
            }
        }
    }

    public static Object get(Object model, String fieldName) throws NoSuchFieldException {
        return get(model, getFieldByName(fieldName, model.getClass()));
    }

    public static void set(Object model, String fieldName, Object data) throws NoSuchFieldException {
        set(model, getFieldByName(fieldName, model.getClass()), data);
    }

    public static void set(Object model, Field field, Object data) {
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), model.getClass());
            propertyDescriptor.getWriteMethod().invoke(model, data);
        } catch (IntrospectionException e) {
            // TODO
        } catch (IllegalAccessException e) {
            // TODO
        } catch (InvocationTargetException e) {
            // TODO
        }
    }

    private static Field getFieldByName(String fieldName, Class<?> className) throws NoSuchFieldException {
        return className.getDeclaredField(fieldName);
    }

    private static Object get(Object model, Field field) {
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), model.getClass());
            return propertyDescriptor.getReadMethod().invoke(model);
        } catch (IntrospectionException e) {
            // TODO
        } catch (IllegalAccessException e) {
            // TODO
        } catch (InvocationTargetException e) {
            // TODO
        }
        return null;
    }
}
