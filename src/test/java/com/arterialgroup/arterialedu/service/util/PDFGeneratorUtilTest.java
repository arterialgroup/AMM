package com.arterialgroup.arterialedu.service.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.arterialgroup.arterialedu.Application;
import com.itextpdf.text.DocumentException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PDFGeneratorUtilTest {

	private final String PDF_TEMPLATE_PATH = "/certificate.pdf";
	private final String PDF_DOCUMENT_PATH = "/generated-cert.pdf";

	@Before
	public void setup(){
		
	}

	@Test
	@Ignore
	public void voidTestPDFGenerationNoException() throws DocumentException,
			IOException {

		Map<String, String> values = new HashMap<String, String>();
		values.put("Prefix and Name", "Dr Test");
		values.put("QI&CPD Number", "123456AF");
		values.put("Date", "01/01/2015");

		PDFGeneratorUtil.stampPDF(getClass().getResource(PDF_TEMPLATE_PATH)
				.getFile(), values, getClass().getResource(PDF_TEMPLATE_PATH)
				.getFile().replace(PDF_TEMPLATE_PATH, PDF_DOCUMENT_PATH));
	}

}
