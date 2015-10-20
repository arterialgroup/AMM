package com.arterialgroup.arterialedu.web.rest.dto;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer;

/**
 * Used for REST webservice requests to pass parameters encapsulated for calling
 * service layer of application Getters only as we don't want to amend the
 * values of the object once created, its for data transfer uni-directional
 * 
 * @author bradleyr
 * 
 */
public class UserProgressDTO {

	private Long userProgressId;
	
	private Long userModuleId;

	private Long stepId;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate startDate;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate endDate;

	public UserProgressDTO() {

	}

	public UserProgressDTO(Long userProgressId, Long userModuleId, Long stepId, LocalDate startDate,
			LocalDate endDate) {
		this.userProgressId = userProgressId;
		this.userModuleId = userModuleId;
		this.stepId = stepId;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public Long getUserProgressId() {
		return userProgressId;
	}
	
	public Long getUserModuleId() {
		return userModuleId;
	}

	public Long getStepId() {
		return stepId;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	@Override
	public String toString() {
		return "UserProgressDTO{" + "userProgressId='" + userProgressId + '\'' + ", userModuleId='" + userModuleId + '\''
				+ ", stepId='" + stepId + '\'' + ", startDate='" + startDate
				+ '\'' + ", endDate='" + endDate + '\'' + '}';
	}
}
