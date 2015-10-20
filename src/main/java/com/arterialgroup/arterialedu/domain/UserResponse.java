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

@Entity
@Table(name = "USERRESPONSE")
public class UserResponse implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	//to assist Spring JPA data queries
	@JoinColumn(name="user_module_id")
	private UserModule userModule;

	@ManyToOne
	private Step step;
	
	@ManyToOne
	private Question question;

	@ManyToOne
	private Answer answer;
	
	@Column(name = "answer_text")
	private String answerText;
	
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

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}
	
	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getAnswerText() {
		return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		UserResponse userResponse = (UserResponse) o;

		if (!Objects.equals(id, userResponse.id))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "UserResponse{" + "id=" + id + '}';
	}
}
