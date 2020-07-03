package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.spring.AbstractUnitTest;

public class BrandServiceTest extends AbstractUnitTest {

	@Autowired
	private BrandService brand_service;

	@Test()
	public void testAdd() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);

	}

	@Test(expected = ApiException.class)
	public void testAddDuplicate() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);
		brand_service.add(p);

	}

	@Test(expected = ApiException.class)
	public void testAddWrong() throws ApiException {

		BrandPojo p = getWrongBrandPojo();
		brand_service.add(p);

	}

	@Test()
	public void testDelete() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		int id = p.getId();
		brand_service.delete(id);

	}

	@Test()
	public void testGet() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		int id = p.getId();
		brand_service.get(id);

	}
	
	@Test(expected = ApiException.class)
	public void testGetNotExisting() throws ApiException {

		int id = 5;
		brand_service.get(id);

	}

	@Test()
	public void testUpdate() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		BrandPojo np = getNewBrandPojo();

		int id = p.getId();
		brand_service.update(id, np);

	}

	@Test(expected = ApiException.class)
	public void testUpdateWrong() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		BrandPojo np = getWrongBrandPojo();

		int id = p.getId();
		brand_service.update(id, np);

	}

	

	@Test()
	public void testGetAll() throws ApiException {

		brand_service.getAll();

	}

	@Test()
	public void testCheckIfExists() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		int id = p.getId();
		brand_service.checkIfExists(id);

	}

	@Test(expected = ApiException.class)
	public void testCheckIfExistsNotExisting() throws ApiException {

		int id = 5;
		brand_service.checkIfExists(id);

	}

	@Test()
	public void testGetId() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		brand_service.getId(p.getBrand(), p.getCategory());

	}

	@Test(expected = ApiException.class)
	public void testGetIdNotExisting() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		brand_service.getId("samplebrand", "samplecategory");

	}

	@Test
	public void testNormalize() throws ApiException {
		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		brand_service.normalize(p);
		assertEquals("parle", p.getBrand());
		assertEquals("biscuits", p.getCategory());
	}

	@Test
	public void testValidate() throws ApiException {
		BrandPojo p = getBrandPojo();

		brand_service.validate(p);
	}

	@Test(expected = ApiException.class)
	public void testValidateAlreadyExists() throws ApiException {
		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		BrandPojo np = getBrandPojo();
		np.setId(2);
		brand_service.validate(p);
	}

	private BrandPojo getBrandPojo() {
		BrandPojo p = new BrandPojo();
		p.setBrand("Parle");
		p.setCategory("Biscuits");
		return p;
	}

	private BrandPojo getNewBrandPojo() {
		BrandPojo p = new BrandPojo();
		p.setBrand("Parle2");
		p.setCategory("Biscuits2");
		return p;
	}

	private BrandPojo getWrongBrandPojo() {
		BrandPojo p = new BrandPojo();
		p.setBrand("");
		p.setCategory("");
		return p;
	}
}
