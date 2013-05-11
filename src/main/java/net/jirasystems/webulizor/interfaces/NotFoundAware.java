package net.jirasystems.webulizor.interfaces;

/**
 * Represents an {@link Action} which wants to know about requested paths which could not be mapped,
 * such as a not-found page.
 * 
 * @author David Carboni
 * 
 */
public interface NotFoundAware {
	/**
	 * @param requestUri
	 *            The request URI that could not be mapped to an {@link Action}.
	 */
	void setRequestUri(String requestUri);
}
