package net.jirasystems.webulizor.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.jirasystems.webulizor.interfaces.Action;

/**
 * Indicates a single {@link Action} which will be invoked if a request path cannot be mapped.
 * 
 * @author david
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExceptionAction {
	// No properties.
}
