package com.increff.pos.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.pos.spring.AbstractUnitTest;

public class BrandDataTest extends AbstractUnitTest {
	
	@Test
	public void testBrandData() {
		int id = 1;
		String brand = "Amul";
		String category = "Dairy";
		BrandData brand_data = new BrandData();
		brand_data.setId(id);
		brand_data.setBrand(brand);
		brand_data.setCategory(category);
		assertEquals(id,brand_data.getId());
		assertEquals(brand,brand_data.getBrand());
		assertEquals(category,brand_data.getCategory());
	}

}
