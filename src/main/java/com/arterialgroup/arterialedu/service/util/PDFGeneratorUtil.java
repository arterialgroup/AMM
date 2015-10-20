package com.arterialgroup.arterialedu.service.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * File generation is external to this class so that fetched templates can be tested for existance and handled
 * by other business logic
 * @author bradleyr
 *
 */
public class PDFGeneratorUtil {

	public static void stampPDF(String templatePath, Map<String, String> templateValues, String destinationPath) throws DocumentException, IOException{
		
		PdfReader reader = new PdfReader(templatePath);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destinationPath));
		AcroFields form = stamper.getAcroFields();
		
		if(form != null && templateValues != null){
			//form.setField throws an exception, because of this
			//a lambda expression would have to internally capture and handle this
			//to simplyfy a normal foreach is used so the method throws declaration covers this
			for(Map.Entry<String, String> values : templateValues.entrySet()){
				form.setField(values.getKey(), values.getValue());
			}
		}

		stamper.close();
		reader.close();
	}
}
