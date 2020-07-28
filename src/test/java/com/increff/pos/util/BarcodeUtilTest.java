package com.increff.pos.util;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.increff.pos.spring.AbstractUnitTest;

public class BarcodeUtilTest extends AbstractUnitTest{
	
	/* Testing generation of Barcode */
	@Test
	public void generateBarcode() {
		String s1 = BarcodeUtil.randomString(8);
		String s2 = BarcodeUtil.randomString(8);
		
		assertFalse(s1.contentEquals(s2));
	}

}
