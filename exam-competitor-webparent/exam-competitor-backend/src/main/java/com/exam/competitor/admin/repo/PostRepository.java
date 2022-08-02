package com.exam.competitor.admin.repo;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.exam.competitor.admin.common.entity.blog.Post;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {

    List<Post> findAllByOrderByCreationDateDesc();

    Optional<Post> findById(Long id);
    
    @Query("SELECT p FROM Post p WHERE p.user.id = ?1 ")
  	List<Post> findAutherPost(Integer integer);
    
    

}
