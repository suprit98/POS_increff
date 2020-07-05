package com.increff.pos.dao;

import java.time.LocalDateTime;
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
	private static String select_date_filter = "select p from OrderPojo p where p.datetime>=:startdate and p.datetime<=:enddate";
	
	
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
		return query.getResultList();
	}
	
	public List<OrderPojo> selectByDate(LocalDateTime startdate, LocalDateTime enddate) {
		TypedQuery<OrderPojo> query = getQuery(select_date_filter);
		query.setParameter("startdate", startdate);
		query.setParameter("enddate", enddate);
		return query.getResultList();
	}
	
	public void update(OrderPojo p) {
		
	}
	
	TypedQuery<OrderPojo> getQuery(String jpql) {
		return em.createQuery(jpql,OrderPojo.class);
	}


}
