package net.jirasystems.webulizor.base;

import java.sql.Connection;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jirasystems.webulizor.annotations.Route;
import net.jirasystems.webulizor.interfaces.Action;

import org.apache.commons.lang.StringUtils;

/**
 * Implements the basic requirements of the {@link Action} interface.
 * 
 * @author David Carboni
 * 
 */
public abstract class AbstractAction implements Action {

	private ServletContext servletContext;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Connection connection;
	private Map<String, Object> context;

	/**
	 * @return the servletContext
	 */
	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * @param servletContext
	 *            the servletContext to set
	 */
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * @return the request
	 */
	@Override
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request
	 *            the request to set
	 */
	@Override
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @return the response
	 */
	@Override
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * @param response
	 *            the response to set
	 */
	@Override
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * Returns the path to access this class. This is useful when building links in Velocity.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result;

		Route routing = this.getClass().getAnnotation(Route.class);
		if (routing != null && StringUtils.isNotBlank(routing.path())) {
			result = routing.path();
		} else {
			result = "/" + this.getClass().getSimpleName().toLowerCase();
		}

		return result;
	}

	/**
	 * @return the connection
	 */
	@Override
	public Connection getConnection() {
		return connection;
	}

	/**
	 * @param connection
	 *            the connection to set
	 */
	@Override
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @return the context
	 */
	@Override
	public Map<String, Object> getContext() {
		return context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	@Override
	public void setContext(Map<String, Object> context) {
		this.context = context;
	}

}
