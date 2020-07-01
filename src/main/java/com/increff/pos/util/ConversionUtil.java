package com.increff.pos.util;

import java.util.ArrayList;
import java.util.List;

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
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductDetailsService;

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

	public static ProductDetailsPojo convert(BrandService brand_service, ProductDetailsForm f) throws ApiException {
		ProductDetailsPojo p = new ProductDetailsPojo();
		p.setName(f.getName());
		p.setMrp(f.getMrp());
		int brand_id = brand_service.getId(f.getBrand(), f.getCategory());
		p.setBrandPojo(brand_service.get(brand_id));
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
	
	public static OrderItemPojo convert(ProductDetailsService product_service, OrderItemForm f) throws ApiException {
		OrderItemPojo p = new OrderItemPojo();
		p.setProductPojo(product_service.get(f.getBarcode()));
		p.setQuantity(f.getQuantity());
		p.setSellingPrice(product_service.get(f.getBarcode()).getMrp());
		return p;
	}

	public static OrderItemData convert(OrderItemPojo p) {
		OrderItemData d = new OrderItemData();
		d.setId(p.getId());
		d.setBarcode(p.getProductPojo().getBarcode());
		d.setQuantity(p.getQuantity());
		d.setOrderId(p.getOrderPojo().getId());
		return d;
	}

	public static List<InvoiceData> convert(ProductDetailsService product_service, List<OrderItemPojo> lis) {
		List<InvoiceData> invoiceLis = new ArrayList<InvoiceData>();
		for(OrderItemPojo p:lis) {
			InvoiceData i = new InvoiceData();
			i.setId(p.getId());
			i.setMrp(p.getProductPojo().getMrp());
			i.setName(p.getProductPojo().getName());
			i.setQuantity(p.getQuantity());
			invoiceLis.add(i);
		}
		return invoiceLis;
	}
	
	public static List<BrandData> convert(List<BrandPojo> list) {
		List<BrandData> list2 = new ArrayList<BrandData>();
		for(BrandPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}
	
	public static List<ProductDetailsData> convertProductList(List<ProductDetailsPojo> list) {
		List<ProductDetailsData> list2 = new ArrayList<ProductDetailsData>();
		for(ProductDetailsPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}
	
	public static List<InventoryData> convertInventoryList(List<InventoryPojo> list) {
		List<InventoryData> list2 = new ArrayList<InventoryData>();
		for(InventoryPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}
	
	public static List<OrderItemData> convertOrderItemList(List<OrderItemPojo> list) {
		List<OrderItemData> list2 = new ArrayList<OrderItemData>();
		for(OrderItemPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}
	
	public static List<OrderItemPojo> convertOrderItemForms(ProductDetailsService product_service, OrderItemForm[] forms) throws ApiException {
		List<OrderItemPojo> list2 = new ArrayList<OrderItemPojo>();
		for(OrderItemForm f : forms) {
			list2.add(convert(product_service,f));
		}
		return list2;
	}

}
