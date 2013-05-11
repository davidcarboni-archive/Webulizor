package net.jirasystems.webulizor.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.jirasystems.webulizor.interfaces.Action;

/**
 * Describes the URL mapping for an {@link Action}.
 * 
 * @author david
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Route {

	/**
	 * @return The URL path to reach this action, excluding servlet context. This should have a
	 *         leading slash. If not, Webulizor will add one on the fly.
	 */
	String path() default "";
}
