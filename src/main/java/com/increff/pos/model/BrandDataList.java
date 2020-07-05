package com.increff.pos.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "brands")
public class BrandDataList {
	
	List<BrandData> brand_list;

	@XmlElement(name = "brand_item")
	public List<BrandData> getBrand_list() {
		return brand_list;
	}

	public void setBrand_list(List<BrandData> brand_list) {
		this.brand_list = brand_list;
	}
	

}
