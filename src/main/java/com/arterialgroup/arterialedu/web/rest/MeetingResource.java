package com.arterialgroup.arterialedu.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.arterialgroup.arterialedu.domain.Meeting;
import com.arterialgroup.arterialedu.repository.MeetingRepository;
import com.arterialgroup.arterialedu.service.MeetingService;
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
import java.net.URL;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Meeting.
 */
@RestController
@RequestMapping("/api")
public class MeetingResource {

	private final Logger log = LoggerFactory.getLogger(MeetingResource.class);

	@Inject
	private MeetingRepository meetingRepository;

	@Inject
	private MeetingService meetingService;

	/**
	 * POST /meetings -> Create a new meeting.
	 */
	@RequestMapping(value = "/meetings", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> create(@Valid @RequestBody Meeting meeting)
			throws URISyntaxException {
		log.debug("REST request to save Meeting : {}", meeting);
		if (meeting.getId() != null) {
			return ResponseEntity
					.badRequest()
					.header("Failure",
							"A new meeting cannot already have an ID").build();
		}
		meetingRepository.save(meeting);
		return ResponseEntity.created(
				new URI("/api/meetings/" + meeting.getId())).build();
	}

	/**
	 * PUT /meetings -> Updates an existing meeting.
	 */
	@RequestMapping(value = "/meetings", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> update(@Valid @RequestBody Meeting meeting)
			throws URISyntaxException {
		log.debug("REST request to update Meeting : {}", meeting);
		if (meeting.getId() == null) {
			return create(meeting);
		}
		meetingRepository.save(meeting);
		return ResponseEntity.ok().build();
	}

	/**
	 * GET /meetings -> get all the meetings.
	 */
	@RequestMapping(value = "/meetings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Meeting>> getAll(
			@RequestParam(value = "page", required = false) Integer offset,
			@RequestParam(value = "per_page", required = false) Integer limit)
			throws URISyntaxException {
		Page<Meeting> page = meetingRepository.findAll(PaginationUtil
				.generatePageRequest(offset, limit));
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
				page, "/api/meetings", offset, limit);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /meetings/:id -> get the "id" meeting.
	 */
	@RequestMapping(value = "/meetings/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Meeting> get(@PathVariable Long id) {
		log.debug("REST request to get Meeting : {}", id);
		return Optional.ofNullable(meetingRepository.findOne(id))
				.map(meeting -> new ResponseEntity<>(meeting, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@RequestMapping(value = "/meetings/resources/{meetingId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<URL>> getMeetingResources(
			@PathVariable Long meetingId) {
		log.debug("REST request to get list of Meeting resources : {}",
				meetingId);
		return Optional
				.ofNullable(meetingService.getResourcesForMeeting(meetingId))
				.map(urls -> new ResponseEntity<>(urls, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /meetings/:id -> delete the "id" meeting.
	 */
	@RequestMapping(value = "/meetings/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public void delete(@PathVariable Long id) {
		log.debug("REST request to delete Meeting : {}", id);
		meetingRepository.delete(id);
	}
}
