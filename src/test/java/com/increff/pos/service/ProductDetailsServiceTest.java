package com.increff.pos.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.spring.AbstractUnitTest;

public class ProductDetailsServiceTest extends AbstractUnitTest {

	@Autowired
	private BrandService brand_service;

	@Autowired
	private ProductDetailsService product_service;
	
	@Before
	public void init() throws ApiException {
		insertPojos();
	}

	@Test()
	public void testAdd() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		List<ProductDetailsPojo> product_list_before = product_service.getAll();
		product_service.add(p);
		List<ProductDetailsPojo> product_list_after = product_service.getAll();
		assertEquals(product_list_before.size()+1,product_list_after.size());
		assertEquals(p.getBarcode(),product_service.get(p.getId()).getBarcode());
		assertEquals(p.getName(),product_service.get(p.getId()).getName());
		assertEquals(p.getMrp(),product_service.get(p.getId()).getMrp(),0.001);
		assertEquals(p.getBrandPojo(),product_service.get(p.getId()).getBrandPojo());

	}

	@Test()
	public void testAddWrong() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getWrongProductDetailsPojo(b);
		try {
			product_service.add(p);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),"The name of product must not be empty");
		}
		

	}

	@Test()
	public void testDelete() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_service.add(p);
		
		List<ProductDetailsPojo> product_list_before = product_service.getAll();
		product_service.delete(p.getId());
		List<ProductDetailsPojo> product_list_after = product_service.getAll();
		assertEquals(product_list_before.size()-1,product_list_after.size());
		try {
			product_service.get(p.getId());
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),"ProductDetails with given ID does not exist, id: " + p.getId());
		}

	}

	@Test()
	public void testGetById() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_service.add(p);

		ProductDetailsPojo db_product_pojo = product_service.get(p.getId());
		assertEquals(p.getBarcode(),db_product_pojo.getBarcode());
		assertEquals(p.getBrandPojo(),db_product_pojo.getBrandPojo());
		assertEquals(p.getMrp(),db_product_pojo.getMrp(),0.001);
		assertEquals(p.getName(),db_product_pojo.getName());

	}

	@Test()
	public void testGetByIdNotExisting() throws ApiException {
		try {
			product_service.get(100);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),"ProductDetails with given ID does not exist, id: " + 100);
		}
		

	}

	@Test()
	public void testGetByBarcode() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_service.add(p);

		ProductDetailsPojo db_product_pojo = product_service.get(p.getBarcode());
		assertEquals(p.getId(),db_product_pojo.getId());
		assertEquals(p.getBrandPojo(),db_product_pojo.getBrandPojo());
		assertEquals(p.getMrp(),db_product_pojo.getMrp(),0.001);
		assertEquals(p.getName(),db_product_pojo.getName());

	}

	@Test()
	public void testGetByBarcodeNotExisting() throws ApiException {

		try {
			product_service.get("abcdefgh");
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),"ProductDetails with given barcode does not exist, barcode: " + "abcdefgh");
		}
		

	}

	@Test
	public void testGetAll() {
		List<ProductDetailsPojo> product_list = product_service.getAll();
		assertEquals(2,product_list.size());
	}

	@Test
	public void testUpdate() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_service.add(p);
		
		ProductDetailsPojo p2 = getNewProductDetailsPojo(b);
		product_service.update(p.getId(), p2);
		assertEquals(p2.getBrandPojo(),product_service.get(p.getId()).getBrandPojo());
		assertEquals(p2.getName(),product_service.get(p.getId()).getName());
		assertEquals(p2.getMrp(),product_service.get(p.getId()).getMrp(),0.001);
	}
	
	@Test()
	public void testUpdateWrong() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_service.add(p);
		
		ProductDetailsPojo p2 = getWrongProductDetailsPojo(b);
		try {
			product_service.update(p.getId(), p2);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),"The name of product must not be empty");
		}
		
	}
	
	@Test()
	public void testCheckIfExistsId() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_service.add(p);
		
		ProductDetailsPojo db_product_pojo = product_service.checkIfExists(p.getId());
		assertEquals(p.getBarcode(),db_product_pojo.getBarcode());
		assertEquals(p.getBrandPojo(),db_product_pojo.getBrandPojo());
		assertEquals(p.getMrp(),db_product_pojo.getMrp(),0.001);
		assertEquals(p.getName(),db_product_pojo.getName());
	}
	
	@Test()
	public void testCheckIfExistsBarcode() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_service.add(p);
		
		ProductDetailsPojo db_product_pojo = product_service.checkIfExists(p.getBarcode());
		assertEquals(p.getBarcode(),db_product_pojo.getBarcode());
		assertEquals(p.getBrandPojo(),db_product_pojo.getBrandPojo());
		assertEquals(p.getMrp(),db_product_pojo.getMrp(),0.001);
		assertEquals(p.getName(),db_product_pojo.getName());
	}
	
	@Test()
	public void testCheckIfExistsIdWrong() throws ApiException {
		
		try {
			product_service.checkIfExists(5);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),"ProductDetails with given ID does not exist, id: " + 5);
		}
		
	}
	
	@Test()
	public void testCheckIfExistsBarcodeWrong() throws ApiException {
		
		try {
			product_service.checkIfExists("abcdefgh");
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),"ProductDetails with given barcode does not exist, barcode: " + "abcdefgh");
		}
	}
	
	@Test
	public void testNormalize() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);

		product_service.normalize(p);
		assertEquals("milk", p.getName());
		
	}
	
	@Test
	public void testValidate() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		
		product_service.validate(p);
		assertTrue(!p.getName().isEmpty());
		assertTrue(p.getMrp()>=0);
		
	}
	
	@Test()
	public void testValidateWrong() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getWrongProductDetailsPojo(b);
		
		try {
			product_service.validate(p);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),"The name of product must not be empty");
		}
		
	}

	private BrandPojo getBrandPojo() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("Amul");
		p.setCategory("Dairy");
		brand_service.add(p);
		return p;
	}

	private ProductDetailsPojo getProductDetailsPojo(BrandPojo b) throws ApiException {
		ProductDetailsPojo p = new ProductDetailsPojo();
		p.setBrandPojo(b);
		p.setName("Milk");
		p.setMrp(50);
		return p;
	}

	private ProductDetailsPojo getNewProductDetailsPojo(BrandPojo b) throws ApiException {
		ProductDetailsPojo p = new ProductDetailsPojo();
		p.setBrandPojo(b);
		p.setName("Milk2");
		p.setMrp(70);
		return p;
	}

	private ProductDetailsPojo getWrongProductDetailsPojo(BrandPojo b) throws ApiException {
		ProductDetailsPojo p = new ProductDetailsPojo();
		p.setBrandPojo(b);
		p.setName("");
		p.setMrp(-5);
		return p;
	}

}
