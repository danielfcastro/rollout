package br.com.danielfcastro.rollout.config;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import io.swagger.jaxrs.config.BeanConfig;

@WebListener
public class SwaggerConfig implements ServletContextListener {
	@Inject
	private ApplicationParameterService parameterService;
	
	private static final String NOME_APP = "global";
	
	private static final String BASE_ADDRESS = "base.address";
	private static final String BASE_PORT = "base.port";
	
	public void contextInitialized(ServletContextEvent event) {

		StringBuilder basePath = new StringBuilder().append(event.getServletContext().getContextPath()).append("/");

		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("1.0.0");
		beanConfig.setSchemes(new String[] { "http" });
		beanConfig.setHost(getHost());
		beanConfig.setBasePath(basePath.toString());
		beanConfig.setResourcePackage("br.com.danielfcastro.pilot.resources");
		beanConfig.setScan(true);
	}

	protected String getHost() {
		StringBuilder host = new StringBuilder();
		host.append(parameterService.getProperty(BASE_ADDRESS, NOME_APP)).append(":").append(parameterService.getProperty(BASE_PORT, NOME_APP));
		return host.toString();
	}

	public void contextDestroyed(ServletContextEvent event) {
	}

}
