package com.increff.pos.util;

import java.io.File;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandDataList;
import com.increff.pos.model.InventoryReportList;
import com.increff.pos.model.InvoiceDataList;
import com.increff.pos.model.SalesDataList;
import com.increff.pos.model.SalesFilter;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.ReportService;
import com.increff.pos.spring.AbstractUnitTest;

public class XmlUtilTest extends AbstractUnitTest{
	
	@Autowired
	private ReportService report_service;
	
	@Before
	public void init() throws ApiException {
		insertPojos();
	}
	
	/* Testing Xml Generation of Brand Report */
	@Test
	public void testXmlBrand() throws Exception {
		BrandDataList brand_list = report_service.generateBrandList();
		XmlUtil.generateXml(new File("brand_report.xml"), brand_list, BrandDataList.class);
	}
	
	/* Testing Xml Generation of Inventory Report */
	@Test
	public void testXmlInventory() throws Exception {
		InventoryReportList inventory_list = report_service.generateInventoryList();
		XmlUtil.generateXml(new File("inventory_report.xml"), inventory_list, InventoryReportList.class);
	}
	
	/* Testing Xml Generation of Sales Report */
	@Test
	public void testXmlSales() throws Exception {
		SalesFilter sales_filter = new SalesFilter();
		sales_filter.setBrand("");
		sales_filter.setCategory("category"+0);
		sales_filter.setStartDate("2020-01-01");
		sales_filter.setEndDate("2020-12-31");
		SalesDataList sales_data_list = report_service.generateSalesList(sales_filter);
		XmlUtil.generateXml(new File("sales_report.xml"), sales_data_list, SalesDataList.class);
	}
	
	/* Testing Xml Generation of Invoice */
	@Test
	public void testXmlInvoice() throws Exception {
		List<OrderItemPojo> orderitem_list = order_service.getAll();
		int order_id = orderitem_list.get(0).getOrderPojo().getId();
		InvoiceDataList idl = report_service.generateInvoiceList(order_id);
		XmlUtil.generateXml(new File("invoice.xml"), idl, InvoiceDataList.class);
	}
	
	/* Testing Pdf Generation */
	@Test
	public void testPdf() throws Exception {
		XmlUtil.generatePDF(new File("brand_report.xml"), new StreamSource("brand_report.xsl"));
	}
}
