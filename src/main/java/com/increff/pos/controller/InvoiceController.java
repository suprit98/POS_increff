package com.increff.pos.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.service.ReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InvoiceController {
	
	
	@Autowired
	private ReportService report_service;
	
	@ApiOperation(value = "Gets Invoice PDF by id")
	@RequestMapping(path = "/api/invoice/{id}", method = RequestMethod.GET)
	public void get(@PathVariable int id, HttpServletResponse response) throws Exception {
		byte[] bytes = report_service.generatePdfResponse("invoice", id);
		createPdfResponse(bytes, response);
	}
	
	public void createPdfResponse(byte[] bytes, HttpServletResponse response) throws IOException {
		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);

		response.getOutputStream().write(bytes);
		response.getOutputStream().flush();
	}

}
