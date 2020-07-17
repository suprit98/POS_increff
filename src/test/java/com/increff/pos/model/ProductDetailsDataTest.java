package com.increff.pos.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.pos.spring.AbstractUnitTest;

public class ProductDetailsDataTest extends AbstractUnitTest {
	
	@Test
	public void testProductDetailsData() {
		int id = 1;
		String brand = "amul";
		String category = "dairy";
		String name = "milk";
		double mrp = 70.00;
		String barcode = "abcdefgh";
		
		ProductDetailsData product = new ProductDetailsData();
		product.setBarcode(barcode);
		product.setBrand(brand);
		product.setCategory(category);
		product.setId(id);
		product.setMrp(mrp);
		product.setName(name);
		
		assertEquals(brand,product.getBrand());
		assertEquals(category,product.getCategory());
		assertEquals(name,product.getName());
		assertEquals(mrp, product.getMrp(),0.001);
		assertEquals(barcode, product.getBarcode());
		assertEquals(id,product.getId());
	}

}
