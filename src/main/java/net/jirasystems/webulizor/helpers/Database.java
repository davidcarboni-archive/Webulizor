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

	private static String url;

	/**
	 * Initialises the JDBC driver.
	 * 
	 * @param servletContext
	 *            Used to set the path for the HSQLDB database files.
	 */
	public static void initialise(ServletContext servletContext) {

		// Load the driver:
		String driverClass = "org.hsqldb.jdbcDriver";
		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unable to locate driver class " + driverClass, e);
		}

		setPath(servletContext.getRealPath("WEB-INF/database"));

	}

	/**
	 * @param path
	 *            The filesystem path for the database.
	 */
	public static void setPath(String path) {
		File file = new File(path, "database");
		String fullPath = FilenameUtils.separatorsToUnix(file.getPath());

		// JDBC URL:
		// Default to always shutting down the database and no write delay.
		// We don't need high performance and preference is for committing data
		// to disk:
		url = "jdbc:hsqldb:" + fullPath + ";shutdown=true;hsqldb.write_delay=false";
	}

	/**
	 * Gets a new connection. The caller is responsible for closing the connection.
	 * 
	 * @return A new connection to the database.
	 */
	public static Connection getConnection() {

		Connection connection;
		try {
			connection = DriverManager.getConnection(url, "sa", "");
		} catch (SQLException e) {

			// We're sometimes attempting to get a connection to 
			// HSQLDB when it's shutting down a connection, so retry:
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// Ignore;
			}
			try {
				connection = DriverManager.getConnection(url, "sa", "");
			} catch (SQLException e2) {
				throw new RuntimeException("Unable to get a connection to " + url, e2);
			}
		}

		return connection;
//		return ConnectionSpy.spy(connection);
	}

}
