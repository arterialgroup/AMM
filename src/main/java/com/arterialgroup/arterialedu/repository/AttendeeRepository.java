package com.arterialgroup.arterialedu.repository;

import com.arterialgroup.arterialedu.domain.Attendee;
import com.arterialgroup.arterialedu.domain.Meeting;
import com.arterialgroup.arterialedu.domain.User;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Attendee entity.
 */
public interface AttendeeRepository extends JpaRepository<Attendee,Long>{

    @Query("select attendee from Attendee attendee where attendee.user.login = ?#{principal.username}")
    List<Attendee> findAllForCurrentUser();
    
    @Query("select attendee from Attendee attendee where attendee.user.login = :login and attendee.meeting.activityId = :activityId")
    Optional<Attendee> findOneByUserLoginAndMeeting(@Param("login")String login, @Param("activityId")Long activityId);
    
    Attendee findByUserAndMeeting(User user, Meeting meeting);
    
    List<Attendee> findAllByMeeting(Meeting meeting);
}
