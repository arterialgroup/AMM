package com.arterialgroup.arterialedu.web.rest.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.arterialgroup.arterialedu.Application;
import com.arterialgroup.arterialedu.domain.Attendee;
import com.arterialgroup.arterialedu.domain.Meeting;
import com.arterialgroup.arterialedu.domain.Module;
import com.arterialgroup.arterialedu.domain.Question;
import com.arterialgroup.arterialedu.domain.QuestionGroup;
import com.arterialgroup.arterialedu.domain.User;
import com.arterialgroup.arterialedu.domain.UserModule;
import com.arterialgroup.arterialedu.domain.UserProgress;
import com.arterialgroup.arterialedu.web.rest.dto.AnswerDTO;
import com.arterialgroup.arterialedu.web.rest.dto.AttendeeStatusDTO;
import com.arterialgroup.arterialedu.web.rest.dto.QuestionDTO;
import com.arterialgroup.arterialedu.web.rest.dto.StepQuestionDTO;
import com.arterialgroup.arterialedu.web.rest.dto.UserModuleStatusDTO;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Helper test unit to verify internal private helper class logic
 * so that integration testing can test end to end functionality
 * @author bradleyr
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DTOUtilTest {

	private QuestionGroup createNoneGroup(Question question){
		QuestionGroup none = new QuestionGroup();
		none.setId(((long)-1*question.getId()));
		none.setText(DTOConstants.QUESTION_GROUP_NONE);
    	return none;
	}
	
	@Test
	public void testMapQuestionsToStepQuestionDtos(){
		
		Question one = new Question();
		one.setText("one");
		one.setId((long)1);
		one.setSeries(1);
		one.setQuestionGroup(null);
		
		QuestionGroup group = new QuestionGroup();
		group.setId((long)1);
		group.setText("group");
		
		Question two = new Question();
		two.setText("two");
		two.setId((long)2);
		two.setSeries(2);
		two.setQuestionGroup(group);
		
		Question three = new Question();
		three.setText("three");
		three.setId((long)3);
		three.setSeries(2);
		three.setQuestionGroup(group);
		
		Question four = new Question();
		four.setText("four");
		four.setId((long)4);
		four.setSeries(3);
		four.setQuestionGroup(null);
		
		List<Question> questions = new ArrayList<Question>();
		questions.add(one);
		questions.add(two);
		questions.add(three);
		questions.add(four);
    	
        Map<QuestionGroup, List<Question>> byQuestionGroup
            = questions.stream()
                       .collect(Collectors.groupingBy((question) -> (question.getQuestionGroup() == null ? createNoneGroup(question) : question.getQuestionGroup()) ));
        
        assertThat(byQuestionGroup).isNotNull();
        assertThat(byQuestionGroup.size()).isGreaterThan(1);
        //two placeholder groups and one real group
        assertThat(byQuestionGroup.size()).isEqualTo(3);
        assertThat(byQuestionGroup.get(group).size()).isEqualTo(2);
        
        List<StepQuestionDTO> stepQuestions = new ArrayList<StepQuestionDTO>();
        
        byQuestionGroup.keySet().forEach((k) -> {

        	List<QuestionDTO> qs = new ArrayList<QuestionDTO>();
        	
        	byQuestionGroup.get(k).forEach((q) -> {

        		List<AnswerDTO> answers = new ArrayList<AnswerDTO>(q.getAnswers().size());
        		q.getAnswers().forEach((ans) -> {
        			answers.add(new AnswerDTO(q.getId(), ans.getId(), ans.getText(), ans.getCorrect()));
        		});
        		
        		qs.add(new QuestionDTO(q.getId(), q.getText(), null, "", q.getSeries(), answers));
        	});
        	
        	Boolean isGroup = (k.getId() > 0);
        	String groupText = (isGroup ? k.getText() : null);
        	Integer order = (qs.size() > 0 && qs.get(0).getSeries() != null ? qs.get(0).getSeries()  : -1);
        	
        	stepQuestions.add(new StepQuestionDTO(isGroup, groupText, order, qs));
        });
        
        assertThat(stepQuestions).isNotNull();
        //step questions should match groups.
        assertThat(stepQuestions.size()).isEqualTo(3);
		
	}
	
	@Test
	public void testMapUserProgressToUserModuleStatusDTOs()
	{
		Map<Attendee, List<UserProgress>> progress = new HashMap<Attendee, List<UserProgress>>();
		
		Meeting meeting = new Meeting();
		meeting.setName("TEST MEETING");
		
		Module module = new Module();
		module.setName("TEST MODULE");
		
		//in progress user
		User one = new User();
		one.setFirstName("ONE F");
		one.setLastName("ONE L");
		Attendee attendeeOne = new Attendee();
		attendeeOne.setId((long)1);
		attendeeOne.setUser(one);
		
		//complete user
		User two = new User();
		two.setFirstName("TWO F");
		two.setLastName("TWO L");
		Attendee attendeeTwo = new Attendee();
		attendeeTwo.setId((long)2);
		attendeeTwo.setUser(two);
		attendeeTwo.setAttended(true);
		attendeeTwo.setCertificatePath("CERT_PATH");
		
		UserModule umOne = new UserModule();
		umOne.setModule(module);
		umOne.setUser(one);
		
		UserProgress attendeeProgressOne = new UserProgress();
		attendeeProgressOne.setUserModule(umOne);
		attendeeProgressOne.setEndDate(null);
		
		List<UserProgress> progressOne = new ArrayList<UserProgress>();
		progressOne.add(attendeeProgressOne);
		
		UserModule umTwo = new UserModule();
		umTwo.setModule(module);
		umTwo.setUser(two);
		
		UserProgress attendeeProgressTwo = new UserProgress();
		attendeeProgressTwo.setUserModule(umTwo);
		attendeeProgressTwo.setEndDate(new LocalDate("2015-06-10"));
		
		List<UserProgress> progressTwo = new ArrayList<UserProgress>();
		progressTwo.add(attendeeProgressTwo);
		
		progress.put(attendeeOne, progressOne);
		progress.put(attendeeTwo, progressTwo);
		
		List<AttendeeStatusDTO> statuses = new ArrayList<AttendeeStatusDTO>(
				progress.keySet().size());
		
		progress.keySet().forEach((k) -> {
			List<UserModuleStatusDTO> umsList = new ArrayList<UserModuleStatusDTO>(
					progress.get(k).size());
			
			progress.get(k).forEach((v) -> {
				UserModuleStatusDTO ums = new UserModuleStatusDTO(v
						.getUserModule().getModule().getName(), (v
						.getEndDate() != null));
				umsList.add(ums);
			});
			
			StringBuilder name = new StringBuilder(k.getUser().getFirstName())
			.append(" ").append(k.getUser().getLastName());

			AttendeeStatusDTO aDto = new AttendeeStatusDTO(name.toString(),
					umsList, k.getAttended(), k.getPointsAwarded(), k
							.getCertificatePath());
			statuses.add(aDto);
		});
		
		assertThat(statuses).isNotNull();
		assertThat(statuses.size()).isEqualTo(2);
	}
	
}
