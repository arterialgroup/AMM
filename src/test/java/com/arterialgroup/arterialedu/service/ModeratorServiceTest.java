package com.arterialgroup.arterialedu.service;

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
import com.arterialgroup.arterialedu.domain.Meeting;
import com.arterialgroup.arterialedu.repository.MeetingRepository;
import com.arterialgroup.arterialedu.repository.ModeratorRepository;
import com.arterialgroup.arterialedu.repository.UserRepository;
import com.arterialgroup.arterialedu.web.rest.dto.UserDTO;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class ModeratorServiceTest {
	
	@Inject
	private ModeratorService moderatorService;
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private ModeratorRepository moderatorRepository;
	
	@Inject
	private MeetingRepository meetingRepository;
	
	@Before
	public void setup(){
		
	}
	
	@Test
	public void testAddModerator(){
		
		//We can pass an empty list of roles as the application defines roles based on the type of meeting user
		UserDTO user = new UserDTO(null, "123456", "test@test.com","123456", "RACGP", "test","last","test@test.com", "NSW", "en",null);
		
		assertThat(userRepository.findOneByLogin(user.getLogin()).isPresent()).isFalse();
		
		Meeting meeting = new Meeting();
		meeting.setName("TEST");
		meeting.setStartDate(new LocalDate("2015-01-01"));
		meeting = meetingRepository.saveAndFlush(meeting);
		
		assertThat(moderatorService.addModerator(user, meeting.getId())).isTrue();
		
		assertThat(userRepository.findOneByLogin(user.getLogin()).isPresent()).isTrue();
		assertThat(moderatorRepository.findOneByUserLoginAndMeeting(user.getLogin(), meeting.getId())).isNotNull();
		
	}

}
