package net.jirasystems.webulizor.helpers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import net.jirasystems.webulizor.annotations.Parameter;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper for processing HTTP parameters.
 * 
 * @author David Carboni
 * 
 */
public class Parameters {

	static final Logger log = LoggerFactory.getLogger(Parameters.class);

	/**
	 * Reads HTTP request parameters into the fields of the given object which are annotated with
	 * {@link Parameter}.
	 * <p>
	 * The following types are supported:
	 * <ul>
	 * <li>{@link String}</li>
	 * <li>{@link Integer} using {@link Integer#valueOf(String)}</li>
	 * <li>{@link Boolean} using {@link BooleanUtils#toBooleanObject(String)}</li>
	 * <li>{@link Enum} using {@link Enum#valueOf(Class, String)}</li>
	 * </ul>
	 * <p>
	 * Unparseable values are logged as warnings and the corresponding {@link Field} left unchanged.
	 * 
	 * @param parameters
	 *            The object to be populated from the HTTP request.
	 * @param request
	 *            The {@link HttpServletRequest}.
	 * @return If {@link Parameter#required()} is <code>true</code> for any parameter which is
	 *         either not present or cannot be parsed, false will be returned.
	 */
	public static boolean readParameters(Object parameters, HttpServletRequest request) {
		boolean result = true;

		if (parameters != null) {

			// Get the list of fields:
			List<Field> fields = getAllFields(parameters.getClass());

			for (Field field : fields) {

				// Get parameter information:
				String name = getName(field);
				boolean required = field.getAnnotation(Parameter.class).required();

				if (request.getParameterMap().containsKey(name)) {

					// The parameter value:
					String value = request.getParameter(name);

					// The field type:
					Class<?> type = field.getType();

					// Populate the field:
					field.setAccessible(true);
					try {
						if (String.class.isAssignableFrom(type)) {
							result = update(required, getString(field, parameters, value));
						} else if (Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type)) {
							result &= update(required, getInteger(field, parameters, value));
						} else if (Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type)) {
							result &= update(required, getLong(field, parameters, value));
						} else if (Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)) {
							result &= update(required, getBoolean(field, parameters, value));
						} else if (Enum.class.isAssignableFrom(type)) {
							result &= update(required, getEnum(field, parameters, value));
						} else {
							throw new IllegalArgumentException("Unable to handle type " + type.getName());
						}
					} catch (IllegalAccessException e) {
						log.warn("For some reason " + field.getName() + " cant' be accessed", e);
					} finally {
						field.setAccessible(false);
					}

				} else if (required) {

					// Required field not present in the request:
					result = false;
				}
			}
		}

		return result;
	}

	/**
	 * Checks whether it matters whether or not a field was set.
	 * 
	 * @param required
	 *            Whether the field is required.
	 * @param result
	 *            The result of setting the field.
	 * @return If the field is required and could not be set, false.
	 */
	private static boolean update(boolean required, boolean result) {
		boolean problem = required && !result;
		return !problem;
	}

	/**
	 * Processes the value as a {@link String}.
	 * 
	 * @param field
	 *            The {@link Field} to be set.
	 * @param parameters
	 *            The instance in which the {@link Field} will be set.
	 * @param value
	 *            The value to set in the {@link Field}.
	 * @return If the value is not empty, true.
	 * @throws IllegalArgumentException
	 *             If an error occurs in setting the {@link Field}
	 * @throws IllegalAccessException
	 *             If an error occurs in setting the {@link Field}
	 */
	private static boolean getString(Field field, Object parameters, String value) throws IllegalArgumentException,
			IllegalAccessException {
		boolean result;
		if (StringUtils.isNotEmpty(value)) {
			field.set(parameters, value);
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * Processes the value as an {@link Integer}.
	 * 
	 * @param field
	 *            The {@link Field} to be set.
	 * @param parameters
	 *            The instance in which the {@link Field} will be set.
	 * @param value
	 *            The value to set in the {@link Field}.
	 * @return If the value can be parsed as an {@link Integer}, true.
	 * @throws IllegalArgumentException
	 *             If an error occurs in setting the {@link Field}
	 * @throws IllegalAccessException
	 *             If an error occurs in setting the {@link Field}
	 */
	private static boolean getInteger(Field field, Object parameters, String value) throws IllegalArgumentException,
			IllegalAccessException {
		boolean result;
		try {
			field.set(parameters, Integer.valueOf(value));
			result = true;
		} catch (NumberFormatException e) {
			log.warn("Unable to parse value [" + value + "] of parameter " + getName(field) + " as an integer");
			result = false;
		}
		return result;
	}

	/**
	 * Processes the value as an {@link Integer}.
	 * 
	 * @param field
	 *            The {@link Field} to be set.
	 * @param parameters
	 *            The instance in which the {@link Field} will be set.
	 * @param value
	 *            The value to set in the {@link Field}.
	 * @return If the value can be parsed as an {@link Integer}, true.
	 * @throws IllegalArgumentException
	 *             If an error occurs in setting the {@link Field}
	 * @throws IllegalAccessException
	 *             If an error occurs in setting the {@link Field}
	 */
	private static boolean getLong(Field field, Object parameters, String value) throws IllegalArgumentException,
			IllegalAccessException {
		boolean result;
		try {
			field.set(parameters, Long.valueOf(value));
			result = true;
		} catch (NumberFormatException e) {
			log.warn("Unable to parse value [" + value + "] of parameter " + getName(field) + " as a long");
			result = false;
		}
		return result;
	}

	/**
	 * Processes the value as a {@link Boolean}.
	 * 
	 * @param field
	 *            The {@link Field} to be set.
	 * @param parameters
	 *            The instance in which the {@link Field} will be set.
	 * @param value
	 *            The value to set in the {@link Field}.
	 * @return If the value can be parsed as a {@link Boolean}, according to
	 *         {@link BooleanUtils#toBooleanObject(boolean)}, true.
	 * @throws IllegalArgumentException
	 *             If an error occurs in setting the {@link Field}
	 * @throws IllegalAccessException
	 *             If an error occurs in setting the {@link Field}
	 */
	private static boolean getBoolean(Field field, Object parameters, String value) throws IllegalArgumentException,
			IllegalAccessException {
		boolean result;
		Boolean booleanValue = BooleanUtils.toBooleanObject(value);
		if (booleanValue != null) {
			field.set(parameters, booleanValue);
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * Processes the value as a {@link Boolean}.
	 * 
	 * @param field
	 *            The {@link Field} to be set.
	 * @param parameters
	 *            The instance in which the {@link Field} will be set.
	 * @param value
	 *            The value to set in the {@link Field}.
	 * @return If the value can be parsed as an enum constant, according to
	 *         {@link Enum#valueOf(Class, String)}, true.
	 * @throws IllegalAccessException
	 *             If an error occurs in setting the {@link Field}
	 */
	private static boolean getEnum(Field field, Object parameters, String value) throws IllegalAccessException {
		boolean result;
		try {
			@SuppressWarnings({"unchecked", "rawtypes"})
			Enum<?> constant = Enum.valueOf((Class<Enum>) field.getType(), value);
			field.set(parameters, constant);
			result = true;
		} catch (IllegalArgumentException e) {
			log.warn("Unable to convert value [" + value + "] of parameter " + getName(field)
					+ " into an enum constant for " + field.getType());
			result = false;
		}
		return result;
	}

	/**
	 * Determines the parameter name for the given field.
	 * 
	 * @param field
	 *            The field to inspect. The field must have a {@link Parameter} annotation.
	 * @return The parameter name, which is either {@link Parameter#name()} or, if that is blank,
	 *         the field name.
	 */
	private static String getName(Field field) {
		String result;

		Parameter parameter = field.getAnnotation(Parameter.class);
		if (StringUtils.isNotBlank(parameter.name())) {
			result = parameter.name();
		} else {
			result = field.getName();
		}

		return result;
	}

	private static Map<Class<?>, List<Field>> typeCache = new ConcurrentHashMap<Class<?>, List<Field>>();

	/**
	 * This method returns a list of all declared fields that have an {@link Parameter} annotation,
	 * from the bean class and all super classes, excluding Object (as no annotations can have been
	 * added to Object).
	 * 
	 * The list of fields is cached, so subsequent calls to this method do not require reflection.
	 * This is reasonable, as a class is unlikely to change at runtime.
	 * 
	 * NB: the returned instance is the same instance which is stored in the cache. Should it be
	 * necessary to make alterations to the list, this can be done on the returned instance and all
	 * other references and subsequent calls to this method will see the changes.
	 * 
	 * @param beanClass
	 *            The bean class to be inspected.
	 * @return A list of all fields in the type hierarchy of the given bean that have a
	 *         {@link Parameter} annotation.
	 */
	private static List<Field> getAllFields(Class<?> beanClass) {

		// Attempt to return a cached value:
		List<Field> result = typeCache.get(beanClass);
		if (result != null) {
			return result;
		}

		// We don't have a cached value, so compute the list of fields.

		// Recursively collect declared fields, starting with the class of the bean:
		Class<?> hierarchyClass = beanClass;
		result = new ArrayList<Field>();
		do {
			// List all fields declared by this type
			Field[] fields = hierarchyClass.getDeclaredFields();

			// Add fields that have a Parameter annotation to the result
			for (Field field : fields) {
				Parameter parameter = field.getAnnotation(Parameter.class);
				if (parameter != null) {
					result.add(field);
				}
			}

			// Recurse up the class hierachy:
			hierarchyClass = hierarchyClass.getSuperclass();

		} while (!hierarchyClass.equals(Object.class));

		// Cache the result:
		typeCache.put(beanClass, Collections.unmodifiableList(result));

		return result;
	}

}
