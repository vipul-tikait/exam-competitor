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

import com.exam.competitor.admin.common.entity.Topic;

@Entity
@Table(name = "question")
public class Question implements Comparable<Question>{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

	@Column(name = "name")
    private String name;
	
	@Column(name = "description")
    private String description;
	
	@Column(name = "image")
    private String image;
	
	@Column(name = "questionPdfName")
    private String questionPdfName;
	
	private boolean enabled;
	
    @ManyToOne
	@JoinColumn(name = "topic_id")
	private Topic topic;

   
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

	
	public String getQuestionPdfName() {
		return questionPdfName;
	}

	public void setQuestionPdfName(String questionPdfName) {
		this.questionPdfName = questionPdfName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	

	public Question(String name, String description, String image, String questionPdfName, boolean enabled, Topic topic) {
		this.name = name;
		this.description = description;
		this.image = image;
		this.questionPdfName = questionPdfName;
		this.enabled = enabled;
		this.topic = topic;
		
	}

	public Question() {
		super();
	}

	@Transient
	public String getQuestionPdfPath() {
		
		if (id == null || questionPdfName == null )  return "/images/thumbnail-default.png";
		
		return "/questions-pdf/"+ this.id +"/pdf_"+this.id;
	}

	@Override
	public int compareTo(Question o) {
		// TODO Auto-generated method stub
		return this.id.compareTo(o.id);
	}
	
}
