package com.increff.pos.util;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InvoiceData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductDetailsData;
import com.increff.pos.model.ProductDetailsForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.service.ApiException;

public class ConversionUtilTest extends AbstractUtilTest {


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
	public void testConvertBrandFormToPojo() {

		BrandForm form = new BrandForm();
		form.setBrand("amul");
		form.setCategory("milk");
		BrandPojo brand_pojo = ConversionUtil.convert(form);
		assertEquals(form.getBrand(), brand_pojo.getBrand());
		assertEquals(form.getCategory(), brand_pojo.getCategory());
	}
	
	@Test
	public void testConvertBrandPojoToData() {

		BrandPojo pojo = new BrandPojo();
		pojo.setId(1);
		pojo.setBrand("amul");
		pojo.setCategory("dairy");
		BrandData brand_data = ConversionUtil.convert(pojo);
		assertEquals(pojo.getBrand(), brand_data.getBrand());
		assertEquals(pojo.getCategory(), brand_data.getCategory());
	}
	
	@Test
	public void testConvertProductFormToPojo() throws ApiException {

		ProductDetailsForm form = new ProductDetailsForm();
		form.setBrand("amul");
		form.setCategory("dairy");
		form.setMrp(50);
		form.setName("milk");
		ProductDetailsPojo product_pojo = ConversionUtil.convert(brand_service,form);
		assertEquals(form.getBrand(), product_pojo.getBrandPojo().getBrand());
		assertEquals(form.getCategory(), product_pojo.getBrandPojo().getCategory());
		assertEquals(form.getName(),product_pojo.getName());
		assertEquals(form.getMrp(),product_pojo.getMrp(),0.001);
	}
	
	@Test
	public void testConvertProductPojoToData() throws ApiException {

		ProductDetailsPojo product_pojo = new ProductDetailsPojo();
		product_pojo.setBarcode("abcdefgh");
		product_pojo.setBrandPojo(brand_service.get(brand_service.getId("amul", "dairy")));
		product_pojo.setMrp(50);
		product_pojo.setName("milk");
		ProductDetailsData product_data = ConversionUtil.convert(product_pojo);
		assertEquals(product_data.getBarcode(), product_pojo.getBarcode());
		assertEquals(product_data.getBrand(), product_pojo.getBrandPojo().getBrand());
		assertEquals(product_data.getCategory(),product_pojo.getBrandPojo().getCategory());
		assertEquals(product_data.getName(),product_pojo.getName());
		assertEquals(product_data.getMrp(),product_pojo.getMrp(),0.001);
	}
	
	@Test
	public void testConvertInventoryFormToPojo() throws ApiException {

		InventoryForm form = new InventoryForm();
		form.setBarcode(barcode_item1);
		form.setQuantity(20);
		InventoryPojo inventory_pojo = ConversionUtil.convert(form, product_service.get(barcode_item1));
		assertEquals(form.getQuantity(),inventory_pojo.getQuantity());
	}
	
	@Test
	public void testConvertInventoryPojoToData() throws ApiException {

		InventoryPojo pojo = new InventoryPojo();
		pojo.setProductPojo(product_service.get(barcode_item1));
		pojo.setQuantity(20);
		InventoryData inventory_data = ConversionUtil.convert(pojo);
		assertEquals(inventory_data.getBarcode(),pojo.getProductPojo().getBarcode());
		assertEquals(inventory_data.getQuantity(),pojo.getQuantity());
	}
	
	@Test
	public void testConvertOrderItemFormToPojo() throws ApiException {

		OrderItemForm form = new OrderItemForm();
		form.setBarcode(barcode_item1);
		form.setQuantity(2);
		OrderItemPojo pojo = ConversionUtil.convert(product_service,form);
		assertEquals(form.getBarcode(),pojo.getProductPojo().getBarcode());
		assertEquals(form.getQuantity(),pojo.getQuantity());
	}
	
	@Test
	public void testConvertOrderItemPojoToData() throws ApiException {

		OrderItemPojo pojo = new OrderItemPojo();
		pojo.setProductPojo(product_service.get(barcode_item1));
		pojo.setOrderPojo(getOrderPojo());
		pojo.setQuantity(2);
		pojo.setSellingPrice(product_service.get(barcode_item1).getMrp());
		OrderItemData data = ConversionUtil.convert(pojo);
		assertEquals(data.getBarcode(),pojo.getProductPojo().getBarcode());
		assertEquals(data.getOrderId(),pojo.getOrderPojo().getId());
		assertEquals(data.getQuantity(),pojo.getQuantity());
	}
	
	@Test
	public void testListBrandPojoToData() {
		List<BrandPojo> brand_list = brand_service.getAll();
		List<BrandData> brand_data_list = ConversionUtil.convert(brand_list);
		assertEquals(brand_list.size(),brand_data_list.size());
		assertEquals(brand_list.get(0).getBrand(),brand_data_list.get(0).getBrand());
		assertEquals(brand_list.get(0).getCategory(),brand_data_list.get(0).getCategory());
	}
	
	@Test
	public void testListProductPojoToData() {
		List<ProductDetailsPojo> product_list = product_service.getAll();
		List<ProductDetailsData> product_data_list = ConversionUtil.convertProductList(product_list);
		assertEquals(product_list.size(),product_data_list.size());
		assertEquals(product_data_list.get(0).getBarcode(),product_list.get(0).getBarcode());
		assertEquals(product_data_list.get(0).getBrand(),product_list.get(0).getBrandPojo().getBrand());
		assertEquals(product_data_list.get(0).getCategory(),product_list.get(0).getBrandPojo().getCategory());
		assertEquals(product_data_list.get(0).getName(),product_list.get(0).getName());
		assertEquals(product_data_list.get(0).getMrp(), product_list.get(0).getMrp(),0.001);
	}
	
	@Test
	public void testListInventoryPojoToData() {
		List<InventoryPojo> inventory_list = inventory_service.getAll();
		List<InventoryData> inventory_data_list = ConversionUtil.convertInventoryList(inventory_list);
		assertEquals(inventory_data_list.size(),inventory_list.size());
		assertEquals(inventory_data_list.get(0).getBarcode(),inventory_list.get(0).getProductPojo().getBarcode());
		assertEquals(inventory_data_list.get(0).getQuantity(),inventory_list.get(0).getQuantity());
	}
	
	@Test
	public void testOrderItemstoInvoice() {
		List<OrderItemPojo> order_item_list = order_service.getAll();
		List<InvoiceData> invoice_list = ConversionUtil.convert(product_service,order_item_list);
		assertEquals(order_item_list.size(),invoice_list.size());
		assertEquals(invoice_list.get(0).getName(),order_item_list.get(0).getProductPojo().getName());
		assertEquals(invoice_list.get(0).getQuantity(),order_item_list.get(0).getQuantity());
		assertEquals(invoice_list.get(0).getMrp(), order_item_list.get(0).getSellingPrice(),0.001);
	}
	
	@Test
	public void testOrderItemsPojotoData() {
		List<OrderItemPojo> order_item_list = order_service.getAll();
		List<OrderItemData> data_list = ConversionUtil.convertOrderItemList(order_item_list);
		assertEquals(order_item_list.size(),data_list.size());
		assertEquals(data_list.get(0).getBarcode(),order_item_list.get(0).getProductPojo().getBarcode());
		assertEquals(data_list.get(0).getOrderId(),order_item_list.get(0).getOrderPojo().getId());
		assertEquals(data_list.get(0).getQuantity(), order_item_list.get(0).getQuantity());
	}
	
	@Test
	public void testOrderItemsFormtoPojo() throws ApiException {
		OrderItemForm[] order_item_forms = new OrderItemForm[1];
		OrderItemForm form1 = new OrderItemForm();
		form1.setBarcode(barcode_item1);
		form1.setQuantity(2);
		order_item_forms[0] = form1;
		List<OrderItemPojo> pojo_list = ConversionUtil.convertOrderItemForms(product_service, order_item_forms);
		assertEquals(1,pojo_list.size());
		assertEquals(order_item_forms[0].getBarcode(), pojo_list.get(0).getProductPojo().getBarcode());
		assertEquals(order_item_forms[0].getQuantity(), pojo_list.get(0).getQuantity());
	}
	
	/*@Test
	public void testSetOrderData() {
		int id=1;
		OrderData order_data = ConversionUtil.convertOrderPojo(id);
		assertEquals(id, order_data.getId());
	}*/
	
	/*@Test
	public void testCreateInventoryReportList() throws ApiException {
		List<InventoryPojo> inventory_list = inventory_service.getAll();
		List<InventoryReportData> inventory_report_list = ConversionUtil.createInventoryReportList(brand_service, inventory_list);
		List<BrandPojo> brand_list = brand_service.getAll();
		assertEquals(brand_list.size(),inventory_report_list.size());
	}*/
	
	/*@Test
	public void testCreateSalesListNoBrand() {
		String category = "dairy";
		List<OrderItemPojo> order_item_list = order_service.getAll();
		List<SalesData> sales_list = ConversionUtil.createSalesList("", category, order_item_list);
		assertEquals(1,sales_list.size());
		assertEquals(sales_list.get(0).getCategory(),category);
	}
	
	@Test
	public void testCreateSalesListNoCategory() {
		String brand = "amul";
		List<OrderItemPojo> order_item_list = order_service.getAll();
		List<SalesData> sales_list = ConversionUtil.createSalesList(brand, "", order_item_list);
		assertEquals(2,sales_list.size());
	}
	
	@Test
	public void testCreateSalesListNoBrandNoCategory() {
		List<OrderItemPojo> order_item_list = order_service.getAll();
		List<SalesData> sales_list = ConversionUtil.createSalesList("", "", order_item_list);
		assertEquals(2,sales_list.size());
	}
	
	@Test
	public void testCreateSalesList() {
		String brand = "amul";
		String category = "bakery";
		List<OrderItemPojo> order_item_list = order_service.getAll();
		List<SalesData> sales_list = ConversionUtil.createSalesList(brand, category, order_item_list);
		assertEquals(1,sales_list.size());
		assertEquals(sales_list.get(0).getCategory(),category);
	}*/

}