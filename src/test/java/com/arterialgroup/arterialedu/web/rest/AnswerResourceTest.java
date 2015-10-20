package com.arterialgroup.arterialedu.web.rest;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.Answer;
import com.arterialgroup.arterialedu.repository.AnswerRepository;

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
 * Test class for the AnswerResource REST controller.
 *
 * @see AnswerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AnswerResourceTest {

    private static final String DEFAULT_TEXT = "SAMPLE_TEXT";
    private static final String UPDATED_TEXT = "UPDATED_TEXT";

    private static final Boolean DEFAULT_CORRECT = false;
    private static final Boolean UPDATED_CORRECT = true;

    @Inject
    private AnswerRepository answerRepository;

    private MockMvc restAnswerMockMvc;

    private Answer answer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AnswerResource answerResource = new AnswerResource();
        ReflectionTestUtils.setField(answerResource, "answerRepository", answerRepository);
        this.restAnswerMockMvc = MockMvcBuilders.standaloneSetup(answerResource).build();
    }

    @Before
    public void initTest() {
        answer = new Answer();
        answer.setText(DEFAULT_TEXT);
        answer.setCorrect(DEFAULT_CORRECT);
    }

    @Test
    @Transactional
    public void createAnswer() throws Exception {
        int databaseSizeBeforeCreate = answerRepository.findAll().size();

        // Create the Answer
        restAnswerMockMvc.perform(post("/api/answers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(answer)))
                .andExpect(status().isCreated());

        // Validate the Answer in the database
        List<Answer> answers = answerRepository.findAll();
        assertThat(answers).hasSize(databaseSizeBeforeCreate + 1);
        Answer testAnswer = answers.get(answers.size() - 1);
        assertThat(testAnswer.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testAnswer.getCorrect()).isEqualTo(DEFAULT_CORRECT);
    }

    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(answerRepository.findAll()).hasSize(0);
        // set the field null
        answer.setText(null);

        // Create the Answer, which fails.
        restAnswerMockMvc.perform(post("/api/answers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(answer)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Answer> answers = answerRepository.findAll();
        assertThat(answers).hasSize(0);
    }

    @Test
    @Transactional
    public void checkCorrectIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(answerRepository.findAll()).hasSize(0);
        // set the field null - will default to false
        answer.setCorrect(null);

        // Create the Answer, which doesn't fail.
        restAnswerMockMvc.perform(post("/api/answers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(answer)))
                .andExpect(status().isCreated());

        // Validate the database entry has been entered
        List<Answer> answers = answerRepository.findAll();
        assertThat(answers).hasSize(1);
    }

    @Test
    @Transactional
    public void getAllAnswers() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answers
        restAnswerMockMvc.perform(get("/api/answers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(answer.getId().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
                .andExpect(jsonPath("$.[*].correct").value(hasItem(DEFAULT_CORRECT.booleanValue())));
    }

    @Test
    @Transactional
    public void getAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get the answer
        restAnswerMockMvc.perform(get("/api/answers/{id}", answer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(answer.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.correct").value(DEFAULT_CORRECT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAnswer() throws Exception {
        // Get the answer
        restAnswerMockMvc.perform(get("/api/answers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

		int databaseSizeBeforeUpdate = answerRepository.findAll().size();

        // Update the answer
        answer.setText(UPDATED_TEXT);
        answer.setCorrect(UPDATED_CORRECT);
        restAnswerMockMvc.perform(put("/api/answers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(answer)))
                .andExpect(status().isOk());

        // Validate the Answer in the database
        List<Answer> answers = answerRepository.findAll();
        assertThat(answers).hasSize(databaseSizeBeforeUpdate);
        Answer testAnswer = answers.get(answers.size() - 1);
        assertThat(testAnswer.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testAnswer.getCorrect()).isEqualTo(UPDATED_CORRECT);
    }

    @Test
    @Transactional
    public void deleteAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

		int databaseSizeBeforeDelete = answerRepository.findAll().size();

        // Get the answer
        restAnswerMockMvc.perform(delete("/api/answers/{id}", answer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Answer> answers = answerRepository.findAll();
        assertThat(answers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
