package com.increff.pos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.pojo.BrandPojo;


@Service
public class BrandService {
	
	@Autowired
	private BrandDao brand_dao;
	
	@Transactional
	public void add(BrandPojo p) throws ApiException {
		validate(p);
		normalize(p);
		brand_dao.insert(p);
	}
	

	@Transactional
	public void delete(int id) {
		brand_dao.delete(id);	
	}
	
	@Transactional
	public BrandPojo get(int id) throws ApiException {
		BrandPojo p = checkIfExists(id);
		return p;
	}
	
	@Transactional
	public List<BrandPojo> getAll() {
		return brand_dao.selectAll();
	}
	
	@Transactional(rollbackFor = ApiException.class)
	public void update(int id, BrandPojo p) throws ApiException {
		normalize(p);
		BrandPojo ex = checkIfExists(id);
		ex.setBrand(p.getBrand());
		ex.setCategory(p.getCategory());
		brand_dao.update(p);
	}
	
	@Transactional(rollbackFor = ApiException.class)
	public BrandPojo checkIfExists(int id) throws ApiException {
		BrandPojo p = brand_dao.select(id);
		if(p == null) {
			throw new ApiException("Brand with given ID does not exist, id: " + id);
		}
		return p;
	}
	
	@Transactional
	public int getId(String brand, String category) throws ApiException {
		List<BrandPojo> lis = getAll();
		int id=-1;
		for(BrandPojo p: lis) {
			if(brand.contentEquals(p.getBrand()) && category.contentEquals(p.getCategory())) {
				id=p.getId();
				break;
			}
		}
		if(id==-1) {
			throw new ApiException("The brand name and category given does not exist " + brand + " " + category);
		}
		return id;
	}
	
	protected static void normalize(BrandPojo p) {
		p.setBrand(p.getBrand().toLowerCase().trim());
		p.setCategory(p.getCategory().toLowerCase().trim());		
	}
	
	private void validate(BrandPojo p) throws ApiException {
		if(p.getBrand().isEmpty() || p.getCategory().isEmpty()) {
			throw new ApiException("Brand and category values must not be empty");
		}
		
		BrandPojo ex = brand_dao.selectAllBrandCategory(p.getBrand(), p.getCategory());
		if(ex!=null) {
			throw new ApiException("Brand and category values entered already exists");
		}
		
	}

}
