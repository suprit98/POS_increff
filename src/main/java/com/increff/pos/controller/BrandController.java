package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandController {
	
	@Autowired
	private BrandService brand_service;
	
	@ApiOperation(value = "Adds Brand Details")
	@RequestMapping(path="/api/brand", method = RequestMethod.POST)
	public void add(@RequestBody BrandForm userform) {
		BrandPojo p = convert(userform);
		brand_service.add(p);
	}
	
	
	@ApiOperation(value = "Deletes a Brand Details record")
	@RequestMapping(path="/api/brand/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		brand_service.delete(id);
	}
	
	@ApiOperation(value = "Gets a Brand details record by id")
	@RequestMapping(path="/api/brand/{id}", method = RequestMethod.GET)
	public BrandData get(@PathVariable int id) throws ApiException {
		BrandPojo p = brand_service.get(id);
		return convert(p);
	}
	
	

	@ApiOperation(value = "Gets list of Brands")
	@RequestMapping(path="/api/brand", method = RequestMethod.GET)
	public List<BrandData> getAll() {
		List<BrandPojo> list = brand_service.getAll();
		List<BrandData> list2 = new ArrayList<BrandData>();
		for(BrandPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}
	
	@ApiOperation(value = "Updates a Brand details record")
	@RequestMapping(path="/api/brand/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id,@RequestBody BrandForm f) throws ApiException {
		BrandPojo p = convert(f);
		brand_service.update(id, p);
	}
	
	private BrandPojo convert(BrandForm d) {
		BrandPojo p = new BrandPojo();
		p.setBrand(d.getBrand());
		p.setCategory(d.getCategory());
		return p;
	}

	private BrandData convert(BrandPojo p) {
		BrandData d = new BrandData();
		d.setBrand(p.getBrand());
		d.setCategory(p.getCategory());
		d.setId(p.getId());
		return d;
	}

}
