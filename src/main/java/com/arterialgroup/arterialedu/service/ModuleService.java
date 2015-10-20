package com.arterialgroup.arterialedu.service;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.arterialgroup.arterialedu.domain.Answer;
import com.arterialgroup.arterialedu.domain.Module;
import com.arterialgroup.arterialedu.domain.Question;
import com.arterialgroup.arterialedu.domain.QuestionGroup;
import com.arterialgroup.arterialedu.domain.Section;
import com.arterialgroup.arterialedu.domain.Step;
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
import com.arterialgroup.arterialedu.web.rest.dto.SectionDTO;
import com.arterialgroup.arterialedu.web.rest.dto.StepDTO;
import com.arterialgroup.arterialedu.web.rest.dto.StepQuestionDTO;

@Service
public class ModuleService {

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

	/**
	 * We handle a lot of database interaction here, so wrap call in a
	 * transaction to ensure the cascading nature of child relationships is ok
	 * 
	 * @param module
	 * @return
	 */
	@Transactional
	public Boolean generateModule(ModuleDTO module) {

		// TODO here we save each entity via its own repo...
		// if we turn on cascading to the object collections (for SAVE ONLY)
		// we could save the high level parents and in return save the others

		// the DTO objects are for the rest service layer to transfer data to
		// and from the client

		// we only use the actual serializable entities if the service is
		// PUT/GET based as we
		// are changing state of that
		// for this services we have a collection of a range of entities so this
		// service uses a transfer
		// object and a POST to save multiple types of data.
		// by default the objects posted back dont generate the full data model
		// in JSON format
		// this lightweight DTO serves that purpose

		Module newModule = new Module();
		newModule.setName(module.getName());
		newModule.setDescription(module.getDescription());
		newModule.setModuleType(moduleTypeRepository.findOne(module
				.getModuleTypeId()));

		newModule = moduleRepository.saveAndFlush(newModule);

		saveSections(newModule, module.getSections());

		return (newModule != null && newModule.getId() != null);
	}

	private void saveSections(Module module, List<SectionDTO> sections) {

		if (sections != null) {
			sections.forEach((section) -> {
				Section newSection = new Section();
				newSection.setModule(module);
				newSection.setName(section.getName());
				newSection.setDescription(section.getDescription());
				newSection.setSection_type(section_typeRepository
						.findOne(section.getSectionTypeId()));
				newSection = sectionRepository.saveAndFlush(newSection);

				saveSteps(newSection, section.getSteps());
			});
		}

	}

	private void saveSteps(Section section, List<StepDTO> steps) {

		if (steps != null) {
			steps.forEach((step) -> {
				Step newStep = new Step();
				newStep.setSection(section);
				newStep.setName(step.getName());
				newStep.setDescription(step.getDescription());
				newStep.setContent(step.getContent());
				newStep = stepRepository.saveAndFlush(newStep);

				saveQuestions(newStep, step.getQuestions());
			});
		}
	}

	private void saveQuestions(Step step, List<StepQuestionDTO> stepQuestions) {

		if (stepQuestions != null) {
			stepQuestions.forEach((q) -> {
				q.getQuestions().forEach((qs) -> {
					QuestionGroup group = null;

					if (q.getIsGroup()) {
						// create a question group instance
						// and use for the questions that follow
						group = new QuestionGroup();
						group.setText(q.getGroupText());
						group = questionGroupRepository.saveAndFlush(group);
					}

					Question question = new Question();
					question.setQuestionGroup(group);
					question.setText(qs.getQuestionText());
					question.setQuestion_type(question_typeRepository
							.findOne(qs.getQuestionTypeId()));

					question = questionRepository.saveAndFlush(question);

					saveAnswers(question, qs.getAnswers());
				});
			});
		}

	}

	private void saveAnswers(Question question, List<AnswerDTO> answers) {

		if (answers != null) {
			answers.forEach((answer) -> {
				Answer newAnswer = new Answer();
				newAnswer.setQuestion(question);
				newAnswer.setText(answer.getAnswerText());
				newAnswer.setCorrect(answer.getCorrect());

				newAnswer = answerRepository.saveAndFlush(newAnswer);
			});
		}
	}

}
