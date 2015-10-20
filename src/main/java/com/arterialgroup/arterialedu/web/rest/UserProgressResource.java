package com.arterialgroup.arterialedu.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.arterialgroup.arterialedu.domain.Attendee;
import com.arterialgroup.arterialedu.domain.UserProgress;
import com.arterialgroup.arterialedu.service.UserProgressService;
import com.arterialgroup.arterialedu.web.propertyeditors.LocaleDateTimeEditor;
import com.arterialgroup.arterialedu.web.rest.dto.AttendeeStatusDTO;
import com.arterialgroup.arterialedu.web.rest.dto.UserModuleStatusDTO;
import com.arterialgroup.arterialedu.web.rest.dto.UserProgressDTO;
import com.codahale.metrics.annotation.Timed;

/**
 * We could have combined progress and response into a single REST api for user
 * module management But progress can be called independently of response as it
 * may be used for display stats etc. Therefore it makes sense to separate them,
 * from a modular approach
 * 
 * @author bradleyr
 * 
 */
@RestController
@RequestMapping("/api")
public class UserProgressResource {

	@Inject
	private UserProgressService userProgressService;

	/**
	 * Required here for binding start and end date from client requests (this
	 * allows null values when start and completion is not being validated)
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(LocalDate.class, new LocaleDateTimeEditor(
				"yyyy-MM-dd", false));
	}

	@RequestMapping(value = "/user/progress/{userModuleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserProgressDTO> getProgress(
			@PathVariable Long userModuleId) {

		return Optional
				.ofNullable(
						userProgressService.getCurrentProgress(userModuleId))
				.map(progress -> new ResponseEntity<>(new UserProgressDTO(
						progress.getId(), progress.getUserModule().getId(),
						(progress.getStep() != null ? progress.getStep().getId() : null), progress.getStartDate(),
						progress.getEndDate()), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

	}

	@RequestMapping(value = "/user/progress/start", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> startProgress(
			@RequestBody UserProgressDTO userProgress) {

		if (userProgress.getStartDate() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			userProgressService.startProgress(userProgress.getUserModuleId(),
					userProgress.getStartDate());
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/user/progress/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateProgress(
			@RequestBody UserProgressDTO userProgress) {

		// TODO may also need to store sectionId just for ease of use, so we can
		// navigate easier

		userProgressService.trackProgress(userProgress.getUserProgressId(),
				userProgress.getStepId());

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/user/progress/end", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> endProgress(
			@RequestBody UserProgressDTO userProgress) {

		if (userProgress.getEndDate() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			userProgressService.endProgress(userProgress.getUserProgressId(),
					userProgress.getStepId(), userProgress.getEndDate());
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * GET /user/progress/formeeting:meetingId -> get the list of statuses for
	 * attendees of a meeting TODO might just want high level instead of
	 * including the user progress
	 */
	@RequestMapping(value = "/user/progress/formeeting/{meetingId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<AttendeeStatusDTO>> getUserProgressForMeeting(
			@PathVariable Long meetingId) {
		return Optional
				.ofNullable(
						userProgressService.getProgressForMeeting(meetingId))
				.map(progress -> new ResponseEntity<>(mapToDto(progress),
						HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
	}

	private List<AttendeeStatusDTO> mapToDto(
			Map<Attendee, List<UserProgress>> progress) {
		List<AttendeeStatusDTO> statuses = new ArrayList<AttendeeStatusDTO>(
				progress.keySet().size());

		progress.keySet().forEach((k) -> {
			List<UserModuleStatusDTO> umsList = new ArrayList<UserModuleStatusDTO>(
					progress.get(k).size());
			
			progress.get(k).forEach((v) -> {
				UserModuleStatusDTO ums = new UserModuleStatusDTO(v
						.getUserModule().getModule().getName(), (v
						.getEndDate() != null));
				umsList.add(ums);
			});
			
			StringBuilder name = new StringBuilder(k.getUser().getFirstName())
			.append(" ").append(k.getUser().getLastName());

			AttendeeStatusDTO aDto = new AttendeeStatusDTO(name.toString(),
					umsList, k.getAttended(), k.getPointsAwarded(), k
							.getCertificatePath());
			statuses.add(aDto);
			
		});

		return statuses;
	}

}
