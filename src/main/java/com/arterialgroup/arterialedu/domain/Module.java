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
 * A Module.
 */
@Entity
@Table(name = "MODULE")
public class Module implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description")
	private String description;

	@OneToMany(mappedBy = "module", fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<Section> sections = new HashSet<>();

	@JoinColumn(name = "module_type_id")
	@ManyToOne
	private ModuleType moduleType;

	@Transient
	private Integer sectionCount;

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

	public Set<Section> getSections() {
		return sections;
	}

	public void setSections(Set<Section> sections) {
		this.sections = sections;
	}

	@JsonProperty
	public Integer getSectionCount() {
		return (this.getSections() != null ? this.getSections().size() : 0);
	}

	public void setSectionCount(Integer sectionCount) {
		this.sectionCount = sectionCount;
	}

	public ModuleType getModuleType() {
		return moduleType;
	}

	public void setModuleType(ModuleType moduleType) {
		this.moduleType = moduleType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Module module = (Module) o;

		if (!Objects.equals(id, module.id))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Module{" + "id=" + id + ", name='" + name + "'"
				+ ", description='" + description + "'" + '}';
	}
}
