package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.BrandPojo;


@Repository
public class BrandDao {
	
	@PersistenceContext
	EntityManager em;
	
	private static String select_all = "select p from BrandPojo p";
	
	public void insert(BrandPojo p) {
		em.persist(p);
	}
	
	public void delete(int id) {
		BrandPojo p = em.find(BrandPojo.class, id);
		em.remove(p);
	}
	
	public BrandPojo select(int id) {
		return em.find(BrandPojo.class, id);
	}
	
	public List<BrandPojo> selectAll() {
		TypedQuery<BrandPojo> query = getQuery(select_all);
		List<BrandPojo> results = query.getResultList();
		return results;	
	}
	
	public void update(BrandPojo p) {
		
	}
	
	TypedQuery<BrandPojo> getQuery(String jpql) {
		return em.createQuery(jpql,BrandPojo.class);
	}
	
	
}
