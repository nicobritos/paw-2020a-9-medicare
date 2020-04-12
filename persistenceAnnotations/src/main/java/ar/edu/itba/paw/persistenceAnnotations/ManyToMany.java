package ar.edu.itba.paw.persistenceAnnotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ManyToMany {
    String name();
    String otherName();
    String tableName();
    Class<?> className();
    boolean required() default false;
}
