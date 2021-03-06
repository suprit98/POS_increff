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
	private static String select_barcode = "select p from ProductDetailsPojo p where barcode=:barcode";
	
	//Insert product details to DB
	public void insert(ProductDetailsPojo p) {
		em.persist(p);
	}
	
	//Delete Product from DB
	public void delete(int id) {
		ProductDetailsPojo p = em.find(ProductDetailsPojo.class, id);
		em.remove(p);
	}
	
	//Select product from DB
	public ProductDetailsPojo select(int id) {
		return em.find(ProductDetailsPojo.class, id);
	}
	
	//Select product by barcode from DB
	public ProductDetailsPojo select(String barcode) {
		TypedQuery<ProductDetailsPojo> query = getQuery(select_barcode);
		query.setParameter("barcode", barcode);
		List<ProductDetailsPojo> lis = query.getResultList();
		if(lis.size()>0) {
			ProductDetailsPojo p = lis.get(0);
			return p;
		}
		else {
			return null;
		}
	}
	
	//Select all products from DB
	public List<ProductDetailsPojo> selectAll() {
		TypedQuery<ProductDetailsPojo> query = getQuery(select_all);
		return query.getResultList();	
	}
	
	//Update Product Details
	public void update(ProductDetailsPojo p) {
		
	}
	
	private TypedQuery<ProductDetailsPojo> getQuery(String jpql) {
		return em.createQuery(jpql,ProductDetailsPojo.class);
	}

}
