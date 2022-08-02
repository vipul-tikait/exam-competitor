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
@Table(name = "exam")
public class Exam {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128, nullable = false, unique = true)
	private String name;
		
	@OneToMany
	private Set<ExamLevel> examLevels = new HashSet<>();
	
	@ManyToOne
	@JoinColumn(name = "main_exam_id")
	private MainExam mainExam;

	public MainExam getMainExam() {
		return mainExam;
	}

	public void setMainExam(MainExam mainExam) {
		this.mainExam = mainExam;
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

	public Set<ExamLevel> getExamLevels() {
		return examLevels;
	}

	public void setExamLevels(Set<ExamLevel> examLevels) {
		this.examLevels = examLevels;
	}

	
	public Exam() {
	}

	public Exam(Integer id, String name, Set<ExamLevel> examLevels) {
		this.id = id;
		this.name = name;
		this.examLevels = examLevels;
	}

	
}
