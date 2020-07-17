package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductDetailsPojo;

@Repository
public class InventoryDao {
	
	@PersistenceContext
	EntityManager em;
	
	private static String select_all = "select p from InventoryPojo p";
	private static String select_product = "select p from InventoryPojo p where p.productPojo=:productpojo";
	
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
	
	public List<InventoryPojo> selectByProduct(ProductDetailsPojo p) {
		TypedQuery<InventoryPojo> query = getQuery(select_product);
		query.setParameter("productpojo", p);
		return query.getResultList();
	}
	
	public List<InventoryPojo> selectAll() {
		TypedQuery<InventoryPojo> query = getQuery(select_all);
		return query.getResultList();
	}
	
	public void update(InventoryPojo p) {
		
	}
	
	private TypedQuery<InventoryPojo> getQuery(String jpql) {
		return em.createQuery(jpql,InventoryPojo.class);
	}

}
