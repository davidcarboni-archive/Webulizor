package net.jirasystems.webulizor.helpers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import net.jirasystems.resourceutil.ResourceUtil;
import net.jirasystems.webulizor.interfaces.Action;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

/**
 * Renders Velocity templates.
 * 
 * @author David Carboni
 * 
 */
public class Velocity {

	private static String velocityLog;
	private static VelocityEngine velocityHtml;
	private static VelocityEngine velocityText;

	/**
	 * Initialises the velocity log.
	 * 
	 * @param servletContext
	 *            Used to get a path to /WEB-INF/velocity.log
	 */
	public static void initialise(String logPath) {
		if (StringUtils.isNotBlank(logPath)) {
			velocityLog = new File(logPath, "velocity.log").getPath();
		}
	}

	/**
	 * Renders a Velocity template to produce text.
	 * 
	 * @param templatePath
	 *            The template path.
	 * @param data
	 *            The context data.
	 * @return The rendered template.
	 * @throws VelocityException
	 *             If an error occurs.
	 */
	public static String renderText(String templatePath,
			Map<String, Object> data) throws VelocityException {

		VelocityEngine velocityEngine = getVelocityText();
		return render(velocityEngine, templatePath, data);
	}

	/**
	 * Renders a Velocity template to produce HTML.
	 * 
	 * @param templatePath
	 *            The template path.
	 * @param data
	 *            The context data. This will be escaped
	 * @return The rendered template.
	 * @throws VelocityException
	 *             If an error occurs.
	 */
	public static String renderHtml(String templatePath,
			Map<String, Object> data) throws VelocityException {

		VelocityEngine velocityEngine = getVelocityHtml();
		return render(velocityEngine, templatePath, data);
	}

	/**
	 * Renders a Velocity template to produce HTML.
	 * 
	 * @param templatePath
	 *            The template path.
	 * @param data
	 *            The context data. This will be escaped
	 * @param next
	 *            Provides the {@link HttpServletResponse}.
	 * @throws VelocityException
	 *             If an error occurs.
	 * @throws IOException
	 *             If an error occurs.
	 */
	public static void renderHtml(String templatePath,
			Map<String, Object> data, Action next) throws VelocityException,
			IOException {

		VelocityEngine velocityEngine = getVelocityHtml();
		String markup = render(velocityEngine, templatePath, data);

		next.getResponse().setContentType("text/html; charset=UTF-8");
		next.getResponse().getWriter().append(markup);
	}

	/**
	 * Convenience method to render a template in a single call.
	 * 
	 * @param templatePath
	 *            The path to the template.
	 * @param data
	 *            Data for the Velocity context.
	 * @return The rendered markup.
	 * @throws VelocityException
	 *             If an error occurs.
	 */
	private static String render(VelocityEngine velocityEngine,
			String templatePath, Map<String, Object> data)
			throws VelocityException {

		// Get the template:
		Template template = velocityEngine.getTemplate(templatePath);
		if (template == null) {
			throw new VelocityException("Unable to locate template "
					+ templatePath);
		}

		// Render:
		return render(template, data);
	}

	/**
	 * This method does the work of processing the VelociMacro.
	 * 
	 * @param template
	 *            The {@link Template} to be rendered.
	 * @param data
	 *            Data to be added to the Velocity context. Can be null.
	 * @return The rendered template.
	 * @throws VelocityException
	 *             If an exception is thrown during template processing.
	 */
	private static String render(Template template, Map<String, Object> data)
			throws VelocityException {

		try {
			// Create a context.
			// NB Whilst the Velocity engine is thread-safe, Contexts are not,
			// so we need to generate a new instance locally for this thread.
			VelocityContext context = new VelocityContext();

			// Add the data to the context:
			if (data != null) {
				for (Entry<String, Object> entry : data.entrySet()) {
					context.put(entry.getKey(), entry.getValue());
				}
			}

			// Now render the template
			StringWriter writer = new StringWriter();
			template.merge(context, writer);

			// Return the result
			return writer.toString().trim();

		} catch (Exception e) {
			throw new VelocityException("Error processing template \""
					+ template.getName() + "\".", e);
		}
	}

	/**
	 * @return A {@link VelocityEngine} which escapes HTML.
	 * @throws VelocityException
	 *             If an error occurs.
	 */
	private static VelocityEngine getVelocityText() throws VelocityException {

		if (velocityText == null) {

			velocityText = newVelocityEngine(false);
		}

		return velocityText;
	}

	/**
	 * @return A {@link VelocityEngine} which doesn't escape HTML.
	 * @throws VelocityException
	 *             If an error occurs.
	 */
	private static VelocityEngine getVelocityHtml() throws VelocityException {

		if (velocityHtml == null) {

			velocityHtml = newVelocityEngine(true);
		}

		return velocityHtml;
	}

	/**
	 * Instantiates a {@link VelocityEngine}.
	 * 
	 * @param escapeHtml
	 *            Whether context varables should be escaped.
	 * @return The initialised {@link VelocityEngine}
	 * @throws VelocityException
	 *             If an error occurs.
	 */
	private static VelocityEngine newVelocityEngine(boolean escapeHtml)
			throws VelocityException {
		VelocityEngine result;

		InputStream inStream = null;
		try {
			Properties defaults = new Properties();
			if (StringUtils.isNotBlank(velocityLog)) {
				defaults.put("runtime.log", velocityLog);
			}

			if (escapeHtml) {
				// Ensure that all values printed to the output are
				// correctly
				// HTML escaped:
				defaults.put("eventhandler.referenceinsertion.class",
						"org.apache.velocity.app.event.implement.EscapeHtmlReference");
			}

			// Override defaults as necessary with the values from
			// velocity.properties:
			Properties properties;
			try {
				properties = ResourceUtil.getProperties("/velocity.properties",
						defaults);
			} catch (IOException e) {
				// Just use the defaults:
				properties = defaults;
			}

			// Initialise the engine:
			VelocityEngine velocityEngine = new VelocityEngine();
			velocityEngine.init(properties);

			// Set the static field only after initialisation, in
			// order to avoid threading issues during initialisation:
			result = velocityEngine;

		} catch (Exception e) {
			throw new VelocityException(
					"Error initialising the Velocity templating engine.", e);
		} finally {
			IOUtils.closeQuietly(inStream);
		}

		return result;
	}
}
