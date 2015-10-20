package com.arterialgroup.arterialedu.web.rest;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.Attendee;
import com.arterialgroup.arterialedu.domain.Authority;
import com.arterialgroup.arterialedu.domain.Meeting;
import com.arterialgroup.arterialedu.domain.User;
import com.arterialgroup.arterialedu.repository.AttendeeRepository;
import com.arterialgroup.arterialedu.repository.MeetingRepository;
import com.arterialgroup.arterialedu.repository.UserRepository;
import com.arterialgroup.arterialedu.security.AuthoritiesConstants;
import com.arterialgroup.arterialedu.service.AttendeeService;
import com.arterialgroup.arterialedu.web.rest.dto.UserDTO;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AttendeeResource REST controller.
 *
 * @see AttendeeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AttendeeResourceTest {


    private static final Boolean DEFAULT_ATTENDED = false;
    private static final Boolean UPDATED_ATTENDED = true;

    @Inject
    private AttendeeRepository attendeeRepository;
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    private MeetingRepository meetingRepository;
    
    @Mock
    private AttendeeService attendeeService; 

    private MockMvc restAttendeeMockMvc;

    private Attendee attendee;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AttendeeResource attendeeResource = new AttendeeResource();
        ReflectionTestUtils.setField(attendeeResource, "attendeeRepository", attendeeRepository);
        ReflectionTestUtils.setField(attendeeResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(attendeeResource, "attendeeService", attendeeService);
        this.restAttendeeMockMvc = MockMvcBuilders.standaloneSetup(attendeeResource).build();
    }

    @Before
    public void initTest() {
        attendee = new Attendee();
        attendee.setAttended(DEFAULT_ATTENDED);
    }

    @Test
    @Transactional
    public void createAttendee() throws Exception {
        int databaseSizeBeforeCreate = attendeeRepository.findAll().size();

        // Create the Attendee
        restAttendeeMockMvc.perform(post("/api/attendees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(attendee)))
                .andExpect(status().isCreated());

        // Validate the Attendee in the database
        List<Attendee> attendees = attendeeRepository.findAll();
        assertThat(attendees).hasSize(databaseSizeBeforeCreate + 1);
        Attendee testAttendee = attendees.get(attendees.size() - 1);
        assertThat(testAttendee.getAttended()).isEqualTo(DEFAULT_ATTENDED);
    }

    @Test
    @Transactional
    public void getAllAttendees() throws Exception {
        // Initialize the database
        attendeeRepository.saveAndFlush(attendee);

        // Get all the attendees
        restAttendeeMockMvc.perform(get("/api/attendees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(attendee.getId().intValue())))
                .andExpect(jsonPath("$.[*].attended").value(hasItem(DEFAULT_ATTENDED.booleanValue())));
    }

    @Test
    @Transactional
    public void getAttendee() throws Exception {
        // Initialize the database
        attendeeRepository.saveAndFlush(attendee);

        // Get the attendee
        restAttendeeMockMvc.perform(get("/api/attendees/{id}", attendee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(attendee.getId().intValue()))
            .andExpect(jsonPath("$.attended").value(DEFAULT_ATTENDED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAttendee() throws Exception {
        // Get the attendee
        restAttendeeMockMvc.perform(get("/api/attendees/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttendee() throws Exception {
        // Initialize the database
        attendeeRepository.saveAndFlush(attendee);

		int databaseSizeBeforeUpdate = attendeeRepository.findAll().size();

        // Update the attendee
        attendee.setAttended(UPDATED_ATTENDED);
        restAttendeeMockMvc.perform(put("/api/attendees")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(attendee)))
                .andExpect(status().isOk());

        // Validate the Attendee in the database
        List<Attendee> attendees = attendeeRepository.findAll();
        assertThat(attendees).hasSize(databaseSizeBeforeUpdate);
        Attendee testAttendee = attendees.get(attendees.size() - 1);
        assertThat(testAttendee.getAttended()).isEqualTo(UPDATED_ATTENDED);
    }

    @Test
    @Transactional
    public void deleteAttendee() throws Exception {
        // Initialize the database
        attendeeRepository.saveAndFlush(attendee);

		int databaseSizeBeforeDelete = attendeeRepository.findAll().size();

        // Get the attendee
        restAttendeeMockMvc.perform(delete("/api/attendees/{id}", attendee.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Attendee> attendees = attendeeRepository.findAll();
        assertThat(attendees).hasSize(databaseSizeBeforeDelete - 1);
    }
    
    @Test
    @Transactional
    public void testRegisterAttendeeForMeetingValid() throws Exception {
        UserDTO u = new UserDTO(
        		null,
            "joe",                  // login
            "password",             // password
            "accreditation",        // accreditation
            "accreditationType",    // accreditationType
            "Joe",                  // firstName
            "Shmoe",                // lastName
            "joe@example.com",      // e-mail (which is the attendee login...)
            "NSW",                  // state
            "en",                   // langKey
            Arrays.asList(AuthoritiesConstants.USER)
        );
        
        Long meetingId = new Long(1);
        
        assertThat(attendeeRepository.findOneByUserLoginAndMeeting("joe@example.com", (long)1).isPresent()).isFalse();
        
        //mock the service layer
        //UserDTO now has equals and hashcode overridden for object comparison, as they are readonly we need to compare
        //them based on unique attribute of the DTO, that being the login.
        when(attendeeService.addAttendee(u, meetingId)).thenReturn(new Boolean(true));

        restAttendeeMockMvc.perform(
            post("/api/attendees/register/{meetingId}", meetingId)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(u)))
            .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    public void testRegisterAttendeeForMeetingThatExistsValid() throws Exception {
        UserDTO u = new UserDTO(
        		null,
            "joe",                  // login
            "password",             // password
            "accreditation",        // accreditation
            "accreditationType",    // accreditationType
            "Joe",                  // firstName
            "Shmoe",                // lastName
            "joe@example.com",      // e-mail (which is the attendee login...)
            "NSW",                  // state
            "en",                   // langKey
            Arrays.asList(AuthoritiesConstants.USER)
        );
        
        //setup DB
        User user = new User();
        Attendee attendee = new Attendee();
        Meeting meeting = new Meeting();
        
        user.setLogin(u.getEmail().toLowerCase());
        user.setEmail(u.getEmail());
        user.setState(u.getState());
        user.setPassword(u.getPassword());
        user.setAccreditation(u.getAccreditation());
        user.setAccreditationType(u.getAccreditationType());
        user.setActivated(true);
        user.setFirstName(u.getFirstName());
        user.setLastName(u.getLastName());
        user.setLangKey(u.getLangKey());
        Authority auth = new Authority();
        auth.setName(AuthoritiesConstants.USER);
        Set<Authority> auths = new HashSet<Authority>();
        auths.add(auth);
        user.setAuthorities(auths);
        
        user = userRepository.saveAndFlush(user);
        
        meeting.setName("TEST");
        meeting.setActivityId((long)123);
        meeting.setStartDate(new LocalDate("2015-01-01"));
        
        meeting = meetingRepository.saveAndFlush(meeting);
        
        attendee.setUser(user);
        attendee.setMeeting(meeting);
        
        attendee = attendeeRepository.saveAndFlush(attendee);
        
        //Make sure its saved (essentially unit testing the repo here)
        assertThat(attendeeRepository.findOneByUserLoginAndMeeting(user.getLogin(), meeting.getActivityId()).isPresent()).isTrue();
        
        //No need to mock service here, shouldn't get called...
        //when(attendeeService.addAttendee(u, meetingId)).thenReturn(new Boolean(true));

        //Make sure the call succeeds but just verifies the check is OK
        restAttendeeMockMvc.perform(
            post("/api/attendees/register/{meetingId}", meeting.getActivityId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(u)))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testRegisterAttendeeForMeetingThatFails() throws Exception {
        UserDTO u = new UserDTO(
        		null,
            "joe",                  // login
            "password",             // password
            "accreditation",        // accreditation
            "accreditationType",    // accreditationType
            "Joe",                  // firstName
            "Shmoe",                // lastName
            "joe@example.com",      // e-mail (which is the attendee login...)
            "NSW",                  // state
            "en",                   // langKey
            Arrays.asList(AuthoritiesConstants.USER)
        );
        
        //Make sure there is no record
        assertThat(attendeeRepository.findOneByUserLoginAndMeeting(u.getEmail(), (long)1).isPresent()).isFalse();
        
        //Mock the service layer to return false, so we didn't create it
        when(attendeeService.addAttendee(u, (long)1)).thenReturn(new Boolean(false));

        //Make sure we mark this as a bad request.
        restAttendeeMockMvc.perform(
            post("/api/attendees/register/{meetingId}", (long)1)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(u)))
            .andExpect(status().isBadRequest());
    }
}
