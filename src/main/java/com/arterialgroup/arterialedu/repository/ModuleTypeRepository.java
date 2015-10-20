package com.arterialgroup.arterialedu.repository;

import com.arterialgroup.arterialedu.domain.ModuleType;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ModuleType entity.
 */
public interface ModuleTypeRepository extends JpaRepository<ModuleType,Long> {

}
