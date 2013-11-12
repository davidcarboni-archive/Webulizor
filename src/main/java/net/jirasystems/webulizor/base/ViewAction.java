package net.jirasystems.webulizor.base;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.jirasystems.webulizor.framework.AppException;
import net.jirasystems.webulizor.helpers.Link;
import net.jirasystems.webulizor.helpers.Velocity;
import net.jirasystems.webulizor.interfaces.Action;

/**
 * An action which renders a Velocity view.
 * 
 * @author David Carboni
 * 
 */
public class ViewAction extends AbstractAction {

	private String templatePath;
	private Map<String, Object> data;
	private Link link;

	/**
	 * Convenience constructor to set the template path.
	 * 
	 * @param templatePath
	 *            The Velocity template for this view.
	 */
	public ViewAction(String templatePath) {
		this.templatePath = templatePath;
	}

	/**
	 * Sets a default template path, which is the fully qualified name of the
	 * class with a .html suffix.
	 * <p>
	 * E.g.: /net/jirasystems/webulizor/base/ViewAction.html
	 */
	public ViewAction() {
		// Default template path.
		this.templatePath = getDefaultTemplatePath(this);
	}

	public static String getDefaultTemplatePath(Class<?> controllerClass) {
		// Default template path for a given class:
		return "/" + controllerClass.getName().replace('.', '/') + ".html";
	}

	public static String getDefaultTemplatePath(Object controller) {
		return getDefaultTemplatePath(controller.getClass());
	}

	@Override
	public Action perform() throws IOException {

		if (templatePath != null) {
			getResponse().setContentType("text/html; charset=UTF-8");
			put("link", getLink());
			Velocity.renderHtml(templatePath, getData(), this);
			return null;
		}

		throw new AppException("Unable to render view. "
				+ "Template path has not been set in "
				+ this.getClass().getName() + ".");
	}

	/**
	 * Adds data to the Velocity context.
	 * 
	 * @param name
	 *            The key name.
	 * @param value
	 *            The value to be added to the context.
	 */
	public void put(String name, Object value) {
		getData().put(name, value);
	}

	Map<String, Object> getData() {
		if (data == null) {
			data = new HashMap<String, Object>();
		}
		return data;
	}

	/**
	 * @return the templatePath
	 */
	public String getTemplatePath() {
		return templatePath;
	}

	/**
	 * @param templatePath
	 *            the templatePath to set
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	/**
	 * @return the link
	 */
	public Link getLink() {
		if (link == null) {
			link = new Link(this.getServletContext(), this.getRequest());
		}
		return link;
	}

}
