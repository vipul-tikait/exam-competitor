
package com.exam.competitor.admin.common.entity.course;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.exam.competitor.admin.common.entity.exam.ExamLevel;

@Entity
@Table(name = "questionSet")
public class QuestionSet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name",nullable = false, unique = true)
	private String name;

	@Column(name = "prefix")
	private String prefix;

	@Column(name = "description")
	private String description;

	@Column(name = "image")
	private String image;

	@Column(name = "pdfName")
	private String pdfName;

	private boolean enabled;

	
	@ManyToOne
	@JoinColumn(name = "exam_level_id")
	private ExamLevel examLevel;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	name="question_set_questions",
	joinColumns = @JoinColumn(name = "que_set_id"),
	inverseJoinColumns = @JoinColumn(name= "que_id")
	)
	@OrderBy("id")
	private List<Question> questions = new ArrayList();
	
	
	public QuestionSet() {

	}
	
	public ExamLevel getExamLevel() {
		return examLevel;
	}

	public void setExamLevel(ExamLevel examLevel) {
		this.examLevel = examLevel;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPdfName() {
		return pdfName;
	}

	public void setPdfName(String pdfName) {
		this.pdfName = pdfName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	
	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	
	
	

}
