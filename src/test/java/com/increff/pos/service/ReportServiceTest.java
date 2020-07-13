package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandDataList;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.InventoryReportList;
import com.increff.pos.model.InvoiceDataList;
import com.increff.pos.model.SalesDataList;
import com.increff.pos.model.SalesFilter;
import com.increff.pos.spring.AbstractUnitTest;

public class ReportServiceTest extends AbstractUnitTest{
	
	@Autowired
	private ReportService report_service;
	
	@Before
	public void init() throws ApiException {
		insertPojos();
	}
	
	@Test
	public void testGenerateBrandList() throws Exception {
		BrandDataList brand_list = report_service.generateBrandList();
		assertEquals(2,brand_list.getBrand_list().size());
		
		int i=0;
		for(BrandData brand:brand_list.getBrand_list()) {
			assertEquals("brand",brand.getBrand());
			assertEquals("category"+i,brand.getCategory());
			i++;
		}
	}
	
	@Test
	public void testGenerateInventoryList() throws Exception {
		InventoryReportList inv_list = report_service.generateInventoryList();
		assertEquals(2,inv_list.getInventory_list().size());
		
		for(InventoryReportData inv: inv_list.getInventory_list()) {
			assertEquals(18,inv.getQuantity());
		}
	}
	
	@Test
	public void testGenerateSalesList() throws Exception {
		SalesFilter sales_filter = new SalesFilter();
		sales_filter.setBrand("");
		sales_filter.setCategory("category"+0);
		sales_filter.setStartDate("2020-01-01");
		sales_filter.setEndDate("2020-12-31");
		SalesDataList sales_list = report_service.generateSalesList(sales_filter);
		assertEquals(1,sales_list.getSales_list().size());
		assertEquals(2,sales_list.getSales_list().get(0).getQuantity());
		assertEquals("category"+0,sales_list.getSales_list().get(0).getCategory());
		assertEquals(2*50,sales_list.getSales_list().get(0).getRevenue(),0.001);
		
		sales_filter.setBrand("brand");
		sales_filter.setCategory("");
		sales_list = report_service.generateSalesList(sales_filter);
		assertEquals(2,sales_list.getSales_list().size());
		
		sales_filter.setBrand("brand");
		sales_filter.setCategory("category"+1);
		sales_list = report_service.generateSalesList(sales_filter);
		assertEquals(1,sales_list.getSales_list().size());
		assertEquals(2,sales_list.getSales_list().get(0).getQuantity());
		assertEquals("category"+1,sales_list.getSales_list().get(0).getCategory());
		assertEquals(2*50,sales_list.getSales_list().get(0).getRevenue(),0.001);
		
		sales_filter.setBrand("");
		sales_filter.setCategory("");
		sales_list = report_service.generateSalesList(sales_filter);
		assertEquals(2,sales_list.getSales_list().size());
	}
	
	@Test
	public void testGenerateInvoiceList() throws Exception {
		InvoiceDataList idl = report_service.generateInvoiceList(order_id);
		assertEquals(2,idl.getInvoiceLis().size());
		assertEquals(200,idl.getTotal(),0.001);
	}
	
	@Test
	public void testPdfResponse() throws Exception {
		report_service.generatePdfResponse("brand");
		
		report_service.generatePdfResponse("inventory");
		
		SalesFilter sales_filter = new SalesFilter();
		sales_filter.setBrand("");
		sales_filter.setCategory("category"+0);
		sales_filter.setStartDate("2020-01-01");
		sales_filter.setEndDate("2020-12-31");
		report_service.generatePdfResponse("sales", sales_filter);
		
		report_service.generatePdfResponse("invoice", order_id);
		
	}
}
