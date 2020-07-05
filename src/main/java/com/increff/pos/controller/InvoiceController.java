package com.increff.pos.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductDetailsService;
import com.increff.pos.util.PdfResponseUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InvoiceController {
	
	@Autowired
	private ProductDetailsService product_service;
	
	@Autowired
	private OrderService order_service;
	
	@ApiOperation(value = "Gets Invoice PDF by id")
	@RequestMapping(path = "/api/invoice/{id}", method = RequestMethod.GET)
	public void get(@PathVariable int id, HttpServletResponse response) throws Exception {
		List<OrderItemPojo> lis = order_service.getOrderItems(id);
		PdfResponseUtil.generateInvoicePdfResponse(product_service, lis, response);
	}

}
