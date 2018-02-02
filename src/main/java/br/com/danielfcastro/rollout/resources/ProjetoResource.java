package br.com.danielfcastro.rollout.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
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

import br.com.danielfcastro.rollout.entity.Projeto;
import br.com.danielfcastro.rollout.entity.Status;
import br.com.danielfcastro.rollout.repository.ProjetoRepositoryImpl;
import br.com.danielfcastro.rollout.repository.StatusRepositoryImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/projeto/v1")
@Api(value = "Projeto")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProjetoResource {
	
	private static final Logger logger = LoggerFactory.getLogger(ProjetoResource.class);
	private static final String CONTENT_TYPE = "Content-Type";
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Inject
	ProjetoRepositoryImpl repository;

	@Inject
	StatusRepositoryImpl statusRepository;	
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "A lista de todos os projetos no registro de piloto")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Retorno de todos os projetos"),
						   @ApiResponse(code = 204, message = "Registro não encontrado")})	
	@Formatted
	public Response getProjetos() {
		logger.info("Início");
		Response response = null;
		List<Projeto> entity = repository.query();
		if (entity.size() != 0) {
			response = Response.ok().entity(entity).build();
		} else {
			response = Response.noContent().build();
		}
		logger.info("Fim");
		return response;
	}

	@GET
	@Path("/{nome}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Dados de um projeto em piloto")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Retorno de dados do projeto", response = Projeto.class ),
						   @ApiResponse(code = 204, message = "Registro não encontrado")})	
	@Formatted
	public Response getProjeto(@ApiParam(value = "id do projeto", required = true) @PathParam("nome") String nome) {
		logger.info("Início");
		Response response = null;
		if (nome == null || nome.isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST).entity("nome não pode ser nulo!").build();
		}
		List<Projeto> entity = repository.queryByName(nome);
		if (entity.size() != 0) {
			response = Response.ok().entity(entity).build();
		} else {
			response = Response.noContent().build();
		}
		logger.info("Fim");
		return response;
	}

	@PUT
	@Path("/add")
	@ApiOperation(value = "Insere um projeto no piloto")
	@ApiResponses(value = {@ApiResponse(code = 201, message = "Se inseriu com sucesso"),
						   @ApiResponse(code = 400, message = "Requisição mal-formada")})	
	@Formatted
	public Response addProjeto(
			@ApiParam(value = "nome do projeto", required = true) @QueryParam("projeto") String dscProjeto, 
			@ApiParam(value = "data fim do projeto no formato yyyy-MM-dd", required = true) @QueryParam("dataFim") String dataFim,
			@ApiParam(value = "status do projeto obtido através da chamada GET a http://host:port/piloto/status", required = true) @QueryParam("statusProjeto") String statusProjeto) {
		logger.info("Início");
		Date _dataFim = null;
		try {
			_dataFim = dateFormat.parse(dataFim);	
		}catch(ParseException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Data de fim de piloto do projeto não pode ser nulo!").build();
		}
		if (dscProjeto == null || dscProjeto.trim().length() == 0) {
			return Response.status(Response.Status.BAD_REQUEST).entity("descrição do projeto não pode ser nulo!")
					.build();
		} else if (statusProjeto == null || statusProjeto.trim().length() == 0) {
			return Response.status(Response.Status.BAD_REQUEST).entity("status do projeto não pode ser nulo!").build();
		}
		//Verificar se o Status do Projeto existe na tabela status
		Status statuses = statusRepository.queryByExactName(statusProjeto);
		if(null == statuses) {
			return Response.status(Response.Status.BAD_REQUEST).entity("status não existe!").build();
		}
		Projeto novo = new Projeto(_dataFim, dscProjeto, statuses.getDescricao());
		repository.add(novo);
		logger.info("Fim");
		return Response.status(Response.Status.CREATED).entity("Projeto inserido com sucesso!").build();
	}

	@POST
	@Path("/{codProjeto}")
	@ApiOperation(value = "Atualiza dados do projeto no piloto")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Atualização bem sucedida"),
						   @ApiResponse(code = 400, message = "Requisição mal-formada")})
	@Formatted
	public Response updateProjeto(
			@ApiParam(value = "id do projeto", required = true) @PathParam("codProjeto") String codProjeto,
			@ApiParam(value = "nome do projeto", required = true) @QueryParam("dscProjeto") String dscProjeto, 
			@ApiParam(value = "data fim do projeto no formato yyyy-MM-dd", required = true) @QueryParam("dataFim") String dataFim,
			@ApiParam(value = "status do projeto obtido através da chamada GET a http://host:port/piloto/status", required = true) @QueryParam("statusProjeto") String statusProjeto) {
		logger.info("Início");
		Projeto _novo = repository.queryById(codProjeto);
		if (codProjeto == null || codProjeto.trim().length() == 0) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Código do projeto não pode ser nulo!").build();
		}
		if(null == _novo) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Código do projeto não existe!").build();
		}
		Date _dataFim = null;
		Status status = null;
		
		try {
			_dataFim = dateFormat.parse(dataFim);	
		}catch(ParseException e) {
			_dataFim = null;
		}
		//Verificar se o Status do Projeto existe na tabela status
		if (statusProjeto == null || statusProjeto.trim().length() == 0) {
			status = statusRepository.queryById(1);	
		}else {
			status = statusRepository.queryByExactName(statusProjeto);
		}
		Projeto novo = new Projeto( _dataFim, dscProjeto, status.getDescricao());
		novo.setId(codProjeto);
		repository.update(novo);
		logger.info("Fim");
		return Response.status(Response.Status.OK).entity("Projeto inserido com sucesso!").build();
	}

	@DELETE
	@Path("/{idProjeto}")
	@ApiOperation(value = "Remove um projeto do piloto")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Remoção bem sucedida"),
						   @ApiResponse(code = 400, message = "Requisição mal-formada")})
	@Formatted
	public Response removeProjeto(@ApiParam(value = "id do projeto", required = true) @PathParam("idProjeto") String idProjeto) {
		logger.info("Início");
		if (idProjeto == null || idProjeto.trim().length() == 0) {
			return Response.status(Response.Status.BAD_REQUEST).entity("id do projeto não pode ser nulo!").build();
		}
		repository.remove(idProjeto);
		logger.info("Fim");
		return Response.status(Response.Status.OK).entity("Projeto removido com sucesso!").build();

	}

	@GET
	@Path("/health_check")
	@Produces(MediaType.APPLICATION_JSON)
	@Formatted
	public Response health_check(@QueryParam("timeout") int timeout) {
		// TODO -IMPLEMENTAR
		logger.info("Início");
		logger.info("Fim");
		return null;
	}

	@OPTIONS
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	public Response options() {
		logger.info("Início");
		Response response = Response.status(200).header("Allow", "POST, PUT, GET, DELETE, OPTIONS, HEAD")
				.header("Content-Type", MediaType.APPLICATION_JSON).header("Content-Length", "0").build();
		logger.info("Fim");
		return response;
	}

	@HEAD
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	public Response head() {
		logger.info("Início");
		Response retorno = Response.ok().header(ProjetoResource.CONTENT_TYPE, MediaType.APPLICATION_JSON).build();
		logger.info("Fim");
		return retorno;
	}
}
