package com.increff.pos.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.pos.spring.AbstractUnitTest;

public class OrderDataTest extends AbstractUnitTest {
	
	@Test
	public void testOrderData() {
		int id = 1;
		String datetime = "2020-01-01";
		
		OrderData order = new OrderData();
		order.setDatetime(datetime);
		order.setId(id);
		
		assertEquals(id,order.getId());
		assertEquals(datetime,order.getDatetime());
	}

}
