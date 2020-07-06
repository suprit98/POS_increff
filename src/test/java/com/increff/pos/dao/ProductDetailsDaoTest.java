package com.increff.pos.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.spring.AbstractUnitTest;

public class ProductDetailsDaoTest extends AbstractUnitTest {
	
	@Autowired
	private BrandDao brand_dao;
	
	@Autowired
	private ProductDetailsDao product_dao;
	
	@Test
	public void testInsert() {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_dao.insert(p);
		
		ProductDetailsPojo inserted_product = product_dao.select(p.getId());
		assertEquals(p.getBarcode(),inserted_product.getBarcode());
		assertEquals(p.getName(),inserted_product.getName());
		assertEquals(p.getMrp(),inserted_product.getMrp(),0.001);
		assertEquals(p.getBrandPojo(),inserted_product.getBrandPojo());
	}
	
	@Test
	public void testDelete() {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_dao.insert(p);
		product_dao.delete(p.getId());
		
		ProductDetailsPojo inserted_product = product_dao.select(p.getId());
		assertNull(inserted_product);
	}
	
	@Test
	public void testSelectByBarcode() {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_dao.insert(p);
		
		ProductDetailsPojo inserted_product = product_dao.select(p.getBarcode());
		assertEquals(p.getBarcode(),inserted_product.getBarcode());
		assertEquals(p.getName(),inserted_product.getName());
		assertEquals(p.getMrp(),inserted_product.getMrp(),0.001);
		assertEquals(p.getBrandPojo(),inserted_product.getBrandPojo());
	}
	
	private BrandPojo getBrandPojo() {
		BrandPojo p = new BrandPojo();
		p.setBrand("Amul");
		p.setCategory("Dairy");
		brand_dao.insert(p);
		return p;
	}

	private ProductDetailsPojo getProductDetailsPojo(BrandPojo b) {
		ProductDetailsPojo p = new ProductDetailsPojo();
		p.setBrandPojo(b);
		p.setName("Milk");
		p.setBarcode("abcdefgh");
		p.setMrp(50);
		return p;
	}

}
