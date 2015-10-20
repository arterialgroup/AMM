package com.arterialgroup.arterialedu.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.junit.Before;
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
import com.arterialgroup.arterialedu.domain.Module;
import com.arterialgroup.arterialedu.domain.Step;
import com.arterialgroup.arterialedu.domain.User;
import com.arterialgroup.arterialedu.domain.UserModule;
import com.arterialgroup.arterialedu.domain.UserProgress;
import com.arterialgroup.arterialedu.repository.AttendeeRepository;
import com.arterialgroup.arterialedu.repository.MeetingRepository;
import com.arterialgroup.arterialedu.repository.ModuleRepository;
import com.arterialgroup.arterialedu.repository.StepRepository;
import com.arterialgroup.arterialedu.repository.UserModuleRepository;
import com.arterialgroup.arterialedu.repository.UserProgressRepository;
import com.arterialgroup.arterialedu.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class UserProgressServiceTest {
	
	@Inject
	private UserProgressRepository userProgressRespository;
	
	@Inject
	private UserModuleRepository userModuleRepository;
	
	@Inject
	private StepRepository stepRepository;
	
	@Inject
	private MeetingRepository meetingRepository;
	
	@Inject
	private ModuleRepository moduleRepository;
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private AttendeeRepository attendeeRepository;
	
	@Inject
	private UserProgressService userProgressService;
	
	private UserModule mockUserModule;
	
	@Before
	public void setup(){
		mockUserModule = new UserModule();
		userModuleRepository.saveAndFlush(mockUserModule);
	}
	
	@Test
	public void testStartProgress(){
		LocalDate now = new LocalDate("2015-01-01");
		userProgressService.startProgress(mockUserModule.getId(), now);
		assertThat(userProgressRespository.count()).isEqualTo(1);
		assertThat(userProgressRespository.findOneByUserModule(mockUserModule).get()).isNotNull();
		assertThat(userProgressRespository.findOneByUserModule(mockUserModule).get().getStartDate()).isEqualTo(new LocalDate("2015-01-01"));
	}
	
	@Test
	public void testUpdateProgress(){
		Step step = new Step();
		step.setName("TEST_STEP");
		stepRepository.saveAndFlush(step);
		
		//mock a startProgress...
		UserProgress progress = new UserProgress();
		progress.setUserModule(mockUserModule);
		progress.setStartDate(new LocalDate("2015-01-01"));
		progress.setStep(null);
		progress = userProgressRespository.saveAndFlush(progress);
		
		//we start with one record
		assertThat(userProgressRespository.count()).isEqualTo(1);
		assertThat(userProgressRespository.findOneByUserModule(mockUserModule).get().getStep()).isNotEqualTo(step);
		
		userProgressService.trackProgress(progress.getId(), step.getId());
		
		//we only update the step record..
		assertThat(userProgressRespository.count()).isEqualTo(1);
		assertThat(userProgressRespository.findOneByUserModule(mockUserModule).get().getStep()).isEqualTo(step);
	}
	
	@Test
	public void testGetProgress(){
		
		Step step = new Step();
		step.setName("TEST_STEP");
		stepRepository.saveAndFlush(step);
		
		//mock a startProgress...
		UserProgress progress = new UserProgress();
		progress.setUserModule(mockUserModule);
		progress.setStartDate(new LocalDate("2015-01-01"));
		progress.setStep(null);
		userProgressRespository.saveAndFlush(progress);
		
		//we must have a record to fetch
		assertThat(userProgressRespository.count()).isEqualTo(1);
		
		UserProgress current = userProgressService.getCurrentProgress(mockUserModule.getId());
		assertThat(current).isNotNull();
		assertThat(current.getStep()).isNotEqualTo(step);
		
		//now mock a track progress
		progress.setStep(step);
		userProgressRespository.saveAndFlush(progress);
		
		current = userProgressService.getCurrentProgress(mockUserModule.getId());
		assertThat(current).isNotNull();
		assertThat(current.getStep()).isEqualTo(step);
	}
	
	@Test
	public void testEndProgress(){
		//step id must be greater then 0
		//We need a progress object to start with
		Step step = new Step();
		step.setName("TEST_STEP");
		stepRepository.saveAndFlush(step);
		
		UserProgress progress = new UserProgress();
		progress.setUserModule(mockUserModule);
		progress.setStartDate(new LocalDate("2015-01-01"));
		progress.setStep(step);
		progress = userProgressRespository.saveAndFlush(progress);
		
		LocalDate now = new LocalDate("2015-02-01");
		userProgressService.endProgress(progress.getId(), step.getId(), now);
		assertThat(userProgressRespository.count()).isEqualTo(1);
		assertThat(userProgressRespository.findOneByUserModule(mockUserModule).get()).isNotNull();
		assertThat(userProgressRespository.findOneByUserModule(mockUserModule).get().getEndDate()).isEqualTo(new LocalDate("2015-02-01"));
	}
	
	@Test
	public void testEndProgressNoRecord(){
		LocalDate now = new LocalDate("2015-02-01");
		userProgressService.endProgress(new Long(1), new Long(1), now);
		//no progress record should exists
		assertThat(userProgressRespository.count()).isEqualTo(0);
		assertThat(userProgressRespository.findOneByUserModule(mockUserModule)).isEmpty();
	}
	
	@Test
	public void testGetProgressForMeeting(){
		
		//create a meeting
		Meeting meeting = new Meeting();
		meeting.setName("TEST MEETING");
		meeting.setStartDate(new LocalDate("2015-01-01"));
		meeting = meetingRepository.saveAndFlush(meeting);
		
		//create an attendee (and user)
		User user = new User();
		user.setLogin("login1234");
		user.setPassword("Password1234");
		user.setLangKey("en");
		user = userRepository.saveAndFlush(user);
		
		Attendee attendee = new Attendee();
		attendee.setUser(user);
		attendee.setMeeting(meeting);
		attendee = attendeeRepository.saveAndFlush(attendee);
	
		//create a module
		Module module = new Module();
		module.setName("TEST MODULE");
		module = moduleRepository.saveAndFlush(module);
		
		//The data module has modules for meeting, but we don't need that modeled here
		//TODO might indicate we dont need this table in the db schema
		//we imply meeting -> user -> module relationships by the meeting and user combo alone
		
		//update the user module
		mockUserModule.setUser(user);
		mockUserModule.setModule(module);
		mockUserModule = userModuleRepository.saveAndFlush(mockUserModule);
		
		//add a start progress entry
		UserProgress progress = new UserProgress();
		progress.setUserModule(mockUserModule);
		progress.setStartDate(new LocalDate("2015-02-01"));
		userProgressRespository.saveAndFlush(progress);
		
		//test one map dto returned for the meeting with one attendee and one progress record
		Map<Attendee, List<UserProgress>> progressForMeeting = userProgressService.getProgressForMeeting(meeting.getId());
		
		assertThat(progressForMeeting).isNotNull();
		assertThat(progressForMeeting.size()).isEqualTo(1);
		assertThat(progressForMeeting.get(attendee)).isNotNull();
		assertThat(progressForMeeting.get(attendee).size()).isEqualTo(1);
		assertThat(progressForMeeting.get(attendee).get(0)).isEqualTo(progress);
		
	}
}
