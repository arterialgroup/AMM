package com.arterialgroup.arterialedu.web.rest.dto;

/**
 * Used for REST webservice requests to pass parameters encapsulated for calling
 * service layer of application Getters only as we don't want to amend the
 * values of the object once created, its for data transfer uni-directional
 * 
 * @author bradleyr
 * 
 */
public class AnswerDTO {

	private Long questionId;
	private Long answerId;
	private String answerText;
	private Boolean correct;

	public AnswerDTO() {

	}

	public AnswerDTO(Long questionId, Long answerId, String answerText, Boolean correct) {
		this.questionId = questionId;
		this.answerId = answerId;
		this.answerText = answerText;
		this.correct = correct;
	}

	public Long getQuestionId() {
		return questionId;
	}
	
	public Long getAnswerId() {
		return answerId;
	}

	public String getAnswerText() {
		return answerText;
	}
	
	public Boolean getCorrect() {
		return correct;
	}

	@Override
	public String toString() {
		return "AnswerDTO{" + "answerId='" + answerId + '\'' + ", answerText='"
				+ answerText + '\'' + ", correct='" + correct + "'}";
	}
}
