package com.arterialgroup.arterialedu.repository;

import com.arterialgroup.arterialedu.domain.User;
import com.arterialgroup.arterialedu.domain.UserModule;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserModule entity.
 */
public interface UserModuleRepository extends JpaRepository<UserModule,Long> {

    @Query("select userModule from UserModule userModule where userModule.user.login = ?#{principal.username}")
    List<UserModule> findAllForCurrentUser();
    
    List<UserModule> findByUser(User user);
}
