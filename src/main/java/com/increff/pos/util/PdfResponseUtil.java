package com.increff.pos.util;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;

import com.increff.pos.model.BrandDataList;
import com.increff.pos.model.InventoryReportList;
import com.increff.pos.model.InvoiceData;
import com.increff.pos.model.InvoiceDataList;
import com.increff.pos.model.SalesDataList;
import com.increff.pos.pojo.OrderItemPojo;
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

	public static byte[] generateBrandReportResponse(BrandDataList brand_list) throws Exception {

		XmlUtil.generateXml(new File("brand_report.xml"), brand_list, BrandDataList.class);
		byte[] bytes = XmlUtil.generatePDF(new File("brand_report.xml"), new StreamSource("brand_report.xsl"));
		return bytes;

	}

	public static byte[] generateInventoryReportResponse(InventoryReportList inventory_list) throws Exception {

		XmlUtil.generateXml(new File("inventory_report.xml"), inventory_list, InventoryReportList.class);
		byte[] bytes = XmlUtil.generatePDF(new File("inventory_report.xml"), new StreamSource("inventory_report.xsl"));
		return bytes;

	}

	public static byte[] generateSalesResponse(SalesDataList sales_data_list) throws Exception {
		XmlUtil.generateXml(new File("sales_report.xml"), sales_data_list, SalesDataList.class);
		byte[] bytes = XmlUtil.generatePDF(new File("sales_report.xml"), new StreamSource("sales_report.xsl"));
		return bytes;
	}

}
