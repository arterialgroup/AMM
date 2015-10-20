package com.arterialgroup.arterialedu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arterialgroup.arterialedu.domain.Moderator;
import com.arterialgroup.arterialedu.repository.ModeratorRepository;
import com.arterialgroup.arterialedu.service.ModeratorService;
import com.arterialgroup.arterialedu.web.rest.dto.UserDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Moderator.
 */
@RestController
@RequestMapping("/api")
public class ModeratorResource {

    private final Logger log = LoggerFactory.getLogger(ModeratorResource.class);

    @Inject
    private ModeratorRepository moderatorRepository;
    
    @Inject
    private ModeratorService moderatorService;

    /**
     * POST  /moderators -> Create a new moderator.
     */
    @RequestMapping(value = "/moderators",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Moderator moderator) throws URISyntaxException {
        log.debug("REST request to save Moderator : {}", moderator);
        if (moderator.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new moderator cannot already have an ID").build();
        }
        moderatorRepository.save(moderator);
        return ResponseEntity.created(new URI("/api/moderators/" + moderator.getId())).build();
    }

    /**
     * PUT  /moderators -> Updates an existing moderator.
     */
    @RequestMapping(value = "/moderators",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Moderator moderator) throws URISyntaxException {
        log.debug("REST request to update Moderator : {}", moderator);
        if (moderator.getId() == null) {
            return create(moderator);
        }
        moderatorRepository.save(moderator);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /moderators -> get all the moderators.
     */
    @RequestMapping(value = "/moderators",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Moderator> getAll() {
        log.debug("REST request to get all Moderators");
        return moderatorRepository.findAll();
    }

    /**
     * GET  /moderators/:id -> get the "id" moderator.
     */
    @RequestMapping(value = "/moderators/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Moderator> get(@PathVariable Long id) {
        log.debug("REST request to get Moderator : {}", id);
        return Optional.ofNullable(moderatorRepository.findOne(id))
            .map(moderator -> new ResponseEntity<>(
                moderator,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /moderators/:id -> delete the "id" moderator.
     */
    @RequestMapping(value = "/moderators/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Moderator : {}", id);
        moderatorRepository.delete(id);
    }
    
    /**
	 * This is an internal service that will allow an administrator 
	 * to sign up moderators to specific meetings using a meeting id
	 * This will create an account, which can then be used to login.
	 */
	@RequestMapping(value = "/moderators/register/{meetingId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<?> registerModeratorForMeeting(
			@Valid @RequestBody UserDTO userDTO, HttpServletRequest request,
			@PathVariable Long meetingId) {
		return moderatorRepository
				.findOneByUserLoginAndMeeting(userDTO.getEmail().toLowerCase(),
						meetingId)
				.map(moderator -> new ResponseEntity<>(
						"Moderator for meeting already exists", HttpStatus.OK))
				.orElseGet(
						() -> {
							
							if (moderatorService.addModerator(userDTO, meetingId)) {
								return new ResponseEntity<>(
										"Registered Moderator for meeting",
										HttpStatus.CREATED);
							} else {
								return new ResponseEntity<>(
										"Unable to register Moderator for meeting",
										HttpStatus.BAD_REQUEST);
							}
						});
	}
}
