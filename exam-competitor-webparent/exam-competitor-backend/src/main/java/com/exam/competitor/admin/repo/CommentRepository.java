package com.exam.competitor.admin.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exam.competitor.admin.common.entity.blog.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
