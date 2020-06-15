package com.increff.pos.service;

import java.util.List;

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
	
	@Transactional
	public void add(ProductDetailsPojo p) {
		normalize(p);
		p.setBarcode(BarcodeUtil.randomString(8));
		productdetails_dao.insert(p);
	}
	
	@Transactional
	public void delete(int id) {
		productdetails_dao.delete(id);	
	}
	
	@Transactional
	public ProductDetailsPojo get(int id) throws ApiException {
		ProductDetailsPojo p = checkIfExists(id);
		return p;
	}
	
	@Transactional
	public ProductDetailsPojo get(String barcode) throws ApiException {
		ProductDetailsPojo p = checkIfExists(barcode);
		return p;
	}
	
	@Transactional
	public List<ProductDetailsPojo> getAll() {
		return productdetails_dao.selectAll();
	}
	
	@Transactional(rollbackFor = ApiException.class)
	public void update(int id, ProductDetailsPojo p) throws ApiException {
		normalize(p);
		ProductDetailsPojo ex = checkIfExists(id);
		ex.setBarcode(BarcodeUtil.randomString(8));
		ex.setBrandPojo(p.getBrandPojo());
		ex.setMrp(p.getMrp());
		ex.setName(p.getName());
		productdetails_dao.update(p);
	}
	
	@Transactional(rollbackFor = ApiException.class)
	public ProductDetailsPojo checkIfExists(int id) throws ApiException {
		ProductDetailsPojo p = productdetails_dao.select(id);
		if(p == null) {
			throw new ApiException("ProductDetails with given ID does not exist, id: " + id);
		}
		return p;
	}
	
	@Transactional(rollbackFor = ApiException.class)
	public ProductDetailsPojo checkIfExists(String barcode) throws ApiException {
		ProductDetailsPojo p = productdetails_dao.select(barcode);
		if(p == null) {
			throw new ApiException("ProductDetails with given barcode does not exist, barcode: " + barcode);
		}
		return p;
	}
	
	protected static void normalize(ProductDetailsPojo p) {
		p.setName(p.getName().toLowerCase());		
	}

}
