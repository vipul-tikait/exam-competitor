package com.exam.competitor.admin.common.entity.exam;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "mainExamType")
public class MainExamType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128, nullable = false, unique = true)
	private String name;
	
	@OneToMany
	private Set<MainExam> mainExams = new HashSet<>();
	
	
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


	
	public MainExamType(String name) {
		this.name = name;
	}
	
	public MainExamType() {
	}


	public Set<MainExam> getMainExams() {
		return mainExams;
	}


	public void setMainExams(Set<MainExam> mainExams) {
		this.mainExams = mainExams;
	}


	public MainExamType(Integer id, String name, Set<MainExam> mainExams) {
		this.id = id;
		this.name = name;
		this.mainExams = mainExams;
	}

	
	
	
}
