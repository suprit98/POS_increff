package com.increff.pos.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductDetailsService;
import com.increff.pos.util.ConversionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderController {

	@Autowired
	private OrderService order_service;

	@Autowired
	private ProductDetailsService product_service;

	@ApiOperation(value = "Adds Order Details")
	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
	public OrderData add(@RequestBody OrderItemForm[] forms, HttpServletResponse response)
			throws ApiException, Exception {
		Map<String,ProductDetailsPojo> barcode_product = product_service.getAllProductPojosByBarcode();
		List<OrderItemPojo> lis = ConversionUtil.convertOrderItemForms(barcode_product, forms);
		int order_id = order_service.add(lis);
		return ConversionUtil.convertOrderPojo(order_service.getOrder(order_id));

	}

	@ApiOperation(value = "Adds an OrderItem to an existing order")
	@RequestMapping(path = "/api/order_item/{order_id}", method = RequestMethod.POST)
	public void addOrderItem(@PathVariable int order_id, @RequestBody OrderItemForm f) throws ApiException {
		ProductDetailsPojo product_pojo = product_service.get(f.getBarcode());
		OrderItemPojo p = ConversionUtil.convert(product_pojo, f);
		order_service.addOrderItem(order_id, p);
	}

	@ApiOperation(value = "Gets a OrderItem details record by id")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.GET)
	public OrderItemData get(@PathVariable int id) throws ApiException {
		OrderItemPojo p = order_service.get(id);
		return ConversionUtil.convert(p);
	}
	
	@ApiOperation(value = "Deletes an Order by id")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.DELETE)
	public void deleteOrder(@PathVariable int id) throws ApiException {
		order_service.deleteOrder(id);
	}

	@ApiOperation(value = "Gets list of Order Items")
	@RequestMapping(path = "/api/order", method = RequestMethod.GET)
	public List<OrderItemData> getAll() {
		List<OrderItemPojo> list = order_service.getAll();
		List<OrderItemData> list2 = ConversionUtil.convertOrderItemList(list);
		return list2;
	}

	@ApiOperation(value = "Gets list of Orders")
	@RequestMapping(path = "/api/all_orders", method = RequestMethod.GET)
	public List<OrderData> getAllOrders() {
		List<OrderPojo> orders_list = order_service.getAllOrders();
		List<OrderData> orders_data_list = ConversionUtil.convertOrderList(orders_list);
		return orders_data_list;
	}

	@ApiOperation(value = "Gets list of Order Items of a particular order")
	@RequestMapping(path = "/api/all_orders/{id}", method = RequestMethod.GET)
	public List<OrderItemData> getOrderItemsbyOrderId(@PathVariable int id) throws ApiException {
		List<OrderItemPojo> list = order_service.getOrderItems(id);
		List<OrderItemData> list2 = ConversionUtil.convertOrderItemList(list);
		return list2;
	}

	@ApiOperation(value = "Deletes Order Item record")
	@RequestMapping(path = "/api/order_item/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		order_service.delete(id);
	}

	@ApiOperation(value = "Updates a OrderItem record")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody OrderItemForm f) throws ApiException {
		ProductDetailsPojo product_pojo = product_service.get(f.getBarcode());
		OrderItemPojo p = ConversionUtil.convert(product_pojo, f);
		order_service.update(id, p);
	}

}
