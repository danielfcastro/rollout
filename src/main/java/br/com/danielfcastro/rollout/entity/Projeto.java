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
 * The persistent class for the projeto database table.
 * 
 */
@Entity
@Table(name="projeto")
@NamedQueries({ 
	@NamedQuery(name = "Projeto.findAll", query = "SELECT p FROM Projeto p"),
	@NamedQuery(name = "Projeto.findById", query = "SELECT p FROM Projeto p WHERE p.id = :id"),
	@NamedQuery(name = "Projeto.findByStatus", query = "SELECT p FROM Projeto p WHERE p.status = :status"),
	@NamedQuery(name = "Projeto.findByName", query = "SELECT p FROM Projeto p WHERE p.description LIKE :description"),
	@NamedQuery(name = "Projeto.findOnPiloto", query = "SELECT p FROM Projeto p WHERE p.status <> 'Inativo' AND p.endDate > CURRENT_DATE") 
})
@XmlRootElement(name = "Projeto")
@ApiModel(value = "Projeto", description = "Returns the data of projects registered inside rollout strategy")
public class Projeto implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 6445579783065651710L;

	//@Id
	//@Column(length = 36, columnDefinition="CHAR")
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
		name = "UUID",
		strategy = "org.hibernate.id.UUIDGenerator"
	)
	@ApiModelProperty(value = "id/pk of project")
	@Column(columnDefinition = "char(36)")
	private String id;

	@Temporal(TemporalType.DATE)
	@Column(name="data_fim")
	@ApiModelProperty(value = "end date for rollout")
	private Date endDate;

	@Column(length = 50, columnDefinition="CHAR")
	@ApiModelProperty(value = "name of project")
	private String description;

	@Column(length = 10, columnDefinition="CHAR")
	@ApiModelProperty(value = "status of project registered inside rollout strategy system")
	private String status;

	public Projeto() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date dataFim) {
		this.endDate = dataFim;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String descricao) {
		this.description = descricao;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Projeto(Date dataFim, String descricao, String status) {
		super();
		this.endDate = dataFim;
		this.description = descricao;
		this.status = status;
	}
	
	public Projeto getClone() {
        try {
            // call clone in Object.
            return (Projeto) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println ("Cloning not allowed. " );
            return this;
        }
	}
}