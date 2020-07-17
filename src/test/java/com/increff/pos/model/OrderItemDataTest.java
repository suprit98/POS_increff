package com.increff.pos.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.pos.spring.AbstractUnitTest;

public class OrderItemDataTest extends AbstractUnitTest {
	
	@Test
	public void testOrderItemData() {
		int id = 1;
		String barcode = "abcdefgh";
		int quantity = 2;
		String name = "milk";
		double mrp = 70.00;
		int orderId = 1;
		
		OrderItemData orderitem = new OrderItemData();
		orderitem.setId(id);
		orderitem.setBarcode(barcode);
		orderitem.setMrp(mrp);
		orderitem.setName(name);
		orderitem.setOrderId(orderId);
		orderitem.setQuantity(quantity);
		
		assertEquals(id,orderitem.getId());
		assertEquals(barcode,orderitem.getBarcode());
		assertEquals(mrp,orderitem.getMrp(),0.001);
		assertEquals(name,orderitem.getName());
		assertEquals(orderId,orderitem.getOrderId());
		assertEquals(quantity,orderitem.getQuantity());
	}

}
