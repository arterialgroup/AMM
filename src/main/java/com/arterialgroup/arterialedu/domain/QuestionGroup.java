package com.arterialgroup.arterialedu.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A QuestionGroup.
 */
@Entity
@Table(name = "QUESTIONGROUP")
public class QuestionGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "text", nullable = false)
    private String text;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuestionGroup questionGroup = (QuestionGroup) o;

        if ( ! Objects.equals(id, questionGroup.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "QuestionGroup{" +
                "id=" + id +
                ", text='" + text + "'" +
                '}';
    }
}
