package com.exam.competitor.admin.common.entity;

import java.util.HashSet;
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
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "brands")
public class Brand {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 45, unique = true)
	private String name;
	
	@Column(nullable = false, length = 128)
	private String logo;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	name = "brands_categories",
	joinColumns = @JoinColumn(name = "brand_id"),
	inverseJoinColumns = @JoinColumn(name="category_id")
	)
	private Set<Category> categories = new HashSet<>();
	
	public Brand() {
	}

	public Brand(String name) {
		this.name = name;
		this.logo = "brand-logo.png";
	}
	
	public Brand(Integer id, String name, String logo, Set<Category> categories) {
		this.id = id;
		this.name = name;
		this.logo = logo;
		this.categories = categories;
	}
	
	public Brand(Integer id, String name) {
		this.id = id;
		this.name = name;
		
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

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public void addCategory(Category category) {
		this.categories.add(category);
	}
	
	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + ", logo=" + logo + ", categories=" + categories + "]";
	}

	@Transient
	public String getBrandImagePath() {
		if (id ==null) return "/images/thumbnail-default.png";
		
		return "/brands-images/"+this.id+"/"+this.logo;
	}
	
	public String getCategoryName(Category category) {
		return category.getName();
	}
	
	
}
