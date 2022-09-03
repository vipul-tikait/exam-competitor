
package com.exam.competitor.admin.common.entity.course;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.exam.competitor.admin.common.entity.Customer;
import com.exam.competitor.admin.common.entity.exam.ExamLevel;

@Entity

@Table(name = "course")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description")
	private String description;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "start_date", nullable = false)
	private Date startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "end_date", nullable = false)
	private Date endDate;

	@Column(name = "num_of_test")
	private Integer numberOfTest;

	private boolean enabled;
	
	private Integer price;
	
	@ManyToOne 
	@JoinColumn(name = "exam_level_id")
	private ExamLevel examLevel;
	
	@ManyToOne 
	@JoinColumn(name = "question_set_id")
	private QuestionSet questionSet;

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CourseDetail> details = new ArrayList<>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
	name = "courses_customers",
	joinColumns = @JoinColumn(name = "course_id"),
	inverseJoinColumns = @JoinColumn(name="customer_id")
	)
	private Set<Customer> customers = new HashSet<>();

	public Course() {

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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getNumberOfTest() {
		return numberOfTest;
	}

	public void setNumberOfTest(Integer numberOfTest) {
		this.numberOfTest = numberOfTest;
	}



	public ExamLevel getExamLevel() {
		return examLevel;
	}

	public void setExamLevel(ExamLevel examLevel) {
		this.examLevel = examLevel;
	}

	public QuestionSet getQuestionSet() {
		return questionSet;
	}

	public void setQuestionSet(QuestionSet questionSet) {
		this.questionSet = questionSet;
	}

	public List<CourseDetail> getDetails() {
		return details;
	}

	public void setDetails(List<CourseDetail> details) {
		this.details = details;
	}
	
	public void addDetail(String name, String value) {
		this.details.add(new CourseDetail(name, value,this));
	}
	
	public void addDetail(Integer id, String name, String value) {
		this.details.add(new CourseDetail(id,name, value,this));
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Set<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}

	
	
	
}
