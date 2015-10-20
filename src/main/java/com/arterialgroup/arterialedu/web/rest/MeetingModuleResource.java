package com.arterialgroup.arterialedu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arterialgroup.arterialedu.domain.MeetingModule;
import com.arterialgroup.arterialedu.repository.MeetingModuleRepository;
import com.arterialgroup.arterialedu.service.AttendeeService;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MeetingModule.
 */
@RestController
@RequestMapping("/api")
public class MeetingModuleResource {

    private final Logger log = LoggerFactory.getLogger(MeetingModuleResource.class);

    @Inject
    private MeetingModuleRepository meetingModuleRepository;
    
    @Inject
    private AttendeeService attendeeService;

    /**
     * POST  /meetingModules -> Create a new meetingModule.
     */
    @RequestMapping(value = "/meetingModules",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody MeetingModule meetingModule) throws URISyntaxException {
        log.debug("REST request to save MeetingModule : {}", meetingModule);
        if (meetingModule.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new meetingModule cannot already have an ID").build();
        }
        
        //save the new meeting module relationship 
        meetingModule = meetingModuleRepository.saveAndFlush(meetingModule);
        
        //assign existing attendees to the new meeting module
        attendeeService.assignModulesForAttendees(meetingModule);
        
        return ResponseEntity.created(new URI("/api/meetingModules/" + meetingModule.getId())).build();
    }

    /**
     * PUT  /meetingModules -> Updates an existing meetingModule.
     */
    @RequestMapping(value = "/meetingModules",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody MeetingModule meetingModule) throws URISyntaxException {
        log.debug("REST request to update MeetingModule : {}", meetingModule);
        if (meetingModule.getId() == null) {
            return create(meetingModule);
        }
        meetingModuleRepository.save(meetingModule);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /meetingModules -> get all the meetingModules.
     */
    @RequestMapping(value = "/meetingModules",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MeetingModule>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<MeetingModule> page = meetingModuleRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/meetingModules", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /meetingModules/:id -> get the "id" meetingModule.
     */
    @RequestMapping(value = "/meetingModules/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MeetingModule> get(@PathVariable Long id) {
        log.debug("REST request to get MeetingModule : {}", id);
        return Optional.ofNullable(meetingModuleRepository.findOne(id))
            .map(meetingModule -> new ResponseEntity<>(
                meetingModule,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /meetingModules/:id -> delete the "id" meetingModule.
     */
    @RequestMapping(value = "/meetingModules/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete MeetingModule : {}", id);
        meetingModuleRepository.delete(id);
    }
}
