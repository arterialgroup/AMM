package com.arterialgroup.arterialedu.web.rest;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.MeetingType;
import com.arterialgroup.arterialedu.repository.MeetingTypeRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MeetingTypeResource REST controller.
 *
 * @see MeetingTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MeetingTypeResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private MeetingTypeRepository meetingTypeRepository;

    private MockMvc restMeetingTypeMockMvc;

    private MeetingType meetingType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MeetingTypeResource meetingTypeResource = new MeetingTypeResource();
        ReflectionTestUtils.setField(meetingTypeResource, "meetingTypeRepository", meetingTypeRepository);
        this.restMeetingTypeMockMvc = MockMvcBuilders.standaloneSetup(meetingTypeResource).build();
    }

    @Before
    public void initTest() {
        meetingType = new MeetingType();
        meetingType.setName(DEFAULT_NAME);
        meetingType.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createMeetingType() throws Exception {
        int databaseSizeBeforeCreate = meetingTypeRepository.findAll().size();

        // Create the MeetingType
        restMeetingTypeMockMvc.perform(post("/api/meetingTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(meetingType)))
                .andExpect(status().isCreated());

        // Validate the MeetingType in the database
        List<MeetingType> meetingTypes = meetingTypeRepository.findAll();
        assertThat(meetingTypes).hasSize(databaseSizeBeforeCreate + 1);
        MeetingType testMeetingType = meetingTypes.get(meetingTypes.size() - 1);
        assertThat(testMeetingType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMeetingType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is empty
    	int databaseSizeBeforeCreate = meetingTypeRepository.findAll().size();
        // set the field null
        meetingType.setName(null);

        // Create the MeetingType, which fails.
        restMeetingTypeMockMvc.perform(post("/api/meetingTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(meetingType)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<MeetingType> meetingTypes = meetingTypeRepository.findAll();
        assertThat(meetingTypes).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMeetingTypes() throws Exception {
        // Initialize the database
        meetingTypeRepository.saveAndFlush(meetingType);

        // Get all the meetingTypes
        restMeetingTypeMockMvc.perform(get("/api/meetingTypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(meetingType.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getMeetingType() throws Exception {
        // Initialize the database
        meetingTypeRepository.saveAndFlush(meetingType);

        // Get the meetingType
        restMeetingTypeMockMvc.perform(get("/api/meetingTypes/{id}", meetingType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(meetingType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMeetingType() throws Exception {
        // Get the meetingType
        restMeetingTypeMockMvc.perform(get("/api/meetingTypes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeetingType() throws Exception {
        // Initialize the database
        meetingTypeRepository.saveAndFlush(meetingType);

		int databaseSizeBeforeUpdate = meetingTypeRepository.findAll().size();

        // Update the meetingType
        meetingType.setName(UPDATED_NAME);
        meetingType.setDescription(UPDATED_DESCRIPTION);
        restMeetingTypeMockMvc.perform(put("/api/meetingTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(meetingType)))
                .andExpect(status().isOk());

        // Validate the MeetingType in the database
        List<MeetingType> meetingTypes = meetingTypeRepository.findAll();
        assertThat(meetingTypes).hasSize(databaseSizeBeforeUpdate);
        MeetingType testMeetingType = meetingTypes.get(meetingTypes.size() - 1);
        assertThat(testMeetingType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeetingType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteMeetingType() throws Exception {
        // Initialize the database
        meetingTypeRepository.saveAndFlush(meetingType);

		int databaseSizeBeforeDelete = meetingTypeRepository.findAll().size();

        // Get the meetingType
        restMeetingTypeMockMvc.perform(delete("/api/meetingTypes/{id}", meetingType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MeetingType> meetingTypes = meetingTypeRepository.findAll();
        assertThat(meetingTypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
