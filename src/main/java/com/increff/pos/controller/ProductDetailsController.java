package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.ProductDetailsData;
import com.increff.pos.model.ProductDetailsForm;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductDetailsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductDetailsController {

	@Autowired
	private ProductDetailsService product_details_service;

	@Autowired
	private BrandService brand_service;

	@ApiOperation(value = "Adds ProductDetails")
	@RequestMapping(path = "/api/product_details", method = RequestMethod.POST)
	public void add(@RequestBody ProductDetailsForm userform) throws ApiException {
		ProductDetailsPojo p = convert(userform);
		product_details_service.add(p);
	}

	@ApiOperation(value = "Deletes a ProductDetails record")
	@RequestMapping(path = "/api/product_details/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		product_details_service.delete(id);
	}

	@ApiOperation(value = "Gets a ProductDetails record by id")
	@RequestMapping(path = "/api/product_details/{id}", method = RequestMethod.GET)
	public ProductDetailsData get(@PathVariable int id) throws ApiException {
		ProductDetailsPojo p = product_details_service.get(id);
		return convert(p);
	}

	@ApiOperation(value = "Gets list of Products")
	@RequestMapping(path = "/api/product_details", method = RequestMethod.GET)
	public List<ProductDetailsData> getAll() {
		List<ProductDetailsPojo> list = product_details_service.getAll();
		List<ProductDetailsData> list2 = new ArrayList<ProductDetailsData>();
		for (ProductDetailsPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates a ProductDetails record")
	@RequestMapping(path = "/api/product_details/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductDetailsForm f) throws ApiException {
		ProductDetailsPojo p = convert(f);
		product_details_service.update(id, p);
	}

	private ProductDetailsPojo convert(ProductDetailsForm f) throws ApiException {
		ProductDetailsPojo p = new ProductDetailsPojo();
		p.setName(f.getName());
		p.setMrp(f.getMrp());
		int brand_id = brand_service.getId(f.getBrand(), f.getCategory());
		p.setBrandPojo(brand_service.get(brand_id));
		return p;
	}

	private ProductDetailsData convert(ProductDetailsPojo p) {
		ProductDetailsData d = new ProductDetailsData();
		d.setId(p.getId());
		d.setBrand(p.getBrandPojo().getBrand());
		d.setCategory(p.getBrandPojo().getCategory());
		d.setMrp(p.getMrp());
		d.setName(p.getName());
		d.setBarcode(p.getBarcode());
		return d;
	}

}
