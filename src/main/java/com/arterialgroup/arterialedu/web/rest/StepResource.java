package com.arterialgroup.arterialedu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arterialgroup.arterialedu.domain.Step;
import com.arterialgroup.arterialedu.repository.SectionRepository;
import com.arterialgroup.arterialedu.repository.StepRepository;
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
 * REST controller for managing Step.
 */
@RestController
@RequestMapping("/api")
public class StepResource {

    private final Logger log = LoggerFactory.getLogger(StepResource.class);

    @Inject
    private StepRepository stepRepository;
    
    
    @Inject
    private SectionRepository sectionRepository;

    /**
     * POST  /steps -> Create a new step.
     */
    @RequestMapping(value = "/steps",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Step step) throws URISyntaxException {
        log.debug("REST request to save Step : {}", step);
        if (step.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new step cannot already have an ID").build();
        }
        stepRepository.save(step);
        return ResponseEntity.created(new URI("/api/steps/" + step.getId())).build();
    }

    /**
     * PUT  /steps -> Updates an existing step.
     */
    @RequestMapping(value = "/steps",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Step step) throws URISyntaxException {
        log.debug("REST request to update Step : {}", step);
        if (step.getId() == null) {
            return create(step);
        }
        stepRepository.save(step);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /steps -> get all the steps.
     */
    @RequestMapping(value = "/steps",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Step>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Step> page = stepRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/steps", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /steps/:id -> get the "id" step.
     */
    @RequestMapping(value = "/steps/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Step> get(@PathVariable Long id) {
        log.debug("REST request to get Step : {}", id);
        return Optional.ofNullable(stepRepository.findOne(id))
            .map(step -> new ResponseEntity<>(
                step,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * GET  /steps/bysection/:sectionId -> get the steps by section.
     */
    @RequestMapping(value = "/steps/bysection/{sectionId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Step>> getBySection(@PathVariable Long sectionId) {
        log.debug("REST request to get Steps by section : {}", sectionId);
        return Optional.ofNullable(stepRepository.findBySection(sectionRepository.findOne(sectionId)))
            .map(steps -> new ResponseEntity<>(
                steps,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /steps/:id -> delete the "id" step.
     */
    @RequestMapping(value = "/steps/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Step : {}", id);
        stepRepository.delete(id);
    }
}
