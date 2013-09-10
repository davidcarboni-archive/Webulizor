package net.jirasystems.webulizor.framework;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.jirasystems.webulizor.helpers.Database;

public class ContextListner implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// ConnectionPool.initialise();
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		Database.shutdown();
	}
}
