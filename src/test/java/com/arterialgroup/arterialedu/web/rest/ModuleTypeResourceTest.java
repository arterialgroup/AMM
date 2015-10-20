package com.arterialgroup.arterialedu.web.rest;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.ModuleType;
import com.arterialgroup.arterialedu.repository.ModuleTypeRepository;

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
 * Test class for the ModuleTypeResource REST controller.
 *
 * @see ModuleTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ModuleTypeResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private ModuleTypeRepository moduleTypeRepository;

    private MockMvc restModuleTypeMockMvc;

    private ModuleType moduleType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ModuleTypeResource moduleTypeResource = new ModuleTypeResource();
        ReflectionTestUtils.setField(moduleTypeResource, "moduleTypeRepository", moduleTypeRepository);
        this.restModuleTypeMockMvc = MockMvcBuilders.standaloneSetup(moduleTypeResource).build();
    }

    @Before
    public void initTest() {
        moduleType = new ModuleType();
        moduleType.setName(DEFAULT_NAME);
        moduleType.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createModuleType() throws Exception {
        int databaseSizeBeforeCreate = moduleTypeRepository.findAll().size();

        // Create the ModuleType
        restModuleTypeMockMvc.perform(post("/api/moduleTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(moduleType)))
                .andExpect(status().isCreated());

        // Validate the ModuleType in the database
        List<ModuleType> moduleTypes = moduleTypeRepository.findAll();
        assertThat(moduleTypes).hasSize(databaseSizeBeforeCreate + 1);
        ModuleType testModuleType = moduleTypes.get(moduleTypes.size() - 1);
        assertThat(testModuleType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testModuleType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is empty
    	//Invalid as we have 4 predefined..
        //assertThat(moduleTypeRepository.findAll()).hasSize(0);
    	int databaseSizeBeforeCreate = moduleTypeRepository.findAll().size();
        // set the field null
        moduleType.setName(null);

        // Create the ModuleType, which fails.
        restModuleTypeMockMvc.perform(post("/api/moduleTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(moduleType)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<ModuleType> moduleTypes = moduleTypeRepository.findAll();
        assertThat(moduleTypes).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllModuleTypes() throws Exception {
        // Initialize the database
        moduleTypeRepository.saveAndFlush(moduleType);

        // Get all the moduleTypes
        restModuleTypeMockMvc.perform(get("/api/moduleTypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(moduleType.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getModuleType() throws Exception {
        // Initialize the database
        moduleTypeRepository.saveAndFlush(moduleType);

        // Get the moduleType
        restModuleTypeMockMvc.perform(get("/api/moduleTypes/{id}", moduleType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(moduleType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingModuleType() throws Exception {
        // Get the moduleType
        restModuleTypeMockMvc.perform(get("/api/moduleTypes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModuleType() throws Exception {
        // Initialize the database
        moduleTypeRepository.saveAndFlush(moduleType);

		int databaseSizeBeforeUpdate = moduleTypeRepository.findAll().size();

        // Update the moduleType
        moduleType.setName(UPDATED_NAME);
        moduleType.setDescription(UPDATED_DESCRIPTION);
        restModuleTypeMockMvc.perform(put("/api/moduleTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(moduleType)))
                .andExpect(status().isOk());

        // Validate the ModuleType in the database
        List<ModuleType> moduleTypes = moduleTypeRepository.findAll();
        assertThat(moduleTypes).hasSize(databaseSizeBeforeUpdate);
        ModuleType testModuleType = moduleTypes.get(moduleTypes.size() - 1);
        assertThat(testModuleType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModuleType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteModuleType() throws Exception {
        // Initialize the database
        moduleTypeRepository.saveAndFlush(moduleType);

		int databaseSizeBeforeDelete = moduleTypeRepository.findAll().size();

        // Get the moduleType
        restModuleTypeMockMvc.perform(delete("/api/moduleTypes/{id}", moduleType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ModuleType> moduleTypes = moduleTypeRepository.findAll();
        assertThat(moduleTypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
