package ar.edu.itba.paw.persistenceAnnotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation enables a given model's field to be mapped to a column in a database
 * For this to work properly, the model must have been annotated with @Table.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * @return The column's name
     */
    String name();

    /**
     * If this is set to true, then an exception will be thrown when trying to save a model
     * with the annotated field set to null
     * @return if this field is required
     */
    boolean required() default false;
}
