package com.arterialgroup.arterialedu.web.rest.dto;

import java.io.Serializable;
import java.util.List;

public class QuestionDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long questionId;
	private String questionText;
	private Long questionTypeId;
	private String questionType;
	private Integer series;
	private List<AnswerDTO> answers;
	
	
	public QuestionDTO(){
		
	}
	
	public QuestionDTO(Long questionId, String questionText, Long questionTypeId, String questionType, Integer series, List<AnswerDTO> answers){
		this.questionId = questionId;
		this.questionText = questionText;
		this.questionTypeId = questionTypeId;
		this.questionType = questionType;
		this.series = series;
		this.answers = answers;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public String getQuestionText() {
		return questionText;
	}

	public Long getQuestionTypeId() {
		return questionTypeId;
	}
	
	public String getQuestionType() {
		return questionType;
	}

	public Integer getSeries() {
		return series;
	}
	
	public List<AnswerDTO> getAnswers() {
		return answers;
	}
}
