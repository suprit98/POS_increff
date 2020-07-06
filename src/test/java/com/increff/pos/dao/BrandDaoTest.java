package com.increff.pos.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.spring.AbstractUnitTest;

public class BrandDaoTest extends AbstractUnitTest {
	
	@Autowired
	private BrandDao brand_dao;
	
	@Test
	public void testInsert() {
		BrandPojo b = getBrandPojo();
		brand_dao.insert(b);
		
		BrandPojo inserted_brand = brand_dao.select(b.getId());
		assertEquals(b.getBrand(),inserted_brand.getBrand());
		assertEquals(b.getCategory(),inserted_brand.getCategory());
	}
	
	@Test()
	public void testDelete() {
		BrandPojo b = getBrandPojo();
		brand_dao.insert(b);
		brand_dao.delete(b.getId());
		
		BrandPojo inserted_brand = brand_dao.select(b.getId());
		assertNull(inserted_brand);
	}
	
	@Test
	public void testSelectAll() {
		List<BrandPojo> brand_list = getMultipleBrandPojo();
		for(BrandPojo brand:brand_list) {
			brand_dao.insert(brand);
		}
		List<BrandPojo> inserted_brand_list = brand_dao.selectAll();
		assertEquals(brand_list.size(),inserted_brand_list.size());
	}
	
	@Test()
	public void testSelectAllBrandCategory() {
		BrandPojo b = getBrandPojo();
		brand_dao.insert(b);
		
		BrandPojo inserted_brand = brand_dao.selectAllBrandCategory(b.getBrand(), b.getCategory());
		assertEquals(b.getId(),inserted_brand.getId());
		assertEquals(b.getBrand(),inserted_brand.getBrand());
		assertEquals(b.getCategory(),inserted_brand.getCategory());
	}
	
	private BrandPojo getBrandPojo() {
		BrandPojo p = new BrandPojo();
		p.setBrand("Parle");
		p.setCategory("Biscuits");
		return p;
	}
	
	private List<BrandPojo> getMultipleBrandPojo() {
		List<BrandPojo> brand_list = new ArrayList<BrandPojo>();
		for(int i=0; i<5; i++) {
			BrandPojo brand = new BrandPojo();
			brand.setBrand("brand"+i);
			brand.setCategory("category"+i);
			brand_list.add(brand);
		}
		return brand_list;
	}

}
