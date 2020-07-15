package com.increff.pos.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
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
	
	@Before
	public void init() throws ApiException {
		//Inserting initial pojos
		insertPojos();
	}

	//Testing adding of order
	@Test
	public void testAdd() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		getInventoryPojo(p);
		OrderItemPojo order_item = getOrderItemPojo(p, 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		List<OrderPojo> order_list_before = order_service.getAllOrders();
		List<OrderItemPojo> orderitem_list_before = order_service.getAll();
		order_service.add(lis);
		List<OrderPojo> order_list_after = order_service.getAllOrders();
		List<OrderItemPojo> orderitem_list_after = order_service.getAll();
		

		assertEquals(order_list_before.size()+1,order_list_after.size());
		assertEquals(orderitem_list_before.size()+1,orderitem_list_after.size());
		List<OrderItemPojo> db_orderitem_list = order_service.getOrderItems(order_item.getOrderPojo().getId());
		assertEquals(lis.size(), db_orderitem_list.size());
		assertEquals(order_item.getOrderPojo(), db_orderitem_list.get(0).getOrderPojo());
		assertEquals(order_item.getProductPojo(), db_orderitem_list.get(0).getProductPojo());
		assertEquals(order_item.getQuantity(), db_orderitem_list.get(0).getQuantity());
		assertEquals(order_item.getSellingPrice(), db_orderitem_list.get(0).getSellingPrice(), 0.001);

	}

	//Testing adding of invalid order. Exception should be thrown
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

	//Testing Get for order items 
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
	
	//Testing Get for order
	@Test
	public void testGetOrder() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		getInventoryPojo(p);
		OrderItemPojo order_item = getOrderItemPojo(p, 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		int order_id = order_service.add(lis);

		OrderPojo db_order_pojo = order_service.getOrder(order_id);
		assertEquals(order_id, db_order_pojo.getId());
	}
	
	//Testing getting all order items for a particular order
	@Test
	public void testGetOrderItemsByOrderId() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		getInventoryPojo(p);
		OrderItemPojo order_item = getOrderItemPojo(p, 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		int order_id = order_service.add(lis);

		List<OrderItemPojo> orderitem_list = order_service.getOrderItems(order_id);
		assertEquals(1,orderitem_list.size());
	}
	
	//Testing getting all orders
	@Test
	public void testGetAllOrders() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		getInventoryPojo(p);
		OrderItemPojo order_item = getOrderItemPojo(p, 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		order_service.add(lis);

		List<OrderPojo> orderitem_list = order_service.getAllOrders();
		assertEquals(2,orderitem_list.size());
	}

	//Testing getting all order items
	@Test
	public void testGetAll() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		getInventoryPojo(p);
		OrderItemPojo order_item = getOrderItemPojo(p, 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		order_service.add(lis);

		List<OrderItemPojo> orderitem_list = order_service.getAll();
		assertEquals(3,orderitem_list.size());
	}

	//Testing deletion of order items
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
		List<OrderPojo> order_list_before = order_service.getAllOrders();
		List<OrderItemPojo> orderitem_list_before = order_service.getAll();
		order_service.delete(id);
		List<OrderPojo> order_list_after = order_service.getAllOrders();
		List<OrderItemPojo> orderitem_list_after = order_service.getAll();
		assertEquals(order_list_before.size()-1,order_list_after.size());
		assertEquals(orderitem_list_before.size()-1,orderitem_list_after.size());
		try {
			order_service.get(id);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "OrderItem with given ID does not exist, id: " + id);
		}
	}

	//Testing updation of order items
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

	//Testing addition of order item to an existing order
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

	//Testing checkifexists for an order item
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

	//Testing checkifexists for a non-existent order item. Should throw exception
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
	
	//Testing checkifexists for Order
	@Test
	public void testCheckIfExistsOrder() throws ApiException {
		BrandPojo b = getBrandPojo();
		ProductDetailsPojo p = getProductDetailsPojo(b);
		getInventoryPojo(p);
		OrderItemPojo order_item = getOrderItemPojo(p, 5);
		List<OrderItemPojo> lis = new ArrayList<OrderItemPojo>();
		lis.add(order_item);
		int order_id = order_service.add(lis);

		OrderPojo db_order_pojo = order_service.checkIfExistsOrder(order_id);
		assertEquals(order_id, db_order_pojo.getId());
	}
	
	//Testing checkifexists for a non-existent order. Should throw exception
	@Test
	public void testCheckIfExistsOrderWrong() throws ApiException {

		try {
			order_service.checkIfExistsOrder(100);
			fail("ApiException did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),"Order with given ID does not exist, id: " + 100);
		}
		
	}

	//Testing updation of inventory
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

	//Testing updation of inventory when quantity is greater than existing quantity. Should throw exception
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

	//Testing updation of inventory during editing of order items
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

	//Testing updation of inventory during edit when quantity is greater than existing quantity. Should throw exception
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
