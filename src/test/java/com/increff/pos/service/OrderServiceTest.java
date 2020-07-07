package com.increff.pos.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.spring.AbstractUnitTest;

public class OrderServiceTest extends AbstractUnitTest {

	@Autowired
	private BrandService brand_service;

	@Autowired
	private ProductDetailsService product_service;

	@Autowired
	private OrderService order_service;

	@Autowired
	private InventoryService inventory_service;

	@Test
	public void testAdd() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		getInventoryPojo(p);
		OrderItemPojo order_item = getOrderItemPojo(p, 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		order_service.add(lis);

		List<OrderItemPojo> db_orderitem_list = order_service.getOrderItems(order_item.getOrderPojo().getId());
		assertEquals(lis.size(), db_orderitem_list.size());
		assertEquals(order_item.getOrderPojo(), db_orderitem_list.get(0).getOrderPojo());
		assertEquals(order_item.getProductPojo(), db_orderitem_list.get(0).getProductPojo());
		assertEquals(order_item.getQuantity(), db_orderitem_list.get(0).getQuantity());
		assertEquals(order_item.getSellingPrice(), db_orderitem_list.get(0).getSellingPrice(), 0.001);

	}

	@Test
	public void testAddWrong() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		getInventoryPojo(p);
		OrderItemPojo order_item = getWrongOrderItemPojo(p);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);

		try {
			order_service.add(lis);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Quantity must be positive");
		}

	}

	@Test
	public void testGet() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		getInventoryPojo(p);
		OrderItemPojo order_item = getOrderItemPojo(p, 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		order_service.add(lis);

		OrderItemPojo db_orderitem_pojo = order_service.get(order_item.getId());
		assertEquals(order_item.getOrderPojo(), db_orderitem_pojo.getOrderPojo());
		assertEquals(order_item.getProductPojo(), db_orderitem_pojo.getProductPojo());
		assertEquals(order_item.getQuantity(), db_orderitem_pojo.getQuantity());
		assertEquals(order_item.getSellingPrice(), db_orderitem_pojo.getSellingPrice(), 0.001);
	}

	@Test
	public void testGetAll() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		getInventoryPojo(p);
		OrderItemPojo order_item = getOrderItemPojo(p, 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		order_service.add(lis);

		order_service.getAll();
	}

	@Test
	public void testDelete() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		getInventoryPojo(p);
		OrderItemPojo order_item = getOrderItemPojo(p, 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		order_service.add(lis);

		int id = order_item.getId();
		order_service.delete(id);

		try {
			order_service.get(id);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "OrderItem with given ID does not exist, id: " + id);
		}
	}

	@Test
	public void testGetByDate() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		getInventoryPojo(p);
		OrderItemPojo order_item = getOrderItemPojo(p, 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		order_service.add(lis);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String startdate = formatter.format(LocalDateTime.now().minusDays(1));
		String enddate = formatter.format(LocalDateTime.now().plusDays(1));
		List<OrderPojo> order_list = order_service.getByDate(startdate, enddate);
		assertEquals(order_list.size(), 1);
		assertEquals(order_item.getOrderPojo().getId(), order_list.get(0).getId());
		assertEquals(order_item.getOrderPojo().getDatetime(), order_list.get(0).getDatetime());
	}

	@Test
	public void testUpdate() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		getInventoryPojo(p);
		OrderItemPojo order_item = getOrderItemPojo(p, 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		order_service.add(lis);

		OrderItemPojo new_order_item = getOrderItemPojo(p, 7);
		order_service.update(order_item.getId(), new_order_item);
		assertEquals(order_item.getProductPojo(), new_order_item.getProductPojo());
		assertEquals(order_item.getQuantity(), new_order_item.getQuantity());
		assertEquals(order_item.getSellingPrice(), new_order_item.getSellingPrice(), 0.001);
	}

	@Test
	public void testAddOrderItem() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		getInventoryPojo(p);
		OrderItemPojo order_item = getOrderItemPojo(p, 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		order_service.add(lis);

		int order_id = order_item.getOrderPojo().getId();
		ProductDetailsPojo new_product = getAnotherProductDetailsPojo(b);
		getInventoryPojo(new_product);
		OrderItemPojo new_order_item = getOrderItemPojo(new_product, 6);
		order_service.addOrderItem(order_id, new_order_item);

		assertEquals(2, order_service.getOrderItems(order_id).size());
	}

	@Test
	public void testCheckIfExists() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		getInventoryPojo(p);
		OrderItemPojo order_item = getOrderItemPojo(p, 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		order_service.add(lis);

		OrderItemPojo db_orderitem_pojo = order_service.checkIfExists(order_item.getId());
		assertEquals(order_item.getOrderPojo(), db_orderitem_pojo.getOrderPojo());
		assertEquals(order_item.getProductPojo(), db_orderitem_pojo.getProductPojo());
		assertEquals(order_item.getQuantity(), db_orderitem_pojo.getQuantity());
		assertEquals(order_item.getSellingPrice(), db_orderitem_pojo.getSellingPrice(), 0.001);
	}

	@Test
	public void testCheckIfExistsWrong() throws ApiException {
		int orderitem_id = 5;

		try {
			order_service.checkIfExists(orderitem_id);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "OrderItem with given ID does not exist, id: " + orderitem_id);
		}

	}

	@Test
	public void testUpdateInventory() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo i = getInventoryPojo(p);
		int original_quantity = i.getQuantity();
		OrderItemPojo order_item = getOrderItemPojo(p, 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		order_service.add(lis);

		assertEquals(original_quantity - order_item.getQuantity(), i.getQuantity());
	}

	@Test
	public void testUpdateInventoryExceeding() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo i = getInventoryPojo(p);
		OrderItemPojo order_item = getOrderItemPojo(p, 100);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);

		try {
			order_service.add(lis);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),
					"Inventory does not contain this much quantity of product. Existing quantity in inventory: "
							+ i.getQuantity());
		}

	}

	@Test
	public void testUpdateInventoryDuringEdit() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo i = getInventoryPojo(p);
		OrderItemPojo order_item = getOrderItemPojo(p, 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		order_service.add(lis);
		int original_quantity = i.getQuantity();
		int old_quantity = order_item.getQuantity();

		OrderItemPojo new_order_item = getOrderItemPojo(p, 7);
		int new_quantity = new_order_item.getQuantity();
		order_service.update(order_item.getId(), new_order_item);
		assertEquals(i.getQuantity(), original_quantity + old_quantity - new_quantity);
	}

	@Test
	public void testUpdateInventoryDuringEditExceeding() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		InventoryPojo i = getInventoryPojo(p);
		OrderItemPojo order_item = getOrderItemPojo(p, 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		order_service.add(lis);
		int original_quantity = i.getQuantity();
		int old_quantity = order_item.getQuantity();

		OrderItemPojo new_order_item = getOrderItemPojo(p, 100);
		try {
			order_service.update(order_item.getId(), new_order_item);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),
					"Inventory does not contain this much quantity of product. Existing quantity in inventory: "
							+ (original_quantity + old_quantity));
		}

	}

	private BrandPojo getBrandPojo() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("Amul");
		p.setCategory("Dairy");
		brand_service.add(p);
		return p;
	}

	private ProductDetailsPojo getProductDetailsPojo(BrandPojo b) throws ApiException {
		ProductDetailsPojo p = new ProductDetailsPojo();
		p.setBrandPojo(b);
		p.setName("Milk");
		p.setMrp(50);
		product_service.add(p);
		return p;
	}

	private ProductDetailsPojo getAnotherProductDetailsPojo(BrandPojo b) throws ApiException {
		ProductDetailsPojo p = new ProductDetailsPojo();
		p.setBrandPojo(b);
		p.setName("Paneer");
		p.setMrp(80);
		product_service.add(p);
		return p;
	}

	private InventoryPojo getInventoryPojo(ProductDetailsPojo p) throws ApiException {
		InventoryPojo i = new InventoryPojo();
		i.setProductPojo(p);
		i.setQuantity(20);
		inventory_service.add(i);
		return i;
	}

	private OrderItemPojo getOrderItemPojo(ProductDetailsPojo p, int quantity) {
		OrderItemPojo order_item = new OrderItemPojo();
		order_item.setProductPojo(p);
		order_item.setQuantity(quantity);
		order_item.setSellingPrice(p.getMrp());
		return order_item;
	}

	private OrderItemPojo getWrongOrderItemPojo(ProductDetailsPojo p) {
		OrderItemPojo order_item = new OrderItemPojo();
		order_item.setProductPojo(p);
		order_item.setQuantity(-5);
		order_item.setSellingPrice(p.getMrp());
		return order_item;
	}

}
