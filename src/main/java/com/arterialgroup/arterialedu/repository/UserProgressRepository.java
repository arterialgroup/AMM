package com.arterialgroup.arterialedu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arterialgroup.arterialedu.domain.User;
import com.arterialgroup.arterialedu.domain.UserModule;
import com.arterialgroup.arterialedu.domain.UserProgress;

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
public interface UserProgressRepository extends
		JpaRepository<UserProgress, Long> {

	// TODO JPA tries to create a query that is userModule_id (camel case
	// attribute plus_id)
	// best to either change the JPQ definition and changelog for liquidbase to
	// reflect this
	// or specifically write out the query here on the interface using @query
	// annotations
	//@Query("select u from UserProgress u where u.userModule.id = :user_module.id")
	//Or specify the name of the generated column on the UserModule object and underlying tables so query knows how to build
	Optional<UserProgress> findOneByUserModule(UserModule user_module);
	
	@Query("select u from UserProgress u where u.userModule.user = :user")
	List<UserProgress> findProgressForUser(@Param("user")User user);
}
