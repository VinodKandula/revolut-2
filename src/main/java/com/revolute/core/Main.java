package com.revolute.core;

import javax.inject.Singleton;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.guice.ext.RequestScopeModule;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.revolute.controller.RevolutBankResource;
import com.revolute.service.RevolutBankService;
import com.revolute.service.RevolutBankServiceImpl;

public class Main {

	public static void main(String[] args) throws Exception {
		Injector injector = Guice.createInjector(new RevolutServiceModule());

		injector.getAllBindings();

		injector.createChildInjector().getAllBindings();

		Server server = new Server(8080);
		ServletContextHandler servletHandler = new ServletContextHandler();
		servletHandler.addEventListener(injector.getInstance(GuiceResteasyBootstrapServletContextListener.class));

		ServletHolder sh = new ServletHolder(HttpServletDispatcher.class);

		servletHandler.addServlet(sh, "/*");

		server.setHandler(servletHandler);
		server.start();
		server.join();
	}

	private static class RevolutServiceModule extends RequestScopeModule {

		@Provides
		@Singleton
		public RevolutBankService getRevolutBankService() {
			return new RevolutBankServiceImpl();
		}

		@Override
		protected void configure() {
			super.configure();
			bind(RevolutBankResource.class);
		}

	}
}
