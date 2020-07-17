package com.increff.pos.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.spring.AbstractUnitTest;

public class BrandServiceTest extends AbstractUnitTest {

	@Before
	public void init() throws ApiException {
		// Inserting some initial pojos
		insertPojos();
	}

	// Testing Adding of brand
	@Test()
	public void testAdd() throws ApiException {

		BrandPojo p = getBrandPojo();
		List<BrandPojo> brand_list_before = brand_service.getAll();
		brand_service.add(p);
		List<BrandPojo> brand_list_after = brand_service.getAll();

		// Number of brands should increase
		assertEquals(brand_list_before.size() + 1, brand_list_after.size());
		assertEquals(p.getBrand(), brand_service.get(p.getId()).getBrand());
		assertEquals(p.getCategory(), brand_service.get(p.getId()).getCategory());

	}

	// Testing Adding of duplicates. Exception should be thrown
	@Test()
	public void testAddDuplicate() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		try {
			brand_service.add(p);
			fail("Api Exception did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Brand and category values entered already exists");
		}

	}

	// Testing adding of invalid brand pojo. Exception should be thrown
	@Test()
	public void testAddWrong() throws ApiException {

		BrandPojo p = getWrongBrandPojo();
		try {
			brand_service.add(p);
			fail("Api Exception did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Brand and category values must not be empty");
		}

	}

	// Testing deletion of an existing brand pojo
	@Test()
	public void testDelete() throws ApiException {

		BrandPojo p = getBrandPojo();
		brand_service.add(p);

		List<BrandPojo> brand_list_before = brand_service.getAll();
		int id = p.getId();
		brand_service.delete(id);
		List<BrandPojo> brand_list_after = brand_service.getAll();

		// Number of brand pojos should decrease by one
		assertEquals(brand_list_before.size() - 1, brand_list_after.size());

		try {
			brand_service.get(id);
			fail("Api Exception did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Brand with given ID does not exist, id: " + id);
		}

	}

	// Testing Get for brand pojo
	@Test()
	public void testGet() throws ApiException {

		int id = brands.get(0).getId();
		brand_service.get(id);
		assertEquals(brands.get(0).getBrand(), brand_service.get(id).getBrand());
		assertEquals(brands.get(0).getCategory(), brand_service.get(id).getCategory());

	}

	// Testing Get for a non-existent pojo. Should throw exception
	@Test()
	public void testGetNotExisting() throws ApiException {

		int id = 5;
		try {
			brand_service.get(id);
			fail("Api Exception did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Brand with given ID does not exist, id: " + id);
		}

	}

	// Testing update
	@Test()
	public void testUpdate() throws ApiException {

		BrandPojo np = getNewBrandPojo();
		int id = brands.get(0).getId();
		brand_service.update(id, np);
		assertEquals(np.getBrand(), brand_service.get(id).getBrand());
		assertEquals(np.getCategory(), brand_service.get(id).getCategory());

	}

	// Testing update with invalid brand pojo. Exception should be thrown
	@Test()
	public void testUpdateWrong() throws ApiException {

		BrandPojo np = getWrongBrandPojo();

		int id = brands.get(0).getId();
		try {
			brand_service.update(id, np);
			fail("Api Exception did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Brand and category values must not be empty");
		}

	}

	// Testing get all brand pojos
	@Test()
	public void testGetAll() throws ApiException {

		List<BrandPojo> get_brand_list = brand_service.getAll();
		assertEquals(2, get_brand_list.size());

	}

	// Testing checkifexists
	@Test()
	public void testCheckIfExists() throws ApiException {

		int id = brands.get(0).getId();
		BrandPojo db_brand_pojo = brand_service.checkIfExists(id);
		assertEquals(brands.get(0).getBrand(), db_brand_pojo.getBrand());
		assertEquals(brands.get(0).getCategory(), db_brand_pojo.getCategory());

	}

	// Testing checkifexists for a non-existent pojo
	@Test()
	public void testCheckIfExistsNotExisting() throws ApiException {

		int id = 100;
		try {
			brand_service.checkIfExists(id);
			fail("Api Exception did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(), "Brand with given ID does not exist, id: " + id);
		}

	}

	// Testing getting of brand id based on brand and category
	@Test()
	public void testGetId() throws ApiException {

		BrandPojo p = brands.get(0);

		BrandPojo brand_pojo = brand_service.getBrandPojo(p.getBrand(), p.getCategory());
		assertEquals(brand_pojo.getBrand(), p.getBrand());
		assertEquals(brand_pojo.getCategory(), p.getCategory());

	}

	// Testing getting of brand id based on a brand and category that does not
	// exist. Should throw an exception
	@Test()
	public void testGetIdNotExisting() throws ApiException {

		try {
			brand_service.getBrandPojo("samplebrand", "samplecategory");
			fail("Api Exception did not occur");
		} catch (ApiException e) {
			assertEquals(e.getMessage(),
					"The brand name and category given does not exist " + "samplebrand" + " " + "samplecategory");
		}

	}

	// Testing normalize
	@Test
	public void testNormalize() throws ApiException {
		BrandPojo p = getBrandPojo();

		brand_service.normalize(p);
		assertEquals("parle", p.getBrand());
		assertEquals("biscuits", p.getCategory());
	}

	// Testing Validate
	@Test
	public void testValidate() throws ApiException {
		BrandPojo p = getBrandPojo();

		brand_service.validate(p);
		assertTrue(!p.getBrand().isEmpty());
		assertTrue(!p.getCategory().isEmpty());
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
