package com.exam.competitor.admin.common.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.exam.competitor.admin.common.entity.exam.ExamLevel;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
  
@Entity
@Table(name = "topics")
public class Topic {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128,nullable = false,unique = true)
	private String name;
	@Column(length = 64,nullable = false,unique = true)
	private String alias;
	@Column(length = 128,nullable = false)
	private String image;
	private boolean enabled;
	
	@Column(name = "all_parent_ids", length = 256, nullable = true)
	private String allParentIDs;
	
	@OneToOne
	@JoinColumn(name = "parent_id")
	@JsonBackReference
	private Topic parent;
	
	@OneToMany(mappedBy = "parent")
	@JsonManagedReference
	private Set<Topic> children = new HashSet<>();
	
	public static Topic copyIdandName(Topic topic) {
		Topic copyTopic = new Topic();
		copyTopic.setId(topic.getId());
		copyTopic.setName(topic.getName());
		return copyTopic;
	}
	
	public static Topic copyIdandName(Integer id, String name) {
		Topic copyTopic = new Topic();
		copyTopic.setId(id);
		copyTopic.setName(name);
		return copyTopic;
	}
	
	public static Topic copyFull(Topic topic) {
		Topic copyTopic = new Topic();
		copyTopic.setId(topic.getId());
		copyTopic.setName(topic.getName());
		copyTopic.setImage(topic.getImage());
		copyTopic.setAlias(topic.getAlias());
		copyTopic.setEnabled(topic.isEnabled());
		copyTopic.setHasChildren(topic.getChildren().size() > 0);
		return copyTopic;
	
	}
	
	public static Topic copyFull(Topic topic, String name) {
		Topic copyTopic = Topic.copyFull(topic);
		copyTopic.setName(name);
		return copyTopic;
	}
	
	public Topic() {
	}

	public Topic(Integer id) {
		this.id = id;
	}

	public Topic(String name) {
		this.name = name;
		this.alias = name;
		this.image = "default.png";
	}

	public Topic(String name, Topic parent) {
		this(name);
		this.parent = parent;
	}
	
	public Topic(Integer id, String name, String alias) {
		this.id = id;
		this.name = name;
		this.alias = alias;
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

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Topic getParent() {
		return parent;
	}

	public void setParent(Topic parent) {
		this.parent = parent;
	}

	public Set<Topic> getChildren() {
		return children;
	}

	public void setChildren(Set<Topic> children) {
		this.children = children;
	}

	public String getAllParentIDs() {
		return allParentIDs;
	}

	public void setAllParentIDs(String allParentIDs) {
		this.allParentIDs = allParentIDs;
	}

	@Transient
	public String getTopicImagePath() {
		
		if (id == null || image == null )  return "/images/thumbnail-default.png";
		
		return "/topic-images/"+ this.id +"/"+this.image;
	}
	
	@Transient
	private boolean hasChildren;

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	
	
	@Override
	public String toString() {
		return this.name;
	}

	
}
