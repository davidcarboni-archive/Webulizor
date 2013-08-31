package net.jirasystems.webulizor.helpers;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;

/**
 * Provides access to the
 * 
 * @author David Carboni
 * 
 */
public class Database {

	/** The default HSQL driver class. */
	public static final String HSQL_DRIVER = "org.hsqldb.jdbcDriver";

	private static String url;
	private static String username = "sa";
	private static String password = "";

	/**
	 * Initialises HSQLDB, using an in-memory database.
	 * <p>
	 * This is a useful hack for persisting the database, but bear in mind it's
	 * not servlet compliant. If your app gets run as an un-unpacked war file,
	 * this won't work.
	 * 
	 * @param servletContext
	 *            Used to set the path for the HSQLDB database files.
	 */
	public static void initialise() {

		initialiseDriver(HSQL_DRIVER);
		url = "jdbc:hsqldb:mem:aname";
		username = "sa";
		password = "";
	}

	/**
	 * Initialises HSQLDB, storing the database under WEB-INF/database.
	 * <p>
	 * This is a useful hack for persisting the database, but bear in mind it's
	 * not servlet compliant. If your app gets run as an un-unpacked war file,
	 * this won't work.
	 * 
	 * @param servletContext
	 *            Used to set the path for the HSQLDB database files.
	 */
	public static void initialise(ServletContext servletContext) {

		initialiseDriver(HSQL_DRIVER);
		setHsqldbPath(servletContext.getRealPath("WEB-INF/database"));
		username = "sa";
		password = "";
	}

	/**
	 * Initialises HSQLDB, storing the database under the given path.
	 * <p>
	 * This allows you to store the database at a configured path.
	 * 
	 * @param servletContext
	 *            Used to set the path for the HSQLDB database files.
	 */
	public static void initialise(String path) {

		initialiseDriver(HSQL_DRIVER);
		setHsqldbPath(path);
		username = "sa";
		password = "";
	}

	/**
	 * Initialises JDBC with the given values. The given driver class will be
	 * loaded as part of this call and you'll get an
	 * {@link IllegalArgumentException} if it's missing from your classpath.
	 * <p>
	 * This allows you to use any JDBC connection, not just default HSQL.
	 * 
	 * @param servletContext
	 *            Used to set the path for the HSQLDB database files.
	 */
	public static void initialise(String driverClass, String url,
			String username, String password) {

		initialiseDriver(driverClass);
		Database.url = url;
		Database.username = username;
		Database.password = password;
	}

	/**
	 * Initialises HSQLDB, storing the database under the given path.
	 * 
	 * @param path
	 *            The filesystem path for HSQLDB.
	 */
	public static void initialiseDriver(String driverClass) {

		// Load the driver:
		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Unable to locate driver class "
					+ driverClass, e);
		}
	}

	/**
	 * @param path
	 *            The filesystem path for HSQLDB.
	 */
	private static void setHsqldbPath(String path) {
		File file = new File(path, "database");
		String fullPath = FilenameUtils.separatorsToUnix(file.getPath());

		// JDBC URL:
		// Default to always shutting down the database and no write delay.
		// We don't need high performance and preference is for committing data
		// to disk:
		url = "jdbc:hsqldb:" + fullPath
				+ ";shutdown=true;hsqldb.write_delay=false";
	}

	/**
	 * Gets a new connection. The caller is responsible for closing the
	 * connection.
	 * 
	 * @return A new connection to the database.
	 */
	public static Connection getConnection() {

		Connection connection;
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {

			// We're sometimes attempting to get a connection to
			// HSQLDB when it's shutting down a connection, so retry:
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// Ignore;
			}
			try {
				connection = DriverManager.getConnection(url, username,
						password);
			} catch (SQLException e2) {
				throw new RuntimeException("Unable to get a connection to "
						+ url, e2);
			}
		}

		return connection;
		// return ConnectionSpy.spy(connection);
	}

	/**
	 * @return the url
	 */
	public static String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public static void setUrl(String url) {
		Database.url = url;
	}

	/**
	 * @return the username
	 */
	public static String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public static void setUsername(String username) {
		Database.username = username;
	}

	/**
	 * @return the password
	 */
	public static String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public static void setPassword(String password) {
		Database.password = password;
	}

}
