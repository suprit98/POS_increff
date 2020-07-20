package com.increff.pos.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.dao.ProductDetailsDao;
import com.increff.pos.pojo.ProductDetailsPojo;
import com.increff.pos.util.BarcodeUtil;

@Service
public class ProductDetailsService {
	
	@Autowired
	private ProductDetailsDao productdetails_dao;
	
	//Adding Product Details
	@Transactional
	public void add(ProductDetailsPojo p) throws ApiException {
		validate(p);
		normalize(p);
		p.setBarcode(BarcodeUtil.randomString(8));
		productdetails_dao.insert(p);
	}
	
	//Deletion of Product Details by id
	@Transactional
	public void delete(int id) {
		productdetails_dao.delete(id);	
	}
	
	//Fetching of product details by id
	@Transactional
	public ProductDetailsPojo get(int id) throws ApiException {
		ProductDetailsPojo p = checkIfExists(id);
		return p;
	}
	
	//Fetching of product details by barcode
	@Transactional
	public ProductDetailsPojo get(String barcode) throws ApiException {
		ProductDetailsPojo p = checkIfExists(barcode);
		return p;
	}
	
	//Fetching all product details
	@Transactional
	public List<ProductDetailsPojo> getAll() {
		return productdetails_dao.selectAll();
	}
	
	//Getting a map of product details pojos with barcode as key
	@Transactional
	public Map<String,ProductDetailsPojo> getAllProductPojosByBarcode() {
		List<ProductDetailsPojo> product_list = getAll();
		Map<String,ProductDetailsPojo> barcode_product = new HashMap<String,ProductDetailsPojo>();
		for(ProductDetailsPojo product:product_list) {
			barcode_product.put(product.getBarcode(), product);
		}
		return barcode_product;
	}
	
	//Update of product details
	@Transactional(rollbackFor = ApiException.class)
	public void update(int id, ProductDetailsPojo p) throws ApiException {
		validate(p);
		normalize(p);
		ProductDetailsPojo ex = checkIfExists(id);
		ex.setBarcode(BarcodeUtil.randomString(8));
		ex.setBrandPojo(p.getBrandPojo());
		ex.setMrp(p.getMrp());
		ex.setName(p.getName());
		productdetails_dao.update(p);
	}
	
	//Check if product exists with given id
	@Transactional(rollbackFor = ApiException.class)
	public ProductDetailsPojo checkIfExists(int id) throws ApiException {
		ProductDetailsPojo p = productdetails_dao.select(id);
		if(p == null) {
			throw new ApiException("ProductDetails with given ID does not exist, id: " + id);
		}
		return p;
	}
	
	//Check if product exists with given barcode
	@Transactional(rollbackFor = ApiException.class)
	public ProductDetailsPojo checkIfExists(String barcode) throws ApiException {
		ProductDetailsPojo p = productdetails_dao.select(barcode);
		if(p == null) {
			throw new ApiException("ProductDetails with given barcode does not exist, barcode: " + barcode);
		}
		return p;
	}
	
	//Normalize
	protected void normalize(ProductDetailsPojo p) {
		p.setName(p.getName().toLowerCase());		
	}
	
	//Validate
	protected void validate(ProductDetailsPojo p) throws ApiException {
		if(p.getName().isEmpty()) {
			throw new ApiException("The name of product must not be empty");
		}
		if(p.getMrp()<=0) {
			throw new ApiException("Mrp value should be positive");
		}
	}

}
