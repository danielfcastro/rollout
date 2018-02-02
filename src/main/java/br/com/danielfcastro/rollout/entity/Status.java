package br.com.danielfcastro.rollout.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * The persistent class for the status database table.
 * 
 */
@Entity
@Table(name = "status")
@NamedQueries({ @NamedQuery(name = "Status.findAll", query = "SELECT s FROM Status s"),
		@NamedQuery(name = "Status.findById", query = "SELECT s FROM Status s WHERE s.id = :id"),
		@NamedQuery(name = "Status.findByDescricao", query = "SELECT s FROM Status s WHERE s.descricao = :descricao") })
@XmlRootElement(name = "Status")
@ApiModel(value = "Status", description = "Returns the data of statuses registered in rollout strategy system")
public class Status implements Serializable, Cloneable {

	private static final long serialVersionUID = 1262236648854650133L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(columnDefinition = "char(36)")
	@ApiModelProperty(value = "id/pk of status")
	private String id;

	@Column(length = 50)
	@ApiModelProperty(value = "name of status")
	private String descricao;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Status(String descricao) {
		super();
		this.descricao = descricao;
	}

	public Status() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Status getClone() {
        try {
            // call clone in Object.
            return (Status) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println ("Cloning not allowed. " );
            return this;
        }
	}
}