package com.arterialgroup.arterialedu.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.Meeting;
import com.arterialgroup.arterialedu.repository.MeetingRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class MeetingServiceTest {

	@Inject
	private MeetingService meetingService;

	@Inject
	private MeetingRepository meetingRepository;

	private static String RESOURCE_PATH = "testresources";
	private static String FILE = "attendee.csv";

	private static String GET_FILE_ONE = "one.txt";
	private static String GET_FILE_TWO = "two.txt";

	@Autowired
	private ServletContext context;

	@Before
	public void setup() throws IOException {
		//TODO setup the folder and tear down...
	}

	@Test
	@Ignore
	public void testAddResourceToMeeting() throws IOException {

		// initialize the database
		Meeting meeting = new Meeting();
		meeting.setName("MEETING");
		meeting.setStartDate(new LocalDate("2015-01-01"));
		meeting.setResourcePath(RESOURCE_PATH);
		meeting = meetingRepository.saveAndFlush(meeting);

		assertThat(
				meetingService.addResourceToMeeting(FILE, FILE.getBytes(),
						meeting.getId())).isNotNull().isTrue();

		File created = new File(context.getRealPath("/") + "/" + RESOURCE_PATH
				+ "/" + FILE);

		assertThat(created.exists()).isTrue();

		// clear down resources...
		created.delete();
	}

	@Test
	@Ignore
	public void testGetResourcesForMeeting() throws IOException {
		// initialize the database
		Meeting meeting = new Meeting();
		meeting.setName("MEETING");
		meeting.setStartDate(new LocalDate("2015-01-01"));
		meeting.setResourcePath(RESOURCE_PATH);
		meeting = meetingRepository.saveAndFlush(meeting);

		// setup files
		File one = new File(context.getRealPath("/") + "/" + RESOURCE_PATH
				+ "/" + GET_FILE_ONE);
		one.createNewFile();
		File two = new File(context.getRealPath("/") + "/" + RESOURCE_PATH
				+ "/" + GET_FILE_TWO);
		two.createNewFile();

		List<URL> fileUrls = meetingService.getResourcesForMeeting(meeting
				.getId());

		assertThat(fileUrls).isNotNull();
		assertThat(fileUrls.size()).isEqualTo(2);

		//tidy up
		one.delete();
		two.delete();
	}

	@After
	public void teardown() {
		//TODO setup the folder and tear down
	}

}
