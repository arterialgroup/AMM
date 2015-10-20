package com.arterialgroup.arterialedu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arterialgroup.arterialedu.domain.ModuleType;
import com.arterialgroup.arterialedu.repository.ModuleTypeRepository;
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
 * REST controller for managing ModuleType.
 */
@RestController
@RequestMapping("/api")
public class ModuleTypeResource {

    private final Logger log = LoggerFactory.getLogger(ModuleTypeResource.class);

    @Inject
    private ModuleTypeRepository moduleTypeRepository;

    /**
     * POST  /moduleTypes -> Create a new moduleType.
     */
    @RequestMapping(value = "/moduleTypes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody ModuleType moduleType) throws URISyntaxException {
        log.debug("REST request to save ModuleType : {}", moduleType);
        if (moduleType.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new moduleType cannot already have an ID").build();
        }
        moduleTypeRepository.save(moduleType);
        return ResponseEntity.created(new URI("/api/moduleTypes/" + moduleType.getId())).build();
    }

    /**
     * PUT  /moduleTypes -> Updates an existing moduleType.
     */
    @RequestMapping(value = "/moduleTypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody ModuleType moduleType) throws URISyntaxException {
        log.debug("REST request to update ModuleType : {}", moduleType);
        if (moduleType.getId() == null) {
            return create(moduleType);
        }
        moduleTypeRepository.save(moduleType);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /moduleTypes -> get all the moduleTypes.
     */
    @RequestMapping(value = "/moduleTypes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ModuleType> getAll() {
        log.debug("REST request to get all ModuleTypes");
        return moduleTypeRepository.findAll();
    }

    /**
     * GET  /moduleTypes/:id -> get the "id" moduleType.
     */
    @RequestMapping(value = "/moduleTypes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ModuleType> get(@PathVariable Long id) {
        log.debug("REST request to get ModuleType : {}", id);
        return Optional.ofNullable(moduleTypeRepository.findOne(id))
            .map(moduleType -> new ResponseEntity<>(
                moduleType,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /moduleTypes/:id -> delete the "id" moduleType.
     */
    @RequestMapping(value = "/moduleTypes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete ModuleType : {}", id);
        moduleTypeRepository.delete(id);
    }
}
