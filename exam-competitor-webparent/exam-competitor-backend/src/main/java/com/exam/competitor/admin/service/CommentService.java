package com.exam.competitor.admin.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exam.competitor.admin.common.entity.blog.Comment;
import com.exam.competitor.admin.repo.CommentRepository;

@Service
public class CommentService{

	@Autowired private  CommentRepository commentRepository;

    public Comment save(Comment comment) {
        return commentRepository.saveAndFlush(comment);
    }

    public Optional<Comment> get(Long id) {
        return Optional.of(commentRepository.getById(id));
    }
    
    public void delete(Long id) {
        commentRepository.deleteById(id);
        
    }
}
