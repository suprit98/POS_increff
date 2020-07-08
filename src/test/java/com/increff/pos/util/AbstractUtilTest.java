package com.increff.pos.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductDetailsService;
import com.increff.pos.spring.AbstractUnitTest;

public abstract class AbstractUtilTest extends AbstractUnitTest {
	
	@Autowired
	protected BrandService brand_service;

	@Autowired
	protected ProductDetailsService product_service;

	@Autowired
	protected InventoryService inventory_service;
	
	@Autowired
	protected OrderService order_service;
	
	protected static String barcode_item1;
	protected static String barcode_item2;
	
	protected BrandPojo getBrandPojo() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("Amul");
		p.setCategory("Dairy");
		brand_service.add(p);
		return p;
	}
	
	protected BrandPojo getAnotherBrandPojo() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("Amul");
		p.setCategory("Bakery");
		brand_service.add(p);
		return p;
	}

	protected ProductDetailsPojo getProductDetailsPojo(BrandPojo b) throws ApiException {
		ProductDetailsPojo p = new ProductDetailsPojo();
		p.setBrandPojo(b);
		p.setName("Milk");
		p.setMrp(50);
		product_service.add(p);
		barcode_item1 = p.getBarcode();
		return p;
	}

	protected ProductDetailsPojo getAnotherProductDetailsPojo(BrandPojo b) throws ApiException {
		ProductDetailsPojo p = new ProductDetailsPojo();
		p.setBrandPojo(b);
		p.setName("Bread");
		p.setMrp(80);
		product_service.add(p);
		barcode_item2 = p.getBarcode();
		return p;
	}

	protected InventoryPojo getInventoryPojo(ProductDetailsPojo p) throws ApiException {
		InventoryPojo i = new InventoryPojo();
		i.setProductPojo(p);
		i.setQuantity(20);
		inventory_service.add(i);
		return i;
	}
	
	protected OrderPojo getOrderPojo() {
		OrderPojo order = new OrderPojo();
		order.setId(1);
		order.setDatetime(LocalDateTime.now());
		return order;
	}
	
	protected List<OrderItemPojo> getOrderItemPojo() throws ApiException {
		OrderItemPojo order_item = new OrderItemPojo();
		order_item.setProductPojo(product_service.get(barcode_item1));
		order_item.setQuantity(2);
		order_item.setSellingPrice(product_service.get(barcode_item1).getMrp());
		OrderItemPojo order_item2 = new OrderItemPojo();
		order_item2.setProductPojo(product_service.get(barcode_item2));
		order_item2.setQuantity(1);
		order_item2.setSellingPrice(product_service.get(barcode_item2).getMrp());
		List<OrderItemPojo> order_item_list = new ArrayList<OrderItemPojo>();
		order_item_list.add(order_item);
		order_item_list.add(order_item2);
		order_service.add(order_item_list);
		return order_item_list;
	}

}
