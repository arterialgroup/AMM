package com.arterialgroup.arterialedu.web.rest.dto;

import java.util.List;

/**
 * Used for REST webservice requests to pass parameters encapsulated for calling
 * service layer of application Getters only as we don't want to amend the
 * values of the object once created, its for data transfer uni-directional
 * 
 * @author bradleyr
 * 
 */
public class UserResponseDTO {

	private Long userModuleId;

	private Long stepId;

	private List<AnswerDTO> answers;

	public UserResponseDTO() {
	}

	public UserResponseDTO(Long userModuleId, Long stepId,
			List<AnswerDTO> answers) {
		this.userModuleId = userModuleId;
		this.stepId = stepId;
		this.answers = answers;
	}

	public Long getUserModuleId() {
		return userModuleId;
	}

	public Long getStepId() {
		return stepId;
	}

	public List<AnswerDTO> getAnswers() {
		return answers;
	}

	@Override
	public String toString() {
		return "UserResponseDTO{" + "userModuleId='" + userModuleId + '\''
				+ ", stepId='" + stepId + '\'' + ", answers='" + answers + '\''
				+ '}';
	}
}
