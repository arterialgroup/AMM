package com.arterialgroup.arterialedu.web.rest;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.Attendee;
import com.arterialgroup.arterialedu.domain.Authority;
import com.arterialgroup.arterialedu.domain.Meeting;
import com.arterialgroup.arterialedu.domain.Moderator;
import com.arterialgroup.arterialedu.domain.User;
import com.arterialgroup.arterialedu.repository.MeetingRepository;
import com.arterialgroup.arterialedu.repository.ModeratorRepository;
import com.arterialgroup.arterialedu.repository.UserRepository;
import com.arterialgroup.arterialedu.security.AuthoritiesConstants;
import com.arterialgroup.arterialedu.service.ModeratorService;
import com.arterialgroup.arterialedu.web.rest.dto.UserDTO;

import org.joda.time.LocalDate;
import org.junit.Before;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ModeratorResource REST controller.
 *
 * @see ModeratorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ModeratorResourceTest {


    @Inject
    private ModeratorRepository moderatorRepository;
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    private MeetingRepository meetingRepository;

    private MockMvc restModeratorMockMvc;

    private Moderator moderator;
    
    @Mock
    private ModeratorService moderatorService;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ModeratorResource moderatorResource = new ModeratorResource();
        ReflectionTestUtils.setField(moderatorResource, "moderatorRepository", moderatorRepository);
        ReflectionTestUtils.setField(moderatorResource, "moderatorService", moderatorService);
        this.restModeratorMockMvc = MockMvcBuilders.standaloneSetup(moderatorResource).build();
    }

    @Before
    public void initTest() {
        moderator = new Moderator();
    }

    @Test
    @Transactional
    public void createModerator() throws Exception {
        int databaseSizeBeforeCreate = moderatorRepository.findAll().size();

        // Create the Moderator
        restModeratorMockMvc.perform(post("/api/moderators")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(moderator)))
                .andExpect(status().isCreated());

        // Validate the Moderator in the database
        List<Moderator> moderators = moderatorRepository.findAll();
        assertThat(moderators).hasSize(databaseSizeBeforeCreate + 1);
        Moderator testModerator = moderators.get(moderators.size() - 1);
    }

    @Test
    @Transactional
    public void getAllModerators() throws Exception {
        // Initialize the database
        moderatorRepository.saveAndFlush(moderator);

        // Get all the moderators
        restModeratorMockMvc.perform(get("/api/moderators"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(moderator.getId().intValue())));
    }

    @Test
    @Transactional
    public void getModerator() throws Exception {
        // Initialize the database
        moderatorRepository.saveAndFlush(moderator);

        // Get the moderator
        restModeratorMockMvc.perform(get("/api/moderators/{id}", moderator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(moderator.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingModerator() throws Exception {
        // Get the moderator
        restModeratorMockMvc.perform(get("/api/moderators/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModerator() throws Exception {
        // Initialize the database
        moderatorRepository.saveAndFlush(moderator);

		int databaseSizeBeforeUpdate = moderatorRepository.findAll().size();

        // Update the moderator
        restModeratorMockMvc.perform(put("/api/moderators")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(moderator)))
                .andExpect(status().isOk());

        // Validate the Moderator in the database
        List<Moderator> moderators = moderatorRepository.findAll();
        assertThat(moderators).hasSize(databaseSizeBeforeUpdate);
        Moderator testModerator = moderators.get(moderators.size() - 1);
    }

    @Test
    @Transactional
    public void deleteModerator() throws Exception {
        // Initialize the database
        moderatorRepository.saveAndFlush(moderator);

		int databaseSizeBeforeDelete = moderatorRepository.findAll().size();

        // Get the moderator
        restModeratorMockMvc.perform(delete("/api/moderators/{id}", moderator.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Moderator> moderators = moderatorRepository.findAll();
        assertThat(moderators).hasSize(databaseSizeBeforeDelete - 1);
    }
    
    @Test
    @Transactional
    public void testRegisterModeratorForMeetingValid() throws Exception {
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
        
        assertThat(moderatorRepository.findOneByUserLoginAndMeeting("joe@example.com", (long)1).isPresent()).isFalse();
        
        //mock the service layer
        //UserDTO now has equals and hashcode overridden for object comparison, as they are readonly we need to compare
        //them based on unique attribute of the DTO, that being the login.
        when(moderatorService.addModerator(u, meetingId)).thenReturn(new Boolean(true));

        restModeratorMockMvc.perform(
            post("/api/moderators/register/{meetingId}", meetingId)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(u)))
            .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    public void testRegisterModeratorForMeetingThatExistsValid() throws Exception {
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
        Moderator moderator = new Moderator();
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
        meeting.setStartDate(new LocalDate("2015-01-01"));
        
        meeting = meetingRepository.saveAndFlush(meeting);
        
        moderator.setUser(user);
        moderator.setMeeting(meeting);
        
        moderator = moderatorRepository.saveAndFlush(moderator);
        
        //Make sure its saved (essentially unit testing the repo here)
        assertThat(moderatorRepository.findOneByUserLoginAndMeeting(user.getLogin(), meeting.getId()).isPresent()).isTrue();
        
        //No need to mock service here, shouldn't get called...
        //when(attendeeService.addAttendee(u, meetingId)).thenReturn(new Boolean(true));

        //Make sure the call succeeds but just verifies the check is OK
        restModeratorMockMvc.perform(
            post("/api/moderators/register/{meetingId}", meeting.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(u)))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testRegisterModeratorForMeetingThatFails() throws Exception {
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
        assertThat(moderatorRepository.findOneByUserLoginAndMeeting(u.getEmail(), (long)1).isPresent()).isFalse();
        
        //Mock the service layer to return false, so we didn't create it
        when(moderatorService.addModerator(u, (long)1)).thenReturn(new Boolean(false));

        //Make sure we mark this as a bad request.
        restModeratorMockMvc.perform(
            post("/api/moderators/register/{meetingId}", (long)1)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(u)))
            .andExpect(status().isBadRequest());
    }
}
