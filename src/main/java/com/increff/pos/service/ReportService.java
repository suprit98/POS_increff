package com.increff.pos.service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandDataList;
import com.increff.pos.model.InventoryReportList;
import com.increff.pos.model.InvoiceData;
import com.increff.pos.model.InvoiceDataList;
import com.increff.pos.model.SalesDataList;
import com.increff.pos.model.SalesFilter;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.XmlUtil;

@Service
public class ReportService {

	@Autowired
	private OrderService order_service;

	@Autowired
	private BrandService brand_service;

	@Autowired
	private InventoryService inventory_service;

	/* General class for generating Pdf Response */
	public byte[] generatePdfResponse(String type, Object... obj) throws Exception {
		if (type.contentEquals("brand")) {
			BrandDataList brand_list = generateBrandList();
			XmlUtil.generateXml(new File("brand_report.xml"), brand_list, BrandDataList.class);
			return XmlUtil.generatePDF(new File("brand_report.xml"), new StreamSource("brand_report.xsl"));
		} else if (type.contentEquals("inventory")) {
			InventoryReportList inventory_list = generateInventoryList();
			XmlUtil.generateXml(new File("inventory_report.xml"), inventory_list, InventoryReportList.class);
			return XmlUtil.generatePDF(new File("inventory_report.xml"), new StreamSource("inventory_report.xsl"));
		} else if (type.contentEquals("sales")) {
			SalesDataList sales_data_list = generateSalesList((SalesFilter) obj[0]);
			if(sales_data_list.getSales_list().isEmpty()) {
				throw new ApiException("No sales was done in this date range for this particular brand and category pair");
			}
			XmlUtil.generateXml(new File("sales_report.xml"), sales_data_list, SalesDataList.class);
			return XmlUtil.generatePDF(new File("sales_report.xml"), new StreamSource("sales_report.xsl"));
		} else {
			InvoiceDataList idl = generateInvoiceList((Integer) obj[0]);
			XmlUtil.generateXml(new File("invoice.xml"), idl, InvoiceDataList.class);
			return XmlUtil.generatePDF(new File("invoice.xml"), new StreamSource("invoice.xsl"));
		}
	}

	/* Generating brand list for brand report */
	public BrandDataList generateBrandList() throws Exception {
		List<BrandPojo> brand_pojo_list = brand_service.getAll();
		List<BrandData> brand_data_list = ConversionUtil.convert(brand_pojo_list);
		BrandDataList brand_list = new BrandDataList();
		brand_list.setBrand_list(brand_data_list);
		return brand_list;
	}

	/* Generate inventory list for inventory report */
	public InventoryReportList generateInventoryList() throws Exception {
		List<InventoryPojo> inventory_pojo_list = inventory_service.getAll();
		Map<BrandPojo, Integer> quantityPerBrandPojo = GroupByBrandCategory(inventory_pojo_list);
		return ConversionUtil.convertInventoryReportList(quantityPerBrandPojo);

	}

	/* Getting inventory per Brand Category */
	private static Map<BrandPojo, Integer> GroupByBrandCategory(List<InventoryPojo> inventory_pojo_list) {
		Map<BrandPojo, Integer> quantityPerBrandPojo = inventory_pojo_list.stream().collect(
				Collectors.groupingBy(InventoryPojo::getBrandPojo, Collectors.summingInt(InventoryPojo::getQuantity)));
		return quantityPerBrandPojo;
	}

	/*Generate sales list for sales report */
	public SalesDataList generateSalesList(SalesFilter sales_filter) throws Exception {

		List<OrderItemPojo> order_list = order_service.getAll();
		List<OrderItemPojo> filtered_orderitem_list = FilterByDate(sales_filter, order_list);
		Map<BrandPojo, Integer> quantityPerBrandCategory = getMapQuantity(sales_filter, filtered_orderitem_list);
		Map<BrandPojo, Double> revenuePerBrandCategory = getMapRevenue(sales_filter, filtered_orderitem_list);
		return ConversionUtil.convertSalesList(quantityPerBrandCategory, revenuePerBrandCategory);
	}

	/*Getting order items based on date */
	private static List<OrderItemPojo> FilterByDate(SalesFilter sales_filter, List<OrderItemPojo> orderitem_list) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime startDate = LocalDate.parse(sales_filter.getStartDate(), formatter).atStartOfDay();
		LocalDateTime endDate = LocalDate.parse(sales_filter.getEndDate(), formatter).atStartOfDay().plusDays(1);
		List<OrderItemPojo> filtered_date_list = orderitem_list.stream()
				.filter(orderitem -> orderitem.getOrderPojo().getDatetime().isAfter(startDate)
						&& orderitem.getOrderPojo().getDatetime().isBefore(endDate))
				.collect(Collectors.toList());
		return filtered_date_list;
	}

	/* Getting quantity sold based on brand category */
	private static Map<BrandPojo, Integer> getMapQuantity(SalesFilter sales_filter, List<OrderItemPojo> orderitem_list) {
		Map<BrandPojo, Integer> quantityPerBrandCategory = orderitem_list.stream()
				.filter(order_item -> Equals(order_item.getBrandPojo().getBrand(), sales_filter.getBrand())
						&& Equals(order_item.getBrandPojo().getCategory(), sales_filter.getCategory()))
				.collect(Collectors.groupingBy(OrderItemPojo::getBrandPojo,
						Collectors.summingInt(OrderItemPojo::getQuantity)));
		return quantityPerBrandCategory;
	}

	/*Getting revenue generated based on brand category */
	private static Map<BrandPojo, Double> getMapRevenue(SalesFilter sales_filter, List<OrderItemPojo> orderitem_list) {
		Map<BrandPojo, Double> revenuePerBrandCategory = orderitem_list.stream()
				.filter(order_item -> Equals(order_item.getBrandPojo().getBrand(), sales_filter.getBrand())
						&& Equals(order_item.getBrandPojo().getCategory(), sales_filter.getCategory()))
				.collect(Collectors.groupingBy(OrderItemPojo::getBrandPojo,
						Collectors.summingDouble(OrderItemPojo::getRevenue)));;
		return revenuePerBrandCategory;
	}

	/*String equals or empty (used for filtering) */
	private static Boolean Equals(String a, String b) {
		return (a.contentEquals(b) || b.isEmpty());
	}

	/* Generate invoice list for invoice PDF */
	public InvoiceDataList generateInvoiceList(int order_id) throws Exception {
		List<OrderItemPojo> lis = order_service.getOrderItems(order_id);
		InvoiceDataList idl = ConversionUtil.convertToInvoiceDataList(lis);
		idl.setOrder_id(lis.get(0).getOrderPojo().getId());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		idl.setDatetime(lis.get(0).getOrderPojo().getDatetime().format(formatter));
		double total = calculateTotal(idl);
		idl.setTotal(total);
		return idl;
	}

	/*Calculating total cost of order */
	private static double calculateTotal(InvoiceDataList idl) {
		double total = 0;
		for (InvoiceData i : idl.getInvoiceLis()) {
			total += (i.getMrp() * i.getQuantity());
		}
		return total;
	}

}
