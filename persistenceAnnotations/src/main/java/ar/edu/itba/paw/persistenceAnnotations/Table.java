package ar.edu.itba.paw.persistenceAnnotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation enables a given model's to be mapped to a table in a database
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    /**
     * @return The table's name
     */
    String name();

    /**
     * @return The primary key's column's name
     */
    String primaryKey();

    /**
     * If set to true then it allows the primary key to be user-set (and not automatically generated in the DB)
     * @return whether the PK can be manually set
     */
    boolean manualPrimaryKey() default false;
}
