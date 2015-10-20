package com.arterialgroup.arterialedu.web.rest.dto;

import java.util.List;

public class SectionDTO {

	private String name;
	private String description;
	private Integer series;
	private Long sectionTypeId;
	
	private List<StepDTO> steps;
	
	public SectionDTO(){
		
	}
	
	public SectionDTO(String name, String description, Integer series, Long sectionTypeId, List<StepDTO> steps){
		this.name = name;
		this.description = description;
		this.series = series;
		this.sectionTypeId = sectionTypeId;
		this.steps = steps;
	}
	
	public List<StepDTO> getSteps(){
		return this.steps;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Integer getSeries() {
		return series;
	}

	public Long getSectionTypeId() {
		return sectionTypeId;
	}
	
}
