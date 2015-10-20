package com.arterialgroup.arterialedu.repository;

import java.util.Optional;

import com.arterialgroup.arterialedu.domain.Moderator;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Moderator entity.
 */
public interface ModeratorRepository extends JpaRepository<Moderator,Long> {

	/**
	 * This is shouting out to use abstract types
	 * We could use the USER table or create a MeetingUser which we can use in the
	 * query and place this into an interface<T> that passes the required type. It
	 * should be clever enough to resolve this
	 * @param login
	 * @param meeting
	 * @return
	 */
	 @Query("select m from Moderator m where m.user.login = :login and m.meeting.id = :meeting")
	 Optional<Moderator> findOneByUserLoginAndMeeting(@Param("login")String login, @Param("meeting")Long meeting);
	
}
