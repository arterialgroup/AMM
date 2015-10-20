package com.arterialgroup.arterialedu.web.rest;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.Section;
import com.arterialgroup.arterialedu.domain.Step;
import com.arterialgroup.arterialedu.repository.SectionRepository;
import com.arterialgroup.arterialedu.repository.StepRepository;

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
 * Test class for the StepResource REST controller.
 *
 * @see StepResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class StepResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    private static final String DEFAULT_CONTENT = "SAMPLE_TEXT";
    private static final String UPDATED_CONTENT = "UPDATED_TEXT";

    private static final Integer DEFAULT_SERIES = 0;
    private static final Integer UPDATED_SERIES = 1;

    @Inject
    private StepRepository stepRepository;
    
    @Inject
    private SectionRepository sectionRepository;

    private MockMvc restStepMockMvc;

    private Step step;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StepResource stepResource = new StepResource();
        ReflectionTestUtils.setField(stepResource, "stepRepository", stepRepository);
        ReflectionTestUtils.setField(stepResource, "sectionRepository", sectionRepository);
        this.restStepMockMvc = MockMvcBuilders.standaloneSetup(stepResource).build();
    }

    @Before
    public void initTest() {
        step = new Step();
        step.setName(DEFAULT_NAME);
        step.setDescription(DEFAULT_DESCRIPTION);
        step.setContent(DEFAULT_CONTENT);
        step.setSeries(DEFAULT_SERIES);
    }

    @Test
    @Transactional
    public void createStep() throws Exception {
        int databaseSizeBeforeCreate = stepRepository.findAll().size();

        // Create the Step
        restStepMockMvc.perform(post("/api/steps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(step)))
                .andExpect(status().isCreated());

        // Validate the Step in the database
        List<Step> steps = stepRepository.findAll();
        assertThat(steps).hasSize(databaseSizeBeforeCreate + 1);
        Step testStep = steps.get(steps.size() - 1);
        assertThat(testStep.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStep.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStep.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testStep.getSeries()).isEqualTo(DEFAULT_SERIES);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(stepRepository.findAll()).hasSize(0);
        // set the field null
        step.setName(null);

        // Create the Step, which fails.
        restStepMockMvc.perform(post("/api/steps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(step)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Step> steps = stepRepository.findAll();
        assertThat(steps).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllSteps() throws Exception {
        // Initialize the database
        stepRepository.saveAndFlush(step);

        // Get all the steps
        restStepMockMvc.perform(get("/api/steps"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(step.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].series").value(hasItem(DEFAULT_SERIES)));
    }
    
    @Test
    @Transactional
    public void getAllStepsBySection() throws Exception {
        Section section = new Section();
        section.setName("TEST SECTION");
        section = sectionRepository.saveAndFlush(section);
    	
    	// Initialize the database
        step.setSection(section);
        stepRepository.saveAndFlush(step);

        // Get all the steps
        restStepMockMvc.perform(get("/api/steps/bysection/{sectionId}", section.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(step.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].series").value(hasItem(DEFAULT_SERIES)));
    }

    @Test
    @Transactional
    public void getStep() throws Exception {
        // Initialize the database
        stepRepository.saveAndFlush(step);

        // Get the step
        restStepMockMvc.perform(get("/api/steps/{id}", step.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(step.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.series").value(DEFAULT_SERIES));
    }

    @Test
    @Transactional
    public void getNonExistingStep() throws Exception {
        // Get the step
        restStepMockMvc.perform(get("/api/steps/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStep() throws Exception {
        // Initialize the database
        stepRepository.saveAndFlush(step);

		int databaseSizeBeforeUpdate = stepRepository.findAll().size();

        // Update the step
        step.setName(UPDATED_NAME);
        step.setDescription(UPDATED_DESCRIPTION);
        step.setContent(UPDATED_CONTENT);
        step.setSeries(UPDATED_SERIES);
        restStepMockMvc.perform(put("/api/steps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(step)))
                .andExpect(status().isOk());

        // Validate the Step in the database
        List<Step> steps = stepRepository.findAll();
        assertThat(steps).hasSize(databaseSizeBeforeUpdate);
        Step testStep = steps.get(steps.size() - 1);
        assertThat(testStep.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStep.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStep.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testStep.getSeries()).isEqualTo(UPDATED_SERIES);
    }

    @Test
    @Transactional
    public void deleteStep() throws Exception {
        // Initialize the database
        stepRepository.saveAndFlush(step);

		int databaseSizeBeforeDelete = stepRepository.findAll().size();

        // Get the step
        restStepMockMvc.perform(delete("/api/steps/{id}", step.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Step> steps = stepRepository.findAll();
        assertThat(steps).hasSize(databaseSizeBeforeDelete - 1);
    }
}
