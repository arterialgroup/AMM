package com.arterialgroup.arterialedu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arterialgroup.arterialedu.domain.Attendee;
import com.arterialgroup.arterialedu.domain.User;
import com.arterialgroup.arterialedu.repository.AttendeeRepository;
import com.arterialgroup.arterialedu.repository.UserRepository;
import com.arterialgroup.arterialedu.service.AttendeeService;
import com.arterialgroup.arterialedu.web.rest.dto.AttendeeStatusDTO;
import com.arterialgroup.arterialedu.web.rest.dto.ResponseDTO;
import com.arterialgroup.arterialedu.web.rest.dto.UserDTO;
import com.arterialgroup.arterialedu.web.rest.util.PaginationUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.Console;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Attendee.
 */
@RestController
@RequestMapping("/api")
public class AttendeeResource {

	private final Logger log = LoggerFactory.getLogger(AttendeeResource.class);

	@Inject
	private AttendeeRepository attendeeRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private AttendeeService attendeeService;

	/**
	 * POST /attendees -> Create a new attendee.
	 */
	@RequestMapping(value = "/attendees", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> create(@RequestBody Attendee attendee)
			throws URISyntaxException {
		log.debug("REST request to save Attendee : {}", attendee);
		if (attendee.getId() != null) {
			return ResponseEntity
					.badRequest()
					.header("Failure",
							"A new attendee cannot already have an ID").build();
		}
		attendeeRepository.save(attendee);
		return ResponseEntity.created(
				new URI("/api/attendees/" + attendee.getId())).build();
	}

	/**
	 * PUT /attendees -> Updates an existing attendee.
	 */
	@RequestMapping(value = "/attendees", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> update(@RequestBody Attendee attendee)
			throws URISyntaxException {
		log.debug("REST request to update Attendee : {}", attendee);
		if (attendee.getId() == null) {
			return create(attendee);
		}
		attendeeRepository.save(attendee);
		return ResponseEntity.ok().build();
	}

	/**
	 * GET /attendees -> get all the attendees.
	 */
	@RequestMapping(value = "/attendees", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Attendee>> getAll(
			@RequestParam(value = "page", required = false) Integer offset,
			@RequestParam(value = "per_page", required = false) Integer limit)
			throws URISyntaxException {
		Page<Attendee> page = attendeeRepository.findAll(PaginationUtil
				.generatePageRequest(offset, limit));
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
				page, "/api/attendees", offset, limit);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /attendees/:id -> get the "id" attendee.
	 */
	@RequestMapping(value = "/attendees/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Attendee> get(@PathVariable Long id) {
		log.debug("REST request to get Attendee : {}", id);
		return Optional.ofNullable(attendeeRepository.findOne(id))
				.map(attendee -> new ResponseEntity<>(attendee, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /attendees/:id -> delete the "id" attendee.
	 */
	@RequestMapping(value = "/attendees/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public void delete(@PathVariable Long id) {
		log.debug("REST request to delete Attendee : {}", id);
		attendeeRepository.delete(id);
	}

	/**
	 * POST /register -> register the attendee for a meeting. IF they already
	 * exist, then just return OK, else CREATED The client is responsible for
	 * then performing authentication via api/authentication which will handle
	 * login etc based on j_username and j_password additional data is only used
	 * for masked registration process
	 */
	@RequestMapping(value = "/attendees/register/{meetingId}", 
			method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<ResponseDTO> registerAttendeeForMeeting(
			@Valid @RequestBody UserDTO userDTO, HttpServletRequest request,
			@PathVariable("meetingId") Long meetingId) {
		return attendeeRepository
				.findOneByUserLoginAndMeeting(userDTO.getEmail().toLowerCase(),
						meetingId)
				.map(attendee -> new ResponseEntity<>(
						new ResponseDTO("Attendee for meeting already exists"), HttpStatus.OK))
				.orElseGet(
						() -> {

							if (attendeeService.addAttendee(userDTO, meetingId)) {
								return new ResponseEntity<>(
										new ResponseDTO("Registered Attendee for meeting"),
										HttpStatus.CREATED);
							} else {
								return new ResponseEntity<>(
										new ResponseDTO("Unable to register attendee for meeting"),
										HttpStatus.BAD_REQUEST);
							}
						});
	}
}
