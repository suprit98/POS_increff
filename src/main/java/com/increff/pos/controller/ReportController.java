package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.SalesFilter;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderService;
import com.increff.pos.util.PdfResponseUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ReportController {
	
	private static final Logger logger = Logger.getLogger(ReportController.class);
	
	@Autowired
	private BrandService brand_service;
	
	@Autowired
	private InventoryService inventory_service;
	
	@Autowired
	private OrderService order_service;
	
	@ApiOperation(value = "Gets Brand Report")
	@RequestMapping(path = "/api/report/brand", method = RequestMethod.GET)
	public void get(HttpServletResponse response) throws Exception {
		List<BrandPojo> brand_pojo_list = brand_service.getAll();
		PdfResponseUtil.generateBrandReportResponse(brand_pojo_list, response);
	}
	
	@ApiOperation(value = "Gets Inventory Report")
	@RequestMapping(path = "/api/report/inventory", method = RequestMethod.GET)
	public void getInventory(HttpServletResponse response) throws Exception {
		List<InventoryPojo> inventory_pojo_list = inventory_service.getAll();
		PdfResponseUtil.generateInventoryReportResponse(brand_service, inventory_pojo_list, response);
	}
	
	@ApiOperation(value = "Gets Sales Report")
	@RequestMapping(path = "/api/report/sales", method = RequestMethod.POST)
	public void getSales(@RequestBody SalesFilter sales_filter,HttpServletResponse response) throws Exception {
		logger.info(sales_filter.getCategory());
		logger.info(sales_filter.getStartDate());
		List<OrderPojo> order_list = order_service.getByDate(sales_filter.getStartDate(), sales_filter.getEndDate());
		List<OrderItemPojo> order_item_list = new ArrayList<OrderItemPojo>();
		for(OrderPojo order_pojo: order_list) {
			order_item_list.addAll(order_service.getOrderItems(order_pojo.getId()));
		}
		PdfResponseUtil.generateSalesReportResponse(sales_filter.getBrand(), sales_filter.getCategory(), order_item_list, response);
	}

}
