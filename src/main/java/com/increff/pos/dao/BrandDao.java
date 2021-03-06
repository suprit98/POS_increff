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
	private static String select_brand_category = "select p from BrandPojo p where p.brand=:brand and p.category=:category";
	
	//Insert brand to database
	public void insert(BrandPojo p) {
		em.persist(p);
	}
	
	//Delete brand from database
	public void delete(int id) {
		BrandPojo p = em.find(BrandPojo.class, id);
		em.remove(p);
	}
	
	//Select brand from DB
	public BrandPojo select(int id) {
		return em.find(BrandPojo.class, id);
	}
	
	//Select All brands from DB
	public List<BrandPojo> selectAll() {
		TypedQuery<BrandPojo> query = getQuery(select_all);
		return query.getResultList();
	}
	
	//Select Brand Pojo based on brand and category values
	public List<BrandPojo> selectAllBrandCategory(String brand, String category) {
		TypedQuery<BrandPojo> query = getQuery(select_brand_category);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		List<BrandPojo> results = query.getResultList();
		return results;
	}
	
	//Update Brand
	public void update(BrandPojo p) {
		
	}
	
	private TypedQuery<BrandPojo> getQuery(String jpql) {
		return em.createQuery(jpql,BrandPojo.class);
	}
	
	
}
