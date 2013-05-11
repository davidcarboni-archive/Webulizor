package net.jirasystems.webulizor.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.jirasystems.webulizor.interfaces.Action;

/**
 * Indicates the single {@link Action} which will be mapped to the default path ("/").
 * 
 * @author david
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface HomeAction {
	// No properties.
}
