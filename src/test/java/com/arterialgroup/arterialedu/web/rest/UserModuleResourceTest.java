package com.arterialgroup.arterialedu.web.rest;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.User;
import com.arterialgroup.arterialedu.domain.UserModule;
import com.arterialgroup.arterialedu.repository.UserModuleRepository;
import com.arterialgroup.arterialedu.repository.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.hasItem;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserModuleResource REST controller.
 *
 * @see UserModuleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserModuleResourceTest {


    @Inject
    private UserModuleRepository userModuleRepository;
    
    @Inject
    private UserRepository userRepository;

    private MockMvc restUserModuleMockMvc;

    private UserModule userModule;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserModuleResource userModuleResource = new UserModuleResource();
        ReflectionTestUtils.setField(userModuleResource, "userModuleRepository", userModuleRepository);
        ReflectionTestUtils.setField(userModuleResource, "userRepository", userRepository);
        this.restUserModuleMockMvc = MockMvcBuilders.standaloneSetup(userModuleResource).build();
    }

    @Before
    public void initTest() {
        userModule = new UserModule();
    }

    @Test
    @Transactional
    public void createUserModule() throws Exception {
        int databaseSizeBeforeCreate = userModuleRepository.findAll().size();

        // Create the UserModule
        restUserModuleMockMvc.perform(post("/api/userModules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userModule)))
                .andExpect(status().isCreated());

        // Validate the UserModule in the database
        List<UserModule> userModules = userModuleRepository.findAll();
        assertThat(userModules).hasSize(databaseSizeBeforeCreate + 1);
        UserModule testUserModule = userModules.get(userModules.size() - 1);
    }

    @Test
    @Transactional
    public void getAllUserModules() throws Exception {
        // Initialize the database
        userModuleRepository.saveAndFlush(userModule);

        // Get all the userModules
        restUserModuleMockMvc.perform(get("/api/userModules"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userModule.getId().intValue())));
    }

    @Test
    @Transactional
    public void getUserModule() throws Exception {
        // Initialize the database
        userModuleRepository.saveAndFlush(userModule);

        // Get the userModule
        restUserModuleMockMvc.perform(get("/api/userModules/{id}", userModule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userModule.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserModule() throws Exception {
        // Get the userModule
        restUserModuleMockMvc.perform(get("/api/userModules/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserModule() throws Exception {
        // Initialize the database
        userModuleRepository.saveAndFlush(userModule);

		int databaseSizeBeforeUpdate = userModuleRepository.findAll().size();

        // Update the userModule
        restUserModuleMockMvc.perform(put("/api/userModules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userModule)))
                .andExpect(status().isOk());

        // Validate the UserModule in the database
        List<UserModule> userModules = userModuleRepository.findAll();
        assertThat(userModules).hasSize(databaseSizeBeforeUpdate);
        UserModule testUserModule = userModules.get(userModules.size() - 1);
    }

    @Test
    @Transactional
    public void deleteUserModule() throws Exception {
        // Initialize the database
        userModuleRepository.saveAndFlush(userModule);

		int databaseSizeBeforeDelete = userModuleRepository.findAll().size();

        // Get the userModule
        restUserModuleMockMvc.perform(delete("/api/userModules/{id}", userModule.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserModule> userModules = userModuleRepository.findAll();
        assertThat(userModules).hasSize(databaseSizeBeforeDelete - 1);
    }
    
    @Test
    @Transactional
    public void getUserModulesByUser() throws Exception {
        
    	User user = new User();
    	user.setLogin("login1234");
		user.setPassword("Password1234");
		user.setLangKey("en");
    	
    	user = userRepository.saveAndFlush(user);
    	
    	assertThat(userRepository.findOne(user.getId())).isEqualTo(user);
    	
    	userModule = new UserModule();
    	userModule.setUser(user);
    	// Initialize the database
    	userModule = userModuleRepository.saveAndFlush(userModule);

        assertThat(userModuleRepository.findOne(userModule.getId())).isEqualTo(userModule);
        
        // Get the userModule
        restUserModuleMockMvc.perform(get("/api/userModules/byuser/{userId}", user.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(userModule.getId().intValue()));
    }
}
