package com.increff.pos.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.pos.spring.AbstractUnitTest;

public class InventoryDataTest extends AbstractUnitTest {
	
	@Test
	public void testInventoryData() {
		int id = 1;
		String barcode = "abcdefgh";
		int quantity = 2;
		
		InventoryData inventory = new InventoryData();
		inventory.setBarcode(barcode);
		inventory.setId(id);
		inventory.setQuantity(quantity);
		
		assertEquals(id,inventory.getId());
		assertEquals(barcode,inventory.getBarcode());
		assertEquals(quantity,inventory.getQuantity());
	}

}
