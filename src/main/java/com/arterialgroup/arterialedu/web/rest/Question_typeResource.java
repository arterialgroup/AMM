package com.arterialgroup.arterialedu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arterialgroup.arterialedu.domain.Question_type;
import com.arterialgroup.arterialedu.repository.Question_typeRepository;
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
 * REST controller for managing Question_type.
 */
@RestController
@RequestMapping("/api")
public class Question_typeResource {

    private final Logger log = LoggerFactory.getLogger(Question_typeResource.class);

    @Inject
    private Question_typeRepository question_typeRepository;

    /**
     * POST  /question_types -> Create a new question_type.
     */
    @RequestMapping(value = "/question_types",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Question_type question_type) throws URISyntaxException {
        log.debug("REST request to save Question_type : {}", question_type);
        if (question_type.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new question_type cannot already have an ID").build();
        }
        question_typeRepository.save(question_type);
        return ResponseEntity.created(new URI("/api/question_types/" + question_type.getId())).build();
    }

    /**
     * PUT  /question_types -> Updates an existing question_type.
     */
    @RequestMapping(value = "/question_types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Question_type question_type) throws URISyntaxException {
        log.debug("REST request to update Question_type : {}", question_type);
        if (question_type.getId() == null) {
            return create(question_type);
        }
        question_typeRepository.save(question_type);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /question_types -> get all the question_types.
     */
    @RequestMapping(value = "/question_types",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Question_type> getAll() {
        log.debug("REST request to get all Question_types");
        return question_typeRepository.findAll();
    }

    /**
     * GET  /question_types/:id -> get the "id" question_type.
     */
    @RequestMapping(value = "/question_types/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Question_type> get(@PathVariable Long id) {
        log.debug("REST request to get Question_type : {}", id);
        return Optional.ofNullable(question_typeRepository.findOne(id))
            .map(question_type -> new ResponseEntity<>(
                question_type,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /question_types/:id -> delete the "id" question_type.
     */
    @RequestMapping(value = "/question_types/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Question_type : {}", id);
        question_typeRepository.delete(id);
    }
}
