package com.exam.competitor.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exam.competitor.admin.common.entity.blog.Post;
import com.exam.competitor.repo.PostRepository;


@Service
public class PostService {

	@Autowired private PostRepository postRepository;

    
    public Optional<Post> getById(Integer id) {
        return postRepository.findById(id);
    }

    
    public List<Post> getAll() {
        return postRepository.findAllByOrderByCreationDateDesc();
    }
    
    public List<Post> findAutherPost(Integer integer) {
        return postRepository.findAutherPost(integer);
    }
    
    
    
	/*
	 * public Post save(Post post) { return postRepository.saveAndFlush(post); }
	 */

    
    public void delete(Integer id) {
        postRepository.deleteById(id);
    }

}
