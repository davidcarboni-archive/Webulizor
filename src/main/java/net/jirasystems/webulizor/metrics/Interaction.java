package net.jirasystems.webulizor.metrics;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Interaction {

	private String contentType;
	private String method;
	private String requestURI;
	private String queryString;
	private Map<String, String> headers;

	public Interaction(HttpServletRequest request) {

		// Basic values:
		contentType = request.getContentType();
		method = request.getMethod();
		requestURI = request.getRequestURI();
		queryString = request.getQueryString();

		// Headers:
		@SuppressWarnings("unchecked")
		Enumeration<String> headerNames = request.getHeaderNames();
		headers = new HashMap<String, String>();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			headers.put(headerName, request.getHeader(headerName));
		}
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @return the requestURI
	 */
	public String getRequestURI() {
		return requestURI;
	}

	/**
	 * @return the queryString
	 */
	public String getQueryString() {
		return queryString;
	}

	/**
	 * @return the headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

}
