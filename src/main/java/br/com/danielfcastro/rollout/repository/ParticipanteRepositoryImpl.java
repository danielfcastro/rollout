package br.com.danielfcastro.rollout.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import br.com.danielfcastro.rollout.entity.Participante;
import br.com.danielfcastro.rollout.utils.EntityManagerProducer;

@Named("participante-repository")
@RequestScoped
public class ParticipanteRepositoryImpl implements Repository<Participante> {

	@PersistenceContext(unitName = "rolloutPU")
	private EntityManager entityManager;

	public ParticipanteRepositoryImpl() {
		super();
	}

	public ParticipanteRepositoryImpl(EntityManager manager) {
		this.entityManager = manager;
	}

	@Transactional(Transactional.TxType.REQUIRED)
	public void add(Participante item) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}

		// Significa que estou adicionando um par cBranch + Employee tenho que apagar, se
		// houver,
		// os pares Branch + nulo ou seja, desautorizações ade sucursal como um todo
		if (null != item.getEmployee()) {
			StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM Participante p WHERE p.Branch = : Branch AND p.Employee IS NULL");
			Query q = entityManager.createQuery(sb.toString());
			q.setParameter("Branch", item.getBranch());
			q.executeUpdate();

		} else
		// Se o Employee for nulo então eu estou autorizando uma sucursal inteira e devo
		// apagar os pares Branch + Employee
		if (null == item.getEmployee()) {
			StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM Participante p WHERE p.Branch = : Branch AND p.Employee IS NOT NULL");
			Query q = entityManager.createQuery(sb.toString());
			q.setParameter("Branch", item.getBranch());
			q.executeUpdate();
		}
		entityManager.persist(item);
	}

	@Transactional(Transactional.TxType.REQUIRED)
	public void update(Participante item) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}
		// Significa que estou adicionando um par cBranch + Employee tenho que apagar, se
		// houver,
		// os pares Branch + nulo ou seja, desautorizações ade sucursal como um todo
		if (null != item.getEmployee()) {
			StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM Participante p WHERE p.Branch = : Branch AND p.Employee IS NULL");
			Query q = entityManager.createQuery(sb.toString());
			q.setParameter("Branch", item.getBranch());
			q.executeUpdate();

		} else
		// Se o Employee for nulo então eu estou autorizando uma sucursal inteira e devo
		// apagar os pares Branch + Employee
		if (null == item.getEmployee()) {
			StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM Participante p WHERE p.Branch = : Branch AND p.Employee IS NOT NULL");
			Query q = entityManager.createQuery(sb.toString());
			q.setParameter("Branch", item.getBranch());
			q.executeUpdate();
		}
		entityManager.merge(item);
	}

	@Transactional(Transactional.TxType.REQUIRED)
	public void remove(Participante item) {
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
		Participante toBeDelete = entityManager.find(Participante.class, id);
		entityManager.remove(toBeDelete);

	}

	@Transactional(Transactional.TxType.REQUIRED)
	public void removePairs(String Branch) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM Participante p WHERE p.Branch = : Branch AND p.Employee IS NOT NULL");
		Query q = entityManager.createQuery(sb.toString());
		q.setParameter("Branch", Branch);
		q.executeUpdate();
	}

	@Transactional(Transactional.TxType.SUPPORTS)
	public List<Participante> query() {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}
		TypedQuery<Participante> query = entityManager.createNamedQuery("Participante.findAll", Participante.class);
		List<Participante> retorno = new ArrayList<Participante>(query.getResultList());
		return retorno;
	}

	@Override
	@Transactional(Transactional.TxType.SUPPORTS)
	public Participante queryById(String id) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}
		TypedQuery<Participante> query = entityManager.createNamedQuery("Participante.findById", Participante.class);
		query.setParameter("id", id);
		Participante retorno = query.getSingleResult();
		retorno = retorno.getClone();
		return retorno;
	}

	@Transactional(Transactional.TxType.SUPPORTS)
	public Participante queryByProjetoOrigemCalculo(String projeto, String codigoOrigemCalculo) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}
		try {
			TypedQuery<Participante> query = entityManager.createNamedQuery("Participante.findOrigemCalculo",
					Participante.class);
			query.setParameter("idProjeto", projeto);
			query.setParameter("codigoOrigemCalculo", codigoOrigemCalculo);

			Date now = new Date(System.currentTimeMillis());
			query.setParameter("currentDate", now);
			Participante retorno = query.getSingleResult();
			retorno = retorno.getClone();
			return retorno;
		} catch (NoResultException e) {
			// Usuário fora do piloto
			return null;
		}

	}

	@Transactional(Transactional.TxType.SUPPORTS)
	public Participante queryByProjetoBranch(String projeto, String Branch) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}

		try {
			TypedQuery<Participante> query = entityManager.createNamedQuery("Participante.findProjetoBranch",
					Participante.class);
			query.setParameter("Branch", Branch);
			query.setParameter("idProjeto", projeto);

			Date now = new Date(System.currentTimeMillis());
			query.setParameter("currentDate", now);
			Participante retorno = query.getSingleResult();
			retorno = retorno.getClone();
			return retorno;
		} catch (NoResultException e) {
			// Usuário fora do piloto
			return null;
		}
	}

	@Transactional(Transactional.TxType.SUPPORTS)
	public Participante queryByProjetoBranchEmployee(String projeto, String Branch, String Employee) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}

		try {
			TypedQuery<Participante> query = entityManager.createNamedQuery("Participante.findProjetoBranchEmployee",
					Participante.class);

			query.setParameter("idProjeto", projeto);
			query.setParameter("Branch", Branch);
			query.setParameter("Employee", Employee);

			Date now = new Date(System.currentTimeMillis());
			query.setParameter("currentDate", now);
			Participante retorno = query.getSingleResult();
			retorno = retorno.getClone();
			return retorno;
		} catch (NoResultException e) {
			// Usuário fora do piloto
			return null;
		}

	}

	@Transactional(Transactional.TxType.SUPPORTS)
	public Long queryCheckByProjetoBranchEmployee(String projeto, String Branch, String Employee) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}

		try {
			TypedQuery<Long> query = entityManager.createNamedQuery("Participante.checkProjetoBranchEmployee",
					Long.class);

			query.setParameter("idProjeto", projeto);
			query.setParameter("Branch", Branch);
			query.setParameter("Employee", Employee);

			Date now = new Date(System.currentTimeMillis());
			query.setParameter("currentDate", now);
			Long retorno = query.getSingleResult();
			retorno = new Long(retorno.longValue());
			return retorno;
		} catch (NoResultException e) {
			// Usuário fora do piloto
			return null;
		}

	}

	@Transactional(Transactional.TxType.SUPPORTS)
	public Participante queryByProjetoBranchEmployeeOrigemCalculo(String projeto, String Branch, String Employee,
			String origemCalculo) {
		if (!entityManager.isOpen()) {
			entityManager = new EntityManagerProducer().createEntityManager();
		}

		try {
			TypedQuery<Participante> query = entityManager.createNamedQuery("Participante.findProjetoBranchEmployeeOC",
					Participante.class);
			query.setParameter("Branch", Branch);
			query.setParameter("Employee", Employee);
			query.setParameter("idProjeto", projeto);
			query.setParameter("origemCalculo", origemCalculo);

			Date now = new Date(System.currentTimeMillis());
			query.setParameter("currentDate", now);
			Participante retorno = query.getSingleResult();
			retorno = retorno.getClone();
			return retorno;
		} catch (NoResultException e) {
			// Usuário fora do piloto
			return null;
		}

	}

	@Override
	public List<Participante> queryByName(String infix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Participante queryByExactName(String infix) {
		// TODO Auto-generated method stub
		return null;
	}

}
