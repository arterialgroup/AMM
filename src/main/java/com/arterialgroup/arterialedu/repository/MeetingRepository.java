package com.arterialgroup.arterialedu.repository;

import java.util.Optional;

import com.arterialgroup.arterialedu.domain.Meeting;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Meeting entity.
 */
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

	@Query("select meeting from Meeting meeting where meeting.activityId = :activityId")
	Meeting findOneByActivityId(@Param("activityId") Long activityId);
}
