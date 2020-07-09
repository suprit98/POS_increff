package com.increff.pos.util;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.service.ApiException;

public class PdfResponseUtilTest extends AbstractUtilTest {
	
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
	public void testGenerateInvoicePdfResponse() throws Exception {
		List<OrderItemPojo> order_item_list = order_service.getAll();
		MockHttpServletResponse response = new MockHttpServletResponse();
		PdfResponseUtil.generateInvoicePdfResponse(product_service, order_item_list, response);
		assertEquals(response.getContentType(),"application/pdf");
		assertEquals(response.getContentAsByteArray().length, response.getContentLength());
	}
	
	/*@Test
	public void testGenerateBrandReportResponse() throws Exception {
		List<BrandPojo> brand_list = brand_service.getAll();
		MockHttpServletResponse response = new MockHttpServletResponse();
		PdfResponseUtil.generateBrandReportResponse(brand_list, response);
		assertEquals(response.getContentType(),"application/pdf");
		assertEquals(response.getContentAsByteArray().length, response.getContentLength());
	}*/
	
	/*@Test
	public void testGenerateInventoryReportResponse() throws Exception {
		List<InventoryPojo> inventory_list = inventory_service.getAll();
		MockHttpServletResponse response = new MockHttpServletResponse();
		PdfResponseUtil.generateInventoryReportResponse(brand_service, inventory_list, response);
		assertEquals(response.getContentType(),"application/pdf");
		assertEquals(response.getContentAsByteArray().length, response.getContentLength());
	}*/
	
	/*@Test
	public void testGenerateSalesReportResponse() throws Exception {
		String category = "dairy";
		List<OrderItemPojo> order_item_list = order_service.getAll();
		MockHttpServletResponse response = new MockHttpServletResponse();
		PdfResponseUtil.generateSalesReportResponse("", category, order_item_list, response);
		assertEquals(response.getContentType(),"application/pdf");
		assertEquals(response.getContentAsByteArray().length, response.getContentLength());
	}*/

}
