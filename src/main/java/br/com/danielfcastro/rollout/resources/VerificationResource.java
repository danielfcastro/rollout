package br.com.danielfcastro.rollout.resources;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.providers.jackson.Formatted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.danielfcastro.rollout.repository.ParticipanteRepositoryImpl;
import br.com.danielfcastro.rollout.repository.ProjetoRepositoryImpl;
import br.com.danielfcastro.rollout.repository.StatusRepositoryImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/verify/v1")
@Api(value = "Participant")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VerificationResource {
	private static final Logger logger = LoggerFactory.getLogger(VerificationResource.class);
	private static final String CONTENT_TYPE = "Content-Type";

	@Inject
	ParticipanteRepositoryImpl repository;

	@Inject
	ProjetoRepositoryImpl projetoRepository;

	@Inject
	StatusRepositoryImpl statusRepository;
	
	@GET
	@Path("/autorizacao/{project}/{branch}/{employee}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Check whether or not the submitted employee is associated with a rollout strategy."
			+ "If only the branch exists in a rollout strategy, all employees associated with that branch will be in a rollout strategy."
			+ "If the branch and an employee exist in a a rollout strategy then the pair (branch + employee) will be verified.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "belongs to a rollout strategy"),
			@ApiResponse(code = 204, message = "does not belongs to a rollout strategy") })
	@Formatted
	public Response getParticipacaoParticipanteProjetoBranchEmployee(
			@ApiParam(value = "id of project in rollout system", required = true) @PathParam("project") String project,
			@ApiParam(value = "branch code", required = true) @PathParam("branch") String branch,
			@ApiParam(value = "name of employee registered in rollout system", required = true) @PathParam("employee") String employee
			) {
		logger.info("Begin");
		Response response = null;
		if (project == null || project.isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("project can not be null!").build();
		}
		if (branch == null || branch.isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("branch can not be null!").build();
		}
		if (employee == null || employee.isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("employee can not be null!").build();
		}
		Long entity = repository.queryCheckByProjetoBranchEmployee(project, branch, employee);
		if (entity == null || 0 == entity.shortValue()) {
			response = Response.noContent().build();
		} else {
			response = Response.status(Response.Status.OK).build();
		}
		logger.info("End");
		return response;
	}	

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	public Response options() {
		logger.info("Begin");
		Response response = Response.status(200).header("Allow", "POST, PUT, GET, DELETE, OPTIONS, HEAD")
				.header("Content-Type", MediaType.APPLICATION_JSON).header("Content-Length", "0").build();
		logger.info("End");
		return response;
	}

	@HEAD
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	public Response head() {
		logger.info("Begin");
		Response retorno = Response.ok().header(VerificationResource.CONTENT_TYPE, MediaType.APPLICATION_JSON).build();
		logger.info("End");
		return retorno;
	}
}
