package com.arterialgroup.arterialedu.web.rest;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.Question_type;
import com.arterialgroup.arterialedu.repository.Question_typeRepository;

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
 * Test class for the Question_typeResource REST controller.
 *
 * @see Question_typeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class Question_typeResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private Question_typeRepository question_typeRepository;

    private MockMvc restQuestion_typeMockMvc;

    private Question_type question_type;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Question_typeResource question_typeResource = new Question_typeResource();
        ReflectionTestUtils.setField(question_typeResource, "question_typeRepository", question_typeRepository);
        this.restQuestion_typeMockMvc = MockMvcBuilders.standaloneSetup(question_typeResource).build();
    }

    @Before
    public void initTest() {
        question_type = new Question_type();
        question_type.setName(DEFAULT_NAME);
        question_type.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createQuestion_type() throws Exception {
        int databaseSizeBeforeCreate = question_typeRepository.findAll().size();

        // Create the Question_type
        restQuestion_typeMockMvc.perform(post("/api/question_types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(question_type)))
                .andExpect(status().isCreated());

        // Validate the Question_type in the database
        List<Question_type> question_types = question_typeRepository.findAll();
        assertThat(question_types).hasSize(databaseSizeBeforeCreate + 1);
        Question_type testQuestion_type = question_types.get(question_types.size() - 1);
        assertThat(testQuestion_type.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQuestion_type.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is not empty
    	int databaseSizeBeforeCreate = question_typeRepository.findAll().size();
        // set the field null
        question_type.setName(null);

        // Create the Question_type, which fails.
        restQuestion_typeMockMvc.perform(post("/api/question_types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(question_type)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Question_type> question_types = question_typeRepository.findAll();
        assertThat(question_types).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllQuestion_types() throws Exception {
        // Initialize the database
        question_typeRepository.saveAndFlush(question_type);

        // Get all the question_types
        restQuestion_typeMockMvc.perform(get("/api/question_types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(question_type.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getQuestion_type() throws Exception {
        // Initialize the database
        question_typeRepository.saveAndFlush(question_type);

        // Get the question_type
        restQuestion_typeMockMvc.perform(get("/api/question_types/{id}", question_type.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(question_type.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQuestion_type() throws Exception {
        // Get the question_type
        restQuestion_typeMockMvc.perform(get("/api/question_types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestion_type() throws Exception {
        // Initialize the database
        question_typeRepository.saveAndFlush(question_type);

		int databaseSizeBeforeUpdate = question_typeRepository.findAll().size();

        // Update the question_type
        question_type.setName(UPDATED_NAME);
        question_type.setDescription(UPDATED_DESCRIPTION);
        restQuestion_typeMockMvc.perform(put("/api/question_types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(question_type)))
                .andExpect(status().isOk());

        // Validate the Question_type in the database
        List<Question_type> question_types = question_typeRepository.findAll();
        assertThat(question_types).hasSize(databaseSizeBeforeUpdate);
        Question_type testQuestion_type = question_types.get(question_types.size() - 1);
        assertThat(testQuestion_type.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQuestion_type.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteQuestion_type() throws Exception {
        // Initialize the database
        question_typeRepository.saveAndFlush(question_type);

		int databaseSizeBeforeDelete = question_typeRepository.findAll().size();

        // Get the question_type
        restQuestion_typeMockMvc.perform(delete("/api/question_types/{id}", question_type.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Question_type> question_types = question_typeRepository.findAll();
        assertThat(question_types).hasSize(databaseSizeBeforeDelete - 1);
    }
}
