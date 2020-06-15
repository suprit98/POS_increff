package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.InventoryData;
import com.increff.pos.model.InventoryForm;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductDetailsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryController {

	@Autowired
	private InventoryService inventory_service;

	@Autowired
	private ProductDetailsService product_service;

	@ApiOperation(value = "Adds Inventory")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm userform) throws ApiException {
		ProductDetailsPojo product = product_service.get(userform.getBarcode());
		InventoryPojo p = convert(userform,product);
		inventory_service.add(p);
	}

	@ApiOperation(value = "Deletes an Inventory record")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		inventory_service.delete(id);
	}

	@ApiOperation(value = "Gets an Inventory record by id")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable int id) throws ApiException {
		InventoryPojo p = inventory_service.get(id);
		return convert(p);
	}

	@ApiOperation(value = "Gets list of Products")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<InventoryData> getAll() {
		List<InventoryPojo> list = inventory_service.getAll();
		List<InventoryData> list2 = new ArrayList<InventoryData>();
		for (InventoryPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates an Inventory record")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody InventoryForm f) throws ApiException {
		ProductDetailsPojo product = product_service.get(f.getBarcode());
		InventoryPojo p = convert(f,product);
		inventory_service.update(id, p);
	}

	private InventoryPojo convert(InventoryForm f, ProductDetailsPojo product_pojo) throws ApiException {
		InventoryPojo p = new InventoryPojo();
		p.setProductPojo(product_pojo);
		p.setQuantity(f.getQuantity());
		return p;
	}

	private InventoryData convert(InventoryPojo p) {
		InventoryData d = new InventoryData();
		d.setId(p.getId());
		d.setBarcode(p.getProductPojo().getBarcode());
		d.setQuantity(p.getQuantity());
		return d;
	}

}
