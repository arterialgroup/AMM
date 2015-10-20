package com.arterialgroup.arterialedu.web.rest;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.QuestionGroup;
import com.arterialgroup.arterialedu.repository.QuestionGroupRepository;

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
 * Test class for the QuestionGroupResource REST controller.
 *
 * @see QuestionGroupResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class QuestionGroupResourceTest {

    private static final String DEFAULT_TEXT = "SAMPLE_TEXT";
    private static final String UPDATED_TEXT = "UPDATED_TEXT";

    @Inject
    private QuestionGroupRepository questionGroupRepository;

    private MockMvc restQuestionGroupMockMvc;

    private QuestionGroup questionGroup;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QuestionGroupResource questionGroupResource = new QuestionGroupResource();
        ReflectionTestUtils.setField(questionGroupResource, "questionGroupRepository", questionGroupRepository);
        this.restQuestionGroupMockMvc = MockMvcBuilders.standaloneSetup(questionGroupResource).build();
    }

    @Before
    public void initTest() {
        questionGroup = new QuestionGroup();
        questionGroup.setText(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    public void createQuestionGroup() throws Exception {
        int databaseSizeBeforeCreate = questionGroupRepository.findAll().size();

        // Create the QuestionGroup
        restQuestionGroupMockMvc.perform(post("/api/questionGroups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionGroup)))
                .andExpect(status().isCreated());

        // Validate the QuestionGroup in the database
        List<QuestionGroup> questionGroups = questionGroupRepository.findAll();
        assertThat(questionGroups).hasSize(databaseSizeBeforeCreate + 1);
        QuestionGroup testQuestionGroup = questionGroups.get(questionGroups.size() - 1);
        assertThat(testQuestionGroup.getText()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(questionGroupRepository.findAll()).hasSize(0);
        // set the field null
        questionGroup.setText(null);

        // Create the QuestionGroup, which fails.
        restQuestionGroupMockMvc.perform(post("/api/questionGroups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionGroup)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<QuestionGroup> questionGroups = questionGroupRepository.findAll();
        assertThat(questionGroups).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllQuestionGroups() throws Exception {
        // Initialize the database
        questionGroupRepository.saveAndFlush(questionGroup);

        // Get all the questionGroups
        restQuestionGroupMockMvc.perform(get("/api/questionGroups"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(questionGroup.getId().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getQuestionGroup() throws Exception {
        // Initialize the database
        questionGroupRepository.saveAndFlush(questionGroup);

        // Get the questionGroup
        restQuestionGroupMockMvc.perform(get("/api/questionGroups/{id}", questionGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(questionGroup.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQuestionGroup() throws Exception {
        // Get the questionGroup
        restQuestionGroupMockMvc.perform(get("/api/questionGroups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestionGroup() throws Exception {
        // Initialize the database
        questionGroupRepository.saveAndFlush(questionGroup);

		int databaseSizeBeforeUpdate = questionGroupRepository.findAll().size();

        // Update the questionGroup
        questionGroup.setText(UPDATED_TEXT);
        restQuestionGroupMockMvc.perform(put("/api/questionGroups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionGroup)))
                .andExpect(status().isOk());

        // Validate the QuestionGroup in the database
        List<QuestionGroup> questionGroups = questionGroupRepository.findAll();
        assertThat(questionGroups).hasSize(databaseSizeBeforeUpdate);
        QuestionGroup testQuestionGroup = questionGroups.get(questionGroups.size() - 1);
        assertThat(testQuestionGroup.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void deleteQuestionGroup() throws Exception {
        // Initialize the database
        questionGroupRepository.saveAndFlush(questionGroup);

		int databaseSizeBeforeDelete = questionGroupRepository.findAll().size();

        // Get the questionGroup
        restQuestionGroupMockMvc.perform(delete("/api/questionGroups/{id}", questionGroup.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<QuestionGroup> questionGroups = questionGroupRepository.findAll();
        assertThat(questionGroups).hasSize(databaseSizeBeforeDelete - 1);
    }
}
