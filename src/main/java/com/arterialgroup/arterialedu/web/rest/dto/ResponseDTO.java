package com.arterialgroup.arterialedu.web.rest.dto;

public class ResponseDTO {

	private String message;

	public ResponseDTO() {

	}

	public ResponseDTO(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public String toString() {
		return "{" + this.message + "}";
	}

}
