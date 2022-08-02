package com.exam.competitor.admin.common.entity.examSample;
/*
 * package com.exam.competitor.admin.common.entity.exam;
 * 
 * import java.util.HashSet; import java.util.Set;
 * 
 * import javax.persistence.CascadeType; import javax.persistence.Column; import
 * javax.persistence.Entity; import javax.persistence.FetchType; import
 * javax.persistence.GeneratedValue; import javax.persistence.GenerationType;
 * import javax.persistence.Id; import javax.persistence.ManyToOne; import
 * javax.persistence.OneToMany;
 * 
 * @Entity public class Quiz {
 * 
 * @Id
 * 
 * @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer qId;
 * 
 * 
 * private String title;
 * 
 * @Column(length = 5000) private String description;
 * 
 * private String maxMarks;
 * 
 * private String numberOfQuestions;
 * 
 * private boolean active = false; //add..
 * 
 * @ManyToOne(fetch = FetchType.EAGER) private ExamCategory examCategory;
 * 
 * @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY, cascade =
 * CascadeType.ALL) private Set<Question> questions = new HashSet<>();
 * 
 * 
 * public Quiz() { }
 * 
 * 
 * 
 * public Integer getqId() { return qId; }
 * 
 * 
 * 
 * public void setqId(Integer qId) { this.qId = qId; }
 * 
 * 
 * 
 * public String getTitle() { return title; }
 * 
 * public void setTitle(String title) { this.title = title; }
 * 
 * public String getDescription() { return description; }
 * 
 * public void setDescription(String description) { this.description =
 * description; }
 * 
 * public String getMaxMarks() { return maxMarks; }
 * 
 * public void setMaxMarks(String maxMarks) { this.maxMarks = maxMarks; }
 * 
 * public String getNumberOfQuestions() { return numberOfQuestions; }
 * 
 * public void setNumberOfQuestions(String numberOfQuestions) {
 * this.numberOfQuestions = numberOfQuestions; }
 * 
 * public boolean isActive() { return active; }
 * 
 * public void setActive(boolean active) { this.active = active; }
 * 
 * 
 * 
 * public ExamCategory getExamCategory() { return examCategory; }
 * 
 * public void setExamCategory(ExamCategory examCategory) { this.examCategory =
 * examCategory; }
 * 
 * public Set<Question> getQuestions() { return questions; }
 * 
 * public void setQuestions(Set<Question> questions) { this.questions =
 * questions; } }
 */