package com.arterialgroup.arterialedu.web.rest.dto;

import java.util.List;

public class StepDTO {

	private String name;
	private String description;
	private String content;
	private Integer series;
	
	private List<StepQuestionDTO> questions;
	
	public StepDTO(){
		
	}
	
	public StepDTO(String name, String description, String content, Integer series, List<StepQuestionDTO> questions){
		this.name = name;
		this.description = description;
		this.content = content;
		this.series = series;
		this.questions = questions;
	}
	
	public List<StepQuestionDTO> getQuestions(){
		return this.questions;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getContent() {
		return content;
	}

	public Integer getSeries() {
		return series;
	}
	
}
