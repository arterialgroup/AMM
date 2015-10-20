package com.arterialgroup.arterialedu.repository;

import com.arterialgroup.arterialedu.domain.Question_type;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Question_type entity.
 */
public interface Question_typeRepository extends JpaRepository<Question_type,Long> {

}
