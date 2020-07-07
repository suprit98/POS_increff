package com.increff.pos.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
	public int add(List<OrderItemPojo> lis) throws ApiException {
		OrderPojo op = new OrderPojo();
		op.setDatetime(LocalDateTime.now());
		int order_id = order_dao.insert(op);
		for(OrderItemPojo p:lis) {
			p.setOrderPojo(order_dao.select(order_id));
			validate(p);
			order_item_dao.insert(p);
			updateInventory(p);
		}
		return order_id;
	}
	


	@Transactional
	public OrderItemPojo get(int id) throws ApiException {
		OrderItemPojo p = checkIfExists(id);
		return p;
	}
	
	@Transactional
	public List<OrderItemPojo> getOrderItems(int order_id) throws ApiException {
		List<OrderItemPojo> lis = order_item_dao.selectOrder(order_id);
		return lis;
	}
	
	@Transactional
	public List<OrderItemPojo> getAll() {
		return order_item_dao.selectAll();
	}
	
	@Transactional
	public void delete(int id) {
		int order_id = order_item_dao.select(id).getOrderPojo().getId();
		order_item_dao.delete(id);
		List<OrderItemPojo> lis = order_item_dao.selectOrder(order_id);
		if(lis.size() == 0) {
			order_dao.delete(order_id);
		}
	}
	
	@Transactional
	public List<OrderPojo> getByDate(String startdate, String enddate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime startDate = LocalDate.parse(startdate, formatter).atStartOfDay();
		LocalDateTime endDate = LocalDate.parse(enddate, formatter).atStartOfDay();
		return order_dao.selectByDate(startDate, endDate);
	}
	
	@Transactional(rollbackFor = ApiException.class)
	public void update(int id, OrderItemPojo p) throws ApiException {
		
		OrderItemPojo ex = checkIfExists(id);
		int old_qty = ex.getQuantity();
		ex.setQuantity(p.getQuantity());
		ex.setProductPojo(p.getProductPojo());
		ex.setSellingPrice(p.getSellingPrice());
		order_item_dao.update(p);
		updateInventory(old_qty,p);
	}
	
	@Transactional(rollbackFor = ApiException.class)
	public void addOrderItem(int order_id, OrderItemPojo p) throws ApiException {
		
		p.setOrderPojo(order_dao.select(order_id));
		order_item_dao.insert(p);
		updateInventory(p);
	}
	
	@Transactional(rollbackFor = ApiException.class)
	public OrderItemPojo checkIfExists(int id) throws ApiException {
		OrderItemPojo p = order_item_dao.select(id);
		if(p == null) {
			throw new ApiException("OrderItem with given ID does not exist, id: " + id);
		}
		return p;
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
	
	private void updateInventory(int old_qty, OrderItemPojo p) throws ApiException {
		int quantity = p.getQuantity();
		
		int quantityInInventory = inventory_service.getByProductId(p.getProductPojo().getId()).getQuantity() + old_qty;
		
		if(quantity>quantityInInventory) {
			throw new ApiException("Inventory does not contain this much quantity of product. Existing quantity in inventory: "+ quantityInInventory);
		}
		else {
			InventoryPojo new_ip = new InventoryPojo();
			new_ip.setQuantity(quantityInInventory-quantity);
			inventory_service.update(inventory_service.getByProductId(p.getProductPojo().getId()).getId(), new_ip);
		}
		
	}
	
	private void validate(OrderItemPojo p) throws ApiException {
		if(p.getQuantity() <=0 ) {
			throw new ApiException("Quantity must be positive");
		}
		
	}
	

}
