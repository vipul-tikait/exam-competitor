package com.exam.competitor.admin.common.entity.exam;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.exam.competitor.admin.common.entity.Topic;

@Entity
@Table(name = "examLevel")
public class ExamLevel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128, nullable = false, unique = true)
	private String name;
		
	
	@ManyToOne
	@JoinColumn(name = "exam_id")
	private Exam exam;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
	name = "exams_level_topics",
	joinColumns = @JoinColumn(name = "exam_level_id"),
	inverseJoinColumns = @JoinColumn(name="topic_id")
	)
	private Set<Topic> topics = new HashSet<>();
	

	public Exam getExam() {
		return exam;
	}

	public void setMainExam(Exam exam) {
		this.exam = exam;
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

	public ExamLevel() {
		// TODO Auto-generated constructor stub
	}

	
	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public Set<Topic> getTopics() {
		return topics;
	}

	public void setTopics(Set<Topic> topics) {
		this.topics = topics;
	}

	
}
