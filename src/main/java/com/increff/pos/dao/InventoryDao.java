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
	
	//Insert inventory to DB
	public void insert(InventoryPojo p) {
		em.persist(p);
	}
	
	//Delete inventory from DB
	public void delete(int id) {
		InventoryPojo p = em.find(InventoryPojo.class, id);
		em.remove(p);
	}
	
	//Select inventory from DB
	public InventoryPojo select(int id) {
		return em.find(InventoryPojo.class, id);
	}
	
	//Select Inventory based on product
	public List<InventoryPojo> selectByProduct(ProductDetailsPojo p) {
		TypedQuery<InventoryPojo> query = getQuery(select_product);
		query.setParameter("productpojo", p);
		return query.getResultList();
	}
	
	//Select All inventory pojos
	public List<InventoryPojo> selectAll() {
		TypedQuery<InventoryPojo> query = getQuery(select_all);
		return query.getResultList();
	}
	
	//Update inventory
	public void update(InventoryPojo p) {
		
	}
	
	private TypedQuery<InventoryPojo> getQuery(String jpql) {
		return em.createQuery(jpql,InventoryPojo.class);
	}

}
