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

	/**
	 * @return Allows you to refer to a different action class for the path. This means one class
	 *         can define the route and another can refer to it, e.g. classes for GET and POST to
	 *         the same URL.
	 */
	Class<? extends Action> sameAs() default Action.class;
}
