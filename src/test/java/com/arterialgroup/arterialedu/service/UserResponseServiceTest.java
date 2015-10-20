package com.arterialgroup.arterialedu.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.Answer;
import com.arterialgroup.arterialedu.domain.Question;
import com.arterialgroup.arterialedu.domain.Step;
import com.arterialgroup.arterialedu.domain.UserModule;
import com.arterialgroup.arterialedu.domain.UserResponse;
import com.arterialgroup.arterialedu.repository.AnswerRepository;
import com.arterialgroup.arterialedu.repository.QuestionRepository;
import com.arterialgroup.arterialedu.repository.StepRepository;
import com.arterialgroup.arterialedu.repository.UserModuleRepository;
import com.arterialgroup.arterialedu.repository.UserResponseRepository;
import com.arterialgroup.arterialedu.web.rest.dto.UserResponseDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class UserResponseServiceTest {
	
	@Inject
	private UserResponseRepository userResponseRepository;
	
	@Inject
	private UserModuleRepository userModuleRepository;
	@Inject 
	private StepRepository stepRepository;
	@Inject
	private AnswerRepository answerRepository;
	@Inject
	private QuestionRepository questionRepository;
	
	@Inject
	private UserResponseService userResponseService;
	
	private Question question;
	private Answer answerOne;
	private Answer answerTwo;
	private UserModule mockUserModule;
	
	@Before
	public void setup(){
		mockUserModule = new UserModule();
		question = new Question();
		question.setText("BLANK");
		
		answerOne = new Answer();
		answerOne.setText("ONE");
		answerOne.setCorrect(true);
		
		answerTwo = new Answer();
		answerTwo.setText("TWO");
		answerTwo.setCorrect(false);
		userModuleRepository.saveAndFlush(mockUserModule);
	}
	
	@Test
	public void testAddResponseValid(){
		
		Step step = new Step();
		step.setName("TEST_STEP");
		step = stepRepository.saveAndFlush(step);
		
		Question q = new Question();
		q.setText("TEST");
		q.setStep(step);
		q = questionRepository.saveAndFlush(q);
		
		Answer a = new Answer();
		a.setQuestion(q);
		a.setText("TEST_BLANK_ANSWER");
		a.setCorrect(true);
		a = answerRepository.saveAndFlush(a);
		
		userResponseService.addUserResponse(mockUserModule.getId(), step.getId(), q.getId(), a.getId(), null);
		assertThat(userResponseRepository.count()).isEqualTo(1);
		assertThat(userResponseRepository.findAll().get(0)).isNotNull();
		assertThat(userResponseRepository.findAll().get(0).getStep()).isEqualTo(step);
		assertThat(userResponseRepository.findAll().get(0).getQuestion()).isEqualTo(q);
		assertThat(userResponseRepository.findAll().get(0).getAnswer()).isEqualTo(a);
		assertThat(userResponseRepository.findAll().get(0).getAnswerText()).isNull();
	}
	
	@Test
	public void testUpdateResponseValid(){
		
		Step step = new Step();
		step.setName("TEST_STEP");
		step = stepRepository.saveAndFlush(step);
		
		Question q = new Question();
		q.setText("TEST");
		q.setStep(step);
		q = questionRepository.saveAndFlush(q);
		
		Answer a = new Answer();
		a.setQuestion(q);
		a.setText("TEST_BLANK_ANSWER");
		a.setCorrect(true);
		a = answerRepository.saveAndFlush(a);
		
		userResponseService.addUserResponse(mockUserModule.getId(), step.getId(), q.getId(), a.getId(), null);
		
		assertThat(userResponseRepository.count()).isEqualTo(1);
		assertThat(userResponseRepository.findAll().get(0)).isNotNull();
		assertThat(userResponseRepository.findAll().get(0).getStep()).isEqualTo(step);
		assertThat(userResponseRepository.findAll().get(0).getQuestion()).isEqualTo(q);
		assertThat(userResponseRepository.findAll().get(0).getAnswer()).isEqualTo(a);
		assertThat(userResponseRepository.findAll().get(0).getAnswerText()).isNull();
		
		//now change to free text..
		userResponseService.addUserResponse(mockUserModule.getId(), step.getId(), q.getId(), null, "BLAH");
		
		assertThat(userResponseRepository.count()).isEqualTo(1);
		assertThat(userResponseRepository.findAll().get(0)).isNotNull();
		assertThat(userResponseRepository.findAll().get(0).getStep()).isEqualTo(step);
		assertThat(userResponseRepository.findAll().get(0).getAnswer()).isNull();
		assertThat(userResponseRepository.findAll().get(0).getAnswerText()).isNotNull();
		assertThat(userResponseRepository.findAll().get(0).getAnswerText()).isEqualTo("BLAH");
	}
	
	@Test
	public void testAddResponseValidText(){
		
		Step step = new Step();
		step.setName("TEST_STEP");
		step = stepRepository.saveAndFlush(step);
		
		Question q = new Question();
		q.setText("TEST");
		q.setStep(step);
		q = questionRepository.saveAndFlush(q);
		
		Answer a = new Answer();
		a.setQuestion(q);
		a.setText("TEST_BLANK_ANSWER");
		a.setCorrect(true);
		a = answerRepository.saveAndFlush(a);
		
		userResponseService.addUserResponse(mockUserModule.getId(), step.getId(), q.getId(), null, "TEST_ANSWER");
		assertThat(userResponseRepository.count()).isEqualTo(1);
		assertThat(userResponseRepository.findAll().get(0)).isNotNull();
		assertThat(userResponseRepository.findAll().get(0).getStep()).isEqualTo(step);
		assertThat(userResponseRepository.findAll().get(0).getAnswer()).isNull();
		assertThat(userResponseRepository.findAll().get(0).getAnswerText()).isEqualTo("TEST_ANSWER");
	}
	
	@Test
	public void testAddResponseInValid(){
		Step step = new Step();
		step.setName("TEST_STEP");
		step = stepRepository.saveAndFlush(step);
		
		Question q = new Question();
		q.setText("TEST");
		q.setStep(step);
		q = questionRepository.saveAndFlush(q);
		
		userResponseService.addUserResponse(mockUserModule.getId(), step.getId(), q.getId(), null, null);
		assertThat(userResponseRepository.count()).isEqualTo(0);
		assertThat(userResponseRepository.findAll()).isEmpty();
	}
	
	@Test
	public void testGetAggregatedReponsesForQuestion(){
		
		//Initialise DB
		//TODO CASCADE and LAY LOADING is yet to be configured on the
		//Entities. If we want to enable save/delete of child objects
		//we need to re-configure the collections with appropriate annotations
		answerOne = answerRepository.saveAndFlush(answerOne);
		answerTwo = answerRepository.saveAndFlush(answerTwo);
		Set<Answer> answers = new HashSet<Answer>();
		answers.add(answerOne);
		answers.add(answerTwo);
		question.setAnswers(answers);
		
		questionRepository.saveAndFlush(question);
		userModuleRepository.saveAndFlush(mockUserModule);
		
		//Create some responses
		UserResponse responseForOne = new UserResponse();
		responseForOne.setUserModule(mockUserModule);
		responseForOne.setAnswer(answerOne);
		userResponseRepository.saveAndFlush(responseForOne);
		
		UserResponse responseForTwo = new UserResponse();
		responseForTwo.setUserModule(mockUserModule);
		responseForTwo.setAnswer(answerTwo);
		userResponseRepository.saveAndFlush(responseForTwo);
		
		Map<Answer, Integer> results = userResponseService.getAggregatedReponsesForQuestion(question.getId());
		assertThat(results).isNotNull();
		assertThat(results.size()).isEqualTo(2);		
		assertThat(results.get(answerOne)).isEqualTo(1);
		assertThat(results.get(answerTwo)).isEqualTo(1);
	}
	
	@Test
	public void testGetAggregatedReponsesForQuestionMultipleResponses(){

		
		//Initialise DB
		//TODO CASCADE and LAY LOADING is yet to be configured on the
		//Entities. If we want to enable save/delete of child objects
		//we need to re-configure the collections with appropriate annotations
		
		
		answerOne = answerRepository.saveAndFlush(answerOne);
		answerTwo = answerRepository.saveAndFlush(answerTwo);
		Set<Answer> answers = new HashSet<Answer>();
		answers.add(answerOne);
		answers.add(answerTwo);
		question.setAnswers(answers);
		
		questionRepository.saveAndFlush(question);
		userModuleRepository.saveAndFlush(mockUserModule);
		
		//Create some responses
		UserResponse responseForOne = new UserResponse();
		responseForOne.setUserModule(mockUserModule);
		responseForOne.setAnswer(answerOne);
		userResponseRepository.saveAndFlush(responseForOne);
		
		UserResponse responseForTwo = new UserResponse();
		responseForTwo.setUserModule(mockUserModule);
		responseForTwo.setAnswer(answerTwo);
		userResponseRepository.saveAndFlush(responseForTwo);
		
		UserResponse responseTwoForTwo = new UserResponse();
		responseTwoForTwo.setUserModule(mockUserModule);
		responseTwoForTwo.setAnswer(answerTwo);
		userResponseRepository.saveAndFlush(responseTwoForTwo);
		
		Map<Answer, Integer> results = userResponseService.getAggregatedReponsesForQuestion(question.getId());
		assertThat(results).isNotNull();
		assertThat(results.size()).isEqualTo(2);		
		assertThat(results.get(answerOne)).isEqualTo(1);
		assertThat(results.get(answerTwo)).isEqualTo(2);
	}
	
	@Test
	public void testGetAggregatedReponsesForQuestionNoResponse(){
		
		//Initialise DB
		//TODO CASCADE and LAY LOADING is yet to be configured on the
		//Entities. If we want to enable save/delete of child objects
		//we need to re-configure the collections with appropriate annotations
		answerOne = answerRepository.saveAndFlush(answerOne);
		answerTwo = answerRepository.saveAndFlush(answerTwo);
		Set<Answer> answers = new HashSet<Answer>();
		answers.add(answerOne);
		answers.add(answerTwo);
		question.setAnswers(answers);
		
		questionRepository.saveAndFlush(question);
		userModuleRepository.saveAndFlush(mockUserModule);
		
		//Create some responses for answer one
		UserResponse responseForOne = new UserResponse();
		responseForOne.setUserModule(mockUserModule);
		responseForOne.setAnswer(answerOne);
		userResponseRepository.saveAndFlush(responseForOne);
		
		//Don't create any for answer two..
		
		Map<Answer, Integer> results = userResponseService.getAggregatedReponsesForQuestion(question.getId());
		assertThat(results).isNotNull();
		assertThat(results.size()).isEqualTo(2);		
		assertThat(results.get(answerOne)).isEqualTo(1);
		assertThat(results.get(answerTwo)).isEqualTo(0);
		
	}
	
	@Test
	public void testGetUserResponseForStepByModuleValid()
	{
		Step step = new Step();
		step.setName("TEST_STEP");
		step = stepRepository.saveAndFlush(step);
		
		Question q = new Question();
		q.setText("TEST");
		q.setStep(step);
		q = questionRepository.saveAndFlush(q);
		
		Answer a = new Answer();
		a.setQuestion(q);
		a.setText("TEST_BLANK_ANSWER");
		a.setCorrect(true);
		a = answerRepository.saveAndFlush(a);
		
		//Create some responses
		UserResponse responseForOne = new UserResponse();
		responseForOne.setUserModule(mockUserModule);
		responseForOne.setStep(step);
		responseForOne.setAnswer(a);
		responseForOne.setQuestion(q);
		responseForOne = userResponseRepository.saveAndFlush(responseForOne);
		
		List<UserResponse> test = userResponseRepository.findAllByAnswer(a);
		assertThat(test).isNotNull();
		assertThat(test.size()).isEqualTo(1);
		
		List<UserResponse> rep = userResponseRepository.findAllByUserModuleAndStep(responseForOne.getUserModule(), responseForOne.getStep());
		assertThat(rep).isNotNull();
		assertThat(rep.size()).isEqualTo(1);
		
		UserResponseDTO dto = userResponseService.getUserReponse(responseForOne.getStep().getId(), responseForOne.getUserModule().getId());
		
		assertThat(dto).isNotNull();
		assertThat(dto.getAnswers()).isNotNull();
		assertThat(dto.getAnswers().size()).isEqualTo(1);
	}
}
