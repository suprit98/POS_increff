package com.increff.pos.controller;

import java.util.List;

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
	public OrderData add(@RequestBody OrderItemForm[] forms, HttpServletResponse response) throws ApiException, Exception {
		List<OrderItemPojo> lis = ConversionUtil.convertOrderItemForms(product_service, forms);
		int order_id = order_service.add(lis);
		OrderData od = new OrderData();
		od.setId(order_id);
		return od;

	}

	@ApiOperation(value = "Gets a OrderItem details record by id")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.GET)
	public OrderItemData get(@PathVariable int id) throws ApiException {
		OrderItemPojo p = order_service.get(id);
		return ConversionUtil.convert(p);
	}

	@ApiOperation(value = "Gets list of Order Items")
	@RequestMapping(path = "/api/order", method = RequestMethod.GET)
	public List<OrderItemData> getAll() {
		List<OrderItemPojo> list = order_service.getAll();
		List<OrderItemData> list2 = ConversionUtil.convertOrderItemList(list);
		return list2;
	}

	@ApiOperation(value = "Deletes Order Item record")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		order_service.delete(id);
	}

	@ApiOperation(value = "Updates a OrderItem record")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody OrderItemForm f) throws ApiException {
		OrderItemPojo p = ConversionUtil.convert(product_service, f);
		order_service.update(id, p);
	}

}
