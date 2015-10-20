package com.arterialgroup.arterialedu.web.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.Answer;
import com.arterialgroup.arterialedu.domain.Question;
import com.arterialgroup.arterialedu.domain.UserModule;
import com.arterialgroup.arterialedu.repository.UserModuleRepository;
import com.arterialgroup.arterialedu.service.UserResponseService;
import com.arterialgroup.arterialedu.web.rest.dto.AnswerDTO;
import com.arterialgroup.arterialedu.web.rest.dto.UserResponseDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserResponseResourceTest {

	@Inject
	private UserModuleRepository userModuleRepository;

	private Answer mockAnswer;
	private UserModule mockUserModule;

	@Mock
	private UserResponseService userResponseService;

	private MockMvc restUserResponseMockMvc;

	@PostConstruct
	public void setup() {
		MockitoAnnotations.initMocks(this);
		UserResponseResource userResponseResource = new UserResponseResource();
		ReflectionTestUtils.setField(userResponseResource,
				"userResponseService", userResponseService);
		this.restUserResponseMockMvc = MockMvcBuilders.standaloneSetup(
				userResponseResource).build();
	}

	@Before
	public void init() {
		mockAnswer = new Answer();
		mockUserModule = new UserModule();
	}

	@Test
	public void testSaveResponse() throws IOException, Exception {

		mockUserModule.setId((long) 1);

		// TODO may have to create the underlying answer and user records????
		AnswerDTO aDto = new AnswerDTO((long) 1, (long) 1, null, true);
		List<AnswerDTO> answers = new ArrayList<AnswerDTO>();
		answers.add(aDto);
		UserResponseDTO udto = new UserResponseDTO(mockUserModule.getId(),
				null, answers);

		Mockito.doNothing().when(userResponseService)
				.addUserResponse((long) 1, (long) 1, (long) 1, (long) 1, null);

		restUserResponseMockMvc.perform(
				post("/api/user/response/save").contentType(
						TestUtil.APPLICATION_JSON_UTF8).content(
						TestUtil.convertObjectToJsonBytes(udto))).andExpect(
				status().isCreated()); // we must have added answers...
	}

	@Test
	public void testSaveResponseBadRequestNoAnswers() throws IOException,
			Exception {

		UserResponseDTO udto = new UserResponseDTO(mockUserModule.getId(),
				null, null);

		restUserResponseMockMvc.perform(
				post("/api/user/response/save").contentType(
						TestUtil.APPLICATION_JSON_UTF8).content(
						TestUtil.convertObjectToJsonBytes(udto))).andExpect(
				status().isNoContent());

	}

	@Test
	public void testGetUserResponsesForQuestion() throws Exception {
		// TODO mock the service and what it returns (objects) so that can
		// verify the MVC REST call works
		Question question = new Question();
		question.setId(new Long(1));

		Map<Answer, Integer> mockResponses = new HashMap<Answer, Integer>();

		// mock the service layer..
		when(
				userResponseService.getAggregatedReponsesForQuestion(question
						.getId())).thenReturn(mockResponses);

		// we actually query this by the user module id... not the id of the
		// progress...
		restUserResponseMockMvc
				.perform(
						get("/api/user/response/byquestion/{questionId}",
								question.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

	}

	@Test
	public void testGetUserResponsesForQuestionEmpty() throws Exception {
		// TODO mock the service and what it returns (objects) so that can
		// verify the MVC REST call works
		Question question = new Question();
		question.setId(new Long(1));

		// mock the service layer..
		when(
				userResponseService.getAggregatedReponsesForQuestion(question
						.getId())).thenReturn(null);

		// we actually query this by the user module id... not the id of the
		// progress...
		restUserResponseMockMvc.perform(
				get("/api/user/response/byquestion/{questionId}",
						question.getId())).andExpect(status().isNoContent());

	}

	@Test
	public void getUserResponseForStepByUserModule() throws Exception {

		// mock the service layer..
		when(userResponseService.getUserReponse((long) 1, (long) 1))
				.thenReturn(new UserResponseDTO((long) 1, (long) 1, null));

		// we actually query this by the user module id... not the id of the
		// progress...
		restUserResponseMockMvc
				.perform(
						get(
								"/api/user/response/byusermoduleforstep/{userModuleId}",1L).param("stepId", "1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	public void getUserResponseForStepByUserModuleEmpty() throws Exception {

		// mock the service layer..
		when(userResponseService.getUserReponse((long) 1, (long) 1))
				.thenReturn(null);

		// we actually query this by the user module id... not the id of the
		// progress...
		restUserResponseMockMvc.perform(
				get("/api/user/response/byusermoduleforstep/{userModuleId}", 1L)
						.param("stepId", "1"))
				.andExpect(status().isNoContent());

	}
}
