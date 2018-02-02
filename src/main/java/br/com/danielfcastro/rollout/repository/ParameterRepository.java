package br.com.danielfcastro.rollout.repository;

import java.util.List;

public interface ParameterRepository<T> {
	
    void add(T item);

    void update(T item);

    void remove(T item);

    void remove(String  id);

    List<T> query();
    
    T queryById(Integer id);
    
    T queryByExactName(String infix);
}
