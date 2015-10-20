package com.arterialgroup.arterialedu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arterialgroup.arterialedu.domain.Attendee;
import com.arterialgroup.arterialedu.domain.Meeting;
import com.arterialgroup.arterialedu.domain.Step;
import com.arterialgroup.arterialedu.domain.UserModule;
import com.arterialgroup.arterialedu.domain.UserProgress;
import com.arterialgroup.arterialedu.repository.AttendeeRepository;
import com.arterialgroup.arterialedu.repository.MeetingRepository;
import com.arterialgroup.arterialedu.repository.StepRepository;
import com.arterialgroup.arterialedu.repository.UserModuleRepository;
import com.arterialgroup.arterialedu.repository.UserProgressRepository;

@Service
@Transactional
public class UserProgressService {

	private final Logger log = LoggerFactory
			.getLogger(UserProgressService.class);

	@Inject
	private UserModuleRepository userModuleRepository;

	@Inject
	private StepRepository stepRespository;

	@Inject
	private UserProgressRepository userProgressRepository;

	@Inject
	private MeetingRepository meetingRepository;

	@Inject
	private AttendeeRepository attendeeRepository;

	@Transactional(readOnly = true)
	private UserProgress getCurrentRecord(UserModule userModule) {
		return userProgressRepository.findOneByUserModule(userModule).orElse(
				null);
	}

	@Transactional
	public void startProgress(Long userModuleId, LocalDate startDate) {

		UserModule moduleForUser = userModuleRepository.findOne(userModuleId);

		// step id is defaulted to zero - 0
		if (getCurrentRecord(moduleForUser) == null) {
			UserProgress progress = new UserProgress();
			progress.setUserModule(moduleForUser);
			progress.setStep(null);
			progress.setStartDate(startDate);

			// TODO check whether we should flush (write to db)
			// or just leave object in level1 cache to speed up system
			userProgressRepository.save(progress);
		} else {
			// Already started....
		}

		// TODO may need section id as well
	}

	/*
	 * Make the call transactional as we will be updating the table based on a
	 * current action
	 */
	@Transactional
	public void trackProgress(Long userProgressId, Long stepId) {

		// find user module instance
		UserProgress currentProgress = userProgressRepository.findOne(userProgressId);

		// we could just loop through modules/sections/steps to find the current
		// step but
		// if we change these to lazy loaded collections we dont need the DB hit
		// additionally we also don't know what section we are on (and don't
		// really care) so
		// one hit to get the step record is safer
		Step completedStep = stepRespository.findOne(stepId);

		currentProgress.setStep(completedStep);

		// use these to update the current record for the user module instance
		userProgressRepository.saveAndFlush(currentProgress);
	}

	@Transactional
	public void endProgress(Long userProgressId, Long stepId, LocalDate endDate) {

		UserProgress current = userProgressRepository.findOne(userProgressId);

		// step id is defaulted to zero - 0
		// TODO place into lambda????
		if (current != null && current.getStep() != null
				&& current.getStep().getId() > 0
				&& current.getStartDate() != null) {
			current.setStep(stepRespository.findOne(stepId));
			current.setEndDate(endDate);
			// TODO save without flushing???
			userProgressRepository.save(current);
		}

	}

	// No write operation...
	// No need to wrap in transaction as private method is..
	public UserProgress getCurrentProgress(Long userModuleId) {
		return getCurrentRecord(userModuleRepository.findOne(userModuleId));
	}

	/**
	 * Return a list (grouped) of user progress for a meeting
	 * 
	 * @param meetingId
	 * @return
	 */
	public Map<Attendee, List<UserProgress>> getProgressForMeeting(
			Long meetingId) {

		final Map<Attendee, List<UserProgress>> progress = new HashMap<Attendee, List<UserProgress>>();

		// find all user progress objects by selecting all modules tied to a
		// meeting
		Meeting meeting = meetingRepository.findOne(meetingId);

		if (meeting != null) {
			List<Attendee> attendees = attendeeRepository
					.findAllByMeeting(meeting);

			attendees
					.forEach((a) -> {
						progress.put(a, userProgressRepository
								.findProgressForUser(a.getUser()));

					});
		}

		return progress;

	}
}
