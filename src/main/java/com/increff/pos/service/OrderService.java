package com.increff.pos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.dao.OrderDao;
import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;

@Service
public class OrderService {
	
	@Autowired
	private OrderDao order_dao;
	
	@Autowired
	private OrderItemDao order_item_dao;
	
	
	@Autowired
	private InventoryService inventory_service;
	
	@Transactional(rollbackFor = ApiException.class)
	public void add(List<OrderItemPojo> lis, OrderPojo o) throws ApiException {
		int order_id = order_dao.insert(o);
		for(OrderItemPojo p:lis) {
			p.setOrderPojo(order_dao.select(order_id));
			order_item_dao.insert(p);
			updateInventory(p);
		}
	}
	
	private void updateInventory(OrderItemPojo p) throws ApiException {
		int quantity = p.getQuantity();
		
		int quantityInInventory = inventory_service.getByProductId(p.getProductPojo().getId()).getQuantity();
		
		if(quantity>quantityInInventory) {
			throw new ApiException("Inventory does not contain this much quantity of product. Existing quantity in inventory: "+ quantityInInventory);
		}
		else {
			InventoryPojo new_ip = new InventoryPojo();
			new_ip.setQuantity(quantityInInventory-quantity);
			inventory_service.update(inventory_service.getByProductId(p.getProductPojo().getId()).getId(), new_ip);
		}
		
	}
	
	
	

}
