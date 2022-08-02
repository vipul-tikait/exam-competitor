
package com.exam.competitor.admin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.exam.competitor.admin.common.entity.course.Course;
import com.exam.competitor.admin.common.entity.course.CourseType;
import com.exam.competitor.admin.common.entity.course.QuestionSet;
import com.exam.competitor.admin.repo.CourseRepository;
import com.exam.competitor.admin.repo.CourseTypeRepository;

@DataJpaTest

@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false) 
public class CourseRepositoryTests {
	
	@Autowired private CourseRepository courseRepo;

	@Autowired private CourseTypeRepository courseTypeRepo;

	@Autowired private TestEntityManager entityManager;

	@Test 
	public void testCreateCourse() {

		CourseType courseType = entityManager.find(CourseType.class, 1); 
		QuestionSet qsSet = entityManager.find(QuestionSet.class, 1); 
		/*Question q1 = 
		entityManager.find(Question.class, 1); Question q2 =
		entityManager.find(Question.class, 2);


		Set<Question> qSet = new HashSet<>(); qSet.add(q1); qSet.add(q2);
*/
		//qsSet.setQuestions(qSet);


		Course course = new Course();

		course.setName("SANKALPA INTENSIVE MAINS ANSWER WRITING, 2022/2023");
		course.setDescription("(12 SECTIONWISE TEST + 8 FULL LENGTH TEST)");
		//course.setCourseType(courseType); course.setQuestionSet(qsSet);
		course.setStartDate(new Date()); course.setEndDate(new Date());
		course.setNumberOfTest(30);
		///course.setQuestionSet(qsSet);
		
		Course savedCourse = courseRepo.save(course);
		assertThat(savedCourse.getId()).isGreaterThan(0);

	}

	
	@Test 
	public void testCreateAddtion() {

		CourseType courseType = entityManager.find(CourseType.class, 1); 
		QuestionSet qsSet = entityManager.find(QuestionSet.class, 1); 
		/*Question q1 = 
		entityManager.find(Question.class, 1); Question q2 =
		entityManager.find(Question.class, 2);


		Set<Question> qSet = new HashSet<>(); qSet.add(q1); qSet.add(q2);
*/
		//qsSet.setQuestions(qSet);


		Course course = new Course();

		course.setName("SANKALPA INTENSIVE MAINS ANSWER WRITING, 2022/2023");
		course.setDescription("(12 SECTIONWISE TEST + 8 FULL LENGTH TEST)");
		//course.setCourseType(courseType); course.setQuestionSet(qsSet);
		course.setStartDate(new Date()); course.setEndDate(new Date());
		course.setNumberOfTest(30);
		//course.setQuestionSet(qsSet);
		
		Course savedCourse = courseRepo.save(course);
		assertThat(savedCourse.getId()).isGreaterThan(0);

	}

/*
 * @Test public void testCreateCourseType() {
 * 
 * CourseType course = new CourseType(); course.setName("MPSC"); // Topics
 * topics = new Topics("Indian Culture & Modern Indian History");
 * 
 * CourseType savedUser = courseTypeRepo.save(course);
 * assertThat(savedUser.getId()).isGreaterThan(0); }
 * 
 * s }
 */
	
}