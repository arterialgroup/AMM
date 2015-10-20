package com.arterialgroup.arterialedu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arterialgroup.arterialedu.domain.Section;
import com.arterialgroup.arterialedu.repository.ModuleRepository;
import com.arterialgroup.arterialedu.repository.SectionRepository;
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
 * REST controller for managing Section.
 */
@RestController
@RequestMapping("/api")
public class SectionResource {

    private final Logger log = LoggerFactory.getLogger(SectionResource.class);

    @Inject
    private SectionRepository sectionRepository;
    
    @Inject
    private ModuleRepository moduleRepository;

    /**
     * POST  /sections -> Create a new section.
     */
    @RequestMapping(value = "/sections",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Section section) throws URISyntaxException {
        log.debug("REST request to save Section : {}", section);
        if (section.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new section cannot already have an ID").build();
        }
        sectionRepository.save(section);
        return ResponseEntity.created(new URI("/api/sections/" + section.getId())).build();
    }

    /**
     * PUT  /sections -> Updates an existing section.
     */
    @RequestMapping(value = "/sections",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Section section) throws URISyntaxException {
        log.debug("REST request to update Section : {}", section);
        if (section.getId() == null) {
            return create(section);
        }
        sectionRepository.save(section);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /sections -> get all the sections.
     */
    @RequestMapping(value = "/sections",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Section>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Section> page = sectionRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sections", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sections/:id -> get the "id" section.
     */
    @RequestMapping(value = "/sections/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Section> get(@PathVariable Long id) {
        log.debug("REST request to get Section : {}", id);
        return Optional.ofNullable(sectionRepository.findOne(id))
            .map(section -> new ResponseEntity<>(
                section,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * GET  /sections/bymodule/:moduleid -> get the sections by module
     */
    @RequestMapping(value = "/sections/bymodule/{moduleId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Section>> getByModule(@PathVariable Long moduleId) {
        log.debug("REST request to get Sections by module id : {}", moduleId);
        return Optional.ofNullable(sectionRepository.findByModule(moduleRepository.findOne(moduleId)))
            .map(sections -> new ResponseEntity<>(
                sections,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sections/:id -> delete the "id" section.
     */
    @RequestMapping(value = "/sections/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Section : {}", id);
        sectionRepository.delete(id);
    }
}
