package com.arterialgroup.arterialedu.service;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arterialgroup.arterialedu.domain.Attendee;
import com.arterialgroup.arterialedu.domain.Authority;
import com.arterialgroup.arterialedu.domain.Meeting;
import com.arterialgroup.arterialedu.domain.MeetingModule;
import com.arterialgroup.arterialedu.domain.User;
import com.arterialgroup.arterialedu.domain.UserModule;
import com.arterialgroup.arterialedu.repository.AttendeeRepository;
import com.arterialgroup.arterialedu.repository.AuthorityRepository;
import com.arterialgroup.arterialedu.repository.MeetingModuleRepository;
import com.arterialgroup.arterialedu.repository.MeetingRepository;
import com.arterialgroup.arterialedu.repository.UserModuleRepository;
import com.arterialgroup.arterialedu.repository.UserRepository;
import com.arterialgroup.arterialedu.service.util.CsvUtil;
import com.arterialgroup.arterialedu.service.util.RandomUtil;
import com.arterialgroup.arterialedu.web.rest.dto.UserDTO;

/**
 * A service layer for managing attendee data
 * 
 * @author bradleyr
 *
 */
@Service
@Transactional
public class AttendeeService {

	// TODO extend this, and possibly handle rep data as well
	@SuppressWarnings("unused")
	private final int TITLE = 0;
	private final int FIRST_NAME = 1;
	private final int LAST_NAME = 2;
	private final int EMAIL = 3;
	private final int PROVIDER_NUMBER = 4;

	private final String LANG_KEY = "en";

	@Inject
	private AttendeeRepository attendeeRepository;
	
	@Inject
    private PasswordEncoder passwordEncoder;

	@Inject
	private UserRepository userRepository;

	@Inject
	private UserModuleRepository userModuleRepository;

	@Inject
	private AuthorityRepository authorityRepository;

	@Inject
	private MeetingRepository meetingRepository;

	@Inject
	private MeetingModuleRepository meetingModuleRepository;

	private Attendee createAttendeeForMeeting(User user, Long meetingId,
			boolean imported) {
		Meeting meeting = meetingRepository.findOneByActivityId(meetingId);
		Attendee newAttendee = new Attendee();
		newAttendee.setUser(user);
		newAttendee.setMeeting(meeting);
		newAttendee.setImported(imported);

		Attendee attendee = attendeeRepository.saveAndFlush(newAttendee);

		// now add all activities for that user..
		List<MeetingModule> meetingActivities = meetingModuleRepository
				.findAllByMeeting(meeting);

		meetingActivities.forEach((a) -> {

			UserModule userModule = new UserModule();
			userModule.setModule(a.getModule());
			userModule.setUser(user);

			userModuleRepository.saveAndFlush(userModule);

		});

		return attendee;
	}

	public void assignModulesForAttendees(MeetingModule meetingModule) {

		List<Attendee> attendees = attendeeRepository
				.findAllByMeeting(meetingModule.getMeeting());
		
		if (attendees != null) {
			attendees.forEach((a) -> {
				UserModule userModule = new UserModule();
				userModule.setModule(meetingModule.getModule());
				userModule.setUser(a.getUser());
				userModuleRepository.saveAndFlush(userModule);
			});
		}
	}

	public void addAttendeesFromFile(InputStream fileStream, Long meetingId) {

		// TODO will need to pass in activity ID as part of the service or
		// dataset and add the attendees to
		// the already existing meeting (via a lookup)
		List<String[]> rawDataMatrix = CsvUtil.extractValues(fileStream);

		// its here in the service that we define what fields map to the objects

		// its means this services encapsulates the business logic

		// the rest controller (client) should only extract data from a file
		// upload object
		// and pass it to the service to actually create the objects
		if (rawDataMatrix != null && !rawDataMatrix.isEmpty()) {
			rawDataMatrix
					.forEach((element) -> {

						User newUser = new User();
						Authority authority = authorityRepository
								.findOne("ROLE_USER");
						Set<Authority> authorities = new HashSet<>();
						newUser.setLogin(element[EMAIL]);
						newUser.setPassword(passwordEncoder.encode(element[PROVIDER_NUMBER]));
						newUser.setFirstName(element[FIRST_NAME]);
						newUser.setLastName(element[LAST_NAME]);
						newUser.setEmail(element[EMAIL]);
						newUser.setLangKey(LANG_KEY);
						newUser.setActivated(true);
						// new user gets registration key
						newUser.setActivationKey(RandomUtil
								.generateActivationKey());
						authorities.add(authority);
						newUser.setAuthorities(authorities);
						userRepository.saveAndFlush(newUser);

						@SuppressWarnings("unused")
						Attendee newAttendee = createAttendeeForMeeting(
								newUser, meetingId, true);
					});
		}
	}

	/**
	 * Service for allowing a user sign in (registration process) to occur and
	 * add the attendee
	 * 
	 * @param newUser
	 * @param meetingId
	 * @return
	 */
	public Boolean addAttendee(UserDTO newUserDTO, Long meetingId) {

		User newUser = new User();
		Authority authority = authorityRepository.findOne("ROLE_USER");
		Set<Authority> authorities = new HashSet<>();
		newUser.setLogin(newUserDTO.getLogin());
		newUser.setPassword(passwordEncoder.encode(newUserDTO.getPassword()));
		newUser.setAccreditation(newUserDTO.getAccreditation());
		newUser.setAccreditationType(newUserDTO.getAccreditationType());
		newUser.setFirstName(newUserDTO.getFirstName());
		newUser.setLastName(newUserDTO.getLastName());
		newUser.setEmail(newUserDTO.getEmail().toLowerCase());
		newUser.setState(newUserDTO.getState());
		newUser.setLangKey(newUserDTO.getLangKey());
		newUser.setActivated(true);
		// new user gets registration key
		newUser.setActivationKey(RandomUtil.generateActivationKey());
		authorities.add(authority);
		newUser.setAuthorities(authorities);
		userRepository.saveAndFlush(newUser);

		Attendee newAttendee = createAttendeeForMeeting(newUser, meetingId,
				false);

		return (newAttendee != null && newAttendee.getId() != null);
	}

	/**
	 * Return the attendee record for the current user request (designed in mind
	 * for HCP interface access selecting their attendance record by meeting)
	 * 
	 * @param userId
	 * @return
	 */
	public Attendee getAttendeeForMeeting(Long userId, Long meetingId) {

		User user = userRepository.findOne(userId);
		Meeting meeting = meetingRepository.findOne(meetingId);

		Attendee attendee = null;

		if (user != null && meeting != null) {
			attendee = attendeeRepository.findByUserAndMeeting(user, meeting);
		}

		return attendee;
	}

	/**
	 * Return all attendees for the current meeting designed in mind to fetch
	 * data for the Rep interface to list all TODO may need rep filter on
	 * attendee/meeting objects
	 * 
	 * @param meetingId
	 * @return
	 */
	public List<Attendee> getAttendeesForMeeting(Long meetingId) {

		Meeting meeting = meetingRepository.findOne(meetingId);

		List<Attendee> attendees = null;

		if (meeting != null) {
			attendees = attendeeRepository.findAllByMeeting(meeting);
		}

		return attendees;
	}
}
