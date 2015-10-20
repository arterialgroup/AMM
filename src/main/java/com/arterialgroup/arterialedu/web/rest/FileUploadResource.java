package com.arterialgroup.arterialedu.web.rest;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arterialgroup.arterialedu.service.AttendeeService;
import com.arterialgroup.arterialedu.service.MeetingService;

@RestController
@RequestMapping("/api")
public class FileUploadResource {

	private final Logger log = LoggerFactory
			.getLogger(FileUploadResource.class);

	@Inject
	private AttendeeService attendeeService;

	@Inject
	private MeetingService meetingService;

	/**
	 * File Upload service for a collection of managed entities
	 * 
	 * @param name
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/attendees/upload/{meetingId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> handleAttendeeFileUpload(
			@RequestParam("name") String name,
			@RequestParam("file") MultipartFile file,
			@PathVariable Long meetingId) {

		if (!file.isEmpty()) {
			try {

				attendeeService.addAttendeesFromFile(file.getInputStream(), meetingId);

				return new ResponseEntity<>(HttpStatus.OK);
			} catch (Exception e) {
				log.debug("Error processing CSV: " + e.getMessage());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			log.debug("CSV is empty: " + name);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	@RequestMapping(value = "/meeting/resource/upload/{meetingId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> handleMeetingResourceUpload(
			@RequestParam("name") String name,
			@RequestParam("file") MultipartFile file,
			@PathVariable Long meetingId) {

		if (!file.isEmpty()) {
			try {

				meetingService.addResourceToMeeting(file.getOriginalFilename(),
						file.getBytes(), meetingId);

				return new ResponseEntity<>(HttpStatus.OK);
			} catch (Exception e) {
				log.debug("Error processing file: " + e.getMessage());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} else {
			log.debug("File is empty: " + name);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

	}

}
