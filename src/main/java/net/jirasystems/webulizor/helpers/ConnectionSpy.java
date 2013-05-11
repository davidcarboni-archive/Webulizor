package net.jirasystems.webulizor.helpers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

/**
 * {@link Connection} implementation that allows you to spy on SQL statements going back and forth.
 * 
 * @author David Carboni
 * 
 */
public class ConnectionSpy {

	/**
	 * Wraps the given {@link PreparedStatement} in a Java dynamic proxy which will log method
	 * calls.
	 * 
	 * @param connection
	 *            The {@link Connection} to be spied.
	 * @return The wrapped {@link PreparedStatement}
	 */
	public static Connection spy(final Connection connection) {

		InvocationHandler handler = new InvocationHandler() {

			private Connection c = connection;

			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

				// Get caller:
				Exception e = new Exception();
				e.fillInStackTrace();
				StackTraceElement[] stackTrace = e.getStackTrace();
				int i = 0;
				boolean found = false;
				while (i < stackTrace.length && !found) {
					StackTraceElement stackTraceElement = stackTrace[i];
					if (stackTraceElement.getClassName().startsWith("org.workdocx.services.")) {
						found = true;
					}
					i++;
				}

				// Log
				if (StringUtils.equals("close", method.getName())) {
					System.out.println(Connection.class.getSimpleName() + "." + method.getName() + "("
							+ Arrays.toString(args) + ")");
				}

				// Invoke the method
				Object result = method.invoke(c, args);

//				// If the result is a PreparedStatement, spy that too:
//				if (result != null && PreparedStatement.class.isAssignableFrom(result.getClass())) {
//					result = spy((PreparedStatement) result, logger);
//				}

				return result;
			}
		};
		Object proxy = Proxy.newProxyInstance(ConnectionSpy.class.getClassLoader(), new Class[] {Connection.class},
				handler);
		return (Connection) proxy;
	}

	/**
	 * Wraps the given {@link PreparedStatement} in a Java dynamic proxy which will log method
	 * calls.
	 * 
	 * @param preparedStatement
	 *            The {@link PreparedStatement} to be spied.
	 * @return The wrapped {@link PreparedStatement}
	 */
	private static PreparedStatement spy(final PreparedStatement preparedStatement) {

		InvocationHandler handler = new InvocationHandler() {

			private PreparedStatement ps = preparedStatement;

			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				System.out.println(PreparedStatement.class.getSimpleName() + "." + method.getName() + "("
						+ Arrays.toString(args) + ")");
				return method.invoke(ps, args);
			}
		};
		Object proxy = Proxy.newProxyInstance(ConnectionSpy.class.getClassLoader(),
				new Class[] {PreparedStatement.class}, handler);
		return (PreparedStatement) proxy;
	}
}
