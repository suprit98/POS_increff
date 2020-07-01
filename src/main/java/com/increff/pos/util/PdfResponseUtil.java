package com.increff.pos.util;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.increff.pos.model.InvoiceData;
import com.increff.pos.model.InvoiceDataList;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.ProductDetailsService;

public class PdfResponseUtil {
	
	public static void generatePdfResponse(ProductDetailsService product_service, List<OrderItemPojo> lis, HttpServletResponse response) throws Exception {
		
		List<InvoiceData> invoiceLis = ConversionUtil.convert(product_service,lis);
		InvoiceDataList idl = new InvoiceDataList();
		idl.setInvoiceLis(invoiceLis);
		idl.setOrder_id(lis.get(0).getOrderPojo().getId());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		idl.setDatetime(lis.get(0).getOrderPojo().getDatetime().format(formatter));
		XmlUtil.generateXML(idl);
		byte[] bytes = XmlUtil.generatePDF();
		
		response.setContentType("application/pdf");
	    response.setContentLength(bytes.length);
	    
	    response.getOutputStream().write(bytes);
	    response.getOutputStream().flush();
		
	}

}
