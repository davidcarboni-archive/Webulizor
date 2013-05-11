package net.jirasystems.webulizor.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.jirasystems.webulizor.annotations.Parameter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author David Carboni
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class ParametersTest {

	@Mock
	private HttpServletRequest request;

	private Map<String, String[]> parameterMap;

	private static enum EnumType {
		A, B
	}

	/**
	 * Instantiates the class under test.
	 */
	@Before
	public void setUp() {
		parameterMap = new HashMap<String, String[]>();
		when(request.getParameterMap()).thenReturn(parameterMap);
	}

	/**
	 * Verifies that an annotated String field is populated.
	 */
	@Test
	public void shouldPopulateString() {

		// Given

		// A parameter
		final String parameterName = "parameter";
		String parameterValue = "value";
		addParameter(parameterName, parameterValue);

		// A class defining a String parameter
		class StringParameter {
			@Parameter(name = parameterName)
			String parameter;
		}
		StringParameter stringParameter = new StringParameter();

		// When 
		// We try to read parameters
		boolean result = Parameters.readParameters(stringParameter, request);

		// Then
		// The parameter should be populated
		assertTrue(result);
		assertEquals(parameterValue, stringParameter.parameter);
	}

	/**
	 * Verifies that an annotated int field is populated.
	 */
	@Test
	public void shouldPopulateInt() {

		// Given

		// A parameter
		final String parameterName = "int";
		final int value = 10;
		String parameterValue = Integer.toString(value);
		addParameter(parameterName, parameterValue);

		// A class defining a String parameter
		class IntParameter {
			@Parameter(name = parameterName)
			int parameter;
		}
		IntParameter intParameter = new IntParameter();

		// When 
		// We try to read parameters
		boolean result = Parameters.readParameters(intParameter, request);

		// Then
		// The parameter should be populated
		assertTrue(result);
		assertEquals(value, intParameter.parameter);
	}

	/**
	 * Verifies that an annotated Integer field is populated.
	 */
	@Test
	public void shouldPopulateInteger() {

		// Given

		// A parameter
		final String parameterName = "integer";
		final Integer value = Integer.valueOf(10);
		String parameterValue = value.toString();
		addParameter(parameterName, parameterValue);

		// A class defining a String parameter
		class IntegerParameter {
			@Parameter(name = parameterName)
			Integer parameter;
		}
		IntegerParameter integerParameter = new IntegerParameter();

		// When 
		// We try to read parameters
		boolean result = Parameters.readParameters(integerParameter, request);

		// Then
		// The parameter should be populated
		assertTrue(result);
		assertEquals(value, integerParameter.parameter);
	}

	/**
	 * Verifies that an annotated boolean field is populated.
	 */
	@Test
	public void shouldPopulateBool() {

		// Given

		// A parameter
		final String parameterName = "boolean";
		final boolean value = true;
		String parameterValue = Boolean.toString(value);
		addParameter(parameterName, parameterValue);

		// A class defining a String parameter
		class BoolParameter {
			@Parameter(name = parameterName)
			boolean parameter;
		}
		BoolParameter boolParameter = new BoolParameter();

		// When 
		// We try to read parameters
		boolean result = Parameters.readParameters(boolParameter, request);

		// Then
		// The parameter should be populated
		assertTrue(result);
		assertTrue(boolParameter.parameter);
	}

	/**
	 * Verifies that an annotated Boolean field is populated.
	 */
	@Test
	public void shouldPopulateBoolean() {

		// Given

		// A parameter
		final String parameterName = "Boolean";
		final Boolean value = Boolean.TRUE;
		String parameterValue = value.toString();
		addParameter(parameterName, parameterValue);

		// A class defining a String parameter
		class BooleanParameter {
			@Parameter(name = parameterName)
			Boolean parameter;
		}
		BooleanParameter booleanParameter = new BooleanParameter();

		// When 
		// We try to read parameters
		boolean result = Parameters.readParameters(booleanParameter, request);

		// Then
		// The parameter should be populated
		assertTrue(result);
		assertEquals(value, booleanParameter.parameter);
	}

	/**
	 * Verifies that an annotated Enum field is populated.
	 */
	@Test
	public void shouldPopulateEnum() {

		// Given

		// A parameter
		final String parameterName = "enum";
		final EnumType value = EnumType.B;
		String parameterValue = value.toString();
		addParameter(parameterName, parameterValue);

		// A class defining a String parameter
		class EnumParameter {
			@Parameter(name = parameterName)
			EnumType parameter;
		}
		EnumParameter enumParameter = new EnumParameter();

		// When 
		// We try to read parameters
		boolean result = Parameters.readParameters(enumParameter, request);

		// Then
		// The parameter should be populated
		assertTrue(result);
		assertEquals(value, enumParameter.parameter);
	}

	/**
	 * Verifies that if a null object is passed, null is gracefully returned.
	 */
	@Test
	public void shouldReturnNullForNull() {

		// Given
		// A null parameter object
		Object parameters = null;

		// When 
		// We try to read parameters
		Parameters.readParameters(parameters, request);

		// Then
		// We shouldn't get a null pointer exception.
	}

	/**
	 * Verifies that an annotated String field is populated.
	 */
	@Test
	public void shouldNotPopulateUnAnnotatedFields() {

		// Given

		// A parameter
		addParameter("stringParameter", "string");
		addParameter("intParameter", Integer.toString(1));
		addParameter("integerParameter", Integer.toString(1));
		addParameter("boolParameter", Boolean.TRUE.toString());
		addParameter("booleanParameter", Boolean.TRUE.toString());
		addParameter("enumParameter", EnumType.A.toString());

		// A class defining unannotated fields
		class UnannotatedFields {
			String stringParameter;
			int intParameter;
			Integer integerParameter;
			boolean boolParameter;
			Boolean booleanParameter;
			EnumType enumParameter;
		}
		UnannotatedFields unannotatedFields = new UnannotatedFields();

		// When 
		// We try to read parameters
		boolean result = Parameters.readParameters(unannotatedFields, request);

		// Then
		// Operation succeeds and no fields are populated, even though they match parameter names:
		assertTrue(result);
		assertNull(unannotatedFields.stringParameter);
		assertEquals(0, unannotatedFields.intParameter);
		assertNull(unannotatedFields.integerParameter);
		assertFalse(unannotatedFields.boolParameter);
		assertNull(unannotatedFields.booleanParameter);
		assertNull(unannotatedFields.enumParameter);
	}

	/**
	 * Verifies that invalid parameters are not populated and that no exception is thrown.
	 */
	@Test
	public void shouldNotPopulateInvalidParameters() {

		// Given

		// A parameter
		addParameter("stringParameter", "");
		addParameter("intParameter", "NaN");
		addParameter("integerParameter", "NaNNeither");
		addParameter("boolParameter", "maybe");
		addParameter("booleanParameter", "guess");
		addParameter("enumParameter", "C");

		// A class defining unannotated fields
		class InvalidFields {
			@Parameter
			String stringParameter;
			@Parameter
			int intParameter;
			@Parameter
			Integer integerParameter;
			@Parameter
			boolean boolParameter;
			@Parameter
			Boolean booleanParameter;
			@Parameter
			EnumType enumParameter;
		}
		InvalidFields invalidFields = new InvalidFields();

		// When 
		// We try to read parameters
		boolean result = Parameters.readParameters(invalidFields, request);

		// Then
		// Operation succeeds but no fields are populated:
		assertTrue(result);
		assertNull(invalidFields.stringParameter);
		assertEquals(0, invalidFields.intParameter);
		assertNull(invalidFields.integerParameter);
		assertFalse(invalidFields.boolParameter);
		assertNull(invalidFields.booleanParameter);
		assertNull(invalidFields.enumParameter);
	}

	/**
	 * Verifies that invalid parameters are not populated and that no exception is thrown.
	 */
	@Test
	public void shouldFailForRequiredButInvalidParameters() {

		// Given

		// A parameter
		addParameter("intParameter", "NaN");
		addParameter("enumParameter", "D");

		// A class defining unannotated fields
		class InvalidFields {
			@Parameter(required = true)
			int intParameter;
			@Parameter(required = true)
			EnumType enumParameter;
		}
		InvalidFields invalidFields = new InvalidFields();

		// When 
		// We try to read parameters
		boolean result = Parameters.readParameters(invalidFields, request);

		// Then
		// Operation fails because the fields are required:
		assertFalse(result);
	}

	/**
	 * Verifies that an annotated field can reference a parameter with a name different from the
	 * field name.
	 */
	@Test
	public void shouldPopulateNamedParameter() {

		// Given

		// A parameter
		final String parameterName = "hulahoop";
		String parameterValue = "snowtime";
		addParameter(parameterName, parameterValue);

		// A class defining a named field
		class NamedField {
			@Parameter(name = parameterName)
			String stringParameter;
		}
		NamedField namedField = new NamedField();

		// When 
		// We try to read parameters
		boolean result = Parameters.readParameters(namedField, request);

		// Then
		// Operation succeeds and no fields are populated, even though they match parameter names:
		assertTrue(result);
		assertEquals(parameterValue, namedField.stringParameter);
	}

	/**
	 * Verifies that an annotated field that is marked as required must be present in the request.
	 */
	@Test
	public void shouldFailIfRequiredParameterMissing() {

		// Given

		// No parameters in the request

		// A class defining a required field
		class RequiredField {
			@Parameter(required = true)
			String stringParameter;
		}
		RequiredField requiredField = new RequiredField();

		// When 
		// We try to read parameters
		boolean result = Parameters.readParameters(requiredField, request);

		// Then
		// Operation succeeds and no fields are populated, even though they match parameter names:
		assertFalse(result);
		assertNull(requiredField.stringParameter);
	}

	/**
	 * Verifies that an annotated String field is populated.
	 */
	@Test
	public void shouldPopulateSuperclass() {

		// Given

		// A parameter
		final String parameterName = "parameter";
		String parameterValue = "value";
		addParameter(parameterName, parameterValue);

		// A class defining a String parameter
		class Parent {
			@Parameter(name = parameterName)
			String parentParameter;
		}
		class Child extends Parent {
			@Parameter
			int childParameter;
		}
		Child childInstance = new Child();

		// When 
		// We try to read parameters
		boolean result = Parameters.readParameters(childInstance, request);

		// Then
		// The parameter should be populated
		assertTrue(result);
		assertEquals(parameterValue, childInstance.parentParameter);
	}

	/**
	 * Sets up an HTTP parameter to be accessed via {@link HttpServletRequest#getParameterMap()},
	 * {@link HttpServletRequest#getParameter(String)} or
	 * {@link HttpServletRequest#getParameterValues(String)}.
	 * 
	 * @param parameterName
	 *            Parameter name.
	 * @param parameterValue
	 *            Parameter value.
	 */
	private void addParameter(String parameterName, String parameterValue) {
		String[] values = parameterMap.get(parameterName);
		if (values == null) {
			values = new String[0];
		}
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(values));
		list.add(parameterValue);
		String[] newValues = list.toArray(values);
		parameterMap.put(parameterName, newValues);
		when(request.getParameter(parameterName)).thenReturn(newValues[0]);
		when(request.getParameterValues(parameterName)).thenReturn(newValues);
	}

}
