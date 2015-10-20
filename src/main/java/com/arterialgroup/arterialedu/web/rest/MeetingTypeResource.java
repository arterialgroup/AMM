package com.arterialgroup.arterialedu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arterialgroup.arterialedu.domain.MeetingType;
import com.arterialgroup.arterialedu.repository.MeetingTypeRepository;
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
 * REST controller for managing MeetingType.
 */
@RestController
@RequestMapping("/api")
public class MeetingTypeResource {

    private final Logger log = LoggerFactory.getLogger(MeetingTypeResource.class);

    @Inject
    private MeetingTypeRepository meetingTypeRepository;

    /**
     * POST  /meetingTypes -> Create a new meetingType.
     */
    @RequestMapping(value = "/meetingTypes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody MeetingType meetingType) throws URISyntaxException {
        log.debug("REST request to save MeetingType : {}", meetingType);
        if (meetingType.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new meetingType cannot already have an ID").build();
        }
        meetingTypeRepository.save(meetingType);
        return ResponseEntity.created(new URI("/api/meetingTypes/" + meetingType.getId())).build();
    }

    /**
     * PUT  /meetingTypes -> Updates an existing meetingType.
     */
    @RequestMapping(value = "/meetingTypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody MeetingType meetingType) throws URISyntaxException {
        log.debug("REST request to update MeetingType : {}", meetingType);
        if (meetingType.getId() == null) {
            return create(meetingType);
        }
        meetingTypeRepository.save(meetingType);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /meetingTypes -> get all the meetingTypes.
     */
    @RequestMapping(value = "/meetingTypes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MeetingType> getAll() {
        log.debug("REST request to get all MeetingTypes");
        return meetingTypeRepository.findAll();
    }

    /**
     * GET  /meetingTypes/:id -> get the "id" meetingType.
     */
    @RequestMapping(value = "/meetingTypes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MeetingType> get(@PathVariable Long id) {
        log.debug("REST request to get MeetingType : {}", id);
        return Optional.ofNullable(meetingTypeRepository.findOne(id))
            .map(meetingType -> new ResponseEntity<>(
                meetingType,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /meetingTypes/:id -> delete the "id" meetingType.
     */
    @RequestMapping(value = "/meetingTypes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete MeetingType : {}", id);
        meetingTypeRepository.delete(id);
    }
}
