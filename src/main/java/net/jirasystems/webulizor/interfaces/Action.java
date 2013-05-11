/**
 * 
 */
package net.jirasystems.webulizor.interfaces;

import java.sql.Connection;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author david
 * 
 */
public interface Action {

	/**
	 * Triggers the logic of this action, returning the next action.
	 * 
	 * @return The next action in the path, or null if request processing is complete.
	 * @throws Exception
	 *             Allows implementations to throw a wide range of exceptions.
	 */
	Action perform() throws Exception;

	/**
	 * @return the servletContext
	 */
	ServletContext getServletContext();

	/**
	 * @param servletContext
	 *            the servletContext to set
	 */
	void setServletContext(ServletContext servletContext);

	/**
	 * @return the request
	 */
	HttpServletRequest getRequest();

	/**
	 * @param request
	 *            the request to set
	 */
	void setRequest(HttpServletRequest request);

	/**
	 * @return the response
	 */
	HttpServletResponse getResponse();

	/**
	 * @param response
	 *            the response to set
	 */
	void setResponse(HttpServletResponse response);

	/**
	 * @return the connection
	 */
	Connection getConnection();

	/**
	 * @param connection
	 *            the connection to set
	 */
	void setConnection(Connection connection);

	/**
	 * @return the context for this request
	 */
	Map<String, Object> getContext();

	/**
	 * @param context
	 *            the context to set for this request
	 */
	void setContext(Map<String, Object> context);

}
