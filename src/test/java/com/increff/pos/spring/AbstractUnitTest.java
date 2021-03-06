package com.increff.pos.spring;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public abstract class AbstractUnitTest {
	
	
	@Autowired
	protected BrandService brand_service;

	@Autowired
	protected ProductDetailsService product_service;

	@Autowired
	protected InventoryService inventory_service;
	
	@Autowired
	protected OrderService order_service;
	
	protected int order_id;
	
	protected List<String> barcodes;
	
	protected List<BrandPojo> brands;
	protected List<ProductDetailsPojo> products;
	protected List<InventoryPojo> inventories;
	protected List<OrderPojo> orders;
	protected List<OrderItemPojo> orderitems;
	
	protected void insertPojos() throws ApiException {
		barcodes = new ArrayList<String>();
		brands = new ArrayList<BrandPojo>();
		products = new ArrayList<ProductDetailsPojo>();
		inventories = new ArrayList<InventoryPojo>();
		orders = new ArrayList<OrderPojo>();
		orderitems = new ArrayList<OrderItemPojo>();
		
		for(int i=0; i<2; i++) {
			BrandPojo brand = new BrandPojo();
			brand.setBrand("brand");
			brand.setCategory("category"+i);
			brand_service.add(brand);
			brands.add(brand);
			
			ProductDetailsPojo product = new ProductDetailsPojo();
			product.setBrandPojo(brand);
			product.setName("product"+i);
			product.setMrp(50);
			product_service.add(product);
			products.add(product);
			barcodes.add(product.getBarcode());
			
			InventoryPojo inventory = new InventoryPojo();
			inventory.setProductPojo(product);
			inventory.setQuantity(20);
			inventory_service.add(inventory);
			inventories.add(inventory);
		}
		
		//Product without inventory
		ProductDetailsPojo product = new ProductDetailsPojo();
		product.setBrandPojo(brands.get(0));
		product.setName("product3");
		product.setMrp(50);
		product_service.add(product);
		products.add(product);
		
		List<OrderItemPojo> order_item_list = new ArrayList<OrderItemPojo>();
		for(int i=0; i<2; i++) {
			OrderItemPojo order_item = new OrderItemPojo();
			order_item.setProductPojo(product_service.get(barcodes.get(i)));
			order_item.setQuantity(2);
			order_item.setSellingPrice(product_service.get(barcodes.get(i)).getMrp());
			order_item_list.add(order_item);
		}
		order_id = order_service.add(order_item_list);
		orders.add(order_service.getOrder(order_id));
		orderitems.addAll(order_item_list);
	}
}
