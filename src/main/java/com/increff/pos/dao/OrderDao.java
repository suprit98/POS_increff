package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderPojo;

@Repository
public class OrderDao {
	
	@PersistenceContext
	EntityManager em;
	
	private static String select_all = "select p from OrderPojo p";
	
	
	public int insert(OrderPojo p) {
		em.persist(p);
		em.flush();
		return p.getId();
	}
	
	public void delete(int id) {
		OrderPojo p = em.find(OrderPojo.class, id);
		em.remove(p);
	}
	
	public OrderPojo select(int id) {
		return em.find(OrderPojo.class, id);
	}
	
	public List<OrderPojo> selectAll() {
		TypedQuery<OrderPojo> query = getQuery(select_all);
		List<OrderPojo> results = query.getResultList();
		return results;	
	}
	
	public void update(OrderPojo p) {
		
	}
	
	TypedQuery<OrderPojo> getQuery(String jpql) {
		return em.createQuery(jpql,OrderPojo.class);
	}

}
