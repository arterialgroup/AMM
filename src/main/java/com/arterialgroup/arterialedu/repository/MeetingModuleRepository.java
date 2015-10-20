package com.arterialgroup.arterialedu.repository;

import com.arterialgroup.arterialedu.domain.Meeting;
import com.arterialgroup.arterialedu.domain.MeetingModule;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MeetingModule entity.
 */
public interface MeetingModuleRepository extends JpaRepository<MeetingModule,Long> {

	List<MeetingModule> findAllByMeeting(Meeting meeting);
}
