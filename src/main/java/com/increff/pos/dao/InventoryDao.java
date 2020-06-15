package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.InventoryPojo;

@Repository
public class InventoryDao {
	
	@PersistenceContext
	EntityManager em;
	
	private static String select_all = "select p from InventoryPojo p";
	
	public void insert(InventoryPojo p) {
		em.persist(p);
	}
	
	public void delete(int id) {
		InventoryPojo p = em.find(InventoryPojo.class, id);
		em.remove(p);
	}
	
	public InventoryPojo select(int id) {
		return em.find(InventoryPojo.class, id);
	}
	
	public List<InventoryPojo> selectAll() {
		TypedQuery<InventoryPojo> query = getQuery(select_all);
		List<InventoryPojo> results = query.getResultList();
		return results;	
	}
	
	public void update(InventoryPojo p) {
		
	}
	
	TypedQuery<InventoryPojo> getQuery(String jpql) {
		return em.createQuery(jpql,InventoryPojo.class);
	}

}
