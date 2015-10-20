package com.arterialgroup.arterialedu.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

import com.arterialgroup.arterialedu.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CsvUtilTest {

	private final String TEST_CSV_FILE_PATH = "/attendee.csv";
	private final String TEST_BLANK_CSV_FILE_PATH = "/blank_attendee.csv";
	
	@Before
	public void setup(){
		
	}
	
	@Test
	@Ignore
	public void testCSVExtract() throws FileNotFoundException{
		
		List<String[]> results = CsvUtil.extractValues(new FileInputStream(new File(getClass().getResource(TEST_CSV_FILE_PATH).getFile())));
		
		assertThat(results).isNotNull();
		assertThat(results).isNotEmpty();
		assertThat(results.size()).isEqualTo(2);
		assertThat(results.get(0).length).isEqualTo(results.get(1).length);
		
	}
	
	@Test
	@Ignore
	public void testCSVExtractNoValues() throws FileNotFoundException{
		
		List<String[]> results = CsvUtil.extractValues(new FileInputStream(new File(getClass().getResource(TEST_BLANK_CSV_FILE_PATH).getFile())));
		
		assertThat(results).isNotNull();
		assertThat(results).isEmpty();
		assertThat(results.size()).isEqualTo(0);
		
	}
}
