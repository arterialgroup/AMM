package com.arterialgroup.arterialedu.repository;

import com.arterialgroup.arterialedu.domain.Answer;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Answer entity.
 */
public interface AnswerRepository extends JpaRepository<Answer,Long> {

}
