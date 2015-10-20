package com.arterialgroup.arterialedu.web.rest;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.MeetingModule;
import com.arterialgroup.arterialedu.repository.MeetingModuleRepository;
import com.arterialgroup.arterialedu.service.AttendeeService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.Mockito;
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

import org.joda.time.LocalDate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MeetingModuleResource REST controller.
 *
 * @see MeetingModuleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MeetingModuleResourceTest {


    private static final LocalDate DEFAULT_DATE_AVAILABLE = new LocalDate(0L);
    private static final LocalDate UPDATED_DATE_AVAILABLE = new LocalDate();

    @Inject
    private MeetingModuleRepository meetingModuleRepository;

    private MockMvc restMeetingModuleMockMvc;

    private MeetingModule meetingModule;
    
    @Mock
    private AttendeeService attendeeService;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MeetingModuleResource meetingModuleResource = new MeetingModuleResource();
        ReflectionTestUtils.setField(meetingModuleResource, "meetingModuleRepository", meetingModuleRepository);
        ReflectionTestUtils.setField(meetingModuleResource, "attendeeService", attendeeService);
        this.restMeetingModuleMockMvc = MockMvcBuilders.standaloneSetup(meetingModuleResource).build();
    }

    @Before
    public void initTest() {
        meetingModule = new MeetingModule();
        meetingModule.setDateAvailable(DEFAULT_DATE_AVAILABLE);
    }

    @Test
    @Transactional
    public void createMeetingModule() throws Exception {
        int databaseSizeBeforeCreate = meetingModuleRepository.findAll().size();

        //stub the attendee interface
        Mockito.doNothing().when(attendeeService).assignModulesForAttendees(Mockito.any());
        
        // Create the MeetingModule
        restMeetingModuleMockMvc.perform(post("/api/meetingModules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(meetingModule)))
                .andExpect(status().isCreated());

        // Validate the MeetingModule in the database
        List<MeetingModule> meetingModules = meetingModuleRepository.findAll();
        assertThat(meetingModules).hasSize(databaseSizeBeforeCreate + 1);
        MeetingModule testMeetingModule = meetingModules.get(meetingModules.size() - 1);
        assertThat(testMeetingModule.getDateAvailable()).isEqualTo(DEFAULT_DATE_AVAILABLE);
    }

    @Test
    @Transactional
    public void getAllMeetingModules() throws Exception {
        // Initialize the database
        meetingModuleRepository.saveAndFlush(meetingModule);

        // Get all the meetingModules
        restMeetingModuleMockMvc.perform(get("/api/meetingModules"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(meetingModule.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateAvailable").value(hasItem(DEFAULT_DATE_AVAILABLE.toString())));
    }

    @Test
    @Transactional
    public void getMeetingModule() throws Exception {
        // Initialize the database
        meetingModuleRepository.saveAndFlush(meetingModule);

        // Get the meetingModule
        restMeetingModuleMockMvc.perform(get("/api/meetingModules/{id}", meetingModule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(meetingModule.getId().intValue()))
            .andExpect(jsonPath("$.dateAvailable").value(DEFAULT_DATE_AVAILABLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMeetingModule() throws Exception {
        // Get the meetingModule
        restMeetingModuleMockMvc.perform(get("/api/meetingModules/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeetingModule() throws Exception {
        // Initialize the database
        meetingModuleRepository.saveAndFlush(meetingModule);

		int databaseSizeBeforeUpdate = meetingModuleRepository.findAll().size();

        // Update the meetingModule
        meetingModule.setDateAvailable(UPDATED_DATE_AVAILABLE);
        restMeetingModuleMockMvc.perform(put("/api/meetingModules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(meetingModule)))
                .andExpect(status().isOk());

        // Validate the MeetingModule in the database
        List<MeetingModule> meetingModules = meetingModuleRepository.findAll();
        assertThat(meetingModules).hasSize(databaseSizeBeforeUpdate);
        MeetingModule testMeetingModule = meetingModules.get(meetingModules.size() - 1);
        assertThat(testMeetingModule.getDateAvailable()).isEqualTo(UPDATED_DATE_AVAILABLE);
    }

    @Test
    @Transactional
    public void deleteMeetingModule() throws Exception {
        // Initialize the database
        meetingModuleRepository.saveAndFlush(meetingModule);

		int databaseSizeBeforeDelete = meetingModuleRepository.findAll().size();

        // Get the meetingModule
        restMeetingModuleMockMvc.perform(delete("/api/meetingModules/{id}", meetingModule.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MeetingModule> meetingModules = meetingModuleRepository.findAll();
        assertThat(meetingModules).hasSize(databaseSizeBeforeDelete - 1);
    }
}
