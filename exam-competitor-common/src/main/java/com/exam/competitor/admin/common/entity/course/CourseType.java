package com.exam.competitor.admin.common.entity.course;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "course_type")
public class CourseType {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_type_id")
    private Integer id;

	@Column(name = "name")
    private String name;
	
	@Column(name = "description")
    private String description;
	
	
	public CourseType(Integer id) {
		this.id= id;
	}
	public CourseType(String name) {
		this.name = name;
	}

	public CourseType() {
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
	
	public CourseType(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	
	
}
