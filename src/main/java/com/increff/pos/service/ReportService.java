package com.increff.pos.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandDataList;
import com.increff.pos.model.InventoryReportList;
import com.increff.pos.model.SalesDataList;
import com.increff.pos.model.SalesFilter;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.PdfResponseUtil;

@Service
public class ReportService {

	@Autowired
	private OrderService order_service;
	
	@Autowired
	private BrandService brand_service;

	@Autowired
	private InventoryService inventory_service;

	public byte[] generateBrandList() throws Exception {
		List<BrandPojo> brand_pojo_list = brand_service.getAll();
		List<BrandData> brand_data_list = ConversionUtil.convert(brand_pojo_list);
		BrandDataList brand_list = new BrandDataList();
		brand_list.setBrand_list(brand_data_list);
		byte[] bytes = PdfResponseUtil.generateBrandReportResponse(brand_list);
		return bytes;
	}

	public byte[] generateInventoryList() throws Exception {
		List<InventoryPojo> inventory_pojo_list = inventory_service.getAll();
		Map<BrandPojo, Integer> quantityPerBrandPojo = GroupByBrandCategory(inventory_pojo_list);
		InventoryReportList inventory_list = ConversionUtil.convertInventoryReportList(quantityPerBrandPojo);
		byte[] bytes = PdfResponseUtil.generateInventoryReportResponse(inventory_list);
		return bytes;
	}

	public Map<BrandPojo, Integer> GroupByBrandCategory(List<InventoryPojo> inventory_pojo_list) {
		Map<BrandPojo, Integer> quantityPerBrandPojo = inventory_pojo_list.stream()
				.collect(Collectors.groupingBy(InventoryPojo::getBrandPojo, Collectors.summingInt(InventoryPojo::getQuantity)));
		return quantityPerBrandPojo;
	}

	public byte[] generateSalesList(SalesFilter sales_filter) throws Exception {

		List<OrderItemPojo> order_list = order_service.getAll();
		List<OrderItemPojo> filtered_orderitem_list = FilterByDate(sales_filter, order_list);
		Map<BrandPojo, Integer> quantityPerBrandCategory = getMapQuantity(sales_filter, filtered_orderitem_list);
		Map<BrandPojo, Double> revenuePerBrandCategory = getMapRevenue(sales_filter, filtered_orderitem_list);
		SalesDataList sales_data_list = ConversionUtil.convertSalesList(quantityPerBrandCategory,
				revenuePerBrandCategory);
		byte[] bytes = PdfResponseUtil.generateSalesResponse(sales_data_list);
		return bytes;
	}

	public List<OrderItemPojo> FilterByDate(SalesFilter sales_filter, List<OrderItemPojo> orderitem_list) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime startDate = LocalDate.parse(sales_filter.getStartDate(), formatter).atStartOfDay();
		LocalDateTime endDate = LocalDate.parse(sales_filter.getEndDate(), formatter).atStartOfDay();
		List<OrderItemPojo> filtered_date_list = orderitem_list.stream()
				.filter(orderitem -> orderitem.getOrderPojo().getDatetime().isAfter(startDate)
						&& orderitem.getOrderPojo().getDatetime().isBefore(endDate))
				.collect(Collectors.toList());
		return filtered_date_list;
	}

	public Map<BrandPojo, Integer> getMapQuantity(SalesFilter sales_filter, List<OrderItemPojo> orderitem_list) {
		Map<BrandPojo, Integer> quantityPerBrandCategory;
		if (sales_filter.getBrand().isEmpty() && !sales_filter.getCategory().isEmpty()) {
			quantityPerBrandCategory = orderitem_list.stream().filter(
					order_item -> order_item.getBrandPojo().getCategory().contentEquals(sales_filter.getCategory()))
					.collect(Collectors.groupingBy(OrderItemPojo::getBrandPojo,
							Collectors.summingInt(OrderItemPojo::getQuantity)));
		} else if (!sales_filter.getBrand().isEmpty() && sales_filter.getCategory().isEmpty()) {
			quantityPerBrandCategory = orderitem_list.stream()
					.filter(order_item -> order_item.getBrandPojo().getBrand().contentEquals(sales_filter.getBrand()))
					.collect(Collectors.groupingBy(OrderItemPojo::getBrandPojo,
							Collectors.summingInt(OrderItemPojo::getQuantity)));
		} else if (!sales_filter.getBrand().isEmpty() && !sales_filter.getCategory().isEmpty()) {
			quantityPerBrandCategory = orderitem_list.stream()
					.filter(order_item -> order_item.getBrandPojo().getBrand().contentEquals(sales_filter.getBrand())
							&& order_item.getBrandPojo().getCategory().contentEquals(sales_filter.getCategory()))
					.collect(Collectors.groupingBy(OrderItemPojo::getBrandPojo,
							Collectors.summingInt(OrderItemPojo::getQuantity)));
		} else {
			quantityPerBrandCategory = orderitem_list.stream().collect(Collectors
					.groupingBy(OrderItemPojo::getBrandPojo, Collectors.summingInt(OrderItemPojo::getQuantity)));
		}
		return quantityPerBrandCategory;
	}

	public Map<BrandPojo, Double> getMapRevenue(SalesFilter sales_filter, List<OrderItemPojo> orderitem_list) {
		Map<BrandPojo, Double> revenuePerBrandCategory;
		if (sales_filter.getBrand().isEmpty() && !sales_filter.getCategory().isEmpty()) {
			revenuePerBrandCategory = orderitem_list.stream().filter(
					order_item -> order_item.getBrandPojo().getCategory().contentEquals(sales_filter.getCategory()))
					.collect(Collectors.groupingBy(OrderItemPojo::getBrandPojo,
							Collectors.summingDouble(OrderItemPojo::getRevenue)));
		} else if (!sales_filter.getBrand().isEmpty() && sales_filter.getCategory().isEmpty()) {
			revenuePerBrandCategory = orderitem_list.stream()
					.filter(order_item -> order_item.getBrandPojo().getBrand().contentEquals(sales_filter.getBrand()))
					.collect(Collectors.groupingBy(OrderItemPojo::getBrandPojo,
							Collectors.summingDouble(OrderItemPojo::getRevenue)));
		} else if (!sales_filter.getBrand().isEmpty() && !sales_filter.getCategory().isEmpty()) {
			revenuePerBrandCategory = orderitem_list.stream()
					.filter(order_item -> order_item.getBrandPojo().getBrand().contentEquals(sales_filter.getBrand())
							&& order_item.getBrandPojo().getCategory().contentEquals(sales_filter.getCategory()))
					.collect(Collectors.groupingBy(OrderItemPojo::getBrandPojo,
							Collectors.summingDouble(OrderItemPojo::getRevenue)));
		} else {
			revenuePerBrandCategory = orderitem_list.stream().collect(Collectors.groupingBy(OrderItemPojo::getBrandPojo,
					Collectors.summingDouble(OrderItemPojo::getRevenue)));
		}
		return revenuePerBrandCategory;
	}

}
