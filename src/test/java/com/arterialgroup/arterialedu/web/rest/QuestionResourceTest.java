package com.arterialgroup.arterialedu.web.rest;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.Question;
import com.arterialgroup.arterialedu.repository.QuestionRepository;

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
 * Test class for the QuestionResource REST controller.
 *
 * @see QuestionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class QuestionResourceTest {

    private static final String DEFAULT_TEXT = "SAMPLE_TEXT";
    private static final String UPDATED_TEXT = "UPDATED_TEXT";

    private static final Integer DEFAULT_SERIES = 0;
    private static final Integer UPDATED_SERIES = 1;

    private static final Boolean DEFAULT_SHOWRESULTS = false;
    private static final Boolean UPDATED_SHOWRESULTS = true;

    @Inject
    private QuestionRepository questionRepository;

    private MockMvc restQuestionMockMvc;

    private Question question;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QuestionResource questionResource = new QuestionResource();
        ReflectionTestUtils.setField(questionResource, "questionRepository", questionRepository);
        this.restQuestionMockMvc = MockMvcBuilders.standaloneSetup(questionResource).build();
    }

    @Before
    public void initTest() {
        question = new Question();
        question.setText(DEFAULT_TEXT);
        question.setSeries(DEFAULT_SERIES);
        question.setShowResults(DEFAULT_SHOWRESULTS);
    }

    @Test
    @Transactional
    public void createQuestion() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();

        // Create the Question
        restQuestionMockMvc.perform(post("/api/questions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(question)))
                .andExpect(status().isCreated());

        // Validate the Question in the database
        List<Question> questions = questionRepository.findAll();
        assertThat(questions).hasSize(databaseSizeBeforeCreate + 1);
        Question testQuestion = questions.get(questions.size() - 1);
        assertThat(testQuestion.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testQuestion.getSeries()).isEqualTo(DEFAULT_SERIES);
        assertThat(testQuestion.getShowResults()).isEqualTo(DEFAULT_SHOWRESULTS);
    }

    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(questionRepository.findAll()).hasSize(0);
        // set the field null
        question.setText(null);

        // Create the Question, which fails.
        restQuestionMockMvc.perform(post("/api/questions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(question)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Question> questions = questionRepository.findAll();
        assertThat(questions).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllQuestions() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questions
        restQuestionMockMvc.perform(get("/api/questions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
                .andExpect(jsonPath("$.[*].series").value(hasItem(DEFAULT_SERIES)))
                .andExpect(jsonPath("$.[*].showResults").value(hasItem(DEFAULT_SHOWRESULTS.booleanValue())));
    }

    @Test
    @Transactional
    public void getQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get the question
        restQuestionMockMvc.perform(get("/api/questions/{id}", question.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(question.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.series").value(DEFAULT_SERIES))
            .andExpect(jsonPath("$.showResults").value(DEFAULT_SHOWRESULTS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingQuestion() throws Exception {
        // Get the question
        restQuestionMockMvc.perform(get("/api/questions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

		int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question
        question.setText(UPDATED_TEXT);
        question.setSeries(UPDATED_SERIES);
        question.setShowResults(UPDATED_SHOWRESULTS);
        restQuestionMockMvc.perform(put("/api/questions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(question)))
                .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questions = questionRepository.findAll();
        assertThat(questions).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questions.get(questions.size() - 1);
        assertThat(testQuestion.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testQuestion.getSeries()).isEqualTo(UPDATED_SERIES);
        assertThat(testQuestion.getShowResults()).isEqualTo(UPDATED_SHOWRESULTS);
    }

    @Test
    @Transactional
    public void deleteQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

		int databaseSizeBeforeDelete = questionRepository.findAll().size();

        // Get the question
        restQuestionMockMvc.perform(delete("/api/questions/{id}", question.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Question> questions = questionRepository.findAll();
        assertThat(questions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
