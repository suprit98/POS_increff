package com.increff.pos.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
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
	
	@Before
	public void init() throws ApiException {
		//Inserting initial pojos into the in-memory DB 
		insertPojos();
	}

	//Testing adding of inventory pojo
	@Test()
	public void testAdd() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo i = getInventoryPojo(p);
		List<InventoryPojo> inv_list_before = inventory_service.getAll();
		inventory_service.add(i);
		List<InventoryPojo> inv_list_after = inventory_service.getAll();
		//Number of brand pojos should increase by one
		assertEquals(inv_list_before.size()+1,inv_list_after.size());
		assertEquals(i.getProductPojo(), inventory_service.get(i.getId()).getProductPojo());
		assertEquals(i.getQuantity(), inventory_service.get(i.getId()).getQuantity());

	}

	//Testing adding of duplicate pojo. Should throw exception
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

	//Testing adding of an invalid pojo. Should throw exception
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

	//Testing deletion of pojo
	@Test()
	public void testDelete() throws ApiException {

		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo i = getInventoryPojo(p);
		inventory_service.add(i);
		List<InventoryPojo> inv_list_before = inventory_service.getAll();
		inventory_service.delete(i.getId());
		List<InventoryPojo> inv_list_after = inventory_service.getAll();
		//Number of pojos should get decreased by one
		assertEquals(inv_list_before.size()-1,inv_list_after.size());
		try {
			inventory_service.get(i.getId());
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory with given ID does not exist, id: " + i.getId());
		}

	}

	//Testing get by id
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

	//Testing get by id of a non-existent pojo. Should throw exception
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

	//Testing get by product id
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
	
	//Testing get by product id of a non-existent pojo. Should throw exception
	@Test()
	public void testGetByProductIdWrong() throws ApiException {

		InventoryPojo db_inventory_pojo = inventory_service.getByProductId(100);
		assertNull(db_inventory_pojo);

	}

	//Testing get all inventory pojos
	@Test()
	public void testGetAll() throws ApiException {

		List<InventoryPojo> inv_list = inventory_service.getAll();
		assertEquals(2,inv_list.size());

	}

	//Testing updation of inventory pojo
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

	//Testing updation of inventory pojo with an invalid pojo. Should throw exception
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

	//Testing checkifexists
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

	//Testing checkifexists with an non-existent id. Should throw exception 
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
	
	//Testing validate
	@Test
	public void testValidate() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo ip = getInventoryPojo(p);
		inventory_service.validate(ip);
		assertTrue(ip.getQuantity()>0);
	}
	
	//Testing validate of invalid inventory pojo.Should throw exception
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
	
	//Testing if inventory of product exists or not
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
