package com.arterialgroup.arterialedu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arterialgroup.arterialedu.domain.Module;
import com.arterialgroup.arterialedu.repository.ModuleRepository;
import com.arterialgroup.arterialedu.service.ModuleService;
import com.arterialgroup.arterialedu.web.rest.dto.ModuleDTO;
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
 * REST controller for managing Module.
 */
@RestController
@RequestMapping("/api")
public class ModuleResource {

    private final Logger log = LoggerFactory.getLogger(ModuleResource.class);

    @Inject
    private ModuleRepository moduleRepository;
    
    @Inject
    private ModuleService moduleService;

    /**
     * POST  /modules -> Create a new module.
     */
    @RequestMapping(value = "/modules",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Module module) throws URISyntaxException {
        log.debug("REST request to save Module : {}", module);
        if (module.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new module cannot already have an ID").build();
        }
        moduleRepository.save(module);
        return ResponseEntity.created(new URI("/api/modules/" + module.getId())).build();
    }

    /**
     * PUT  /modules -> Updates an existing module.
     */
    @RequestMapping(value = "/modules",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Module module) throws URISyntaxException {
        log.debug("REST request to update Module : {}", module);
        if (module.getId() == null) {
            return create(module);
        }
        moduleRepository.save(module);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /modules -> get all the modules.
     */
    @RequestMapping(value = "/modules",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Module>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Module> page = moduleRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/modules", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /modules/:id -> get the "id" module.
     */
    @RequestMapping(value = "/modules/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Module> get(@PathVariable Long id) {
        log.debug("REST request to get Module : {}", id);
        return Optional.ofNullable(moduleRepository.findOne(id))
            .map(module -> new ResponseEntity<>(
                module,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /modules/:id -> delete the "id" module.
     */
    @RequestMapping(value = "/modules/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Module : {}", id);
        moduleRepository.delete(id);
    }
    
    /**
     * TODO flesh out this service to perform more error checking
     * @param module
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/modules/generate",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> generateModule(@Valid @RequestBody ModuleDTO module) throws URISyntaxException {
        log.debug("REST request to save Module : {}", module);
        if(moduleService.generateModule(module)){
        	return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else{
        	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
