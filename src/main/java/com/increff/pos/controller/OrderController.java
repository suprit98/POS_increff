package com.increff.pos.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
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
	public void add(@RequestBody OrderItemForm[] forms) throws ApiException {
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		for(OrderItemForm f:forms) {
			lis.add(ConversionUtil.convert(product_service,f));
		}
		OrderPojo op = new OrderPojo();
		op.setDatetime(LocalDateTime.now());
		order_service.add(lis,op);
	}


}
