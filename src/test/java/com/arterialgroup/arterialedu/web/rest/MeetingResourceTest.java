package com.arterialgroup.arterialedu.web.rest;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.Meeting;
import com.arterialgroup.arterialedu.repository.MeetingRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
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
 * Test class for the MeetingResource REST controller.
 *
 * @see MeetingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MeetingResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_START_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_START_DATE = new LocalDate();

    private static final Integer DEFAULT_DAYS = 0;
    private static final Integer UPDATED_DAYS = 1;

    private static final Long DEFAULT_ACTIVITY_ID = 0L;
    private static final Long UPDATED_ACTIVITY_ID = 1L;

    @Inject
    private MeetingRepository meetingRepository;

    private MockMvc restMeetingMockMvc;

    private Meeting meeting;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MeetingResource meetingResource = new MeetingResource();
        ReflectionTestUtils.setField(meetingResource, "meetingRepository", meetingRepository);
        this.restMeetingMockMvc = MockMvcBuilders.standaloneSetup(meetingResource).build();
    }

    @Before
    public void initTest() {
        meeting = new Meeting();
        meeting.setName(DEFAULT_NAME);
        meeting.setStartDate(DEFAULT_START_DATE);
        meeting.setDays(DEFAULT_DAYS);
        meeting.setActivityId(DEFAULT_ACTIVITY_ID);
    }

    @Test
    @Transactional
    public void createMeeting() throws Exception {
        int databaseSizeBeforeCreate = meetingRepository.findAll().size();

        // Create the Meeting
        restMeetingMockMvc.perform(post("/api/meetings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(meeting)))
                .andExpect(status().isCreated());

        // Validate the Meeting in the database
        List<Meeting> meetings = meetingRepository.findAll();
        assertThat(meetings).hasSize(databaseSizeBeforeCreate + 1);
        Meeting testMeeting = meetings.get(meetings.size() - 1);
        assertThat(testMeeting.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMeeting.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testMeeting.getDays()).isEqualTo(DEFAULT_DAYS);
        assertThat(testMeeting.getActivityId()).isEqualTo(DEFAULT_ACTIVITY_ID);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(meetingRepository.findAll()).hasSize(0);
        // set the field null
        meeting.setName(null);

        // Create the Meeting, which fails.
        restMeetingMockMvc.perform(post("/api/meetings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(meeting)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Meeting> meetings = meetingRepository.findAll();
        assertThat(meetings).hasSize(0);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(meetingRepository.findAll()).hasSize(0);
        // set the field null
        meeting.setStartDate(null);

        // Create the Meeting, which fails.
        restMeetingMockMvc.perform(post("/api/meetings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(meeting)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Meeting> meetings = meetingRepository.findAll();
        assertThat(meetings).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllMeetings() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetings
        restMeetingMockMvc.perform(get("/api/meetings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(meeting.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].days").value(hasItem(DEFAULT_DAYS)))
                .andExpect(jsonPath("$.[*].activityId").value(hasItem(DEFAULT_ACTIVITY_ID.intValue())));
    }

    @Test
    @Transactional
    public void getMeeting() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get the meeting
        restMeetingMockMvc.perform(get("/api/meetings/{id}", meeting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(meeting.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.days").value(DEFAULT_DAYS))
            .andExpect(jsonPath("$.activityId").value(DEFAULT_ACTIVITY_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMeeting() throws Exception {
        // Get the meeting
        restMeetingMockMvc.perform(get("/api/meetings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeeting() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

		int databaseSizeBeforeUpdate = meetingRepository.findAll().size();

        // Update the meeting
        meeting.setName(UPDATED_NAME);
        meeting.setStartDate(UPDATED_START_DATE);
        meeting.setDays(UPDATED_DAYS);
        meeting.setActivityId(UPDATED_ACTIVITY_ID);
        restMeetingMockMvc.perform(put("/api/meetings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(meeting)))
                .andExpect(status().isOk());

        // Validate the Meeting in the database
        List<Meeting> meetings = meetingRepository.findAll();
        assertThat(meetings).hasSize(databaseSizeBeforeUpdate);
        Meeting testMeeting = meetings.get(meetings.size() - 1);
        assertThat(testMeeting.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeeting.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testMeeting.getDays()).isEqualTo(UPDATED_DAYS);
        assertThat(testMeeting.getActivityId()).isEqualTo(UPDATED_ACTIVITY_ID);
    }

    @Test
    @Transactional
    public void deleteMeeting() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

		int databaseSizeBeforeDelete = meetingRepository.findAll().size();

        // Get the meeting
        restMeetingMockMvc.perform(delete("/api/meetings/{id}", meeting.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Meeting> meetings = meetingRepository.findAll();
        assertThat(meetings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
