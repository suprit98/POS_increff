package com.increff.pos.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.SalesFilter;
import com.increff.pos.service.ReportService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ReportController {

	@Autowired
	private ReportService report_service;

	@ApiOperation(value = "Gets Brand Report")
	@RequestMapping(path = "/api/report/brand", method = RequestMethod.GET)
	public void get(HttpServletResponse response) throws Exception {
		byte[] bytes = report_service.generatePdfResponse("brand");
		createPdfResponse(bytes, response);
	}

	@ApiOperation(value = "Gets Inventory Report")
	@RequestMapping(path = "/api/report/inventory", method = RequestMethod.GET)
	public void getInventory(HttpServletResponse response) throws Exception {
		byte[] bytes = report_service.generatePdfResponse("inventory");
		createPdfResponse(bytes, response);
	}

	@ApiOperation(value = "Gets Sales Report")
	@RequestMapping(path = "/api/report/sales", method = RequestMethod.POST)
	public void getSales(@RequestBody SalesFilter sales_filter, HttpServletResponse response) throws Exception {
		byte[] bytes = report_service.generatePdfResponse("sales", sales_filter);
		createPdfResponse(bytes, response);
	}

	public void createPdfResponse(byte[] bytes, HttpServletResponse response) throws IOException {
		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);

		response.getOutputStream().write(bytes);
		response.getOutputStream().flush();
	}

}
