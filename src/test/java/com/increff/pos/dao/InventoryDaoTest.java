package com.increff.pos.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;

public class InventoryDaoTest extends AbstractUnitTest {
	
	@Autowired
	private InventoryDao inventory_dao;
	
	@Before
	public void init() throws ApiException {
		// Inserting some initial pojos
		insertPojos();
	}
	
	@Test
	public void testInsert() {
		InventoryPojo inventory = getInventoryPojo(products.get(2));
		List<InventoryPojo> inventory_list_before = inventory_dao.selectAll();
		inventory_dao.insert(inventory);
		List<InventoryPojo> inventory_list_after = inventory_dao.selectAll();
		
		assertEquals(inventory_list_before.size()+1,inventory_list_after.size());
		assertEquals(inventory.getProductPojo(),inventory_dao.select(inventory.getId()).getProductPojo());
		assertEquals(inventory.getQuantity(),inventory_dao.select(inventory.getId()).getQuantity());
	}
	
	@Test
	public void testDelete() {
		InventoryPojo inventory = getInventoryPojo(products.get(2));
		
		inventory_dao.insert(inventory);
		List<InventoryPojo> inventory_list_before = inventory_dao.selectAll();
		inventory_dao.delete(inventory.getId());
		List<InventoryPojo> inventory_list_after = inventory_dao.selectAll();
		
		assertEquals(inventory_list_before.size()-1,inventory_list_after.size());
	}
	
	@Test
	public void testSelectByProduct() {
		List<InventoryPojo> inventory_list = inventory_dao.selectByProduct(products.get(0));
		
		assertEquals(1,inventory_list.size());
		assertEquals(inventory_list.get(0).getProductPojo(),products.get(0));
	}
	
	@Test
	public void testSelectAll() {
		List<InventoryPojo> inventory_list = inventory_dao.selectAll();
		
		assertEquals(2,inventory_list.size());
	}
	
	private InventoryPojo getInventoryPojo(ProductDetailsPojo p) {
		InventoryPojo i = new InventoryPojo();
		i.setProductPojo(p);
		i.setQuantity(20);
		return i;
	}

}
