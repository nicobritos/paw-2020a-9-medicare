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

/**
 * This class provides methods to access and manipulate instance's fields via reflection invoking its public
 * getters and setters. It can also get fields that are marked as private without any getter associated with it.
 *
 * Its usage is discouraged in most cases as it is difficult to understand and it gives the sensation of working by
 * magic, but it allows for really great abstraction and generalization, specially when instantiating models from data
 * saved in the DB without having to implement each method in each specific DAO, let alone using hydration.
 *
 * Every method access an object fields and its parents
 */
public abstract class ReflectionGetterSetter {
    public static Map<String, ?> listValues(Object object) {
        Map<String, Object> map = new HashMap<>();

        for (Field field : object.getClass().getDeclaredFields()) {
            map.put(field.getName(), get(object, field));
        }
        // We also need parent
        for (Field field : object.getClass().getSuperclass().getDeclaredFields()) {
            map.put(field.getName(), get(object, field));
        }

        return map;
    }

    public static Map<String, ?> listValues(Object object, Class<? extends Annotation> annotationClass) {
        Map<String, Object> map = new HashMap<>();

        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                map.put(field.getName(), get(object, field));
            }
        }
        // We also need parent
        for (Field field : object.getClass().getSuperclass().getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                map.put(field.getName(), get(object, field));
            }
        }

        return map;
    }

    public static <T extends Annotation> Map<String, ?> listValues(Object object, Class<T> annotationClass, Function<Field, String> mapper) {
        Map<String, Object> map = new HashMap<>();

        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                map.put(mapper.apply(field), get(object, field));
            }
        }
        // We also need parent
        for (Field field : object.getClass().getSuperclass().getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                map.put(mapper.apply(field), get(object, field));
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
        // We also need parent
        for (Field field : mClass.getSuperclass().getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                consumer.accept(field);
            }
        }
    }

    public static <T extends Annotation> void iterateValues(Object object, Class<T> annotationClass, BiConsumer<Field, Object> consumer) {
        iterateValues(object, annotationClass, false, consumer);
    }

    public static <T extends Annotation> void iterateValues(Object object, Class<T> annotationClass, boolean directAccess, BiConsumer<Field, Object> consumer) {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                consumer.accept(field, get(object, field, directAccess));
            }
        }
        // We also need parent
        for (Field field : object.getClass().getSuperclass().getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                consumer.accept(field, get(object, field, directAccess));
            }
        }
    }

    public static Object get(Object object, String fieldName) throws NoSuchFieldException {
        return get(object, getFieldByName(fieldName, object.getClass()), false);
    }

    public static void set(Object object, String fieldName, Object data) throws NoSuchFieldException {
        set(object, getFieldByName(fieldName, object.getClass()), data);
    }

    public static void set(Object object, Field field, Object data) {
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), object.getClass());
            propertyDescriptor.getWriteMethod().invoke(object, data);
        } catch (IntrospectionException e) {
            // TODO
        } catch (IllegalAccessException e) {
            // TODO
        } catch (InvocationTargetException e) {
            // TODO
        }
    }

    private static Field getFieldByName(String fieldName, Class<?> className) throws NoSuchFieldException {
        try {
            return className.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return className.getSuperclass().getDeclaredField(fieldName);
        }
    }

    private static Object get(Object object, Field field) {
        return get(object, field, false);
    }

    /**
     * @param object the object owning the field
     * @param field the field
     * @param direct whether the access method should be directly by its field or using getters
     * @return the value associated with the field
     */
    private static Object get(Object object, Field field, boolean direct) {
        try {
            if (direct) {
                boolean accessible = field.isAccessible();
                if (accessible) {
                    return field.get(object);
                } else {
                    field.setAccessible(true);
                    Object value = field.get(object);
                    field.setAccessible(accessible);
                    return value;
                }
            } else {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), object.getClass());
                return propertyDescriptor.getReadMethod().invoke(object);
            }
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
