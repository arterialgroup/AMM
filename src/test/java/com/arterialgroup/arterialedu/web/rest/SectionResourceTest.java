package com.arterialgroup.arterialedu.web.rest;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.Module;
import com.arterialgroup.arterialedu.domain.Section;
import com.arterialgroup.arterialedu.repository.ModuleRepository;
import com.arterialgroup.arterialedu.repository.SectionRepository;

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
 * Test class for the SectionResource REST controller.
 *
 * @see SectionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SectionResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    private static final Integer DEFAULT_SERIES = 0;
    private static final Integer UPDATED_SERIES = 1;

    @Inject
    private SectionRepository sectionRepository;
    
    @Inject
    private ModuleRepository moduleRepository;

    private MockMvc restSectionMockMvc;

    private Section section;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SectionResource sectionResource = new SectionResource();
        ReflectionTestUtils.setField(sectionResource, "sectionRepository", sectionRepository);
        ReflectionTestUtils.setField(sectionResource, "moduleRepository", moduleRepository);
        this.restSectionMockMvc = MockMvcBuilders.standaloneSetup(sectionResource).build();
    }

    @Before
    public void initTest() {
        section = new Section();
        section.setName(DEFAULT_NAME);
        section.setDescription(DEFAULT_DESCRIPTION);
        section.setSeries(DEFAULT_SERIES);
    }

    @Test
    @Transactional
    public void createSection() throws Exception {
        int databaseSizeBeforeCreate = sectionRepository.findAll().size();

        // Create the Section
        restSectionMockMvc.perform(post("/api/sections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(section)))
                .andExpect(status().isCreated());

        // Validate the Section in the database
        List<Section> sections = sectionRepository.findAll();
        assertThat(sections).hasSize(databaseSizeBeforeCreate + 1);
        Section testSection = sections.get(sections.size() - 1);
        assertThat(testSection.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSection.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSection.getSeries()).isEqualTo(DEFAULT_SERIES);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(sectionRepository.findAll()).hasSize(0);
        // set the field null
        section.setName(null);

        // Create the Section, which fails.
        restSectionMockMvc.perform(post("/api/sections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(section)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Section> sections = sectionRepository.findAll();
        assertThat(sections).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllSections() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sections
        restSectionMockMvc.perform(get("/api/sections"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(section.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].series").value(hasItem(DEFAULT_SERIES)));
    }
    
    @Test
    @Transactional
    public void getAllSectionsByModule() throws Exception {
        Module module = new Module();
        module.setName("TEST NAME");
        module = moduleRepository.saveAndFlush(module);
    	
    	// Initialize the database
        section.setModule(module);
        sectionRepository.saveAndFlush(section);

        // Get all the sections
        restSectionMockMvc.perform(get("/api/sections/bymodule/{moduleId}", module.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(section.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].series").value(hasItem(DEFAULT_SERIES)))
                .andExpect(jsonPath("$.[*].stepCount").value(hasItem(0)));
    }

    @Test
    @Transactional
    public void getSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get the section
        restSectionMockMvc.perform(get("/api/sections/{id}", section.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(section.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.series").value(DEFAULT_SERIES));
    }

    @Test
    @Transactional
    public void getNonExistingSection() throws Exception {
        // Get the section
        restSectionMockMvc.perform(get("/api/sections/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

		int databaseSizeBeforeUpdate = sectionRepository.findAll().size();

        // Update the section
        section.setName(UPDATED_NAME);
        section.setDescription(UPDATED_DESCRIPTION);
        section.setSeries(UPDATED_SERIES);
        restSectionMockMvc.perform(put("/api/sections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(section)))
                .andExpect(status().isOk());

        // Validate the Section in the database
        List<Section> sections = sectionRepository.findAll();
        assertThat(sections).hasSize(databaseSizeBeforeUpdate);
        Section testSection = sections.get(sections.size() - 1);
        assertThat(testSection.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSection.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSection.getSeries()).isEqualTo(UPDATED_SERIES);
    }

    @Test
    @Transactional
    public void deleteSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

		int databaseSizeBeforeDelete = sectionRepository.findAll().size();

        // Get the section
        restSectionMockMvc.perform(delete("/api/sections/{id}", section.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Section> sections = sectionRepository.findAll();
        assertThat(sections).hasSize(databaseSizeBeforeDelete - 1);
    }
}
