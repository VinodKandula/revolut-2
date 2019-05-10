package com.revolute.controller.util;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.guice.ext.RequestScopeModule;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.junit.After;
import org.junit.Before;

import com.google.inject.Binder;
import com.google.inject.Guice;

public abstract class JettyGuiceRestEasyTest {

	protected abstract void configure(Binder b);

	private Server server;

	@Before
	public void setUp() throws Exception {

		server = new Server(8080);
		ServletContextHandler servletHandler = new ServletContextHandler();
		servletHandler.addEventListener(Guice.createInjector(new TestModule())
				.getInstance((GuiceResteasyBootstrapServletContextListener.class)));
		servletHandler.addServlet(HttpServletDispatcher.class, "/*");

		server.setHandler(servletHandler);
		server.start();
	}

	@After
	public void afterExecute() throws Exception {
		server.stop();
	}

	protected void configureRequestContext() {
		return;
	}

	private class TestModule extends RequestScopeModule {

		@Override
		protected void configure() {
			super.configure();
			JettyGuiceRestEasyTest.this.configure(this.binder());
		}
	}

}
