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
		return query.getResultList();
	}
	
	public BrandPojo selectAllBrandCategory(String brand, String category) {
		TypedQuery<BrandPojo> query = getQuery(select_brand_category);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		List<BrandPojo> results = query.getResultList();
		if(results.size()>0) {
			return results.get(0);
		}
		else {
			return null;
		}
	}
	
	public void update(BrandPojo p) {
		
	}
	
	TypedQuery<BrandPojo> getQuery(String jpql) {
		return em.createQuery(jpql,BrandPojo.class);
	}
	
	
}
