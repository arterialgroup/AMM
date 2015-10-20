package com.arterialgroup.arterialedu.web.rest.dto;

import java.util.List;

/**
 * DTO interfaces should only have GETTERS The concept of this class is to
 * define a data object that will provide information for a variety of client
 * interfaces, that being: 1) HCP interface to view which modules they are
 * signed up too and completed, and if their certificate is available Only one
 * record will be returned for a HCP 2) Rep interface to view which modules
 * their attendees have completed and access to their certificates (optionally)
 * Multiple will be returned based on a meeting id (and in the future possibly a
 * rep id filter)
 * 
 * @author bradleyr
 *
 */
public class AttendeeStatusDTO {

	private String attendeeFullName;
	private List<UserModuleStatusDTO> moduleStatuses;
	private Boolean attended;
	private Boolean pointsAwarded;
	private String certificatePath;

	public AttendeeStatusDTO() {

	}

	public AttendeeStatusDTO(String attendeeFullName,
			List<UserModuleStatusDTO> moduleStatuses, Boolean attended,
			Boolean pointsAwarded, String certificatePath) {
		this.attendeeFullName = attendeeFullName;
		this.moduleStatuses = moduleStatuses;
		this.attended = attended;
		this.pointsAwarded = pointsAwarded;
		this.certificatePath = certificatePath;
	}

	public String getAttendeeFullName() {
		return attendeeFullName;
	}

	public List<UserModuleStatusDTO> getModuleStatuses() {
		return moduleStatuses;
	}

	public Boolean getAttended() {
		return attended;
	}

	public Boolean getPointsAwarded() {
		return pointsAwarded;
	}

	public String getCertificatePath() {
		return certificatePath;
	}

	public Boolean getCertificateExists() {
		return (certificatePath != null && !certificatePath.isEmpty());
	}
}
