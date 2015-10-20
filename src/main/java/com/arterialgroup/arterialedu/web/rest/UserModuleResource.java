package com.arterialgroup.arterialedu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arterialgroup.arterialedu.domain.User;
import com.arterialgroup.arterialedu.domain.UserModule;
import com.arterialgroup.arterialedu.repository.UserModuleRepository;
import com.arterialgroup.arterialedu.repository.UserRepository;
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
 * REST controller for managing UserModule.
 */
@RestController
@RequestMapping("/api")
public class UserModuleResource {

    private final Logger log = LoggerFactory.getLogger(UserModuleResource.class);

    @Inject
    private UserRepository userRepository;
    
    @Inject
    private UserModuleRepository userModuleRepository;

    /**
     * POST  /userModules -> Create a new userModule.
     */
    @RequestMapping(value = "/userModules",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody UserModule userModule) throws URISyntaxException {
        log.debug("REST request to save UserModule : {}", userModule);
        if (userModule.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new userModule cannot already have an ID").build();
        }
        userModuleRepository.save(userModule);
        return ResponseEntity.created(new URI("/api/userModules/" + userModule.getId())).build();
    }

    /**
     * PUT  /userModules -> Updates an existing userModule.
     */
    @RequestMapping(value = "/userModules",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody UserModule userModule) throws URISyntaxException {
        log.debug("REST request to update UserModule : {}", userModule);
        if (userModule.getId() == null) {
            return create(userModule);
        }
        userModuleRepository.save(userModule);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /userModules -> get all the userModules.
     */
    @RequestMapping(value = "/userModules",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserModule>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<UserModule> page = userModuleRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userModules", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /userModules/:id -> get the "id" userModule.
     */
    @RequestMapping(value = "/userModules/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserModule> get(@PathVariable Long id) {
        log.debug("REST request to get UserModule : {}", id);
        return Optional.ofNullable(userModuleRepository.findOne(id))
            .map(userModule -> new ResponseEntity<>(
                userModule,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * GET  /userModules/byuser:userId -> get the usermodules by user
     */
    @RequestMapping(value = "/userModules/byuser/{userId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserModule>> getByUser(@PathVariable Long userId) {
        log.debug("REST request to get UserModule by user : {}", userId);
        return Optional.ofNullable(userModuleRepository.findByUser(userRepository.findOne(userId)))
            .map((userModules) -> new ResponseEntity<>(
                userModules,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userModules/:id -> delete the "id" userModule.
     */
    @RequestMapping(value = "/userModules/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete UserModule : {}", id);
        userModuleRepository.delete(id);
    }
}
