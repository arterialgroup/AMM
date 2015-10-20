package com.arterialgroup.arterialedu.web.rest.dto;

public class UserModuleStatusDTO {

	private String moduleName;
	private Boolean completed;

	public UserModuleStatusDTO(){
		
	}
	
	public UserModuleStatusDTO(String moduleName, Boolean completed){
		this.moduleName = moduleName;
		this.completed = completed;
	}

	public String getModuleName() {
		return moduleName;
	}
	
	public Boolean getCompleted() {
		return completed;
	}	
}
