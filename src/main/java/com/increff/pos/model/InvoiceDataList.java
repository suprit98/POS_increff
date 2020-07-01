package com.increff.pos.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "items")
public class InvoiceDataList {
	
	
	private int order_id;
	private String datetime;
	private double total;
	
	@XmlElement(name="total")
	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	@XmlElement(name="order_id")
	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	
	@XmlElement(name="datetime")
	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	private List<InvoiceData> invoiceLis;

	@XmlElement(name="item")
	public List<InvoiceData> getInvoiceLis() {
		return invoiceLis;
	}

	public void setInvoiceLis(List<InvoiceData> invoiceLis) {
		this.invoiceLis = invoiceLis;
	}
	
	

}
