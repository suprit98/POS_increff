package com.increff.pos.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.model.SalesFilter;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.util.PdfResponseUtil;

@Service
public class ReportService {
	
	@Autowired
	private OrderService order_service;
	
	public void generateResponse(SalesFilter sales_filter) {
		
		List<OrderItemPojo> order_list = order_service.getAll();
		List<OrderItemPojo> filtered_orderitem_list = generateFilteredOrderItems();
		/*List<OrderItemPojo> order_item_list = new ArrayList<OrderItemPojo>();
		for(OrderPojo order_pojo: order_list) {
			order_item_list.addAll(order_service.getOrderItems(order_pojo.getId()));
		}*/
		PdfResponseUtil.generateSalesReportResponse(sales_filter.getBrand(), sales_filter.getCategory(), order_item_list, response);
	}
	
	public List<OrderItemPojo> generateFilteredOrderItems(String start_date, String end_date, List<OrderItemPojo> orderitem_list) {
		//TODO:Stream to filter
		return null;
	}
	
	//Stream Reduce 
	//Best way for builder pattern

}
