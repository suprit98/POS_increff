package com.increff.pos.util;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandDataList;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.InventoryReportList;
import com.increff.pos.model.InvoiceData;
import com.increff.pos.model.InvoiceDataList;
import com.increff.pos.model.SalesData;
import com.increff.pos.model.SalesDataList;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductDetailsService;

public class PdfResponseUtil {

	public static void generateInvoicePdfResponse(ProductDetailsService product_service, List<OrderItemPojo> lis,
			HttpServletResponse response) throws Exception {

		List<InvoiceData> invoiceLis = ConversionUtil.convert(product_service, lis);
		InvoiceDataList idl = new InvoiceDataList();
		idl.setInvoiceLis(invoiceLis);
		idl.setOrder_id(lis.get(0).getOrderPojo().getId());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		idl.setDatetime(lis.get(0).getOrderPojo().getDatetime().format(formatter));
		XmlUtil.generateXmlInvoice(idl);
		byte[] bytes = XmlUtil.generatePDF(new File("invoice.xml"), new StreamSource("invoice.xsl"));

		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);

		response.getOutputStream().write(bytes);
		response.getOutputStream().flush();

	}

	public static void generateBrandReportResponse(List<BrandPojo> brand_pojo_list, HttpServletResponse response)
			throws Exception {

		List<BrandData> converted_list = ConversionUtil.convert(brand_pojo_list);
		BrandDataList brand_data_list = new BrandDataList();
		brand_data_list.setBrand_list(converted_list);

		XmlUtil.generateXmlBrandReport(brand_data_list);
		byte[] bytes = XmlUtil.generatePDF(new File("brand_report.xml"), new StreamSource("brand_report.xsl"));

		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);

		response.getOutputStream().write(bytes);
		response.getOutputStream().flush();

	}

	public static void generateInventoryReportResponse(BrandService brand_service,
			List<InventoryPojo> inventory_pojo_list, HttpServletResponse response) throws Exception {

		List<InventoryReportData> converted_list = createInventoryReportList(brand_service, inventory_pojo_list);
		InventoryReportList inventory_data_list = new InventoryReportList();
		inventory_data_list.setInventory_list(converted_list);

		XmlUtil.generateXmlInventoryReport(inventory_data_list);
		byte[] bytes = XmlUtil.generatePDF(new File("inventory_report.xml"), new StreamSource("inventory_report.xsl"));

		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);

		response.getOutputStream().write(bytes);
		response.getOutputStream().flush();

	}

	public static void generateSalesReportResponse(String brand, String category, List<OrderItemPojo> orderitem_list,
			HttpServletResponse response) throws Exception {

		List<SalesData> converted_list = createSalesList(brand, category, orderitem_list);
		SalesDataList sales_data_list = new SalesDataList();
		sales_data_list.setSales_list(converted_list);

		XmlUtil.generateXmlSalesReport(sales_data_list);
		byte[] bytes = XmlUtil.generatePDF(new File("sales_report.xml"), new StreamSource("sales_report.xsl"));

		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);

		response.getOutputStream().write(bytes);
		response.getOutputStream().flush();

	}

	private static List<InventoryReportData> createInventoryReportList(BrandService brand_service,
			List<InventoryPojo> inventory_list) throws ApiException {
		Map<Integer, Integer> map_brandid_quantity = new HashMap<Integer, Integer>();
		for (InventoryPojo p : inventory_list) {
			int brand_id = p.getProductPojo().getBrandPojo().getId();
			if (map_brandid_quantity.get(brand_id) != null) {
				int qty = map_brandid_quantity.get(brand_id);
				qty += p.getQuantity();
				map_brandid_quantity.put(brand_id, qty);
			} else {
				map_brandid_quantity.put(brand_id, p.getQuantity());
			}
		}
		List<InventoryReportData> inventory_report_list = new ArrayList<InventoryReportData>();
		for (Integer brand_id : map_brandid_quantity.keySet()) {
			InventoryReportData d = new InventoryReportData();
			d.setBrand(brand_service.get(brand_id).getBrand());
			d.setCategory(brand_service.get(brand_id).getCategory());
			d.setQuantity(map_brandid_quantity.get(brand_id));
			inventory_report_list.add(d);
		}
		return inventory_report_list;
	}

	private static List<SalesData> createSalesList(String brand, String category, List<OrderItemPojo> orderitem_list) {
		if (brand.isEmpty() && !category.isEmpty()) {
			int qty = 0;
			double revenue = 0;

			for (OrderItemPojo order_item_pojo : orderitem_list) {
				if (order_item_pojo.getProductPojo().getBrandPojo().getCategory().contentEquals(category)) {
					qty += order_item_pojo.getQuantity();
					revenue += (order_item_pojo.getQuantity() * order_item_pojo.getSellingPrice());
				}
			}
			List<SalesData> sales_list = new ArrayList<SalesData>();
			SalesData s = new SalesData();
			s.setCategory(category);
			s.setQuantity(qty);
			s.setRevenue(revenue);
			sales_list.add(s);
			return sales_list;
		} else if (!brand.isEmpty() && category.isEmpty()) {
			Map<String, Integer> map_quantity = new HashMap<String, Integer>();
			Map<String, Double> map_revenue = new HashMap<String, Double>();

			for (OrderItemPojo order_item_pojo : orderitem_list) {
				if (order_item_pojo.getProductPojo().getBrandPojo().getBrand().contentEquals(brand)) {
					if (map_quantity.get(order_item_pojo.getProductPojo().getBrandPojo().getCategory()) == null) {
						map_quantity.put(order_item_pojo.getProductPojo().getBrandPojo().getCategory(),
								order_item_pojo.getQuantity());
						map_revenue.put(order_item_pojo.getProductPojo().getBrandPojo().getCategory(),
								(order_item_pojo.getQuantity() * order_item_pojo.getSellingPrice()));
					} else {
						int qty = map_quantity.get(order_item_pojo.getProductPojo().getBrandPojo().getCategory());
						double revenue = map_quantity
								.get(order_item_pojo.getProductPojo().getBrandPojo().getCategory());
						qty += order_item_pojo.getQuantity();
						revenue += (order_item_pojo.getQuantity() * order_item_pojo.getSellingPrice());
						map_quantity.put(order_item_pojo.getProductPojo().getBrandPojo().getCategory(), qty);
						map_revenue.put(order_item_pojo.getProductPojo().getBrandPojo().getCategory(), revenue);
					}
				}
			}
			List<SalesData> sales_list = new ArrayList<SalesData>();
			for (String category_it : map_quantity.keySet()) {
				SalesData s = new SalesData();
				s.setCategory(category_it);
				s.setQuantity(map_quantity.get(category_it));
				s.setRevenue(map_revenue.get(category_it));
				sales_list.add(s);
			}
			return sales_list;
		} else if (!brand.isEmpty() && !category.isEmpty()) {
			int qty = 0;
			double revenue = 0;

			for (OrderItemPojo order_item_pojo : orderitem_list) {
				if (order_item_pojo.getProductPojo().getBrandPojo().getCategory().contentEquals(category)
						&& order_item_pojo.getProductPojo().getBrandPojo().getBrand().contentEquals(brand)) {
					qty += order_item_pojo.getQuantity();
					revenue += (order_item_pojo.getQuantity() * order_item_pojo.getSellingPrice());
				}
			}
			List<SalesData> sales_list = new ArrayList<SalesData>();
			SalesData s = new SalesData();
			s.setCategory(category);
			s.setQuantity(qty);
			s.setRevenue(revenue);
			sales_list.add(s);
			return sales_list;
		} else {

			Map<String, Integer> map_quantity = new HashMap<String, Integer>();
			Map<String, Double> map_revenue = new HashMap<String, Double>();

			for (OrderItemPojo order_item_pojo : orderitem_list) {

				if (map_quantity.get(order_item_pojo.getProductPojo().getBrandPojo().getCategory()) == null) {
					map_quantity.put(order_item_pojo.getProductPojo().getBrandPojo().getCategory(),
							order_item_pojo.getQuantity());
					map_revenue.put(order_item_pojo.getProductPojo().getBrandPojo().getCategory(),
							(order_item_pojo.getQuantity() * order_item_pojo.getSellingPrice()));
				} else {
					int qty = map_quantity.get(order_item_pojo.getProductPojo().getBrandPojo().getCategory());
					double revenue = map_revenue.get(order_item_pojo.getProductPojo().getBrandPojo().getCategory());
					qty += order_item_pojo.getQuantity();
					revenue += (order_item_pojo.getQuantity() * order_item_pojo.getSellingPrice());
					map_quantity.put(order_item_pojo.getProductPojo().getBrandPojo().getCategory(), qty);
					map_revenue.put(order_item_pojo.getProductPojo().getBrandPojo().getCategory(), revenue);
				}

			}
			List<SalesData> sales_list = new ArrayList<SalesData>();
			for (String category_it : map_quantity.keySet()) {
				SalesData s = new SalesData();
				s.setCategory(category_it);
				s.setQuantity(map_quantity.get(category_it));
				s.setRevenue(map_revenue.get(category_it));
				sales_list.add(s);
			}
			return sales_list;

		}

	}
}
