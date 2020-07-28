package com.increff.pos.dao;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.spring.AbstractUnitTest;

public class OrderItemDaoTest extends AbstractUnitTest {

	@Autowired
	private OrderDao order_dao;

	@Autowired
	private OrderItemDao orderitem_dao;

	@Before
	public void init() throws ApiException {
		// Inserting some initial pojos
		insertPojos();
	}

	@Test
	public void testInsert() {
		List<OrderItemPojo> orderitem_list_before = orderitem_dao.selectAll();

		OrderPojo order = getOrderPojo();
		order_dao.insert(order);
		OrderItemPojo orderitem = getOrderItemPojo(products.get(0), 2);
		orderitem.setOrderPojo(order);
		orderitem_dao.insert(orderitem);

		List<OrderItemPojo> orderitem_list_after = orderitem_dao.selectAll();
		assertEquals(orderitem_list_before.size() + 1, orderitem_list_after.size());
		assertEquals(orderitem.getProductPojo(), orderitem_dao.select(orderitem.getId()).getProductPojo());
		assertEquals(orderitem.getQuantity(), orderitem_dao.select(orderitem.getId()).getQuantity());
		assertEquals(orderitem.getSellingPrice(), orderitem_dao.select(orderitem.getId()).getSellingPrice(), 0.001);
	}

	@Test
	public void testDelete() {

		OrderPojo order = getOrderPojo();
		order_dao.insert(order);
		OrderItemPojo orderitem = getOrderItemPojo(products.get(0), 2);
		orderitem.setOrderPojo(order);
		orderitem_dao.insert(orderitem);

		List<OrderItemPojo> orderitem_list_before = orderitem_dao.selectAll();
		orderitem_dao.delete(orderitem.getId());
		List<OrderItemPojo> orderitem_list_after = orderitem_dao.selectAll();
		assertEquals(orderitem_list_before.size() - 1, orderitem_list_after.size());
	}
	
	@Test
	public void testSelectAll() {
		assertEquals(2,orderitem_dao.selectAll().size());
	}
	
	@Test
	public void testSelectOrder() {
		List<OrderItemPojo> orderitem_list = orderitem_dao.selectOrder(order_id);
		assertEquals(2,orderitem_list.size());
	}

	private OrderPojo getOrderPojo() {
		OrderPojo order = new OrderPojo();
		order.setDatetime(LocalDateTime.now());
		return order;
	}

	private OrderItemPojo getOrderItemPojo(ProductDetailsPojo p, int quantity) {
		OrderItemPojo order_item = new OrderItemPojo();
		order_item.setProductPojo(p);
		order_item.setQuantity(quantity);
		order_item.setSellingPrice(p.getMrp());
		return order_item;
	}

}
