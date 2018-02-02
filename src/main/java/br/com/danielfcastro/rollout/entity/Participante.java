package br.com.danielfcastro.rollout.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * The persistent class for the participante database table.
 * 
 */
@Entity
@Table(name="participante")
@NamedQueries({ 
	@NamedQuery(name="Participante.findAll", query="SELECT p FROM Participante p"),
	@NamedQuery(name="Participante.findById", query = "SELECT p FROM Participante p WHERE p.id = :id"),
	@NamedQuery(name="Participante.findProjeto", query="SELECT p FROM Participante p WHERE p.idProjeto = :idProjeto AND :currentDate < p.dataFim"),
	@NamedQuery(name="Participante.findProjetoBranch", query="SELECT p FROM Participante p WHERE p.branch = :branch AND p.idProjeto = :idProjeto AND :currentDate between p.dataInicio and p.dataFim"),
	@NamedQuery(name="Participante.findProjetoBranchemployee", query="SELECT p FROM Participante p WHERE p.idProjeto = :idProjeto AND p.branch = :branch AND p.employee = :employee AND :currentDate between p.dataInicio and p.dataFim"),
	@NamedQuery(name="Participante.checkProjetoBranchEmployee", query="SELECT COUNT(p.id) FROM Participante p WHERE p.idProjeto = :idProjeto AND (p.branch = :branch AND (p.employee IS NULL) OR p.branch = :branch AND p.employee LIKE :employee) AND :currentDate between p.dataInicio and p.dataFim"),
	@NamedQuery(name="Participante.findDataInicio", query="SELECT p FROM Participante p WHERE p.dataInicio <= :dataInicio")
})
@XmlRootElement(name = "Participante")
@ApiModel(value = "Participante", description = "Retorna os dados de um participante do sistema de piloto")
public class Participante implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 8086667651103733359L;

	//@Id
	//@Column(length = 36, columnDefinition="CHAR")
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
		name = "UUID",
		strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(columnDefinition = "char(36)")
	@ApiModelProperty(value = "id/pk of participant of rollout system")
	private String id;
	
	@Column(name="id_projeto", length = 36, columnDefinition="CHAR")
	@ApiModelProperty(value = "id/pk of project")
	private String idProjeto;

	@Column(length = 2, columnDefinition="CHAR")
	@ApiModelProperty(value = "branch code registered on rollout system")
	private String branch;
	
	@Column(length = 4, columnDefinition="CHAR")
	@ApiModelProperty(value = "name of employee registered in rollout system")
	private String employee;
	
	@Temporal(TemporalType.DATE)
	@Column(name="data_inicio")
	@ApiModelProperty(value = "date for this participant begins in rollout")
	private Date dataInicio;

	@Temporal(TemporalType.DATE)
	@Column(name="data_fim")
	@ApiModelProperty(value = "end date for this participant in rollout")
	private Date dataFim;

	public Participante(String idProjeto, String branch, String employee, Date dataInicio, Date dataFim) {
		this.idProjeto = idProjeto;
		this.branch = branch;
		this.employee = employee;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
	}

	public Participante() {
	}
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDataInicio() {
		return this.dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getIdProjeto() {
		return this.idProjeto;
	}

	public void setIdProjeto(String idProjeto) {
		this.idProjeto = idProjeto;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public Participante getClone() {
        try {
            // call clone in Object.
            return (Participante) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println ("Cloning not allowed. " );
            return this;
        }
	}	
	
}