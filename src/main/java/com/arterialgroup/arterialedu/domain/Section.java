package com.arterialgroup.arterialedu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Section.
 */
@Entity
@Table(name = "SECTION")
public class Section implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "series")
    private Integer series;

    @ManyToOne
    private Module module;

    @ManyToOne
    private Section_type section_type;

    @OneToMany(mappedBy = "section", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Step> steps = new HashSet<>();
    
    @Transient
    private Integer stepCount;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSeries() {
        return series;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Section_type getSection_type() {
        return section_type;
    }

    public void setSection_type(Section_type section_type) {
        this.section_type = section_type;
    }

    public Set<Step> getSteps() {
        return steps;
    }

    public void setSteps(Set<Step> steps) {
        this.steps = steps;
    }
    
    @JsonProperty
    public Integer getStepCount() {
		return (this.getSteps() != null ? this.getSteps().size() : 0);
	}

	public void setStepCount(Integer stepCount) {
		this.stepCount = stepCount;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Section section = (Section) o;

        if ( ! Objects.equals(id, section.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", description='" + description + "'" +
                ", series='" + series + "'" +
                '}';
    }
}
