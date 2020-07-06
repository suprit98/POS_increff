package com.increff.pos.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.spring.AbstractUnitTest;

public class InventoryServiceTest extends AbstractUnitTest {

	@Autowired
	private BrandService brand_service;

	@Autowired
	private ProductDetailsService product_service;

	@Autowired
	private InventoryService inventory_service;

	@Test()
	public void testAdd() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo i = getInventoryPojo(p);
		inventory_service.add(i);
		assertEquals(i.getProductPojo(), inventory_service.get(i.getId()).getProductPojo());
		assertEquals(i.getQuantity(), inventory_service.get(i.getId()).getQuantity());

	}

	@Test()
	public void testAddDuplicate() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo i = getInventoryPojo(p);
		inventory_service.add(i);

		try {
			inventory_service.add(i);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),
					"Inventory for this product already exists. You can edit the inventory details if you want to");
		}

	}

	@Test()
	public void testAddWrong() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo i = getWrongInventoryPojo(p);

		try {
			inventory_service.add(i);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory quantity should be positive");
		}

	}

	@Test()
	public void testDelete() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo i = getInventoryPojo(p);
		inventory_service.add(i);
		inventory_service.delete(i.getId());
		try {
			inventory_service.get(i.getId());
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory with given ID does not exist, id: " + i.getId());
		}

	}

	@Test()
	public void testGetById() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo i = getInventoryPojo(p);
		inventory_service.add(i);

		InventoryPojo db_inventory_pojo = inventory_service.get(i.getId());
		assertEquals(i.getProductPojo(), db_inventory_pojo.getProductPojo());
		assertEquals(i.getQuantity(), db_inventory_pojo.getQuantity());

	}

	@Test()
	public void testGetByIdNotExisting() throws ApiException {

		int id = 5;
		try {
			inventory_service.get(5);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory with given ID does not exist, id: " + id);
		}

	}

	@Test()
	public void testGetByProductId() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo i = getInventoryPojo(p);
		inventory_service.add(i);

		InventoryPojo db_inventory_pojo = inventory_service.getByProductId(i.getProductPojo().getId());
		assertEquals(i.getProductPojo(), db_inventory_pojo.getProductPojo());
		assertEquals(i.getQuantity(), db_inventory_pojo.getQuantity());

	}

	@Test()
	public void testGetAll() throws ApiException {

		inventory_service.getAll();

	}

	@Test()
	public void testUpdate() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo i = getInventoryPojo(p);
		inventory_service.add(i);

		InventoryPojo new_ip = getNewInventoryPojo(p);
		inventory_service.update(i.getId(), new_ip);
		assertEquals(new_ip.getProductPojo(), inventory_service.get(i.getId()).getProductPojo());
		assertEquals(new_ip.getQuantity(), inventory_service.get(i.getId()).getQuantity());

	}

	@Test()
	public void testUpdateWrong() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo i = getInventoryPojo(p);
		inventory_service.add(i);

		try {
			InventoryPojo new_ip = getWrongInventoryPojo(p);
			inventory_service.update(i.getId(), new_ip);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory quantity should be positive");
		}

	}

	@Test()
	public void testCheckIfExistsId() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo i = getInventoryPojo(p);
		inventory_service.add(i);

		InventoryPojo db_inventory_pojo = inventory_service.checkIfExists(i.getId());
		assertEquals(i.getProductPojo(), db_inventory_pojo.getProductPojo());
		assertEquals(i.getQuantity(), db_inventory_pojo.getQuantity());
	}

	@Test()
	public void testCheckIfExistsIdWrong() throws ApiException {
		int id = 5;
		try {
			inventory_service.checkIfExists(5);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory with given ID does not exist, id: " + id);
		}
	}
	
	@Test
	public void testValidate() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo ip = getInventoryPojo(p);
		inventory_service.validate(ip);
		assertTrue(ip.getQuantity()>0);
	}
	
	@Test
	public void testValidateWrong() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo ip = getWrongInventoryPojo(p);
		
		try {
			inventory_service.validate(ip);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory quantity should be positive");
		}
	}
	
	@Test
	public void testCheckIfBarcodePresent() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo ip = getInventoryPojo(p);
		inventory_service.add(ip);
		try {
			inventory_service.checkIfBarcodePresent(ip);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory for this product already exists. You can edit the inventory details if you want to");
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
		product_service.add(p);
		return p;
	}

	private InventoryPojo getInventoryPojo(ProductDetailsPojo p) {
		InventoryPojo i = new InventoryPojo();
		i.setProductPojo(p);
		i.setQuantity(20);
		return i;
	}

	private InventoryPojo getNewInventoryPojo(ProductDetailsPojo p) {
		InventoryPojo i = new InventoryPojo();
		i.setProductPojo(p);
		i.setQuantity(30);
		return i;
	}

	private InventoryPojo getWrongInventoryPojo(ProductDetailsPojo p) {
		InventoryPojo i = new InventoryPojo();
		i.setProductPojo(p);
		i.setQuantity(-5);
		return i;
	}

}
