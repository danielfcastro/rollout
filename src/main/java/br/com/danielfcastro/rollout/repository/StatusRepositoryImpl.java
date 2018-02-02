package br.com.danielfcastro.rollout.repository;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import br.com.danielfcastro.rollout.entity.Status;
import br.com.danielfcastro.rollout.qualifier.RolloutQualifier;
import br.com.danielfcastro.rollout.utils.EntityManagerProducer;

@Named("status-repository")
@RequestScoped
public class StatusRepositoryImpl implements ParameterRepository<Status> {

	@RolloutQualifier
	private EntityManager entityManager;

	public StatusRepositoryImpl() {
		super();
	}

	public StatusRepositoryImpl(EntityManager manager) {
		this.entityManager = manager;
	}

	@Transactional(Transactional.TxType.REQUIRED)
	public void add(Status item) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}		
		entityManager.persist(item);	}

	@Transactional(Transactional.TxType.REQUIRED)
	public void update(Status item) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}		
		entityManager.merge(item);	}

	@Transactional(Transactional.TxType.REQUIRED)
	public void remove(Status item) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}		
		entityManager.remove(item);	}

	@Transactional(Transactional.TxType.REQUIRED)
	public void remove(String id) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}		
		Status toBeDelete = entityManager.find(Status.class, id);
		entityManager.remove(toBeDelete);
	}

	public List<Status> query() {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}		
		TypedQuery<Status> query = null;
		query = entityManager.createNamedQuery("Status.findAll", Status.class);
		List<Status> retorno = new ArrayList<Status>(query.getResultList());
		return retorno;
	}

	public Status queryById(Integer id) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}		
		TypedQuery<Status> query = null;
		query = entityManager.createNamedQuery("Status.findById", Status.class);
		query.setParameter("id", id);
		Status retorno = query.getSingleResult();
		retorno = retorno.getClone();
		return retorno;
	}

	public Status queryByExactName(String infix) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}		
		TypedQuery<Status> query = null;
		query = entityManager.createNamedQuery("Status.findByDescricao", Status.class);
		query.setParameter("descricao", infix);
		Status retorno = query.getSingleResult();
		retorno = retorno.getClone();
		return retorno;

	}
}