package br.com.danielfcastro.rollout.resources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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

import br.com.danielfcastro.rollout.entity.Status;
import br.com.danielfcastro.rollout.repository.StatusRepositoryImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/status/v1")
@Api(value = "Status")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StatusResource {
	private static final Logger logger = LoggerFactory.getLogger(StatusResource.class);

	@Inject
	StatusRepositoryImpl repository;

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "A lista de todos os status possíveis")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Retorno de todos os status", response = Status.class),
			@ApiResponse(code = 204, message = "Registro não encontrado") })
	@Formatted
	public Response getStatuses() {
		logger.info("Início");
		Response response = null;
		List<Status> entity = repository.query();
		if (entity.size() != 0) {
			response = Response.ok().entity(entity).build();
		} else {
			response = Response.noContent().build();
		}
		logger.info("Fim");
		return response;
	}

	@PUT
	@Path("/")
	@ApiOperation(value = "Adiciona um Status à parametrização de Status do Sistema de Piloto")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Inserção bem sucedida"),
			@ApiResponse(code = 400, message = "Requisição mal-formada") })
	@Formatted
	// short codProjeto, String dscProjeto, String statusProjeto
	public Response addStatus(
			@ApiParam(value = "descrição do status", required = true) @QueryParam("nome") String nome) {
		logger.info("Início");

		if (nome == null || nome.trim().length() == 0) {
			return Response.status(Response.Status.BAD_REQUEST).entity("descrição do status não pode ser nulo!")
					.build();
		}
		Status novo = new Status(nome);
		repository.add(novo);
		logger.info("Fim");
		return Response.status(Response.Status.CREATED).entity("Status inserido com sucesso!").build();
	}

	@POST
	@Path("/{id}")
	@ApiOperation(value = "Atualiza um Status à parametrização de Status do Sistema de Piloto")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Atualização bem sucedida"),
			@ApiResponse(code = 400, message = "Requisição mal-formada") })
	@Formatted
	// short codProjeto, String dscProjeto, String statusProjeto
	public Response updateStatus(@ApiParam(value = "id do status", required = true) @PathParam("id") String id,
			@ApiParam(value = "descrição do status", required = true) @QueryParam("nome") String nome) {
		logger.info("Início");
		if (id == null || id.trim().length() == 0) {
			return Response.status(Response.Status.BAD_REQUEST).entity("id não pode ser nulo!").build();
		}
		if (nome == null || nome.trim().length() == 0) {
			return Response.status(Response.Status.BAD_REQUEST).entity("descrição do status não pode ser nulo!")
					.build();
		}
		Status novo = new Status(nome);
		novo.setId(id);
		repository.update(novo);
		logger.info("Fim");
		return Response.status(Response.Status.OK).entity("Status atualizado com sucesso!").build();
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Remove um Status da parametrização de Status do Sistema de Piloto")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Remoção bem sucedida"),
			@ApiResponse(code = 400, message = "Requisição mal-formada") })
	@Formatted
	public Response remove(@ApiParam(value = "id do status", required = true) @PathParam("id") String id) {
		logger.info("Início");
		if (id == null || id.trim().length() == 0) {
			return Response.status(Response.Status.BAD_REQUEST).entity("id não pode ser nulo!").build();
		}
		repository.remove(id);
		logger.info("Fim");
		return Response.status(Response.Status.OK).entity("Status removido com sucesso!").build();

	}
}