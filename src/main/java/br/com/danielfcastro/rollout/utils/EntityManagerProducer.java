package br.com.danielfcastro.rollout.utils;


import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import br.com.danielfcastro.rollout.qualifier.RolloutQualifier;

@ApplicationScoped
public class EntityManagerProducer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1877889201546984355L;
	@PersistenceUnit(unitName = "nas-pilot")
	private EntityManagerFactory factoryBrasiliaComm;

    @RequestScoped
    @RolloutQualifier
    @Default
    @Produces
    public EntityManager createEntityManager() {
        return factoryBrasiliaComm.createEntityManager();
    }
}

