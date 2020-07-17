package com.increff.pos.util;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.model.InventoryReportData;
import com.increff.pos.model.InventoryReportList;
import com.increff.pos.model.InvoiceData;
import com.increff.pos.model.InvoiceDataList;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import com.increff.pos.model.OrderItemForm;
import com.increff.pos.model.ProductDetailsData;
import com.increff.pos.model.ProductDetailsForm;
import com.increff.pos.model.SalesData;
import com.increff.pos.model.SalesDataList;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.service.ApiException;

public class ConversionUtil {

	public static BrandPojo convert(BrandForm d) {
		BrandPojo p = new BrandPojo();
		p.setBrand(d.getBrand());
		p.setCategory(d.getCategory());
		return p;
	}

	public static BrandData convert(BrandPojo p) {
		BrandData d = new BrandData();
		d.setBrand(p.getBrand());
		d.setCategory(p.getCategory());
		d.setId(p.getId());
		return d;
	}

	public static ProductDetailsPojo convert(BrandPojo brand_pojo, ProductDetailsForm f) throws ApiException {
		ProductDetailsPojo p = new ProductDetailsPojo();
		p.setName(f.getName());
		p.setMrp(f.getMrp());
		p.setBrandPojo(brand_pojo);
		return p;
	}

	public static ProductDetailsData convert(ProductDetailsPojo p) {
		ProductDetailsData d = new ProductDetailsData();
		d.setId(p.getId());
		d.setBrand(p.getBrandPojo().getBrand());
		d.setCategory(p.getBrandPojo().getCategory());
		d.setMrp(p.getMrp());
		d.setName(p.getName());
		d.setBarcode(p.getBarcode());
		return d;
	}

	public static InventoryPojo convert(InventoryForm f, ProductDetailsPojo product_pojo) throws ApiException {
		InventoryPojo p = new InventoryPojo();
		p.setProductPojo(product_pojo);
		p.setQuantity(f.getQuantity());
		return p;
	}

	public static InventoryData convert(InventoryPojo p) {
		InventoryData d = new InventoryData();
		d.setId(p.getId());
		d.setBarcode(p.getProductPojo().getBarcode());
		d.setQuantity(p.getQuantity());
		return d;
	}

	public static OrderItemPojo convert(ProductDetailsPojo product_pojo, OrderItemForm f) throws ApiException {
		OrderItemPojo p = new OrderItemPojo();
		p.setProductPojo(product_pojo);
		p.setQuantity(f.getQuantity());
		if(product_pojo != null) {
			p.setSellingPrice(product_pojo.getMrp());
		}
		
		return p;
	}

	public static OrderItemData convert(OrderItemPojo p) {
		OrderItemData d = new OrderItemData();
		d.setId(p.getId());
		d.setBarcode(p.getProductPojo().getBarcode());
		d.setQuantity(p.getQuantity());
		d.setOrderId(p.getOrderPojo().getId());
		d.setName(p.getProductPojo().getName());
		d.setMrp(p.getSellingPrice());
		return d;
	}

	public static InvoiceDataList convertToInvoiceDataList(List<OrderItemPojo> lis) {
		List<InvoiceData> invoiceLis = new ArrayList<InvoiceData>();
		for (OrderItemPojo p : lis) {
			InvoiceData i = new InvoiceData();
			i.setId(p.getId());
			i.setMrp(p.getProductPojo().getMrp());
			i.setName(p.getProductPojo().getName());
			i.setQuantity(p.getQuantity());
			invoiceLis.add(i);
		}
		InvoiceDataList idl = new InvoiceDataList();
		idl.setInvoiceLis(invoiceLis);
		return idl;
	}

	public static List<BrandData> convert(List<BrandPojo> list) {
		List<BrandData> list2 = new ArrayList<BrandData>();
		for (BrandPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	public static List<ProductDetailsData> convertProductList(List<ProductDetailsPojo> list) {
		List<ProductDetailsData> list2 = new ArrayList<ProductDetailsData>();
		for (ProductDetailsPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	public static List<InventoryData> convertInventoryList(List<InventoryPojo> list) {
		List<InventoryData> list2 = new ArrayList<InventoryData>();
		for (InventoryPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	public static List<OrderItemData> convertOrderItemList(List<OrderItemPojo> list) {
		List<OrderItemData> list2 = new ArrayList<OrderItemData>();
		for (OrderItemPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	public static List<OrderItemPojo> convertOrderItemForms(Map<String,ProductDetailsPojo> barcode_product,
			OrderItemForm[] forms) throws ApiException {
		List<OrderItemPojo> list2 = new ArrayList<OrderItemPojo>();
		for (OrderItemForm f : forms) {
			list2.add(convert(barcode_product.get(f.getBarcode()), f));
		}
		return list2;
	}

	public static OrderData convertOrderPojo(OrderPojo pojo) {
		OrderData d = new OrderData();
		d.setId(pojo.getId());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String datetime = pojo.getDatetime().format(formatter);
		d.setDatetime(datetime);
		return d;
	}
	
	public static List<OrderData> convertOrderList(List<OrderPojo> list) {
		List<OrderData> list2 = new ArrayList<OrderData>();
		for (OrderPojo p : list) {
			list2.add(convertOrderPojo(p));
		}
		return list2;
	}

	public static InventoryReportList convertInventoryReportList(Map<BrandPojo, Integer> quantityPerBrandPojo) {
		List<InventoryReportData> inventory_report_list = new ArrayList<InventoryReportData>();
		for (BrandPojo brand_pojo : quantityPerBrandPojo.keySet()) {
			InventoryReportData d = new InventoryReportData();
			d.setBrand(brand_pojo.getBrand());
			d.setCategory(brand_pojo.getCategory());
			d.setQuantity(quantityPerBrandPojo.get(brand_pojo));
			inventory_report_list.add(d);
		}
		InventoryReportList inventory_list = new InventoryReportList();
		inventory_list.setInventory_list(inventory_report_list);
		return inventory_list;
	}
	

	public static SalesDataList convertSalesList(Map<BrandPojo, Integer> quantityPerBrandCategory,
			Map<BrandPojo, Double> revenuePerBrandCategory) {
		
		List<SalesData> sales_list = new ArrayList<SalesData>();
		for(BrandPojo brand: quantityPerBrandCategory.keySet()) {
			SalesData sales = new SalesData();
			sales.setBrand(brand.getBrand());
			sales.setCategory(brand.getCategory());
			sales.setQuantity(quantityPerBrandCategory.get(brand));
			sales.setRevenue(revenuePerBrandCategory.get(brand));
			sales_list.add(sales);
		}
		SalesDataList sales_data_list = new SalesDataList();
		sales_data_list.setSales_list(sales_list);
		return sales_data_list;

	}


}
