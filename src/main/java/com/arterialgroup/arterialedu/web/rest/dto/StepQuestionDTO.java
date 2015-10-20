package com.arterialgroup.arterialedu.web.rest.dto;

import java.util.List;

/**
 * This is a container to house questions for a step
 * Not all questions in the system will have a question group so
 * indicate that here
 * If the step just have one question, this becomes a lighweight object...
 * @author bradleyr
 *
 */
public class StepQuestionDTO {

	private Boolean isGroup;
	private String groupText;
	private Integer order;
	private List<QuestionDTO> questions;
	
	public StepQuestionDTO(){
		
	}
	
	public StepQuestionDTO(Boolean isGroup, String groupText, Integer order, List<QuestionDTO> questions){
		this.isGroup = isGroup;
		this.groupText = groupText;
		this.order = order;
		this.questions = questions;
	}

	public List<QuestionDTO> getQuestions() {
		return questions;
	}

	public Boolean getIsGroup() {
		return isGroup;
	}

	public String getGroupText() {
		return groupText;
	}

	public Integer getOrder() {
		return order;
	}
}
