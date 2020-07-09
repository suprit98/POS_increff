package com.increff.pos.util;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;

import com.increff.pos.model.InvoiceData;
import com.increff.pos.model.InvoiceDataList;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.service.ApiException;

public class XmlUtilTest extends AbstractUtilTest{
	
	@Before
	public void init() throws ApiException {
		BrandPojo brand = getBrandPojo();
		BrandPojo brand2 = getAnotherBrandPojo();
		ProductDetailsPojo product1 = getProductDetailsPojo(brand);
		ProductDetailsPojo product2 = getAnotherProductDetailsPojo(brand2);
		getInventoryPojo(product1);
		getInventoryPojo(product2);
		getOrderItemPojo();
	}
	
	@Test
	public void testGenerateXmlInvoice() throws Exception {
		List<OrderItemPojo> order_item_list = order_service.getAll();
		List<InvoiceData> invoice_list = ConversionUtil.convert(product_service,order_item_list);
		InvoiceDataList idl = new InvoiceDataList();
		idl.setInvoiceLis(invoice_list);
		idl.setOrder_id(order_item_list.get(0).getOrderPojo().getId());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		idl.setDatetime(order_item_list.get(0).getOrderPojo().getDatetime().format(formatter));
		
		XmlUtil.generateXmlInvoice(idl);
	}
	
	/*@Test
	public void testGenerateXmlBrandReport() throws Exception {
		List<BrandPojo> brand_list = brand_service.getAll();
		List<BrandData> brand_data_list = ConversionUtil.convert(brand_list);
		BrandDataList bdl = new BrandDataList();
		bdl.setBrand_list(brand_data_list);
		XmlUtil.generateXmlBrandReport(bdl);
	}*/
	
	/*@Test
	public void testGenerateXmlInventoryReport() throws Exception {
		List<InventoryPojo> inventory_list = inventory_service.getAll();
		List<InventoryReportData> inventory_report_list = ConversionUtil.createInventoryReportList(brand_service, inventory_list);
		InventoryReportList idl = new InventoryReportList();
		idl.setInventory_list(inventory_report_list);
		XmlUtil.generateXmlInventoryReport(idl);
	}*/
	
	/*@Test
	public void testGenerateSalesReport() throws JAXBException {
		String category = "dairy";
		List<OrderItemPojo> order_item_list = order_service.getAll();
		List<SalesData> sales_list = ConversionUtil.createSalesList("", category, order_item_list);
		SalesDataList sales_data_list = new SalesDataList();
		sales_data_list.setSales_list(sales_list);
		XmlUtil.generateXmlSalesReport(sales_data_list);
	}*/
	
	@Test
	public void testGeneratePdf() throws Exception {
		List<OrderItemPojo> order_item_list = order_service.getAll();
		List<InvoiceData> invoice_list = ConversionUtil.convert(product_service,order_item_list);
		InvoiceDataList idl = new InvoiceDataList();
		idl.setInvoiceLis(invoice_list);
		idl.setOrder_id(order_item_list.get(0).getOrderPojo().getId());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		idl.setDatetime(order_item_list.get(0).getOrderPojo().getDatetime().format(formatter));
		
		XmlUtil.generateXmlInvoice(idl);
		XmlUtil.generatePDF(new File("brand_report.xml"), new StreamSource("brand_report.xsl"));
	}

}
