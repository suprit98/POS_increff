package com.increff.pos.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;

public class ProductDaoTest extends AbstractUnitTest{
	
	@Autowired
	private ProductDetailsDao product_dao;
	
	@Before
	public void init() throws ApiException {
		// Inserting some initial pojos
		insertPojos();
	}
	
	@Test
	public void testInsert() {
		List<ProductDetailsPojo> product_list_before = product_dao.selectAll();
		ProductDetailsPojo product = getProductDetailsPojo(brands.get(0));
		product_dao.insert(product);
		
		List<ProductDetailsPojo> product_list_after = product_dao.selectAll();
		assertEquals(product_list_before.size()+1,product_list_after.size());
		assertEquals("milk",product_dao.select(product.getId()).getName());
		assertEquals(50,product_dao.select(product.getId()).getMrp(),0.001);
		assertEquals(brands.get(0),product_dao.select(product.getId()).getBrandPojo());
		
	}
	
	@Test
	public void testDelete() {
		
		ProductDetailsPojo product = getProductDetailsPojo(brands.get(0));
		product_dao.insert(product);
		
		List<ProductDetailsPojo> product_list_before = product_dao.selectAll();
		product_dao.delete(product.getId());
		List<ProductDetailsPojo> product_list_after = product_dao.selectAll();
		assertEquals(product_list_before.size()-1,product_list_after.size());
		
	}
	
	@Test
	public void testSelectBarcode() {
		ProductDetailsPojo product = product_dao.select(products.get(0).getBarcode());
		
		assertEquals(product.getBarcode(),products.get(0).getBarcode());
		assertEquals(product.getBrandPojo(),products.get(0).getBrandPojo());
		assertEquals(product.getName(),products.get(0).getName());
		assertEquals(product.getMrp(),products.get(0).getMrp(),0.001);
	}
	
	@Test
	public void testSelectAll() {
		
		List<ProductDetailsPojo> product_list = product_dao.selectAll();
		assertEquals(3,product_list.size());
		
	}
	
	private ProductDetailsPojo getProductDetailsPojo(BrandPojo b){
		ProductDetailsPojo p = new ProductDetailsPojo();
		p.setBrandPojo(b);
		p.setName("milk");
		p.setMrp(50);
		return p;
	}

}
