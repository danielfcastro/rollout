package br.com.danielfcastro.rollout.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import br.com.danielfcastro.rollout.resources.ProjetoResource;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;

@ApplicationPath("/rollout/")
@SwaggerDefinition(
		info = @Info(
		title = "REST service for verification if a project, branches , employee are asigned for rollout plan", 
		version = "1.0", 
		description = "Service for rollout strategy check", 
		license = @License(name = "Apache 2.0", 
		url = "http://www.apache.org/licenses/LICENSE-2.0"), 
		contact = @Contact(name = "Daniel Castro", 
		email = "dfcastro@gmail.com")))
public class PilotoApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();

	public void RestEasyServices() {
		singletons.add(new ProjetoResource());
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
