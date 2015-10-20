package com.arterialgroup.arterialedu.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.arterialgroup.arterialedu.domain.util.CustomLocalDateSerializer;
import com.arterialgroup.arterialedu.domain.util.ISO8601LocalDateDeserializer;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Meeting.
 */
@Entity
@Table(name = "MEETING")
public class Meeting implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(name = "name", nullable = false)
	private String name;

	@NotNull
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = ISO8601LocalDateDeserializer.class)
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "days")
	private Integer days;

	@Column(name = "activity_id")
	private Long activityId;

	@JoinColumn(name = "meetingtype_id")
	@ManyToOne
	private MeetingType meetingType;

	@Column(name = "resource_path")
	private String resourcePath;

	@Column(name = "css_file")
	private String cssFile;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public MeetingType getMeetingType() {
		return meetingType;
	}

	public void setMeetingType(MeetingType meetingType) {
		this.meetingType = meetingType;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getCssFile() {
		return cssFile;
	}

	public void setCssFile(String cssFile) {
		this.cssFile = cssFile;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Meeting meeting = (Meeting) o;

		if (!Objects.equals(id, meeting.id))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Meeting{" + "id=" + id + ", name='" + name + "'"
				+ ", startDate='" + startDate + "'" + ", days='" + days + "'"
				+ ", activityId='" + activityId + "'" + '}';
	}
}
