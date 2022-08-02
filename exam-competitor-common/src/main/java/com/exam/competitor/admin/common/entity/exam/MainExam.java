package com.exam.competitor.admin.common.entity.exam;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "mainExam")
public class MainExam {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128, nullable = false, unique = true)
	private String name;
	
	@OneToMany
	private Set<Exam> exams = new HashSet<>();
	
	@ManyToOne
	@JoinColumn(name = "main_exam_type_id")
	private MainExamType mainExamType;

	

	public MainExamType getMainExamType() {
		return mainExamType;
	}


	public void setMainExamType(MainExamType mainExamType) {
		this.mainExamType = mainExamType;
	}


	public Set<Exam> getExams() {
		return exams;
	}


	public void setExams(Set<Exam> exams) {
		this.exams = exams;
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


	
	public MainExam(String name) {
		this.name = name;
	}
	
	public MainExam() {
	}


	public MainExam(Integer id, String name, Set<Exam> exams) {
		this.id = id;
		this.name = name;
		this.exams = exams;
	}


	

	
	
}
