/*
 * package com.exam.competitor.admin.controller;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.DeleteMapping; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.PathVariable; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.PutMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import com.exam.competitor.admin.common.entity.exam.ExamCategory; import
 * com.exam.competitor.admin.service.ExamCategoryService;
 * 
 * @RestController
 * 
 * @RequestMapping("/exam/category") public class ExamCategoryController {
 * 
 * @Autowired private ExamCategoryService categoryService;
 * 
 * //add category
 * 
 * @PostMapping("/") public ResponseEntity<ExamCategory>
 * addCategory(@RequestBody ExamCategory category) { ExamCategory category1 =
 * this.categoryService.addExamCategory(category); return
 * ResponseEntity.ok(category1); }
 * 
 * //get category
 * 
 * @GetMapping("/{categoryId}") public ExamCategory
 * getCategory(@PathVariable("categoryId") Integer categoryId) { return
 * this.categoryService.getExamCategory(categoryId); }
 * 
 * //get all categories
 * 
 * @GetMapping("/") public ResponseEntity<?> getCategories() { return
 * ResponseEntity.ok(this.categoryService.getCategories()); }
 * 
 * //update category
 * 
 * @PutMapping("/") public ExamCategory updateCategory(@RequestBody ExamCategory
 * category) { return this.categoryService.updateExamCategory(category); }
 * 
 * //delete category
 * 
 * @DeleteMapping("/{categoryId}") public void
 * deleteCategory(@PathVariable("categoryId") Integer categoryId) {
 * this.categoryService.deleteExamCategory(categoryId); }
 * 
 * }
 */