package com.arterialgroup.arterialedu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Question.
 */
@Entity
@Table(name = "QUESTION")
public class Question implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "series")
    private Integer series;

    @Column(name = "showResults")
    private Boolean showResults = false;

    @ManyToOne
    private Question_type question_type;
    
    @JoinColumn(name = "question_group_id")
    @ManyToOne
    private QuestionGroup questionGroup;

	@ManyToOne
    private Step step;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Answer> answers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getSeries() {
        return series;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }

    public Boolean getShowResults() {
        return showResults;
    }

    public void setShowResults(Boolean showResults) {
        this.showResults = showResults;
    }

    public Question_type getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(Question_type question_type) {
        this.question_type = question_type;
    }

    public QuestionGroup getQuestionGroup() {
		return questionGroup;
	}

	public void setQuestionGroup(QuestionGroup questionGroup) {
		this.questionGroup = questionGroup;
	}
    
    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Question question = (Question) o;

        if ( ! Objects.equals(id, question.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text='" + text + "'" +
                ", series='" + series + "'" +
                ", showResults='" + showResults + "'" +
                '}';
    }
}
