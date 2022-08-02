package com.exam.competitor.admin.common.entity.course;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "course_details")
public class CourseDetail  {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column( length = 255)
	private String name;
	
	@Column( length = 5)
	private String value;
	
	@ManyToOne
	@JoinColumn(name = "course_id")
	private Course course;

	public CourseDetail() {
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


	
	public Course getCourse() {
		return course;
	}


	public void setCourse(Course course) {
		this.course = course;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public CourseDetail(Integer id, String name, String value, Course course) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.course = course;
	}

	public CourseDetail(String name, String value, Course course) {
		super();
		this.name = name;
		this.value = value;
		this.course = course;
	}


	
}
