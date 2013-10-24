package net.jirasystems.webulizor.helpers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class ForwardedRequest {

	private static boolean spoken;

	/**
	 * Wraps the given {@link PreparedStatement} in a Java dynamic proxy which
	 * will log method calls.
	 * 
	 * @param connection
	 *            The {@link Connection} to be spied.
	 * @return The wrapped {@link PreparedStatement}
	 */
	public static HttpServletRequest newInstance(
			final HttpServletRequest httpServletRequest) {

		InvocationHandler handler = new InvocationHandler() {

			private HttpServletRequest request = httpServletRequest;
			private int serverPort;
			private boolean secure;
			private String scheme;
			private String host;
			private String client;
			private boolean initialised;

			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {

				getRequestInformation();
				String methodName = method.getName();
				if (StringUtils.equals("getServerPort", methodName)) {
					return serverPort;
				} else if (StringUtils.equals("isSecure", methodName)) {
					return secure;
				} else if (StringUtils.equals("getScheme", methodName)) {
					return scheme;
				} else if (StringUtils.equals("getServerName", methodName)) {
					return host;
				} else if (StringUtils.equals("getRemoteHost", methodName)) {
					return client;
				} else {
					// Invoke the method
					return method.invoke(request, args);
				}
			}

			private void getRequestInformation() {

				if (!initialised) {

					// URL components:
					serverPort = request.getServerPort();
					secure = request.isSecure();
					scheme = request.getScheme();
					host = request.getServerName();
					client = request.getRemoteHost();

					// Do we need to consider X-Forwarded-* headers?

					String forwardedHost = request
							.getHeader("X-Forwarded-Host");
					String forwardedScheme = request
							.getHeader("X-Forwarded-Proto");
					String forwardedClient = request
							.getHeader("X-Forwarded-For");

					// Update server name:
					if (forwardedHost != null
							&& !StringUtils.equals(forwardedHost, host)) {
						host = forwardedHost;
						if (!ForwardedRequest.spoken) {
							System.out.println("Host updated to: " + host);
						}
					}

					// Update request scheme and port:
					if (forwardedScheme != null
							&& !StringUtils.equals(forwardedScheme, scheme)) {
						scheme = forwardedScheme;
						// Assume standard https or http ports if it's been
						// forwarded:
						if (StringUtils.equalsIgnoreCase("https", scheme)) {
							serverPort = 443;
							secure = true;
						} else {
							serverPort = 80;
							secure = false;
						}
						if (!ForwardedRequest.spoken) {
							System.out
									.println("Secure/Scheme/Port updated to: "
											+ scheme + "/" + secure + "/"
											+ serverPort);
						}
					}

					// Update client:
					if (forwardedClient != null
							&& !StringUtils.equals(forwardedClient, client)) {
						client = forwardedClient;
					}

					ForwardedRequest.spoken = true;
					initialised = true;
				}
			}

		};
		Object proxy = Proxy.newProxyInstance(
				ForwardedRequest.class.getClassLoader(),
				new Class[] { HttpServletRequest.class }, handler);
		return (HttpServletRequest) proxy;
	}
}
