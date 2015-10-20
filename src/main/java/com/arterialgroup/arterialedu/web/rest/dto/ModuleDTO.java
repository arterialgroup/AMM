package com.arterialgroup.arterialedu.web.rest.dto;

import java.util.List;

public class ModuleDTO {

	private String name;
	private String description;
	private Long moduleTypeId;
	
	private List<SectionDTO> sections;
	
	public ModuleDTO(){
		
	}
	
	public ModuleDTO(String name, String description, Long moduleTypeId, List<SectionDTO> sections){
		this.name = name;
		this.description = description;
		this.moduleTypeId = moduleTypeId;
		this.sections = sections;
	}
	
	public List<SectionDTO> getSections(){
		return sections;
	}

	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Long getModuleTypeId() {
		return moduleTypeId;
	}
}
