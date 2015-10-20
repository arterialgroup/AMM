package com.arterialgroup.arterialedu.repository;

import com.arterialgroup.arterialedu.domain.Module;
import com.arterialgroup.arterialedu.domain.Section;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Section entity.
 */
public interface SectionRepository extends JpaRepository<Section,Long> {
	
	public List<Section> findByModule(Module module);
}
