package net.jirasystems.webulizor.helpers;

import net.jirasystems.webulizor.annotations.HomeAction;
import net.jirasystems.webulizor.annotations.Route;
import net.jirasystems.webulizor.framework.AppException;
import net.jirasystems.webulizor.interfaces.Action;

import org.apache.commons.lang.StringUtils;

/**
 * Resolves the path to an {@link Action}.
 * 
 * @author David Carboni
 * 
 */
public class Path {

	/**
	 * Resolves the path the the given {@link Action} using an instance.
	 * 
	 * @param action
	 *            An {@link Action} instance.
	 * @return The path.
	 */
	public static String getPath(Action action) {
		return getPath(action.getClass());
	}

	/**
	 * Resolves the path the the given {@link Action} using the Class.
	 * 
	 * @param actionClass
	 *            The {@link Action} class.
	 * @return The path.
	 */
	public static String getPath(Class<? extends Action> actionClass) {
		String path = null;

		// Get the path:
		if (actionClass.getAnnotation(HomeAction.class) != null) {
			path = "/";
		} else {
			Route route = actionClass.getAnnotation(Route.class);
			try {
				if (StringUtils.isNotBlank(route.path())) {
					path = route.path();
				} else {
					path = actionClass.getSimpleName().toLowerCase();
				}
			} catch (NullPointerException e) {
				throw new AppException("No " + Route.class.getSimpleName() + " annotation found on "
						+ actionClass.getName(), e);
			}
		}

		// Ensure we have a leading slash:
		if (!path.startsWith("/")) {
			path = "/" + path;
		}

		return path;
	}
}