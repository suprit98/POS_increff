package com.increff.pos.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UIController {

	@Value("${app.baseUrl}")
	private String baseUrl;

	@RequestMapping(value = "")
	public ModelAndView index() {
		return mav("index.html");
	}

	@RequestMapping(value = "/ui/home")
	public ModelAndView home() {
		return mav("home.html");
	}

	@RequestMapping(value = "/ui/brand")
	public ModelAndView brand() {
		return mav("brand.html");
	}

	@RequestMapping(value = "/ui/product_details")
	public ModelAndView product_details() {
		return mav("product_details.html");
	}

	@RequestMapping(value = "/ui/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}

	@RequestMapping(value = "/ui/order")
	public ModelAndView order() {
		return mav("order.html");
	}
	
	@RequestMapping(value = "/ui/previous_orders")
	public ModelAndView previous_orders() {
		return mav("previousorders.html");
	}
	
	
	@RequestMapping(value = "/ui/reports")
	public ModelAndView reports() {
		return mav("reports.html");
	}

	private ModelAndView mav(String page) {
		ModelAndView mav = new ModelAndView(page);
		mav.addObject("baseUrl", baseUrl);
		return mav;

	}

}
