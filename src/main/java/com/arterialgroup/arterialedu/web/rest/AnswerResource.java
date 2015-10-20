package com.arterialgroup.arterialedu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arterialgroup.arterialedu.domain.Answer;
import com.arterialgroup.arterialedu.repository.AnswerRepository;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Answer.
 */
@RestController
@RequestMapping("/api")
public class AnswerResource {

    private final Logger log = LoggerFactory.getLogger(AnswerResource.class);

    @Inject
    private AnswerRepository answerRepository;

    /**
     * POST  /answers -> Create a new answer.
     */
    @RequestMapping(value = "/answers",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Answer answer) throws URISyntaxException {
        log.debug("REST request to save Answer : {}", answer);
        if (answer.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new answer cannot already have an ID").build();
        }
        answerRepository.save(answer);
        return ResponseEntity.created(new URI("/api/answers/" + answer.getId())).build();
    }

    /**
     * PUT  /answers -> Updates an existing answer.
     */
    @RequestMapping(value = "/answers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Answer answer) throws URISyntaxException {
        log.debug("REST request to update Answer : {}", answer);
        if (answer.getId() == null) {
            return create(answer);
        }
        answerRepository.save(answer);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /answers -> get all the answers.
     */
    @RequestMapping(value = "/answers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Answer>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Answer> page = answerRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/answers", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /answers/:id -> get the "id" answer.
     */
    @RequestMapping(value = "/answers/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Answer> get(@PathVariable Long id) {
        log.debug("REST request to get Answer : {}", id);
        return Optional.ofNullable(answerRepository.findOne(id))
            .map(answer -> new ResponseEntity<>(
                answer,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /answers/:id -> delete the "id" answer.
     */
    @RequestMapping(value = "/answers/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Answer : {}", id);
        answerRepository.delete(id);
    }
}
