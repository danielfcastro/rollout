package br.com.danielfcastro.rollout.repository;

import java.util.List;

public interface Repository<T> {
	
    void add(T item);

    void update(T item);

    void remove(T item);

    void remove(String  id);

    List<T> query();
    
    T queryById(String id);
    
    List<T> queryByName(String infix);
    
    T queryByExactName(String infix);
}
