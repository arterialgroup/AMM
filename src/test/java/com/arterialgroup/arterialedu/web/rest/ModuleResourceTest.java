package com.arterialgroup.arterialedu.web.rest;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.Module;
import com.arterialgroup.arterialedu.repository.ModuleRepository;
import com.arterialgroup.arterialedu.service.ModuleService;
import com.arterialgroup.arterialedu.web.rest.dto.ModuleDTO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import scala.Predef.any2stringadd;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ModuleResource REST controller.
 *
 * @see ModuleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ModuleResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";

    @Inject
    private ModuleRepository moduleRepository;
    
    @Mock
    private ModuleService moduleService;

    private MockMvc restModuleMockMvc;

    private Module module;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ModuleResource moduleResource = new ModuleResource();
        ReflectionTestUtils.setField(moduleResource, "moduleRepository", moduleRepository);
        ReflectionTestUtils.setField(moduleResource, "moduleService", moduleService);
        this.restModuleMockMvc = MockMvcBuilders.standaloneSetup(moduleResource).build();
    }

    @Before
    public void initTest() {
        module = new Module();
        module.setName(DEFAULT_NAME);
        module.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createModule() throws Exception {
        int databaseSizeBeforeCreate = moduleRepository.findAll().size();

        // Create the Module
        restModuleMockMvc.perform(post("/api/modules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(module)))
                .andExpect(status().isCreated());

        // Validate the Module in the database
        List<Module> modules = moduleRepository.findAll();
        assertThat(modules).hasSize(databaseSizeBeforeCreate + 1);
        Module testModule = modules.get(modules.size() - 1);
        assertThat(testModule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testModule.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(moduleRepository.findAll()).hasSize(0);
        // set the field null
        module.setName(null);

        // Create the Module, which fails.
        restModuleMockMvc.perform(post("/api/modules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(module)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Module> modules = moduleRepository.findAll();
        assertThat(modules).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllModules() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get all the modules
        restModuleMockMvc.perform(get("/api/modules"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(module.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getModule() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

        // Get the module
        restModuleMockMvc.perform(get("/api/modules/{id}", module.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(module.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingModule() throws Exception {
        // Get the module
        restModuleMockMvc.perform(get("/api/modules/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModule() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

		int databaseSizeBeforeUpdate = moduleRepository.findAll().size();

        // Update the module
        module.setName(UPDATED_NAME);
        module.setDescription(UPDATED_DESCRIPTION);
        restModuleMockMvc.perform(put("/api/modules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(module)))
                .andExpect(status().isOk());

        // Validate the Module in the database
        List<Module> modules = moduleRepository.findAll();
        assertThat(modules).hasSize(databaseSizeBeforeUpdate);
        Module testModule = modules.get(modules.size() - 1);
        assertThat(testModule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModule.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteModule() throws Exception {
        // Initialize the database
        moduleRepository.saveAndFlush(module);

		int databaseSizeBeforeDelete = moduleRepository.findAll().size();

        // Get the module
        restModuleMockMvc.perform(delete("/api/modules/{id}", module.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Module> modules = moduleRepository.findAll();
        assertThat(modules).hasSize(databaseSizeBeforeDelete - 1);
    }

    /**
     * TODO just a placeholder test, once completed test more ..
     * @throws Exception
     */
    @Test
    @Transactional
    public void generateModule() throws Exception {

    	ModuleDTO moduleDTO = new ModuleDTO();
    	
    	when(moduleService.generateModule(Mockito.any())).thenReturn(true);
    	
        // Create the Module
        restModuleMockMvc.perform(post("/api/modules/generate")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(moduleDTO)))
                .andExpect(status().isCreated());

    }
    
    @Test
    @Transactional
    public void generateModuleInvalid() throws Exception {

    	ModuleDTO moduleDTO = new ModuleDTO();
    	
    	when(moduleService.generateModule(Mockito.any())).thenReturn(false);
    	
        // Create the Module
        restModuleMockMvc.perform(post("/api/modules/generate")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(moduleDTO)))
                .andExpect(status().isNoContent());

    }
}
