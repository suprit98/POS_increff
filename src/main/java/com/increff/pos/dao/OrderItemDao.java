package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderItemPojo;

@Repository
public class OrderItemDao {
	
	@PersistenceContext
	EntityManager em;
	
	private static String select_all = "select p from OrderItemPojo p";
	
	public void insert(OrderItemPojo p) {
		em.persist(p);
	}
	
	public void delete(int id) {
		OrderItemPojo p = em.find(OrderItemPojo.class, id);
		em.remove(p);
	}
	
	public OrderItemPojo select(int id) {
		return em.find(OrderItemPojo.class, id);
	}
	
	
	public List<OrderItemPojo> selectAll() {
		TypedQuery<OrderItemPojo> query = getQuery(select_all);
		List<OrderItemPojo> results = query.getResultList();
		return results;	
	}
	
	public void update(OrderItemPojo p) {
		
	}
	
	TypedQuery<OrderItemPojo> getQuery(String jpql) {
		return em.createQuery(jpql,OrderItemPojo.class);
	}

}