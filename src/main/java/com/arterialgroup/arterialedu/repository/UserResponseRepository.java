package com.arterialgroup.arterialedu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arterialgroup.arterialedu.domain.Answer;
import com.arterialgroup.arterialedu.domain.Question;
import com.arterialgroup.arterialedu.domain.Step;
import com.arterialgroup.arterialedu.domain.UserModule;
import com.arterialgroup.arterialedu.domain.UserResponse;

/**
 * Spring JPA will automatically create queries based on the naming convention
 * of the methods and parameters as its smart enough to resolve this...
 * http://docs
 * .spring.io/spring-data/data-commons/docs/1.6.1.RELEASE/reference/html
 * /repositories.html
 * 
 * @author bradleyr
 * 
 */
public interface UserResponseRepository extends
		JpaRepository<UserResponse, Long> {

	// TODO JPA tries to create a query that is userModule_id (camel case
	// attribute plus_id)
	// best to either change the JPQ definition and changelog for liquidbase to
	// reflect this
	// or specifically write out the query here on the interface using @query
	// annotations
	//@Query("select u from UserResponse u where u.userModule.id = :user_module.id")
	Optional<UserResponse> findOneByUserModule(UserModule user_module);
	
	//@Query("select u from UserResponse u where u.userModule.id = :user_module.id and u.step.id = :step.id")
	Optional<UserResponse> findOneByUserModuleAndQuestionAndStep(UserModule user_module, Question question, Step step);
	
	List<UserResponse> findAllByUserModuleAndStep(UserModule user_module, Step step);
	
	List<UserResponse> findAllByAnswer(Answer answer);

}
