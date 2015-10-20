package com.arterialgroup.arterialedu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arterialgroup.arterialedu.domain.Question;
import com.arterialgroup.arterialedu.domain.QuestionGroup;
import com.arterialgroup.arterialedu.repository.QuestionRepository;
import com.arterialgroup.arterialedu.repository.StepRepository;
import com.arterialgroup.arterialedu.web.rest.dto.QuestionDTO;
import com.arterialgroup.arterialedu.web.rest.dto.AnswerDTO;
import com.arterialgroup.arterialedu.web.rest.dto.StepQuestionDTO;
import com.arterialgroup.arterialedu.web.rest.util.DTOConstants;
import com.arterialgroup.arterialedu.web.rest.util.PaginationUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Question.
 */
@RestController
@RequestMapping("/api")
public class QuestionResource {

	private final Logger log = LoggerFactory.getLogger(QuestionResource.class);

	@Inject
	private QuestionRepository questionRepository;

	@Inject
	private StepRepository stepRepository;

	/**
	 * POST /questions -> Create a new question.
	 */
	@RequestMapping(value = "/questions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> create(@Valid @RequestBody Question question)
			throws URISyntaxException {
		log.debug("REST request to save Question : {}", question);
		if (question.getId() != null) {
			return ResponseEntity
					.badRequest()
					.header("Failure",
							"A new question cannot already have an ID").build();
		}
		questionRepository.save(question);
		return ResponseEntity.created(
				new URI("/api/questions/" + question.getId())).build();
	}

	/**
	 * PUT /questions -> Updates an existing question.
	 */
	@RequestMapping(value = "/questions", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> update(@Valid @RequestBody Question question)
			throws URISyntaxException {
		log.debug("REST request to update Question : {}", question);
		if (question.getId() == null) {
			return create(question);
		}
		questionRepository.save(question);
		return ResponseEntity.ok().build();
	}

	/**
	 * GET /questions -> get all the questions.
	 */
	@RequestMapping(value = "/questions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Question>> getAll(
			@RequestParam(value = "page", required = false) Integer offset,
			@RequestParam(value = "per_page", required = false) Integer limit)
			throws URISyntaxException {
		Page<Question> page = questionRepository.findAll(PaginationUtil
				.generatePageRequest(offset, limit));
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
				page, "/api/questions", offset, limit);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /questions/:id -> get the "id" question.
	 */
	@RequestMapping(value = "/questions/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Question> get(@PathVariable Long id) {
		log.debug("REST request to get Question : {}", id);
		return Optional.ofNullable(questionRepository.findOne(id))
				.map(question -> new ResponseEntity<>(question, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * This service now returns the questions, grouped by step and by question
	 * group to allow the front end an easier way to render.
	 * 
	 * @param stepId
	 * @return
	 */
	@RequestMapping(value = "/questions/bystep/{stepId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<StepQuestionDTO>> getByStep(@PathVariable Long stepId) {
		log.debug(
				"REST request to get Questions by step id in DTO format : {}",
				stepId);
		return Optional
				.ofNullable(
						questionRepository.findByStep(stepRepository
								.findOne(stepId)))
				.map(questions -> new ResponseEntity<>(mapToDTO(questions),
						HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	private QuestionGroup createNoneGroup(Question question){
		QuestionGroup none = new QuestionGroup();
		none.setId(((long)-1*question.getId()));
		none.setText(DTOConstants.QUESTION_GROUP_NONE);
    	return none;
	}
	
	private List<StepQuestionDTO> mapToDTO(List<Question> domainObjects){

    	QuestionGroup placeholder = new QuestionGroup();
    	placeholder.setId((long)-1);
    	placeholder.setText(DTOConstants.QUESTION_GROUP_NONE);
    	
        Map<QuestionGroup, List<Question>> byQuestionGroup
            = domainObjects.stream()
                       .collect(Collectors.groupingBy((question) -> (question.getQuestionGroup() == null ? createNoneGroup(question) : question.getQuestionGroup())) );

        List<StepQuestionDTO> stepQuestions = new ArrayList<StepQuestionDTO>();
        
        byQuestionGroup.keySet().forEach((k) -> {

        	List<QuestionDTO> qs = new ArrayList<QuestionDTO>();
        	
        	byQuestionGroup.get(k).forEach((q) -> {

        		List<AnswerDTO> answers = new ArrayList<AnswerDTO>(q.getAnswers().size());
        		q.getAnswers().forEach((ans) -> {
        			answers.add(new AnswerDTO(q.getId(), ans.getId(), ans.getText(), ans.getCorrect()));
        		});
        		
        		qs.add(new QuestionDTO(q.getId(), q.getText(), q.getQuestion_type().getId(), q.getQuestion_type().getName(), q.getSeries(), answers));
        	});
        	
        	Boolean isGroup = (k.getId() > 0);
        	String groupText = (isGroup ? k.getText() : null);
        	//A bit of an assumption, but better none the less
        	//none grouped should be one single question, grouped will be multiple but just take the first element
        	Integer order = (qs.size() > 0 && qs.get(0).getSeries() != null ? qs.get(0).getSeries()  : -1);
        	
        	stepQuestions.add(new StepQuestionDTO(isGroup, groupText, order, qs));
        });
        
    	return stepQuestions;
    }

	/**
	 * DELETE /questions/:id -> delete the "id" question.
	 */
	@RequestMapping(value = "/questions/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public void delete(@PathVariable Long id) {
		log.debug("REST request to delete Question : {}", id);
		questionRepository.delete(id);
	}
}
