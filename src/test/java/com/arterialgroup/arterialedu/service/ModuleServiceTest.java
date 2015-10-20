package com.arterialgroup.arterialedu.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.ModuleType;
import com.arterialgroup.arterialedu.domain.Question_type;
import com.arterialgroup.arterialedu.domain.Section_type;
import com.arterialgroup.arterialedu.repository.AnswerRepository;
import com.arterialgroup.arterialedu.repository.ModuleRepository;
import com.arterialgroup.arterialedu.repository.ModuleTypeRepository;
import com.arterialgroup.arterialedu.repository.QuestionGroupRepository;
import com.arterialgroup.arterialedu.repository.QuestionRepository;
import com.arterialgroup.arterialedu.repository.Question_typeRepository;
import com.arterialgroup.arterialedu.repository.SectionRepository;
import com.arterialgroup.arterialedu.repository.Section_typeRepository;
import com.arterialgroup.arterialedu.repository.StepRepository;
import com.arterialgroup.arterialedu.web.rest.dto.AnswerDTO;
import com.arterialgroup.arterialedu.web.rest.dto.ModuleDTO;
import com.arterialgroup.arterialedu.web.rest.dto.QuestionDTO;
import com.arterialgroup.arterialedu.web.rest.dto.SectionDTO;
import com.arterialgroup.arterialedu.web.rest.dto.StepDTO;
import com.arterialgroup.arterialedu.web.rest.dto.StepQuestionDTO;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class ModuleServiceTest {

	@Inject
	private ModuleService moduleService;

	@Inject
	private ModuleRepository moduleRepository;

	@Inject
	private ModuleTypeRepository moduleTypeRepository;

	@Inject
	private SectionRepository sectionRepository;

	@Inject
	private Section_typeRepository section_typeRepository;

	@Inject
	private StepRepository stepRepository;

	@Inject
	private QuestionGroupRepository questionGroupRepository;

	@Inject
	private QuestionRepository questionRepository;

	@Inject
	private Question_typeRepository question_typeRepository;

	@Inject
	private AnswerRepository answerRepository;

	private ModuleType mt;
	private Section_type st;
	private Question_type qt;
	
	@Before
	public void setup() {
		// setup the type tables...
		mt = new ModuleType();
		mt.setName("MODTYPE");
		mt = moduleTypeRepository.saveAndFlush(mt);

		st = new Section_type();
		st.setName("SECTYPE");
		st = section_typeRepository.saveAndFlush(st);

		qt = new Question_type();
		qt.setName("QUETYPE");
		qt = question_typeRepository.saveAndFlush(qt);
	}

	@Test
	public void testGenerateModule() {

		AnswerDTO answerTrue = new AnswerDTO(null, null, "one", true);
		AnswerDTO answerFalse = new AnswerDTO(null, null, "two", false);
		
		List<AnswerDTO> answers = new ArrayList<AnswerDTO>(2);
		answers.add(answerTrue);
		answers.add(answerFalse);
		
		QuestionDTO question = new QuestionDTO(null, "question", qt.getId(), qt.getName(), 1, answers);
		
		List<QuestionDTO> questions = new ArrayList<QuestionDTO>(1);
		questions.add(question);
		
		StepQuestionDTO stepQuestion = new StepQuestionDTO(true, "group", 1, questions);
		
		List<StepQuestionDTO> stepQuestions = new ArrayList<StepQuestionDTO>(1);
		stepQuestions.add(stepQuestion);
		
		StepDTO step = new StepDTO("step", "step", "content", 1, stepQuestions);
		
		List<StepDTO> steps = new ArrayList<StepDTO>(1);
		steps.add(step);
		
		SectionDTO section = new SectionDTO("section", "section", 1, st.getId(), steps);
		
		List<SectionDTO> sections = new ArrayList<SectionDTO>(1);
		sections.add(section);
		
		ModuleDTO module = new ModuleDTO("module", "module", mt.getId(), sections);

		assertThat(moduleService.generateModule(module)).isNotNull().isTrue();
		assertThat(moduleRepository.findAll().size()).isEqualTo(1);
		assertThat(moduleRepository.findAll().get(0).getName()).isEqualTo(module.getName());
		
		assertThat(sectionRepository.findAll().size()).isEqualTo(1);
		assertThat(sectionRepository.findAll().get(0).getName()).isEqualTo(section.getName());
		
		assertThat(stepRepository.findAll().size()).isEqualTo(1);
		assertThat(stepRepository.findAll().get(0).getName()).isEqualTo(step.getName());
		
		assertThat(questionGroupRepository.findAll().size()).isEqualTo(1);
		assertThat(questionGroupRepository.findAll().get(0).getText()).isEqualTo("group");
		
		assertThat(questionRepository.findAll().size()).isEqualTo(1);
		assertThat(questionRepository.findAll().get(0).getText()).isEqualTo(question.getQuestionText());
		
		assertThat(answerRepository.findAll().size()).isEqualTo(2);
	}

	
	@Test
	public void testGenerateModuleNoQuestions() {
		
		StepDTO step = new StepDTO("step", "step", "content", 1, null);
		
		List<StepDTO> steps = new ArrayList<StepDTO>(1);
		steps.add(step);
		
		SectionDTO section = new SectionDTO("section", "section", 1, st.getId(), steps);
		
		List<SectionDTO> sections = new ArrayList<SectionDTO>(1);
		sections.add(section);
		
		ModuleDTO module = new ModuleDTO("module", "module", mt.getId(), sections);

		assertThat(moduleService.generateModule(module)).isNotNull().isTrue();
		assertThat(moduleRepository.findAll().size()).isEqualTo(1);
		assertThat(moduleRepository.findAll().get(0).getName()).isEqualTo(module.getName());
		
		assertThat(sectionRepository.findAll().size()).isEqualTo(1);
		assertThat(sectionRepository.findAll().get(0).getName()).isEqualTo(section.getName());
		
		assertThat(stepRepository.findAll().size()).isEqualTo(1);
		assertThat(stepRepository.findAll().get(0).getName()).isEqualTo(step.getName());
		
		assertThat(questionRepository.findAll().size()).isEqualTo(0);
		assertThat(questionGroupRepository.findAll().size()).isEqualTo(0);
		assertThat(answerRepository.findAll().size()).isEqualTo(0);
	}

	@Test
	public void testGenerateModuleNoQuestionGroup() {

		AnswerDTO answerTrue = new AnswerDTO(null, null, "one", true);
		AnswerDTO answerFalse = new AnswerDTO(null, null, "two", false);
		
		List<AnswerDTO> answers = new ArrayList<AnswerDTO>(2);
		answers.add(answerTrue);
		answers.add(answerFalse);
		
		QuestionDTO question = new QuestionDTO(null, "question", qt.getId(), qt.getName(), 1, answers);
		
		List<QuestionDTO> questions = new ArrayList<QuestionDTO>(1);
		questions.add(question);
		
		StepQuestionDTO stepQuestion = new StepQuestionDTO(false, null, 1, questions);
		
		List<StepQuestionDTO> stepQuestions = new ArrayList<StepQuestionDTO>(1);
		stepQuestions.add(stepQuestion);
		
		StepDTO step = new StepDTO("step", "step", "content", 1, stepQuestions);
		
		List<StepDTO> steps = new ArrayList<StepDTO>(1);
		steps.add(step);
		
		SectionDTO section = new SectionDTO("section", "section", 1, st.getId(), steps);
		
		List<SectionDTO> sections = new ArrayList<SectionDTO>(1);
		sections.add(section);
		
		ModuleDTO module = new ModuleDTO("module", "module", mt.getId(), sections);

		assertThat(moduleService.generateModule(module)).isNotNull().isTrue();
		assertThat(moduleRepository.findAll().size()).isEqualTo(1);
		assertThat(moduleRepository.findAll().get(0).getName()).isEqualTo(module.getName());
		
		assertThat(sectionRepository.findAll().size()).isEqualTo(1);
		assertThat(sectionRepository.findAll().get(0).getName()).isEqualTo(section.getName());
		
		assertThat(stepRepository.findAll().size()).isEqualTo(1);
		assertThat(stepRepository.findAll().get(0).getName()).isEqualTo(step.getName());
		
		assertThat(questionRepository.findAll().size()).isEqualTo(1);
		assertThat(questionGroupRepository.findAll().size()).isEqualTo(0);
		assertThat(questionRepository.findAll().get(0).getText()).isEqualTo(question.getQuestionText());
		
		assertThat(answerRepository.findAll().size()).isEqualTo(2);
	}
}
