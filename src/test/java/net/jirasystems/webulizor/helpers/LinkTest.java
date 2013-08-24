/**
 * 
 */
package net.jirasystems.webulizor.helpers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.URI;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.jirasystems.webulizor.annotations.Route;
import net.jirasystems.webulizor.base.AbstractAction;
import net.jirasystems.webulizor.interfaces.Action;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author david
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class LinkTest {

	public static final String pathNoSlash = "test";
	public static final String pathSlash = "/test";

	@Mock
	Action action;

	@Mock
	private ServletContext servletContext;

	@Mock
	private HttpServletRequest request;

	// Path information:
	private String contextPath = "/" + this.getClass().getSimpleName();

	// Class under test.
	private Link link;

	/**
	 * Sets up the class under test.
	 */
	@Before
	public void setUp() throws Exception {
		when(action.getServletContext()).thenReturn(servletContext);
		when(action.getRequest()).thenReturn(request);
		when(servletContext.getContextPath()).thenReturn(contextPath);
		link = new Link(servletContext, request);
	}

	/**
	 * Verifies that a path is correctly resolved in the ROOT context.
	 */
	@Test
	public void shouldResolvePathInRootContext() {

		// Given
		when(servletContext.getContextPath()).thenReturn("/");
		link = new Link(servletContext, request);
		String path = "/path";

		// When
		URI resolved = link.resolve(path);

		// Then
		assertEquals(path, resolved.getPath());
	}

	/**
	 * Verifies that a path can be resolved with no context.
	 */
	@Test
	public void shouldRemoveContext() {

		// Given
		Link.DROP_CONTEXT = true;
		link = new Link(servletContext, request);
		Link.DROP_CONTEXT = false;
		String path = "/path";

		// When
		URI resolved = link.resolve(path);

		// Then
		assertEquals(path, resolved.getPath());
	}

	/**
	 * Verifies that a path with a leading slash doesn't end up with a
	 * double-slash.
	 */
	@Test
	public void shouldResolvePathWithLeadingSlash() {

		// Given
		String path = "/path";

		// When
		URI resolved = link.resolve(path);

		// Then
		assertEquals(contextPath + path, resolved.getPath());
	}

	/**
	 * Verifies that a path without a leading slash doesn't end up with a
	 * missing slash.
	 */
	@Test
	public void shouldResolvePathWithoutLeadingSlash() {

		// Given
		String path = "path";

		// When
		URI resolved = link.resolve(path);

		// Then
		assertEquals(contextPath + "/" + path, resolved.getPath());
	}

	/**
	 * Verifies that a path is correctly resolved from an {@link Action} class.
	 */
	@Test
	public void shouldResolvePathFromActionClassWithSlash() {

		// Given
		Class<? extends Action> action = ActionSlash.class;

		// When
		URI resolved = link.resolve(action);

		// Then
		assertEquals(contextPath + "/" + pathNoSlash, resolved.getPath());
	}

	/**
	 * Verifies that a path is correctly resolved from an {@link Action} class.
	 */
	@Test
	public void shouldResolvePathFromActionClassWithNoSlash() {

		// Given
		Class<? extends Action> action = ActionNoSlash.class;

		// When
		URI resolved = link.resolve(action);

		// Then
		assertEquals(contextPath + pathSlash, resolved.getPath());
	}

	/**
	 * Verifies that a path is correctly resolved from an {@link Action} class.
	 */
	@Test
	public void shouldResolvePathFromActionClassWithDefaultPath() {

		// Given
		Class<? extends Action> action = ActionDefault.class;

		// When
		URI resolved = link.resolve(action);

		// Then
		assertEquals(contextPath + "/"
				+ ActionDefault.class.getSimpleName().toLowerCase(),
				resolved.getPath());
	}

	@Route(path = LinkTest.pathSlash)
	public static class ActionSlash extends AbstractAction {
		@Override
		public Action perform() throws Exception {
			return null;
		}
	}

	@Route(path = LinkTest.pathNoSlash)
	public static class ActionNoSlash extends AbstractAction {
		@Override
		public Action perform() throws Exception {
			return null;
		}
	}

	@Route()
	public static class ActionDefault extends AbstractAction {
		@Override
		public Action perform() throws Exception {
			return null;
		}
	}

	@Test
	public void shouldBuildUrl() {

		// Given
		setupUrl(8080, false, "localhost");

		// When
		URI url = link.url();

		// Then
		assertEquals("http://localhost:8080" + contextPath, url.toString());
	}

	@Test
	public void shouldBuildUrlWithoutContext() {

		// Given
		Link.DROP_CONTEXT = true;
		setupUrl(8080, false, "localhost");
		Link.DROP_CONTEXT = false;

		// When
		URI url = link.url();

		// Then
		assertEquals("http://localhost:8080", url.toString());
	}

	@Test
	public void shouldBuildSecureUrl() {

		// Given
		setupUrl(8080, true, "localhost");

		// When
		URI url = link.url();

		// Then
		assertEquals("https://localhost:8080" + contextPath, url.toString());
	}

	@Test
	public void shouldHidePort80() {

		// Given
		setupUrl(80, false, "localhost");

		// When
		URI url = link.url();

		// Then
		assertEquals("http://localhost" + contextPath, url.toString());
	}

	@Test
	public void shouldHidePort443() {

		// Given
		setupUrl(443, true, "localhost");

		// When
		URI url = link.url();

		// Then
		assertEquals("https://localhost" + contextPath, url.toString());
	}

	@Test
	public void shouldBuildUrlForAction() {

		// Given
		setupUrl(80, false, "localhost");

		// When
		URI url = link.url(ActionSlash.class);

		// Then
		assertEquals("http://localhost" + contextPath + pathSlash,
				url.toString());
	}

	@Test
	public void shouldBuildUrlForActionWithQuery() {

		// Given
		setupUrl(80, false, "localhost");
		String name = "name";
		String value = "value";
		QueryString queryString = new QueryString("name", "value");

		// When
		URI url = link.url(ActionSlash.class, queryString);

		// Then
		assertEquals("http://localhost" + contextPath + pathSlash + "?" + name
				+ "=" + value, url.toString());
	}

	/**
	 * Re-instantiates {@link #link} with the given values for the URL.
	 * 
	 * @param serverPort
	 *            Port.
	 * @param secure
	 *            Https?
	 * @param host
	 *            Host name.
	 */
	private void setupUrl(int serverPort, boolean secure, String host) {
		String scheme;
		if (secure) {
			scheme = "https";
		} else {
			scheme = "http";
		}
		when(request.getServerPort()).thenReturn(serverPort);
		when(request.isSecure()).thenReturn(secure);
		when(request.getScheme()).thenReturn(scheme);
		when(request.getServerName()).thenReturn(host);

		// Create a new instance:
		link = new Link(servletContext, request);
	}

}
