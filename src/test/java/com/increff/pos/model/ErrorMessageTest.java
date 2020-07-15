package com.increff.pos.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.increff.pos.spring.AbstractUnitTest;

public class ErrorMessageTest extends AbstractUnitTest{
	
	@Test
	public void testErrorMessage() {
		ErrorMessage error_message = new ErrorMessage();
		error_message.setMessage("Api Exception");
		assertEquals(error_message.getMessage(),"Api Exception");
	}

}
