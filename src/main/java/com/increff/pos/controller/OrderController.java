package com.increff.pos.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.InvoiceData;
import com.increff.pos.model.InvoiceDataList;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductDetailsService;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.XmlUtil;

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
	public void add(@RequestBody OrderItemForm[] forms, HttpServletResponse response) throws ApiException,Exception {
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		for(OrderItemForm f:forms) {
			lis.add(ConversionUtil.convert(product_service,f));
		}
		OrderPojo op = new OrderPojo();
		op.setDatetime(LocalDateTime.now());
		order_service.add(lis,op);
		
		List<InvoiceData> invoiceLis = ConversionUtil.convert(product_service,lis);
		InvoiceDataList idl = new InvoiceDataList();
		idl.setInvoiceLis(invoiceLis);
		idl.setOrder_id(lis.get(0).getOrderPojo().getId());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		idl.setDatetime(lis.get(0).getOrderPojo().getDatetime().format(formatter));
		XmlUtil.generateXML(idl);
		byte[] bytes = XmlUtil.generatePDF();
		
		response.setContentType("application/pdf");
	    response.setContentLength(bytes.length);
	    
	    response.getOutputStream().write(bytes);
	    response.getOutputStream().flush();
	    
	    
	}
	
	@ApiOperation(value = "Gets a OrderItem details record by id")
	@RequestMapping(path="/api/order/{id}", method = RequestMethod.GET)
	public OrderItemData get(@PathVariable int id) throws ApiException {
		OrderItemPojo p = order_service.get(id);
		return ConversionUtil.convert(p);
	}
	
	@ApiOperation(value = "Gets list of Order Items")
	@RequestMapping(path="/api/order", method = RequestMethod.GET)
	public List<OrderItemData> getAll() {
		List<OrderItemPojo> list = order_service.getAll();
		List<OrderItemData> list2 = new ArrayList<OrderItemData>();
		for(OrderItemPojo p : list) {
			list2.add(ConversionUtil.convert(p));
		}
		return list2;
	}
	
	@ApiOperation(value = "Deletes Order Item record")
	@RequestMapping(path="/api/order/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		order_service.delete(id);
	}
	
	@ApiOperation(value = "Updates a OrderItem record")
	@RequestMapping(path="/api/order/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id,@RequestBody OrderItemForm f) throws ApiException {
		OrderItemPojo p = ConversionUtil.convert(product_service,f);
		order_service.update(id, p);
	}


}
