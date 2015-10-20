package com.arterialgroup.arterialedu.repository;

import com.arterialgroup.arterialedu.domain.Question;
import com.arterialgroup.arterialedu.domain.Step;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Question entity.
 */
public interface QuestionRepository extends JpaRepository<Question,Long> {
	
	public List<Question> findByStep(Step step);

}
