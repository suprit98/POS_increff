package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

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

	@Test()
	public void testAdd() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_service.add(p);

	}

	@Test(expected = ApiException.class)
	public void testAddWrong() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getWrongProductDetailsPojo(b);
		product_service.add(p);

	}

	@Test()
	public void testDelete() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_service.add(p);

		product_service.delete(p.getId());

	}

	@Test()
	public void testGetById() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_service.add(p);

		product_service.get(p.getId());

	}

	@Test(expected = ApiException.class)
	public void testGetByIdNotExisting() throws ApiException {

		product_service.get(5);

	}

	@Test()
	public void testGetByBarcode() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_service.add(p);

		product_service.get(p.getBarcode());

	}

	@Test(expected = ApiException.class)
	public void testGetByBarcodeNotExisting() throws ApiException {

		product_service.get("abcdefgh");

	}

	@Test
	public void testGetAll() {
		product_service.getAll();
	}

	@Test
	public void testUpdate() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_service.add(p);
		
		ProductDetailsPojo p2 = getNewProductDetailsPojo(b);
		product_service.update(p.getId(), p2);
	}
	
	@Test(expected = ApiException.class)
	public void testUpdateWrong() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_service.add(p);
		
		ProductDetailsPojo p2 = getWrongProductDetailsPojo(b);
		product_service.update(p.getId(), p2);
	}
	
	@Test()
	public void testCheckIfExistsId() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_service.add(p);
		
		product_service.checkIfExists(p.getId());
	}
	
	@Test()
	public void testCheckIfExistsBarcode() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		product_service.add(p);
		
		product_service.checkIfExists(p.getBarcode());
	}
	
	@Test(expected = ApiException.class)
	public void testCheckIfExistsIdWrong() throws ApiException {
		
		product_service.checkIfExists(5);
	}
	
	@Test(expected = ApiException.class)
	public void testCheckIfExistsBarcodeWrong() throws ApiException {
		
		product_service.checkIfExists("abcdefgh");
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
		
	}
	
	@Test(expected = ApiException.class)
	public void testValidateWrong() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getWrongProductDetailsPojo(b);
		
		product_service.validate(p);
		
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
