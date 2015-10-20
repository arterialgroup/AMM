package com.arterialgroup.arterialedu.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

@Entity
@Table(name = "USERPROGRESS")
public class UserProgress implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	//to assist Spring JPA data queries
	@JoinColumn(name="user_module_id")
	private UserModule userModule;

	//We could store the section as well
	//but again as with the UserResponse object we can get the current section from the
	//step via relationship (step.getSection().getId())
	//this limits relationship queries, table size and object references
	@ManyToOne
	private Step step;

	@Column(name = "start_date")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate startDate;

	@Column(name = "end_date")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate endDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserModule getUserModule() {
		return userModule;
	}

	public void setUserModule(UserModule userModule) {
		this.userModule = userModule;
	}

	public Step getStep() {
		return step;
	}

	public void setStep(Step step) {
		this.step = step;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		UserProgress userProgress = (UserProgress) o;

		if (!Objects.equals(id, userProgress.id))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "UserProgress{" + "id=" + id + '}';
	}
}
