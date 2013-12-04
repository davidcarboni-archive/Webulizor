package net.jirasystems.webulizor.base;

import java.net.URI;
import java.net.URISyntaxException;

import net.jirasystems.webulizor.annotations.DatabaseConnection;
import net.jirasystems.webulizor.annotations.Route;
import net.jirasystems.webulizor.helpers.Link;
import net.jirasystems.webulizor.helpers.Path;
import net.jirasystems.webulizor.helpers.QueryString;
import net.jirasystems.webulizor.interfaces.Action;
import net.jirasystems.webulizor.interfaces.Get;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;

/**
 * Simple redirect implementation.
 * 
 * @author David Carboni
 * 
 */
@DatabaseConnection(required = false)
public class RedirectAction extends AbstractAction {

	private int statusCode = 302;
	private URI location;
	private QueryString queryString;
	private String fragment;

	/**
	 * Convenience constructor.
	 * 
	 * @param location
	 *            The location. If this is relative to the context and servlet
	 *            path, it will be expanded to an absolute URI.
	 */
	public RedirectAction(URI location) {
		this.location = location;
	}

	/**
	 * Convenience constructor.
	 * 
	 * @param next
	 *            Redirecs to the given {@link Action} class. The class must be
	 *            annotated with {@link Route}.
	 */
	public RedirectAction(Class<? extends Get> next) {
		setLocation(next);
	}

	/**
	 * Convenience constructor.
	 * 
	 * @param next
	 *            Redirecs to the given {@link Action} class. The class must be
	 *            annotated with {@link Route}.
	 */
	public RedirectAction(Class<? extends Get> next, String fragment) {
		setLocation(next);
		this.fragment = fragment;
	}

	/**
	 * Convenience constructor.
	 * 
	 * @param action
	 *            Redirects to the given {@link Action} class. The class must
	 *            implement {@link Get} and be annotated with {@link Route}.
	 * @param queryString
	 *            A {@link QueryString} to append to the redirect location.
	 */
	public RedirectAction(Class<? extends Get> action, QueryString queryString) {
		this.queryString = queryString;
		location = URI.create(Path.getPath(action));
	}

	@Override
	public Action perform() {
		getResponse().setStatus(statusCode);
		getResponse().setHeader("Location", absoluteLocation().toString());
		return null;
	}

	/**
	 * Absolutizzes the location if necessary.
	 * 
	 * @return If {@link #location} is absolute, {@link #location}, otherwise
	 *         makes {@link #location} into an absolute URL, as it should be for
	 *         a redirect.
	 */
	public URI absoluteLocation() {

		URI absoluteLocation = location;

		// Update to a full URL on the current host:
		if (StringUtils.isEmpty(location.getHost())) {

			// Get request URI information:
			Link link = new Link(getServletContext(), getRequest());
			URI url = link.url();
			String scheme = url.getScheme();
			String host = url.getHost();
			int port = url.getPort();
			String path = location.getPath();
			String query;
			if (queryString != null) {
				query = queryString.toQueryString();
			} else {
				query = location.getQuery();
			}
			// String fragment = location.getFragment();

			// Adjust for default ports:
			if (port == 80 || port == 443) {
				port = -1;
			}

			// Sort out the path:
			path = getRequest().getContextPath() + path;

			// Build the URI:
			try {
				URIBuilder uriBuilder = new URIBuilder();
				uriBuilder.setScheme(scheme);
				uriBuilder.setHost(host);
				uriBuilder.setPort(port);
				uriBuilder.setPath(path);
				uriBuilder.setQuery(query);
				uriBuilder.setFragment(fragment);
				absoluteLocation = uriBuilder.build();
				getServletContext().log(
						"Updated redirect URI to an absolute URL: "
								+ absoluteLocation);
			} catch (URISyntaxException e) {
				throw new RuntimeException("Error adjusting redirect URI", e);
			}
		}

		return absoluteLocation;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode
	 *            the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return the location
	 */
	public URI getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(URI location) {
		this.location = location;
	}

	/**
	 * @param location
	 *            the {@link Get} action to redirect to.
	 */
	public void setLocation(Class<? extends Get> location) {
		this.location = URI.create(Path.getPath(location));
	}

	/**
	 * @return the queryString
	 */
	public QueryString getQueryString() {
		return queryString;
	}

	/**
	 * @param queryString
	 *            the queryString to set
	 */
	public void setQueryString(QueryString queryString) {
		this.queryString = queryString;
	}

}
