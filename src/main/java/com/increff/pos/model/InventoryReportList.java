package com.increff.pos.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "inventory_items")
public class InventoryReportList {
	
	private List<InventoryReportData> inventory_list;

	@XmlElement(name="inventory_item")
	public List<InventoryReportData> getInventory_list() {
		return inventory_list;
	}

	public void setInventory_list(List<InventoryReportData> inventory_list) {
		this.inventory_list = inventory_list;
	}

}
