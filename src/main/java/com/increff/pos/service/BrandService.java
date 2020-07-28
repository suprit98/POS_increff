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

	/* Adding Brand */
	@Transactional
	public void add(BrandPojo p) throws ApiException {
		validate(p);
		normalize(p);
		brand_dao.insert(p);
	}

	/* Deleting brand */
	@Transactional
	public void delete(int id) {
		brand_dao.delete(id);
	}

	/* Fetching Brand by Id */
	@Transactional
	public BrandPojo get(int id) throws ApiException {
		BrandPojo p = checkIfExists(id);
		return p;
	}

	/* Fetching all brands */
	@Transactional
	public List<BrandPojo> getAll() {
		return brand_dao.selectAll();
	}

	/* Updating existing brand */
	@Transactional(rollbackFor = ApiException.class)
	public void update(int id, BrandPojo p) throws ApiException {
		validate(p);
		normalize(p);
		BrandPojo ex = checkIfExists(id);
		ex.setBrand(p.getBrand());
		ex.setCategory(p.getCategory());
		brand_dao.update(p);
	}

	/* Checking if a particular brand exists */
	@Transactional(rollbackFor = ApiException.class)
	public BrandPojo checkIfExists(int id) throws ApiException {
		BrandPojo p = brand_dao.select(id);
		if (p == null) {
			throw new ApiException("Brand with given ID does not exist, id: " + id);
		}
		return p;
	}

	/* Getting a BrandPojo with particular brand and category */
	@Transactional
	public BrandPojo getBrandPojo(String brand, String category) throws ApiException {
		List<BrandPojo> brand_list = brand_dao.selectAllBrandCategory(brand, category);

		if (brand_list.isEmpty()) {
			throw new ApiException("The brand name and category given does not exist " + brand + " " + category);
		}
		return brand_list.get(0);
	}

	/* Normalizing */
	protected void normalize(BrandPojo p) {
		p.setBrand(p.getBrand().toLowerCase().trim());
		p.setCategory(p.getCategory().toLowerCase().trim());
	}

	/* Validation */
	protected void validate(BrandPojo p) throws ApiException {
		if (p.getBrand().isEmpty() || p.getCategory().isEmpty()) {
			throw new ApiException("Brand and category values must not be empty");
		}

		List<BrandPojo> ex = brand_dao.selectAllBrandCategory(p.getBrand(), p.getCategory());
		if (!ex.isEmpty()) {
			throw new ApiException("Brand and category values entered already exists");
		}

	}

}
