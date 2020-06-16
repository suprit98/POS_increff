package com.increff.pos.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderItemPojo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "orderId", nullable = false)
	private OrderPojo orderPojo;

	@ManyToOne(optional = false)
	@JoinColumn(name = "productId", nullable = false)
	private ProductDetailsPojo productPojo;

	private int quantity;

	private double sellingPrice;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public OrderPojo getOrderPojo() {
		return orderPojo;
	}

	public void setOrderPojo(OrderPojo orderPojo) {
		this.orderPojo = orderPojo;
	}

	public ProductDetailsPojo getProductPojo() {
		return productPojo;
	}

	public void setProductPojo(ProductDetailsPojo productPojo) {
		this.productPojo = productPojo;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

}
