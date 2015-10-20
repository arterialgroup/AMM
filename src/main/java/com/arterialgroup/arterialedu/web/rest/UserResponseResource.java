package com.arterialgroup.arterialedu.web.rest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arterialgroup.arterialedu.domain.Answer;
import com.arterialgroup.arterialedu.domain.UserResponse;
import com.arterialgroup.arterialedu.service.UserResponseService;
import com.arterialgroup.arterialedu.web.rest.dto.UserResponseDTO;
import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping("/api")
public class UserResponseResource {

	private final Logger log = LoggerFactory.getLogger(UserResponseResource.class);
	
	@Inject
	private UserResponseService userResponseService;

	@RequestMapping(value = "/user/response/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> saveResponse(
			@RequestBody UserResponseDTO userResponse) {

		// We only add responses if the user has answered questions, if the step
		// contains none this record is not required
		if (userResponse.getAnswers() != null
				&& userResponse.getAnswers().size() > 0) {

			userResponse.getAnswers().forEach(
					(answer) -> userResponseService.addUserResponse(
							userResponse.getUserModuleId(),
							userResponse.getStepId(), answer.getQuestionId(),
							answer.getAnswerId(), answer.getAnswerText()));
			return new ResponseEntity<>(HttpStatus.CREATED);

		} else {
			// we didn't find any responses to update
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	@RequestMapping(value = "/user/response/byquestion/{questionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Map<Answer, Integer>> getUserResponsesForQuestion(
			@PathVariable Long questionId) {
		return Optional
				.ofNullable(
						userResponseService
								.getAggregatedReponsesForQuestion(questionId))
				.map((results) -> new ResponseEntity<>(results, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
	}

	@RequestMapping(value = "/user/response/byusermoduleforstep/{userModuleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<UserResponseDTO> getUserResponseForStepByUserModule(
			 @PathVariable Long userModuleId, @RequestParam("stepId") Long stepId) {
		
		return Optional
				.ofNullable(
						userResponseService
								.getUserReponse(stepId, userModuleId))
				.map((results) -> new ResponseEntity<>(results, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
	}
}
