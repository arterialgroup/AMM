package com.arterialgroup.arterialedu.web.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.Attendee;
import com.arterialgroup.arterialedu.domain.Module;
import com.arterialgroup.arterialedu.domain.User;
import com.arterialgroup.arterialedu.domain.UserModule;
import com.arterialgroup.arterialedu.domain.UserProgress;
import com.arterialgroup.arterialedu.repository.UserModuleRepository;
import com.arterialgroup.arterialedu.repository.UserProgressRepository;
import com.arterialgroup.arterialedu.service.UserProgressService;
import com.arterialgroup.arterialedu.web.rest.dto.UserProgressDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserProgressResourceTest {

	@Inject
	private UserProgressRepository userProgressRepository;

	@Inject
	private UserModuleRepository userModuleRepository;

	private UserModule mockUserModule;

	@Mock
	private UserProgressService userProgressService;

	private MockMvc restUserProgressMockMvc;

	private UserProgress userProgress;

	@PostConstruct
	public void setup() {
		MockitoAnnotations.initMocks(this);
		UserProgressResource userProgressResource = new UserProgressResource();
		ReflectionTestUtils.setField(userProgressResource,
				"userProgressService", userProgressService);
		this.restUserProgressMockMvc = MockMvcBuilders.standaloneSetup(
				userProgressResource).build();
	}

	@Before
	public void init() {
		mockUserModule = new UserModule();
		userProgress = new UserProgress();
	}

	@Test
	public void testStartProgress() throws IOException, Exception {
		// Initialize the DB
		userModuleRepository.saveAndFlush(mockUserModule);

		UserProgressDTO dto = new UserProgressDTO(null, mockUserModule.getId(),
				new Long(1), new LocalDate(), null);

		restUserProgressMockMvc.perform(
				post("/api/user/progress/start").contentType(
						TestUtil.APPLICATION_JSON_UTF8).content(
						TestUtil.convertObjectToJsonBytes(dto))).andExpect(
				status().isOk());
	}

	@Test
	public void testStartProgressBadRequest() throws IOException, Exception {
		// Initialize the DB
		userModuleRepository.saveAndFlush(mockUserModule);

		UserProgressDTO dto = new UserProgressDTO(null, mockUserModule.getId(),
				new Long(1), null, null);

		restUserProgressMockMvc.perform(
				post("/api/user/progress/start").contentType(
						TestUtil.APPLICATION_JSON_UTF8).content(
						TestUtil.convertObjectToJsonBytes(dto))).andExpect(
				status().isBadRequest());
	}

	@Test
	public void testGetProgress() throws Exception {
		// Initialize the DB
		userModuleRepository.saveAndFlush(mockUserModule);
		UserProgress newProg = new UserProgress();
		newProg.setUserModule(mockUserModule);
		newProg.setStep(null);
		newProg = userProgressRepository.saveAndFlush(newProg);
		
		//mock the service layer..
        when(userProgressService.getCurrentProgress(mockUserModule.getId())).thenReturn(newProg);

		//we actually query this by the user module id... not the id of the progress...
		restUserProgressMockMvc
				.perform(get("/api/user/progress/{userModuleId}", mockUserModule.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.userProgressId").value(newProg.getId().intValue()));
	}

	@Test
	public void testUpdateProgress() throws IOException, Exception {
		// Initialize the DB
		userModuleRepository.saveAndFlush(mockUserModule);

		//mock a save
		UserProgress newProg = new UserProgress();
		newProg.setUserModule(mockUserModule);
		newProg.setStep(null);
		newProg = userProgressRepository.saveAndFlush(newProg);
		
		//update to step 1
		UserProgressDTO dto = new UserProgressDTO(newProg.getId(), mockUserModule.getId(),
				new Long(1), null, new LocalDate());
		
		restUserProgressMockMvc.perform(
				post("/api/user/progress/update").contentType(
						TestUtil.APPLICATION_JSON_UTF8).content(
						TestUtil.convertObjectToJsonBytes(dto))).andExpect(
				status().isCreated());
	}
	
	@Test
	public void testEndProgress() throws IOException, Exception {
		// Initialize the DB
		userModuleRepository.saveAndFlush(mockUserModule);
		
		//mock a save
		UserProgress newProg = new UserProgress();
		newProg.setUserModule(mockUserModule);
		newProg.setStep(null);
		newProg = userProgressRepository.saveAndFlush(newProg);

		UserProgressDTO dto = new UserProgressDTO(newProg.getId(), mockUserModule.getId(),
				new Long(1), null, new LocalDate());

		restUserProgressMockMvc.perform(
				post("/api/user/progress/end").contentType(
						TestUtil.APPLICATION_JSON_UTF8).content(
						TestUtil.convertObjectToJsonBytes(dto))).andExpect(
				status().isOk());
	}

	@Test
	public void testEndProgressBadRequest() throws IOException, Exception {
		// Initialize the DB
		userModuleRepository.saveAndFlush(mockUserModule);

		UserProgressDTO dto = new UserProgressDTO(new Long(1), mockUserModule.getId(),
				new Long(1), null, null);

		restUserProgressMockMvc.perform(
				post("/api/user/progress/end").contentType(
						TestUtil.APPLICATION_JSON_UTF8).content(
						TestUtil.convertObjectToJsonBytes(dto))).andExpect(
				status().isBadRequest());
	}
	
	@Test
    public void testGetProgressForMeeting() throws Exception {

		//mock the service layer..
		Map<Attendee, List<UserProgress>> map = new HashMap<Attendee, List<UserProgress>>();
		
		Attendee a = new Attendee();
		a.setAttended(true);
		a.setPointsAwarded(false);
		User u = new User();
		u.setFirstName("FIRST");
		u.setLastName("LAST");
		a.setUser(u);
		
		List<UserProgress> prog = new ArrayList<UserProgress>();
		Module m = new Module();
		m.setName("MOD");
		UserModule um = new UserModule();
		um.setModule(m);
		um.setUser(u);
		UserProgress p = new UserProgress();
		p.setUserModule(um);
		prog.add(p);
		
		map.put(a, prog);

        when(userProgressService.getProgressForMeeting((long)1)).thenReturn(map);

        // Test the call maps POJO to DTO..
		restUserProgressMockMvc.perform(get("/api/user/progress/formeeting/{meetingId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].attendeeFullName").value(hasItem("FIRST LAST")))
                .andExpect(jsonPath("$.[*].attended").value(hasItem(true)))
                .andExpect(jsonPath("$.[*].pointsAwarded").value(hasItem(false)))
                .andExpect(jsonPath("$.[*].moduleStatuses.[*].moduleName").value(hasItem("MOD")));
    }
	
	@Test
    public void testGetProgressForMeetingNotExists() throws Exception {

		//mock the service layer..
        when(userProgressService.getProgressForMeeting((long)1)).thenReturn(null);

        // Test REST service returns correct state
		restUserProgressMockMvc.perform(get("/api/user/progress/formeeting/{meetingId}", 1))
                .andExpect(status().isNoContent());
    }

	
}
