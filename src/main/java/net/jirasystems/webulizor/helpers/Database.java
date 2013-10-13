package net.jirasystems.webulizor.helpers;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

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
	private static String username;
	private static String password;
	private static BoneCP connectionPool;

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
	public static void initialiseHsql() {

		initialiseDriver(HSQL_DRIVER);
		url = "jdbc:hsqldb:mem:aname";
		username = "sa";
		password = "";
		initialiseConnectionPool();
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
	public static void initialiseHsql(ServletContext servletContext) {

		initialiseDriver(HSQL_DRIVER);
		setHsqldbPath(servletContext.getRealPath("WEB-INF/database"));
		username = "sa";
		password = "";
		initialiseConnectionPool();
	}

	/**
	 * Initialises HSQLDB, storing the database under the given path.
	 * <p>
	 * This allows you to store the database at a configured path.
	 * 
	 * @param servletContext
	 *            Used to set the path for the HSQLDB database files.
	 */
	public static void initialiseHsql(String path) {

		initialiseDriver(HSQL_DRIVER);
		setHsqldbPath(path);
		username = "sa";
		password = "";
		initialiseConnectionPool();
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
		initialiseConnectionPool();
	}

	/**
	 * Initialises HSQLDB, storing the database under the given path.
	 * 
	 * @param path
	 *            The filesystem path for HSQLDB.
	 */
	private static void initialiseDriver(String driverClass) {

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

	private static void initialiseConnectionPool() {

		// Configure the connection pool:
		BoneCPConfig boneCPConfig = new BoneCPConfig();
		boneCPConfig.setJdbcUrl(url);
		boneCPConfig.setUsername(username);
		boneCPConfig.setPassword(password);
		boneCPConfig.setMinConnectionsPerPartition(5);
		boneCPConfig.setMaxConnectionsPerPartition(1);
		boneCPConfig.setPartitionCount(1);

		// Instantiate the connection pool:
		try {
			connectionPool = new BoneCP(boneCPConfig);
		} catch (SQLException e) {
			throw new RuntimeException("Error initialising connection pool", e);
		}

		System.out.println("Database is at " + url);
	}

	/**
	 * Shuts down the connection pool.
	 */
	public static void shutdown() {
		if (connectionPool != null) {
			System.out.println("Shutting down connection pool..");
			connectionPool.shutdown();
			System.out.println("Connection pool shut down.");
		}
	}

	/**
	 * Gets a new connection. The caller is responsible for closing the
	 * connection.
	 * 
	 * @return A new connection to the database.
	 */
	public static Connection getConnection() {
		try {
//			System.out.println("Connection pool has "
//					+ connectionPool.getTotalLeased()
//					+ " connections in use and "
//					+ connectionPool.getTotalFree() + " free connections.");
			Connection connection = connectionPool.getConnection();
			connection.setAutoCommit(false);
//			return ConnectionSpy.spy(connection);
			return connection;
		} catch (SQLException e) {
			throw new RuntimeException("Error getting a database connection", e);
		}
	}

	/**
	 * @return the url
	 */
	public static String getUrl() {
		return url;
	}

	/**
	 * @return the username
	 */
	public static String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public static String getPassword() {
		return password;
	}

}
