package com.arterialgroup.arterialedu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arterialgroup.arterialedu.domain.QuestionGroup;
import com.arterialgroup.arterialedu.repository.QuestionGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing QuestionGroup.
 */
@RestController
@RequestMapping("/api")
public class QuestionGroupResource {

    private final Logger log = LoggerFactory.getLogger(QuestionGroupResource.class);

    @Inject
    private QuestionGroupRepository questionGroupRepository;

    /**
     * POST  /questionGroups -> Create a new questionGroup.
     */
    @RequestMapping(value = "/questionGroups",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody QuestionGroup questionGroup) throws URISyntaxException {
        log.debug("REST request to save QuestionGroup : {}", questionGroup);
        if (questionGroup.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new questionGroup cannot already have an ID").build();
        }
        questionGroupRepository.save(questionGroup);
        return ResponseEntity.created(new URI("/api/questionGroups/" + questionGroup.getId())).build();
    }

    /**
     * PUT  /questionGroups -> Updates an existing questionGroup.
     */
    @RequestMapping(value = "/questionGroups",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody QuestionGroup questionGroup) throws URISyntaxException {
        log.debug("REST request to update QuestionGroup : {}", questionGroup);
        if (questionGroup.getId() == null) {
            return create(questionGroup);
        }
        questionGroupRepository.save(questionGroup);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /questionGroups -> get all the questionGroups.
     */
    @RequestMapping(value = "/questionGroups",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<QuestionGroup> getAll() {
        log.debug("REST request to get all QuestionGroups");
        return questionGroupRepository.findAll();
    }

    /**
     * GET  /questionGroups/:id -> get the "id" questionGroup.
     */
    @RequestMapping(value = "/questionGroups/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QuestionGroup> get(@PathVariable Long id) {
        log.debug("REST request to get QuestionGroup : {}", id);
        return Optional.ofNullable(questionGroupRepository.findOne(id))
            .map(questionGroup -> new ResponseEntity<>(
                questionGroup,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /questionGroups/:id -> delete the "id" questionGroup.
     */
    @RequestMapping(value = "/questionGroups/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete QuestionGroup : {}", id);
        questionGroupRepository.delete(id);
    }
}
