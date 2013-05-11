package net.jirasystems.webulizor.interfaces;

/**
 * Represents an {@link Action} which wants to know about an exception thrown during request
 * processing, such as an error page.
 * 
 * @author David Carboni
 * 
 */
public interface ExceptionAware {

	/**
	 * @param t
	 *            The {@link Throwable} that the implementing class wants to know about.
	 */
	void setException(Throwable t);
}
