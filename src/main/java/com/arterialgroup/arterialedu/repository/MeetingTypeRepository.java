package com.arterialgroup.arterialedu.repository;

import com.arterialgroup.arterialedu.domain.MeetingType;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MeetingType entity.
 */
public interface MeetingTypeRepository extends JpaRepository<MeetingType,Long> {

}
