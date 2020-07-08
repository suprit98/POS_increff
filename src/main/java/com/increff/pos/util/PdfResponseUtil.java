package com.increff.pos.util;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

		List<InventoryReportData> converted_list = ConversionUtil.createInventoryReportList(brand_service, inventory_pojo_list);
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

		List<SalesData> converted_list = ConversionUtil.createSalesList(brand, category, orderitem_list);
		SalesDataList sales_data_list = new SalesDataList();
		sales_data_list.setSales_list(converted_list);

		XmlUtil.generateXmlSalesReport(sales_data_list);
		byte[] bytes = XmlUtil.generatePDF(new File("sales_report.xml"), new StreamSource("sales_report.xsl"));

		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);

		response.getOutputStream().write(bytes);
		response.getOutputStream().flush();

	}

}
