package ar.edu.itba.paw.persistence.utils;

import org.springframework.beans.BeanUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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

        Class<?> c = object.getClass();
        do {
            for (Field field : c.getDeclaredFields()) {
                map.put(field.getName(), get(c.cast(object), field));
            }
        } while ((c = c.getSuperclass()) != Object.class && c != null);

        return map;
    }

    public static Map<String, ?> listValues(Object object, Class<? extends Annotation> annotationClass) {
        Map<String, Object> map = new HashMap<>();

        Class<?> c = object.getClass();
        do {
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotationClass)) {
                    map.put(field.getName(), get(c.cast(object), field));
                }
            }
        } while ((c = c.getSuperclass()) != Object.class && c != null);

        return map;
    }

    public static <T extends Annotation> Map<String, ?> listValues(Object object, Class<T> annotationClass, Function<Field, String> mapper) {
        Map<String, Object> map = new HashMap<>();

        Class<?> c = object.getClass();
        do {
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotationClass)) {
                    map.put(mapper.apply(field), get(c.cast(object), field));
                }
            }
        } while ((c = c.getSuperclass()) != Object.class && c != null);

        return map;
    }

    public static void setValues(Object object, Map<String, ?> values, boolean direct) {
        Class<?> c = object.getClass();
        do {
            for (Field field : c.getDeclaredFields()) {
                set(object, field, values.get(field.getName()), direct);
            }
        } while ((c = c.getSuperclass()) != Object.class && c != null);
    }

    public static <T extends Annotation> void iterateFields(Class<?> mClass, Class<T> annotationClass, Consumer<Field> consumer) {
        Class<?> c = mClass;
        do {
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotationClass)) {
                    consumer.accept(field);
                }
            }
        } while ((c = c.getSuperclass()) != Object.class && c != null);
    }

    public static <T extends Annotation> void iterateValues(Object object, Class<T> annotationClass, BiConsumer<Field, Object> consumer) {
        iterateValues(object, annotationClass, false, consumer);
    }

    public static <T extends Annotation> void iterateValues(Object object, Class<T> annotationClass, boolean directAccess, BiConsumer<Field, Object> consumer) {
        Class<?> c = object.getClass();
        do {
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotationClass)) {
                    consumer.accept(field, get(c.cast(object), field, directAccess));
                }
            }
        } while ((c = c.getSuperclass()) != Object.class && c != null);
    }

    public static <T extends Annotation> Object getValueAnnotatedWith(Object object, Class<T> annotationClass, Predicate<T> checkAnnotation) {
        Class<?> c = object.getClass();
        do {
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotationClass) && checkAnnotation.test(field.getAnnotation(annotationClass))) {
                    return get(c.cast(object), field);
                }
            }
        } while ((c = c.getSuperclass()) != Object.class && c != null);

        return null;
    }

    public static Object get(Object object, String fieldName) throws NoSuchFieldException {
        return get(object, getFieldByName(fieldName, object.getClass()), false);
    }

    public static Object get(Object object, String fieldName, boolean direct) throws NoSuchFieldException {
        return get(object, getFieldByName(fieldName, object.getClass()), direct);
    }

    /**
     * @param object the object owning the field
     * @param field the field
     * @param direct whether the access method should be directly by its field or using getters
     * @return the value associated with the field
     */
    public static Object get(Object object, Field field, boolean direct) {
        try {
            if (direct) {
                if (field.isAccessible()) {
                    return field.get(object);
                } else {
                    field.setAccessible(true);
                    Object value = field.get(object);
                    field.setAccessible(false);
                    return value;
                }
            } else {
                Method method = BeanUtils.getPropertyDescriptor(object.getClass(), field.getName()).getReadMethod();
                if (method.isAccessible()) {
                    return method.invoke(object);
                } else {
                    method.setAccessible(true);
                    Object value = method.invoke(object);
                    method.setAccessible(false);
                    return value;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getter(Class<?> className, Field field) {
        PropertyDescriptor propertyDescriptor = null;
        try {
            propertyDescriptor = new PropertyDescriptor(field.getName(), className);
        } catch (IntrospectionException e) {
        }
        if (propertyDescriptor == null) {
            propertyDescriptor = BeanUtils.getPropertyDescriptor(className, field.getName());
        }
        if (propertyDescriptor != null)
            return propertyDescriptor.getReadMethod();
        return null;
    }

    public static void set(Object object, String fieldName, Object data) throws NoSuchFieldException {
        set(object, getFieldByName(fieldName, object.getClass()), data, false);
    }

    public static void set(Object object, String fieldName, Object data, boolean direct) throws NoSuchFieldException {
        set(object, getFieldByName(fieldName, object.getClass()), data, direct);
    }

    public static void set(Object object, Field field, Object value) {
        set(object, field, value, false);
    }

    public static void set(Object object, Field field, Object value, boolean direct) {
        try {
            if (direct) {
                if (field.isAccessible()) {
                    field.set(object, value);
                } else {
                    field.setAccessible(true);
                    field.set(object, value);
                    field.setAccessible(false);
                }
            } else {
                Method method = BeanUtils.getPropertyDescriptor(object.getClass(), field.getName()).getWriteMethod();
                if (method.isAccessible()) {
                    method.invoke(object, value);
                } else {
                    method.setAccessible(true);
                    method.invoke(object, value);
                    method.setAccessible(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Field getFieldByName(String fieldName, Class<?> className) throws NoSuchFieldException {
        Class<?> c = className;
        do {
            try {
                return c.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
            }
        } while ((c = c.getSuperclass()) != Object.class && c != null);
        return null;
    }

    private static Object get(Object object, Field field) {
        return get(object, field, false);
    }
}
