package com.arterialgroup.arterialedu.repository;

import com.arterialgroup.arterialedu.domain.QuestionGroup;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the QuestionGroup entity.
 */
public interface QuestionGroupRepository extends JpaRepository<QuestionGroup,Long> {

}
