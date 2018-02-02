package br.com.danielfcastro.rollout.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import br.com.danielfcastro.rollout.entity.Projeto;
import br.com.danielfcastro.rollout.qualifier.RolloutQualifier;
import br.com.danielfcastro.rollout.utils.EntityManagerProducer;

@Named("projeto-repository")
@RequestScoped
public class ProjetoRepositoryImpl implements Repository<Projeto> {

	@RolloutQualifier
	private EntityManager entityManager;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public ProjetoRepositoryImpl() {
		super();
	}

	public ProjetoRepositoryImpl(EntityManager manager) {
		this.entityManager = manager;
	}

	@Transactional(Transactional.TxType.REQUIRED)
	public void add(Projeto item) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}
		entityManager.persist(item);
	}

	@Transactional(Transactional.TxType.REQUIRED)
	public void update(Projeto item) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}
		entityManager.merge(item);
	}

	@Transactional(Transactional.TxType.REQUIRED)
	public void remove(Projeto item) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}
		entityManager.remove(item);
	}

	@Transactional(Transactional.TxType.REQUIRED)
	public void remove(String id) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}
		Projeto toBeDelete = entityManager.find(Projeto.class, id);
		entityManager.remove(toBeDelete);
	}
	
	@Transactional(Transactional.TxType.REQUIRED)
	public void removeExpired() throws ParseException {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM Projeto p WHERE p.dataFim < :data");
		Query q = entityManager.createQuery(sb.toString());
		LocalDate today = LocalDate.now();
		today = today.minusMonths(3);
		Date data = sdf.parse(today.toString());
		q.setParameter("data", data);
		q.executeUpdate();
	}	

	@Transactional(Transactional.TxType.SUPPORTS)
	public List<Projeto> query() {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}
		TypedQuery<Projeto> query = entityManager.createNamedQuery("Projeto.findAll", Projeto.class);
		List<Projeto> results = new ArrayList<Projeto>(query.getResultList());
		return results;
	}

	@Transactional(Transactional.TxType.SUPPORTS)
	public Projeto queryById(String id) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}
		TypedQuery<Projeto> query = entityManager.createNamedQuery("Projeto.findById", Projeto.class);
		query.setParameter("id", id);
		Projeto retorno = query.getSingleResult();
		retorno = retorno.getClone();
		return retorno;
	}

	@Transactional(Transactional.TxType.SUPPORTS)
	public List<Projeto> queryByName(String infix) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}
		TypedQuery<Projeto> query = null;
		query = entityManager.createNamedQuery("Projeto.findByName", Projeto.class);
		query.setParameter("descricao", "%" + infix + "%");
		List<Projeto> results = new ArrayList<Projeto>(query.getResultList());
		return results;
	}

	@Override
	@Transactional(Transactional.TxType.SUPPORTS)
	public Projeto queryByExactName(String infix) {
		// TODO Auto-generated method stub
		return null;
	}
}