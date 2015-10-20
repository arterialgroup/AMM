package com.arterialgroup.arterialedu.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

public class CsvUtil {
	
	private static final Logger log = LoggerFactory.getLogger(CsvUtil.class);
	
	public static List<String[]> extractValues(InputStream stream){
		
		//Matrix of values
		List<String[]> extractedValues = null;
		
		CSVReader reader = new CSVReader(new InputStreamReader(stream));
		
		if(reader != null){
			try {
				extractedValues = reader.readAll();
			} catch (IOException e) {
				log.error("Unable to read CSV file: " + e.getMessage());
			}
		}
		
		return extractedValues;
	}

}
