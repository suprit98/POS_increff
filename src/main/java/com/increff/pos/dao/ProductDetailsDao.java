package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.ProductDetailsPojo;

@Repository
public class ProductDetailsDao {
	
	@PersistenceContext
	EntityManager em;
	
	private static String select_all = "select p from ProductDetailsPojo p";
	
	public void insert(ProductDetailsPojo p) {
		em.persist(p);
	}
	
	public void delete(int id) {
		ProductDetailsPojo p = em.find(ProductDetailsPojo.class, id);
		em.remove(p);
	}
	
	public ProductDetailsPojo select(int id) {
		return em.find(ProductDetailsPojo.class, id);
	}
	
	public List<ProductDetailsPojo> selectAll() {
		TypedQuery<ProductDetailsPojo> query = getQuery(select_all);
		List<ProductDetailsPojo> results = query.getResultList();
		return results;	
	}
	
	public void update(ProductDetailsPojo p) {
		
	}
	
	TypedQuery<ProductDetailsPojo> getQuery(String jpql) {
		return em.createQuery(jpql,ProductDetailsPojo.class);
	}

}