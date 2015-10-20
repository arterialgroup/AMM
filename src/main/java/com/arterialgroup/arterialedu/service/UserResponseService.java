package com.arterialgroup.arterialedu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.arterialgroup.arterialedu.web.rest.dto.AnswerDTO;
import com.arterialgroup.arterialedu.web.rest.dto.UserResponseDTO;

/**
 * Spring JPA will automatically create queries based on the naming convention
 * of the methods and parameters as its smart enough to resolve this...
 * http://docs
 * .spring.io/spring-data/data-commons/docs/1.6.1.RELEASE/reference/html
 * /repositories.html
 * 
 * @author bradleyr
 * 
 */
@Service
@Transactional
public class UserResponseService {

	private final Logger log = LoggerFactory
			.getLogger(UserResponseService.class);

	@Inject
	private UserResponseRepository userResponseRespository;

	@Inject
	private UserModuleRepository userModuleRepository;

	@Inject
	private StepRepository stepRespository;

	@Inject
	private QuestionRepository questionRepository;

	@Inject
	private AnswerRepository answerRepository;

	/*
	 * AnswerText can be null (if answerId != null then its a valid selection,
	 * otherwise free text) At present we only add new responses, probably want
	 * to handle if they already exist so they can be updated..
	 */
	public void addUserResponse(Long userModuleId, Long stepId,
			Long questionId, Long answerId, String answerText) {

		boolean save = true;

		// These have to exist...
		UserModule userModule = userModuleRepository.findOne(userModuleId);
		Step step = stepRespository.findOne(stepId);
		Question question = questionRepository.findOne(questionId);

		Answer answer = null;
		if (answerId != null) {
			answer = answerRepository.findOne(answerId);
		}

		UserResponse userResponse = userResponseRespository
				.findOneByUserModuleAndQuestionAndStep(userModule, question,
						step).orElse(null);

		if (userResponse == null) {
			userResponse = new UserResponse();
			userResponse.setUserModule(userModule);
			// only update step reference if not found..
			userResponse.setQuestion(question);
			userResponse.setStep(step);
		}

		// determine how we set the answer..
		if (answer != null) {
			userResponse.setAnswer(answer);
			userResponse.setAnswerText(answerText);
		} 
		else if (answerText == null || answerText.isEmpty()) {
			save = false;
		} 
		else {
			userResponse.setAnswerText(answerText);
			// if it was a valid answer, but now free text (if config changes)
			// then update
			userResponse.setAnswer(null);
		}

		if (save) {
			userResponseRespository.saveAndFlush(userResponse);
		}
	}

	/**
	 * Return the lighweight DTO object for smaller front end payload.
	 * 
	 * @param stepId
	 * @param userModuleId
	 * @return
	 */
	public UserResponseDTO getUserReponse(Long stepId, Long userModuleId) {
		Step step = stepRespository.findOne(stepId);
		UserModule userModule = userModuleRepository.findOne(userModuleId);
		List<UserResponse> responses = userResponseRespository
				.findAllByUserModuleAndStep(userModule, step);

		UserResponseDTO dto = null;
		final List<AnswerDTO> answers = new ArrayList<AnswerDTO>(
				responses.size());

		if (responses != null) {
			responses.forEach((r) -> {

				answers.add(new AnswerDTO(r.getQuestion().getId(), (r
						.getAnswer() == null ? null : r.getAnswer().getId()), r
						.getAnswerText(), (r.getAnswer() == null ? null : r
						.getAnswer().getCorrect())));
			});

			dto = new UserResponseDTO(userModuleId, stepId, answers);
		}

		return dto;
	}

	@Transactional(readOnly = true)
	public Map<Answer, Integer> getAggregatedReponsesForQuestion(Long questionId) {

		// We need to double check that the hashcode for answers allows storage
		// in this map correctly...
		Map<Answer, Integer> results = new HashMap<Answer, Integer>();

		// TODO null check?
		Set<Answer> answers = questionRepository.findOne(questionId)
				.getAnswers();

		// we just return the raw data here. The client can then mark this as a
		// percentage using the
		// source data set size vs. the value
		if (answers != null && answers.size() > 0) {
			// get ALL user responses for answer
			// then percentage against number of answers
			answers.forEach((a) -> results.put(a, userResponseRespository
					.findAllByAnswer(a).size()));
		}

		return results;
	}
}
