package com.exam.competitor.admin.common.entity.course;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.exam.competitor.admin.common.entity.Customer;
import com.exam.competitor.admin.common.entity.User;

@Entity
@Table(name = "attemptHistory")
public class AttemptHistory {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

	@Column(name = "status")
    private String status;
	
	@Column(name = "description")
    private String description;
	
	@Column(name = "answeredPdfName")
    private String answeredPdfName;
	
	@Column(name = "evaluatedPdfName")
    private String evaluatedPdfName;
	
	@ManyToOne 
	@JoinColumn(name = "course_id")
	private Course course;
	
	@ManyToOne 
	@JoinColumn(name = "question_id")
	private Question question;
	
	@ManyToOne 
	@JoinColumn(name = "moderator_id")
	private User user;
	
	@ManyToOne 
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	
	private boolean enabled;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAnsweredPdfName() {
		return answeredPdfName;
	}

	public void setAnsweredPdfName(String answeredPdfName) {
		this.answeredPdfName = answeredPdfName;
	}

	public String getEvaluatedPdfName() {
		return evaluatedPdfName;
	}

	public void setEvaluatedPdfName(String evaluatedPdfName) {
		this.evaluatedPdfName = evaluatedPdfName;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	
	public AttemptHistory(Integer id, String status, String description, String answeredPdfName,
			String evaluatedPdfName, Course course, Question question, User user, Customer customer, boolean enabled) {
		super();
		this.id = id;
		this.status = status;
		this.description = description;
		this.answeredPdfName = answeredPdfName;
		this.evaluatedPdfName = evaluatedPdfName;
		this.course = course;
		this.question = question;
		this.user = user;
		this.customer = customer;
		this.enabled = enabled;
	}

	public AttemptHistory() {
		super();
	}
	
	@Transient
	public String getAnsweredPdfPath() {
		
		if (id == null || answeredPdfName == null )  return "/images/thumbnail-default-pdf.png";
		
		return "/"+course.getId()+"/"+customer.getId()+"/"+question.getId()+"/"+ this.id +"/answeredpdf_"+this.id;
	}
	
	@Transient
	public String getEvaluatedPdfPath() {
		
		if (id == null || evaluatedPdfName == null )  return "/images/thumbnail-default-pdf.png";
		
		return "/"+course.getId()+"/"+customer.getId()+"/"+question.getId()+"/"+ this.id +"/evaluatedpdf_"+this.id;
	}

    
	
}
