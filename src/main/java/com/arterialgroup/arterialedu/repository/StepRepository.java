package com.arterialgroup.arterialedu.repository;

import com.arterialgroup.arterialedu.domain.Section;
import com.arterialgroup.arterialedu.domain.Step;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Step entity.
 */
public interface StepRepository extends JpaRepository<Step,Long> {
	
	public List<Step> findBySection(Section section);

}
