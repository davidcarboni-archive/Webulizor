package net.jirasystems.webulizor.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.sql.Connection;

import net.jirasystems.webulizor.interfaces.Action;

/**
 * Indicates an {@link Action} class should be provided with a database {@link Connection}.
 * <p>
 * The default is that a connection with a transaction is required. If this annotation is not
 * present, this default is assumed.
 * 
 * @author david
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseConnection {

	/**
	 * @return Whether or not a database connection is required by this {@link Action}.
	 */
	boolean required() default true;

	/**
	 * @return Whether or not a transaction is required by this {@link Action}.
	 */
	boolean transaction() default true;
}
