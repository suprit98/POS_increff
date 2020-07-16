package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.ProductDetailsData;
import com.increff.pos.model.ProductDetailsForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.ProductDetailsService;
import com.increff.pos.util.ConversionUtil;

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
		BrandPojo brand_pojo = brand_service.getBrandPojo(userform.getBrand(), userform.getCategory()); 
		ProductDetailsPojo p = ConversionUtil.convert(brand_pojo,userform);
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
		return ConversionUtil.convert(p);
	}

	@ApiOperation(value = "Gets list of Products")
	@RequestMapping(path = "/api/product_details", method = RequestMethod.GET)
	public List<ProductDetailsData> getAll() {
		List<ProductDetailsPojo> list = product_details_service.getAll();
		List<ProductDetailsData> list2 = ConversionUtil.convertProductList(list);
		return list2;
	}

	@ApiOperation(value = "Updates a ProductDetails record")
	@RequestMapping(path = "/api/product_details/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductDetailsForm userform) throws ApiException {
		BrandPojo brand_pojo = brand_service.getBrandPojo(userform.getBrand(), userform.getCategory());
		ProductDetailsPojo p = ConversionUtil.convert(brand_pojo,userform);
		product_details_service.update(id, p);
	}

	

}
