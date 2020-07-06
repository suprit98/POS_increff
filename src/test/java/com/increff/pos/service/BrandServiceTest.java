package com.increff.pos.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

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
		
		assertEquals(p.getBrand(),brand_service.get(p.getId()).getBrand());
		assertEquals(p.getCategory(),brand_service.get(p.getId()).getCategory());

	}

	@Test()
	public void testAddDuplicate() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);
		
		try {
			brand_service.add(p);
			fail("Api Exception did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),"Brand and category values entered already exists");
		}
		

	}

	@Test()
	public void testAddWrong() throws ApiException {

		BrandPojo p = getWrongBrandPojo();
		try {
			brand_service.add(p);
			fail("Api Exception did not occur");
		} catch(ApiException e) {
			assertEquals(e.getMessage(),"Brand and category values must not be empty");
		}
		

	}

	@Test()
	public void testDelete() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		int id = p.getId();
		brand_service.delete(id);
		
		try {
			brand_service.get(id);
			fail("Api Exception did not occur");
		} catch(ApiException e) {
			assertEquals(e.getMessage(),"Brand with given ID does not exist, id: " + id);
		}

	}

	@Test()
	public void testGet() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		int id = p.getId();
		brand_service.get(id);
		assertEquals(p.getBrand(),brand_service.get(id).getBrand());
		assertEquals(p.getCategory(),brand_service.get(id).getCategory());

	}
	
	@Test()
	public void testGetNotExisting() throws ApiException {

		int id = 5;
		try {
			brand_service.get(id);
			fail("Api Exception did not occur");
		} catch(ApiException e) {
			assertEquals(e.getMessage(),"Brand with given ID does not exist, id: " + id);
		}
		

	}

	@Test()
	public void testUpdate() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		BrandPojo np = getNewBrandPojo();

		int id = p.getId();
		brand_service.update(id, np);
		assertEquals(np.getBrand(),brand_service.get(id).getBrand());
		assertEquals(np.getCategory(),brand_service.get(id).getCategory());

	}

	@Test()
	public void testUpdateWrong() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		BrandPojo np = getWrongBrandPojo();

		int id = p.getId();
		try {
			brand_service.update(id, np);
			fail("Api Exception did not occur");
		} catch(ApiException e) {
			assertEquals(e.getMessage(),"Brand and category values must not be empty");
		}
		

	}

	

	@Test()
	public void testGetAll() throws ApiException {
		
		List<BrandPojo> brand_list = getMultipleBrandPojo();
		for(BrandPojo brand:brand_list) {
			brand_service.add(brand);
		}
		List<BrandPojo> get_brand_list = brand_service.getAll();
		assertEquals(brand_list.size(),get_brand_list.size());

	}

	@Test()
	public void testCheckIfExists() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		int id = p.getId();
		BrandPojo db_brand_pojo = brand_service.checkIfExists(id);
		assertEquals(p.getBrand(),db_brand_pojo.getBrand());
		assertEquals(p.getCategory(),db_brand_pojo.getCategory());

	}

	@Test()
	public void testCheckIfExistsNotExisting() throws ApiException {

		int id = 5;
		try{
			brand_service.checkIfExists(id);
			fail("Api Exception did not occur");
		} catch(ApiException e) {
			assertEquals(e.getMessage(),"Brand with given ID does not exist, id: " + id);
		}
		

	}

	@Test()
	public void testGetId() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		int id = brand_service.getId(p.getBrand(), p.getCategory());
		assertEquals(brand_service.get(id).getBrand(), p.getBrand());
		assertEquals(brand_service.get(id).getCategory(), p.getCategory());

	}

	@Test()
	public void testGetIdNotExisting() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);
		try {
			brand_service.getId("samplebrand", "samplecategory");
			fail("Api Exception did not occur");
		} catch(ApiException e) {
			assertEquals(e.getMessage(),"The brand name and category given does not exist " + "samplebrand" + " " + "samplecategory");
		}
		

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
		assertTrue(!p.getBrand().isEmpty());
		assertTrue(!p.getCategory().isEmpty());
	}

	@Test()
	public void testValidateAlreadyExists() throws ApiException {
		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		BrandPojo np = getBrandPojo();
		np.setId(2);
		
		try {
			brand_service.validate(p);
			fail("Api Exception did not occur");
		} catch(ApiException e) {
			assertEquals(e.getMessage(),"Brand and category values entered already exists");
		}
		
	}

	private BrandPojo getBrandPojo() {
		BrandPojo p = new BrandPojo();
		p.setBrand("Parle");
		p.setCategory("Biscuits");
		return p;
	}
	
	private List<BrandPojo> getMultipleBrandPojo() {
		List<BrandPojo> brand_list = new ArrayList<BrandPojo>();
		for(int i=0; i<5; i++) {
			BrandPojo brand = new BrandPojo();
			brand.setBrand("brand"+i);
			brand.setCategory("category"+i);
			brand_list.add(brand);
		}
		return brand_list;
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
