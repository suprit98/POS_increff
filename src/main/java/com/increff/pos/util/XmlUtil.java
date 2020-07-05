package com.increff.pos.util;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import com.increff.pos.model.BrandDataList;
import com.increff.pos.model.InventoryReportList;
import com.increff.pos.model.InvoiceData;
import com.increff.pos.model.InvoiceDataList;
import com.increff.pos.model.SalesDataList;

public class XmlUtil {

	public static void generateXmlInvoice(InvoiceDataList idl) throws Exception {

		double total = calculateTotal(idl);

		idl.setTotal(total);

		File file = new File("invoice.xml");
		JAXBContext context = JAXBContext.newInstance(InvoiceDataList.class);
		Marshaller m = context.createMarshaller();
		// for pretty-print XML in JAXB
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(idl, file);
	}

	public static void generateXmlBrandReport(BrandDataList idl) throws Exception {


		File file = new File("brand_report.xml");
		JAXBContext context = JAXBContext.newInstance(BrandDataList.class);
		Marshaller m = context.createMarshaller();
		// for pretty-print XML in JAXB
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(idl, file);
	}
	
	public static void generateXmlInventoryReport(InventoryReportList idl) throws Exception {


		File file = new File("inventory_report.xml");
		JAXBContext context = JAXBContext.newInstance(InventoryReportList.class);
		Marshaller m = context.createMarshaller();
		// for pretty-print XML in JAXB
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(idl, file);
	}

	public static byte[] generatePDF(File xml_file, StreamSource xsl_source) throws Exception {
		FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
		// Setup a buffer to obtain the content length
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// Setup FOP
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(xsl_source);
		// Make sure the XSL transformation's result is piped through to FOP
		Result res = new SAXResult(fop.getDefaultHandler());

		// Setup input
		Source src = new StreamSource(xml_file);

		// Start the transformation and rendering process
		transformer.transform(src, res);

		byte[] bytes = out.toByteArray();

		out.close();
		out.flush();

		return bytes;

	}

	private static double calculateTotal(InvoiceDataList idl) {
		double total = 0;
		for (InvoiceData i : idl.getInvoiceLis()) {
			total += (i.getMrp() * i.getQuantity());
		}
		return total;
	}

	public static void generateXmlSalesReport(SalesDataList sales_data_list) throws JAXBException {
		File file = new File("sales_report.xml");
		JAXBContext context = JAXBContext.newInstance(SalesDataList.class);
		Marshaller m = context.createMarshaller();
		// for pretty-print XML in JAXB
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(sales_data_list, file);
		
	}


}
