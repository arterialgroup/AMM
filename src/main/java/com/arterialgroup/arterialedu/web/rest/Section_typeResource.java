package com.arterialgroup.arterialedu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arterialgroup.arterialedu.domain.Section_type;
import com.arterialgroup.arterialedu.repository.Section_typeRepository;
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
 * REST controller for managing Section_type.
 */
@RestController
@RequestMapping("/api")
public class Section_typeResource {

    private final Logger log = LoggerFactory.getLogger(Section_typeResource.class);

    @Inject
    private Section_typeRepository section_typeRepository;

    /**
     * POST  /section_types -> Create a new section_type.
     */
    @RequestMapping(value = "/section_types",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Section_type section_type) throws URISyntaxException {
        log.debug("REST request to save Section_type : {}", section_type);
        if (section_type.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new section_type cannot already have an ID").build();
        }
        section_typeRepository.save(section_type);
        return ResponseEntity.created(new URI("/api/section_types/" + section_type.getId())).build();
    }

    /**
     * PUT  /section_types -> Updates an existing section_type.
     */
    @RequestMapping(value = "/section_types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Section_type section_type) throws URISyntaxException {
        log.debug("REST request to update Section_type : {}", section_type);
        if (section_type.getId() == null) {
            return create(section_type);
        }
        section_typeRepository.save(section_type);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /section_types -> get all the section_types.
     */
    @RequestMapping(value = "/section_types",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Section_type> getAll() {
        log.debug("REST request to get all Section_types");
        return section_typeRepository.findAll();
    }

    /**
     * GET  /section_types/:id -> get the "id" section_type.
     */
    @RequestMapping(value = "/section_types/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Section_type> get(@PathVariable Long id) {
        log.debug("REST request to get Section_type : {}", id);
        return Optional.ofNullable(section_typeRepository.findOne(id))
            .map(section_type -> new ResponseEntity<>(
                section_type,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /section_types/:id -> delete the "id" section_type.
     */
    @RequestMapping(value = "/section_types/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Section_type : {}", id);
        section_typeRepository.delete(id);
    }
}
