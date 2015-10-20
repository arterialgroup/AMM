package com.arterialgroup.arterialedu.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.Attendee;
import com.arterialgroup.arterialedu.domain.Meeting;
import com.arterialgroup.arterialedu.domain.MeetingModule;
import com.arterialgroup.arterialedu.domain.Module;
import com.arterialgroup.arterialedu.domain.User;
import com.arterialgroup.arterialedu.repository.AttendeeRepository;
import com.arterialgroup.arterialedu.repository.MeetingModuleRepository;
import com.arterialgroup.arterialedu.repository.MeetingRepository;
import com.arterialgroup.arterialedu.repository.ModuleRepository;
import com.arterialgroup.arterialedu.repository.UserModuleRepository;
import com.arterialgroup.arterialedu.repository.UserRepository;
import com.arterialgroup.arterialedu.web.rest.dto.UserDTO;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class AttendeeServiceTest {

	private final String TEST_CSV_FILE_PATH = "/attendee.csv";
	private final String TEST_BLANK_CSV_FILE_PATH = "/blank_attendee.csv";

	@Inject
	private AttendeeRepository attendeeRepository;
	
	@Inject
	private UserRepository userRepository;

	@Inject
	private MeetingRepository meetingRepository;

	@Inject
	private ModuleRepository moduleRepository;

	@Inject
	private MeetingModuleRepository meetingModuleRepository;

	@Inject
	private UserModuleRepository userModuleRepository;

	@Inject
	private AttendeeService attendeeService;

	@Before
	public void setup() {
	}

	@Test
	@Ignore
	public void testAddAttendees() throws FileNotFoundException {

		// initialize the db
		Meeting meeting = new Meeting();
		meeting.setName("TEST");
		meeting.setStartDate(new LocalDate("2015-01-01"));
		meeting = meetingRepository.saveAndFlush(meeting);

		attendeeService.addAttendeesFromFile(new FileInputStream(new File(
				getClass().getResource(TEST_CSV_FILE_PATH).getFile())), meeting
				.getId());

		assertThat(attendeeRepository.findAll()).isNotNull();
		assertThat(attendeeRepository.findAll().size()).isEqualTo(2);
		assertThat(attendeeRepository.findAll().get(0).getUser()).isNotNull();
		assertThat(attendeeRepository.findAll().get(0).getUser().getAccreditation())
				.isEqualTo("123456");
		assertThat(attendeeRepository.findAll().get(0).getUser().getAccreditationType())
				.isEqualTo("RACGP");
		assertThat(attendeeRepository.findAll().get(0).getUser().getFirstName())
				.isEqualTo("Test");
		assertThat(attendeeRepository.findAll().get(0).getUser().getLastName())
				.isEqualTo("Doctor");
		assertThat(attendeeRepository.findAll().get(0).getUser().getEmail())
				.isEqualTo("test@doctor.com");
		assertThat(attendeeRepository.findAll().get(0).getUser().getState())
				.isEqualTo("NSW");
		assertThat(attendeeRepository.findAll().get(0).getUser().getLogin())
				.isEqualTo("test@doctor.com");
		assertThat(attendeeRepository.findAll().get(0).getImported())
				.isEqualTo(true);

		assertThat(attendeeRepository.findAll().get(1).getUser()).isNotNull();
		assertThat(attendeeRepository.findAll().get(1).getUser().getAccreditation())
				.isEqualTo("123456");
		assertThat(attendeeRepository.findAll().get(1).getUser().getAccreditationType())
				.isEqualTo("RACGP");
		assertThat(attendeeRepository.findAll().get(1).getUser().getFirstName())
				.isEqualTo("Second");
		assertThat(attendeeRepository.findAll().get(1).getUser().getLastName())
				.isEqualTo("Person");
		assertThat(attendeeRepository.findAll().get(1).getUser().getEmail())
				.isEqualTo("second@person.com");
		assertThat(attendeeRepository.findAll().get(1).getUser().getState())
				.isEqualTo("NSW");
		assertThat(attendeeRepository.findAll().get(1).getUser().getLogin())
				.isEqualTo("second@person.com");
		assertThat(attendeeRepository.findAll().get(1).getImported())
			.isEqualTo(true);

	}

	@Test
	@Ignore
	public void testAddNoAttendees() throws FileNotFoundException {

		// initialize the db
		Meeting meeting = new Meeting();
		meeting.setName("TEST");
		meeting.setStartDate(new LocalDate("2015-01-01"));
		meeting = meetingRepository.saveAndFlush(meeting);

		attendeeService.addAttendeesFromFile(new FileInputStream(new File(
				getClass().getResource(TEST_BLANK_CSV_FILE_PATH).getFile())),
				meeting.getId());

		assertThat(attendeeRepository.findAll().size()).isEqualTo(0);
	}

	@Test
	public void testAddAttendeeFromRegistration() {

		// initialize the db
		Meeting meeting = new Meeting();
		meeting.setName("TEST");
		meeting.setActivityId((long)123);
		meeting.setStartDate(new LocalDate("2015-01-01"));
		meeting = meetingRepository.saveAndFlush(meeting);

		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_USER");

		//public UserDTO(Long id, String login, String password, String accreditation, String firstName, String lastName, String email, String langKey,

		UserDTO user = new UserDTO(null, "test@test.com", "RACGP", "123456", "123456", "first", "last",
				"test@test.com", "NSW", "en", roles);

		assertThat(attendeeService.addAttendee(user, meeting.getActivityId()))
				.isNotNull().isTrue();

		assertThat(attendeeRepository.findAll()).isNotNull();
		assertThat(attendeeRepository.findAll().size()).isEqualTo(1);
		assertThat(attendeeRepository.findAll().get(0).getUser()).isNotNull();
		
		assertThat(attendeeRepository.findAll().get(0).getUser().getLogin())
				.isEqualTo("test@test.com");
		assertThat(attendeeRepository.findAll().get(0).getUser().getAccreditation())
				.isEqualTo("123456");
		assertThat(attendeeRepository.findAll().get(0).getUser().getAccreditationType())
				.isEqualTo("123456");
		assertThat(attendeeRepository.findAll().get(0).getUser().getFirstName())
				.isEqualTo("first");
		assertThat(attendeeRepository.findAll().get(0).getUser().getLastName())
				.isEqualTo("last");
		assertThat(attendeeRepository.findAll().get(0).getUser().getEmail())
				.isEqualTo("test@test.com");
		assertThat(attendeeRepository.findAll().get(0).getUser().getState())
				.isEqualTo(null);
		assertThat(attendeeRepository.findAll().get(0).getImported())
			.isEqualTo(false);
	}

	@Test
	public void testAddAttendeeFromRegistrationWithModules() {

		// initialize the db
		Meeting meeting = new Meeting();
		meeting.setName("TEST");
		meeting.setActivityId((long)123);
		meeting.setStartDate(new LocalDate("2015-01-01"));
		meeting = meetingRepository.saveAndFlush(meeting);

		Module module = new Module();
		module.setName("MODULE");
		module = moduleRepository.saveAndFlush(module);

		MeetingModule meetingModule = new MeetingModule();
		meetingModule.setMeeting(meeting);
		meetingModule.setModule(module);
		meetingModule = meetingModuleRepository.saveAndFlush(meetingModule);

		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_USER");

		// UserDTO user = new UserDTO(null, "123456", "test@test.com", "test@test.com", "first", "last",
		// 		"test@test.com", "en", roles);

		UserDTO user = new UserDTO(null, "test@test.com", "123456", "123456", "RACGP", "first", "last",
				"test@test.com", "NSW", "en", roles);

		assertThat(attendeeService.addAttendee(user, meeting.getActivityId()))
				.isNotNull().isTrue();

		assertThat(attendeeRepository.findAll()).isNotNull();
		assertThat(attendeeRepository.findAll().size()).isEqualTo(1);
		assertThat(attendeeRepository.findAll().get(0).getUser()).isNotNull();

		Attendee attendee = attendeeRepository.findAll().get(0);
		
		assertThat(attendee.getUser().getAccreditation()).isEqualTo("123456");
		assertThat(attendee.getUser().getFirstName()).isEqualTo("first");
		assertThat(attendee.getUser().getLastName()).isEqualTo("last");
		assertThat(attendee.getUser().getEmail()).isEqualTo("test@test.com");
		assertThat(attendee.getUser().getState()).isEqualTo(null);
		assertThat(attendee.getUser().getLogin()).isEqualTo("test@test.com");
		assertThat(attendee.getMeeting()).isEqualTo(meeting);

		assertThat(userModuleRepository.findByUser(attendee.getUser()))
				.isNotNull();
		assertThat(
				userModuleRepository.findByUser(attendee.getUser()).size())
				.isEqualTo(1);
	}
	
	@Test
	public void testAssignModulesToAttendee(){
		
		//create an attendee assigned to a meeting
		User user = new User();
		user.setLogin("login1234");
		user.setPassword("Password1234");
		user.setLangKey("en");
		user = userRepository.saveAndFlush(user);
		
		Meeting meeting = new Meeting();
		meeting.setName("MEETING");
		meeting.setStartDate(new LocalDate("2015-01-01"));
		meeting = meetingRepository.saveAndFlush(meeting);
		
		Attendee attendee = new Attendee();
		attendee.setUser(user);
		attendee.setMeeting(meeting);
		attendee = attendeeRepository.saveAndFlush(attendee);
		
		Module module = new Module();
		module.setName("MODULE");

		module = moduleRepository.saveAndFlush(module);
		
		MeetingModule meetMod = new MeetingModule();
		meetMod.setMeeting(meeting);
		meetMod.setModule(module);
		
		//We mock a module being added to a meeting after the attendee exists
		meetMod = meetingModuleRepository.saveAndFlush(meetMod);
		
		//Verify this attendee is not assigned to this meeting
		assertThat(userModuleRepository.findByUser(attendee.getUser()).size()).isEqualTo(0);
		
		//now add them
		attendeeService.assignModulesForAttendees(meetMod);
		
		//Verify we signed them up accordingly
		assertThat(userModuleRepository.findByUser(attendee.getUser()).size()).isEqualTo(1);
		assertThat(userModuleRepository.findByUser(attendee.getUser()).get(0).getUser()).isEqualTo(attendee.getUser());
		assertThat(userModuleRepository.findByUser(attendee.getUser()).get(0).getModule()).isEqualTo(module);
	}

}
