package com.increff.pos.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;

public class BrandDaoTest extends AbstractUnitTest{
	
	@Autowired
	private BrandDao brand_dao;
	
	@Before
	public void init() throws ApiException {
		// Inserting some initial pojos
		insertPojos();
	}
	
	@Test
	public void testInsert() {
		List<BrandPojo> brand_list_before = brand_dao.selectAll();
		BrandPojo brand = getBrandPojo();
		brand_dao.insert(brand);
		
		List<BrandPojo> brand_list_after = brand_dao.selectAll();
		assertEquals(brand_list_before.size()+1,brand_list_after.size());
		assertEquals("parle",brand_dao.select(brand.getId()).getBrand());
		assertEquals("biscuits",brand_dao.select(brand.getId()).getCategory());
		
	}
	
	@Test
	public void testDelete() {
		BrandPojo brand = getBrandPojo();
		brand_dao.insert(brand);
		List<BrandPojo> brand_list_before = brand_dao.selectAll();
		
		brand_dao.delete(brand.getId());
		
		List<BrandPojo> brand_list_after = brand_dao.selectAll();
		assertEquals(brand_list_before.size()-1,brand_list_after.size());
		
	}
	
	@Test
	public void testSelectAll() {
	
		List<BrandPojo> brand_list = brand_dao.selectAll();
		assertEquals(2,brand_list.size());
		
	}
	
	@Test
	public void testSelectAllBrandCategory() {
		List<BrandPojo> brand_list = brand_dao.selectAllBrandCategory(brands.get(0).getBrand(), brands.get(0).getCategory());
		
		assertEquals("brand",brand_list.get(0).getBrand());
		assertEquals("category0",brand_list.get(0).getCategory());
	}
	
	private BrandPojo getBrandPojo() {
		BrandPojo p = new BrandPojo();
		p.setBrand("parle");
		p.setCategory("biscuits");
		return p;
	}

}
