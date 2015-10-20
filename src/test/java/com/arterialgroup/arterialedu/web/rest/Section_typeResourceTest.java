package com.arterialgroup.arterialedu.web.rest;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.Section_type;
import com.arterialgroup.arterialedu.repository.Section_typeRepository;

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
 * Test class for the Section_typeResource REST controller.
 *
 * @see Section_typeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class Section_typeResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private Section_typeRepository section_typeRepository;

    private MockMvc restSection_typeMockMvc;

    private Section_type section_type;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Section_typeResource section_typeResource = new Section_typeResource();
        ReflectionTestUtils.setField(section_typeResource, "section_typeRepository", section_typeRepository);
        this.restSection_typeMockMvc = MockMvcBuilders.standaloneSetup(section_typeResource).build();
    }

    @Before
    public void initTest() {
        section_type = new Section_type();
        section_type.setName(DEFAULT_NAME);
        section_type.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createSection_type() throws Exception {
        int databaseSizeBeforeCreate = section_typeRepository.findAll().size();

        // Create the Section_type
        restSection_typeMockMvc.perform(post("/api/section_types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(section_type)))
                .andExpect(status().isCreated());

        // Validate the Section_type in the database
        List<Section_type> section_types = section_typeRepository.findAll();
        assertThat(section_types).hasSize(databaseSizeBeforeCreate + 1);
        Section_type testSection_type = section_types.get(section_types.size() - 1);
        assertThat(testSection_type.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSection_type.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is not empty
    	int databaseSizeBeforeCreate = section_typeRepository.findAll().size();
        // set the field null
        section_type.setName(null);

        // Create the Section_type, which fails.
        restSection_typeMockMvc.perform(post("/api/section_types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(section_type)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Section_type> section_types = section_typeRepository.findAll();
        assertThat(section_types).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSection_types() throws Exception {
        // Initialize the database
        section_typeRepository.saveAndFlush(section_type);

        // Get all the section_types
        restSection_typeMockMvc.perform(get("/api/section_types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(section_type.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getSection_type() throws Exception {
        // Initialize the database
        section_typeRepository.saveAndFlush(section_type);

        // Get the section_type
        restSection_typeMockMvc.perform(get("/api/section_types/{id}", section_type.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(section_type.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSection_type() throws Exception {
        // Get the section_type
        restSection_typeMockMvc.perform(get("/api/section_types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSection_type() throws Exception {
        // Initialize the database
        section_typeRepository.saveAndFlush(section_type);

		int databaseSizeBeforeUpdate = section_typeRepository.findAll().size();

        // Update the section_type
        section_type.setName(UPDATED_NAME);
        section_type.setDescription(UPDATED_DESCRIPTION);
        restSection_typeMockMvc.perform(put("/api/section_types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(section_type)))
                .andExpect(status().isOk());

        // Validate the Section_type in the database
        List<Section_type> section_types = section_typeRepository.findAll();
        assertThat(section_types).hasSize(databaseSizeBeforeUpdate);
        Section_type testSection_type = section_types.get(section_types.size() - 1);
        assertThat(testSection_type.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSection_type.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteSection_type() throws Exception {
        // Initialize the database
        section_typeRepository.saveAndFlush(section_type);

		int databaseSizeBeforeDelete = section_typeRepository.findAll().size();

        // Get the section_type
        restSection_typeMockMvc.perform(delete("/api/section_types/{id}", section_type.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Section_type> section_types = section_typeRepository.findAll();
        assertThat(section_types).hasSize(databaseSizeBeforeDelete - 1);
    }
}
