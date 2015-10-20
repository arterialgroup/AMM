package com.arterialgroup.arterialedu.web.rest;
/*TODO figure out why mock REST MVC for file upload always fails...*/
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.service.AttendeeService;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FileUploadResourceTest {

	private MockMultipartFile csvFile;

	@Mock
	private AttendeeService mockAttendeeService;

	private final String CSV_FILE = "/attendee.csv";

	private MockMvc mockRestMvcFileUpload;

	@PostConstruct
	public void setup() {
		MockitoAnnotations.initMocks(this);
		FileUploadResource fileUploadResource = new FileUploadResource();
		ReflectionTestUtils.setField(fileUploadResource, "attendeeService",
				mockAttendeeService);
		this.mockRestMvcFileUpload = MockMvcBuilders.standaloneSetup(
				fileUploadResource).build();
	}

	@Before
	public void before() throws FileNotFoundException, IOException {
		csvFile = new MockMultipartFile("attendees", new FileInputStream(
				new File(getClass().getResource(CSV_FILE).getFile())));
	}

	@Test
	@Ignore("Not ready yet, need to refactor service")
	public void testHandleAttendeeFileUpload() throws IOException, Exception {

		assertThat(csvFile).isNotNull();
		assertThat(csvFile.getBytes()).isNotNull();
		
		//TODO the service should just be passed a file, and REST service merely an endpoit
		Mockito.doNothing().when(mockAttendeeService).addAttendeesFromFile(csvFile.getInputStream(), (long)1);

		mockRestMvcFileUpload.perform(
				MockMvcRequestBuilders.fileUpload("/api/attendees/upload")
						.file(csvFile)).andExpect(status().isOk());

	}
}

