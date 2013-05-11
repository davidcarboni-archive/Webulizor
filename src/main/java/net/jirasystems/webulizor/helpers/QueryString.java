package net.jirasystems.webulizor.helpers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

/**
 * Utility class to wrap creation of a {@link NameValuePair} list and formatting
 * it as a query string using
 * {@link URLEncodedUtils#format(java.util.List, String)}.
 * 
 * @author David Carboni
 * 
 */
public class QueryString extends ArrayList<NameValuePair> {

	/**
	 * Generated by Eclipse.
	 */
	private static final long serialVersionUID = -1273749802923951701L;

	/**
	 * Default constructor.
	 */
	public QueryString() {
		// No initialisation.
	}

	/**
	 * @param queryString
	 *            A query string, for example, as obtained from an
	 *            <code>HttpServletRequest</code>.
	 */
	public QueryString(String queryString) {
		if (!StringUtils.isEmpty(queryString)) {
			URLEncodedUtils.parse(this, new Scanner(queryString), "UTF8");
		}
	}

	/**
	 * 
	 * Adds a single parameter. Zero or more values can be provided and a
	 * parameter will be added for each.
	 * 
	 * @param name
	 *            The name for the parameter.
	 * @param value
	 *            Zero or more values. This determines the number of times the
	 *            parameter is added.
	 */
	public QueryString(String name, String... value) {
		add(name, value);
	}

	// /**
	// *
	// * Adds a single transaction code parameter.
	// *
	// * @param name
	// * The name for the parameter.
	// * @param transaction
	// * The {@link Transaction} class whose transaction code will be added an
	// the
	// * parameter value.
	// */
	// public QueryString(String name, Class<? extends Action> transaction) {
	// add(name, transaction);
	// }

	/**
	 * Constructor.
	 * <p>
	 * Construct a query string using the query portion of the given URI.
	 * 
	 * @param uri
	 *            the URI
	 */
	public QueryString(URI uri) {

		if ((uri != null) && StringUtils.isNotBlank(uri.getQuery())) {
			this.addAll(URLEncodedUtils.parse(uri, "UTF8"));
		}

	}

	/**
	 * @param paramName
	 *            the name
	 * @return the {@link NameValuePair} with the given name if present, null
	 *         otherwise
	 */
	public NameValuePair get(String paramName) {

		for (NameValuePair nameValuePair : this) {
			if (StringUtils.equals(nameValuePair.getName(), paramName)) {
				return nameValuePair;
			}
		}
		return null;
	}

	/**
	 * @param paramName
	 *            the name
	 * @return the value of the {@link NameValuePair} with the given name if
	 *         present, null otherwise
	 */
	public String getValue(String paramName) {

		String result = null;
		NameValuePair nameValuePair = get(paramName);
		if (nameValuePair != null) {
			result = nameValuePair.getValue();
		}
		return result;
	}

	/**
	 * Adds a parameter to the query string. Zero or more values can be provided
	 * and a parameter will be added for each.
	 * 
	 * @param name
	 *            The parameter name. Multiple parameters of the same name can
	 *            be added.
	 * @param value
	 *            Zero or more values. This determines the number of times the
	 *            parameter is added.
	 */
	public void add(String name, String... value) {
		for (String singleValue : value) {
			add(new BasicNameValuePair(name, singleValue));
		}
	}

	// /**
	// * Convenience method that allows you to add a {@link Transaction} class
	// as a parameter. The
	// * parameter will be added as the transaction code of the class.
	// *
	// * @param name
	// * The parameter name.
	// * @param transaction
	// * The {@link Transaction} class.
	// */
	// public void add(String name, Class<? extends Transaction> transaction) {
	// add(name, AnnotationUtils.getTxCode(transaction));
	// }

	/**
	 * Convenience method that allows you to add a {@link URI} as a parameter.
	 * 
	 * @param name
	 *            The parameter name.
	 * @param uri
	 *            The {@link URI} to be added as the parameter value.
	 */
	public void add(String name, URI uri) {
		add(name, uri.toString());
	}

	/**
	 * Removes all parameters of the given name.
	 * 
	 * @param name
	 *            The name to remove.
	 * @return A list of matching parameters.
	 */
	public List<NameValuePair> remove(String name) {

		// First find the parameters to be removed:
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		for (NameValuePair parameter : this) {
			if (parameter.getName().equalsIgnoreCase(name)) {
				result.add(parameter);
			}
		}

		// Now remove them. This avoids concurrent modification:
		for (NameValuePair parameter : result) {
			remove(parameter);
		}

		return result;
	}

	/**
	 * @return the query String using UTF-8. If the query string is empty, null
	 *         is returned, which is suitable for passing to the
	 *         <code>URIUtils.createURI(...)</code>
	 */
	public String toQueryString() {
		String result = URLEncodedUtils.format(this, "UTF8");
		if (!StringUtils.isEmpty(result)) {
			return result;
		}
		return null;
	}

	/**
	 * @return the query String using UTF-8. If the query string is null, an
	 *         empty String is returned, which is suitable for printing out, but
	 *         passing it to <code>URIUtils.createURI(...)</code> will render as
	 *         <code>http://path/stuff?</code> (a question mark at the end with
	 *         an empty query string).
	 */
	@Override
	public String toString() {
		String result = toQueryString();
		if (result == null) {
			result = StringUtils.EMPTY;
		}
		return result;
	}

}