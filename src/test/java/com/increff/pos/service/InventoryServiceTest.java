package com.increff.pos.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.spring.AbstractUnitTest;

public class InventoryServiceTest extends AbstractUnitTest {

	@Before
	public void init() throws ApiException {
		// Inserting initial pojos into the in-memory DB
		insertPojos();
	}

	/* Testing adding of inventory pojo */
	@Test()
	public void testAdd() throws ApiException {

		InventoryPojo i = getInventoryPojo(products.get(2));
		List<InventoryPojo> inv_list_before = inventory_service.getAll();
		inventory_service.add(i);
		List<InventoryPojo> inv_list_after = inventory_service.getAll();
		// Number of brand pojos should increase by one
		assertEquals(inv_list_before.size() + 1, inv_list_after.size());
		assertEquals(i.getProductPojo(), inventory_service.get(i.getId()).getProductPojo());
		assertEquals(i.getQuantity(), inventory_service.get(i.getId()).getQuantity());

	}

	/* Testing adding of duplicate pojo. Should throw exception */
	@Test()
	public void testAddDuplicate() throws ApiException {

		InventoryPojo i = getInventoryPojo(products.get(2));
		inventory_service.add(i);

		try {
			inventory_service.add(i);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),
					"Inventory for this product already exists. You can edit the inventory details if you want to");
		}

	}

	/* Testing adding of an invalid pojo. Should throw exception */
	@Test()
	public void testAddWrong() throws ApiException {

		InventoryPojo i = getWrongInventoryPojo(products.get(0));

		try {
			inventory_service.add(i);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory quantity should be positive");
		}

	}

	/* Testing deletion of pojo */
	@Test()
	public void testDelete() throws ApiException {

		InventoryPojo i = getInventoryPojo(products.get(2));
		inventory_service.add(i);
		List<InventoryPojo> inv_list_before = inventory_service.getAll();
		inventory_service.delete(i.getId());
		List<InventoryPojo> inv_list_after = inventory_service.getAll();
		// Number of pojos should get decreased by one
		assertEquals(inv_list_before.size() - 1, inv_list_after.size());
		try {
			inventory_service.get(i.getId());
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory with given ID does not exist, id: " + i.getId());
		}

	}

	/* Testing get by id */
	@Test()
	public void testGetById() throws ApiException {

		InventoryPojo db_inventory_pojo = inventory_service.get(inventories.get(0).getId());
		assertEquals(inventories.get(0).getProductPojo(), db_inventory_pojo.getProductPojo());
		assertEquals(inventories.get(0).getQuantity(), db_inventory_pojo.getQuantity());

	}

	/* Testing get by id of a non-existent pojo. Should throw exception */
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

	/* Testing get by product id */
	@Test()
	public void testGetByProductId() throws ApiException {

		InventoryPojo db_inventory_pojo = inventory_service.getByProductId(inventories.get(0).getProductPojo().getId());
		assertEquals(inventories.get(0).getProductPojo(), db_inventory_pojo.getProductPojo());
		assertEquals(inventories.get(0).getQuantity(), db_inventory_pojo.getQuantity());

	}

	/* Testing get by product id of a non-existent pojo. Should throw exception */
	@Test()
	public void testGetByProductIdWrong() throws ApiException {

		InventoryPojo db_inventory_pojo = inventory_service.getByProductId(100);
		assertNull(db_inventory_pojo);

	}

	/* Testing get all inventory pojos */
	@Test()
	public void testGetAll() throws ApiException {

		List<InventoryPojo> inv_list = inventory_service.getAll();
		assertEquals(2, inv_list.size());

	}

	/* Testing updation of inventory pojo */
	@Test()
	public void testUpdate() throws ApiException {

		InventoryPojo new_ip = getNewInventoryPojo(products.get(0));
		inventory_service.update(inventories.get(0).getId(), new_ip);
		assertEquals(new_ip.getProductPojo(), inventories.get(0).getProductPojo());
		assertEquals(new_ip.getQuantity(), inventories.get(0).getQuantity());

	}

	/*
	 * Testing updation of inventory pojo with an invalid pojo. Should throw
	 * exception
	 */
	@Test()
	public void testUpdateWrong() throws ApiException {

		try {
			InventoryPojo new_ip = getWrongInventoryPojo(products.get(0));
			inventory_service.update(inventories.get(0).getId(), new_ip);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory quantity should be positive");
		}

	}

	/* Testing checkifexists */
	@Test()
	public void testCheckIfExistsId() throws ApiException {

		InventoryPojo db_inventory_pojo = inventory_service.checkIfExists(inventories.get(0).getId());
		assertEquals(inventories.get(0).getProductPojo(), db_inventory_pojo.getProductPojo());
		assertEquals(inventories.get(0).getQuantity(), db_inventory_pojo.getQuantity());
	}

	/* Testing checkifexists with an non-existent id. Should throw exception */
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

	/* Testing validate */
	@Test
	public void testValidate() throws ApiException {
		InventoryPojo ip = getInventoryPojo(products.get(2));
		inventory_service.validate(ip);
		assertTrue(ip.getQuantity() > 0);
	}

	/* Testing validate of invalid inventory pojo.Should throw exception */
	@Test
	public void testValidateWrong() throws ApiException {
		InventoryPojo ip = getWrongInventoryPojo(products.get(0));

		try {
			inventory_service.validate(ip);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Inventory quantity should be positive");
		}
	}

	/* Testing if inventory of product exists or not */
	@Test
	public void testCheckIfBarcodePresent() throws ApiException {
		try {
			inventory_service.checkIfBarcodePresent(inventories.get(0));
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),
					"Inventory for this product already exists. You can edit the inventory details if you want to");
		}
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
