package net.jirasystems.webulizor.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.apache.commons.lang.StringUtils;

/**
 * Describes an HTTP parameter.
 * 
 * @author David Carboni
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameter {

	/**
	 * @return The name of the parameter. Defaults to an empty String, which indicates the parameter
	 *         name is the same as the field name.
	 */
	String name() default StringUtils.EMPTY;

	/**
	 * @return Whether this parameter is required. Defaults to false.
	 */
	boolean required() default false;

}
