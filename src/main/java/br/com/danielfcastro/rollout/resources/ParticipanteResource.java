package br.com.danielfcastro.rollout.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.providers.jackson.Formatted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.danielfcastro.rollout.entity.Participante;
import br.com.danielfcastro.rollout.entity.Projeto;
import br.com.danielfcastro.rollout.repository.ParticipanteRepositoryImpl;
import br.com.danielfcastro.rollout.repository.ProjetoRepositoryImpl;
import br.com.danielfcastro.rollout.repository.StatusRepositoryImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/participant/v1")
@Api(value = "Participant")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ParticipanteResource {
	private static final Logger logger = LoggerFactory.getLogger(ParticipanteResource.class);
	private static final String CONTENT_TYPE = "Content-Type";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Inject
	ParticipanteRepositoryImpl repository;

	@Inject
	ProjetoRepositoryImpl projetoRepository;

	@Inject
	StatusRepositoryImpl statusRepository;

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "List of all rollout participants")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return all participants", response = Participante.class),
			@ApiResponse(code = 204, message = "Registry not found") })
	@Formatted
	public Response getParticipante() {
		logger.info("Begin");
		Response response = null;
		List<Participante> entity = repository.query();
		if (entity.size() != 0) {
			response = Response.ok().entity(entity).build();
		} else {
			response = Response.noContent().build();
		}
		logger.info("End");
		return response;
	}

	@GET
	@Path("/{project}/{branch}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Returns the data of the participant, if any")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The data of the participant", response = Participante.class),
			@ApiResponse(code = 204, message = "Registry not found") })
	@Formatted
	public Response getParticipanteProjetoBranch(
			@ApiParam(value = "id of project in rollout system", required = true) @PathParam("project") String project,
			@ApiParam(value = "branch code", required = true) @PathParam("branch") String branch) {
		logger.info("Begin");
		Response response = null;
		if (project == null || project.isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("project can not be null!").build();
		}
		if (branch == null || branch.isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("branch can not be nul!!").build();
		}

		Participante entity = repository.queryByProjetoBranch(project, branch);
		if (entity == null) {
			response = Response.noContent().build();
		} else {
			response = Response.status(Response.Status.OK).entity(entity).build();
		}
		logger.info("End");
		return response;
	}

	@GET
	@Path("/{project}/{branch}/{employee}")
	@ApiOperation(value = "Returns the data of the participant, if any")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The data of the participant", response = Participante.class),
			@ApiResponse(code = 204, message = "Registry not found") })
	@Formatted
	public Response getParticipanteProjetoBranchEmployee(
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
		Participante entity = repository.queryByProjetoBranchEmployee(project, branch, employee);
		if (entity == null) {
			response = Response.noContent().build();
		} else {
			response = Response.status(Response.Status.OK).entity(entity).build();		}
		logger.info("End");
		return response;
	}
	
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

	@PUT
	@Path("/")
	@ApiOperation(value = "Add participant to a rollout strategy."
			+ "If I add only one branch (employee null) I remove all the branch + employee pairs, leaving only the branch registry and authorizing all employees of that branch to a rollout strategy."
			+ "If I add a branch + employee record I remove the branch + null pair, globally disallowing the branch and only authorizing the employee + branch")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "participante adicionado"),
			@ApiResponse(code = 400, message = "Bad request") })
	@Formatted
	public Response addParticipante(
			@ApiParam(value = "id of project in rollout system", required = true) @QueryParam("projectId") String idProjeto,
			@ApiParam(value = "branch code", required = true) @QueryParam("branch") String branch,
			@ApiParam(value = "name of employee registered in rollout system", required = false) @QueryParam("employee") String employee,
			@ApiParam(value = "date for this participant begins in rollout", required = true) @QueryParam("dataInicio") String dataInicio,
			@ApiParam(value = "end date for this participant in rollout", required = true) @QueryParam("dataEnd") String dataEnd) {
		logger.info("Begin");
		Date _dataInicio = null;
		Projeto projeto = null;
		try {
			projeto = projetoRepository.queryById(idProjeto);
		} catch (NoResultException | NonUniqueResultException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("project does not exists!").build();
		}
		try {
			_dataInicio = dateFormat.parse(dataInicio);
		} catch (ParseException e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("The date for this participant begins in rollout can not be null!").build();
		}
		if ((null == branch || branch.isEmpty()) && (null == employee || employee.isEmpty())) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("branch and employee can not be all null!").build();

		}
		Participante novo = new Participante(projeto.getId(), branch, employee, _dataInicio, projeto.getEndDate());
		repository.add(novo);
		logger.info("End");
		return Response.status(Response.Status.CREATED).entity("Participant created with success!").build();
	}

	@POST
	@Path("/{participantPK}")
	@ApiOperation(value = "Updates a participant to a rollout strategy."
			+ "If I add only one branch (employee null) I remove all the branch + employee pairs, leaving only the branch registry and authorizing all employees of that branch to a rollout strategy."
			+ "If I add a branch + employee record I remove the branch + null pair, globally disallowing the branch and only authorizing the employee + branch")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "participante atualizado com sucesso"),
			@ApiResponse(code = 400, message = "Requisição mal-formada") })
	@Formatted
	public Response updateParticipante(
			@ApiParam(value = "id of participant in rollout system", required = true) @PathParam("participantPK") String participantPK,
			@ApiParam(value = "id of project in rollout system", required = true) @QueryParam("idProjeto") String idProjeto,
			@ApiParam(value = "branch code", required = true) @QueryParam("branch") String branch,
			@ApiParam(value = "name of employee registered in rollout system", required = false) @QueryParam("employee") String employee,
			@ApiParam(value = "date for this participant begins in rollout", required = true) @QueryParam("dataInicio") String dataInicio,
			@ApiParam(value = "end date for this participant in rollout", required = true) @QueryParam("dataEnd") String dataEnd) {
		logger.info("Begin");

		Date _dataInicio = null;
		Projeto projeto = null;
		try {
			repository.queryById(participantPK);
		} catch (NoResultException | NonUniqueResultException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("participant does not exists").build();
		}
		try {
			projeto = projetoRepository.queryById(idProjeto);
		} catch (NoResultException | NonUniqueResultException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("project does not exists!").build();
		}
		try {
			_dataInicio = dateFormat.parse(dataInicio);
		} catch (ParseException e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("The date for this participant begins in rollout can not be null!").build();
		}
		if ((null == branch || branch.isEmpty()) && (null == employee || employee.isEmpty())) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("branch, employee can not be both nulls!").build();

		}
		Participante novo = new Participante(projeto.getId(), branch, employee, _dataInicio, projeto.getEndDate());
		novo.setId(participantPK);
		repository.update(novo);
		logger.info("End");
		return Response.status(Response.Status.OK).entity("Participant updated with success!").build();
	}

	@DELETE
	@Path("/{idParticipante}")
	@ApiOperation(value = "Remove participante do piloto")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "participante removido com sucesso"),
			@ApiResponse(code = 400, message = "Requisição mal-formada") })
	@Formatted
	public Response removeParticipante(
			@ApiParam(value = "código do participante do piloto", required = true) @PathParam("idParticipante") String idParticipante) {
		logger.info("Begin");
		if (idParticipante == null || idParticipante.trim().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("id do Participante can not be null!").build();
		}
		repository.remove(idParticipante);
		logger.info("End");
		return Response.status(Response.Status.OK).entity("Participante removido com sucesso!").build();

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
		Response retorno = Response.ok().header(ParticipanteResource.CONTENT_TYPE, MediaType.APPLICATION_JSON).build();
		logger.info("End");
		return retorno;
	}
}
