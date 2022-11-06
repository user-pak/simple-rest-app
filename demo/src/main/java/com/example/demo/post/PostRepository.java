package com.example.demo.post;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="apiPosts", path="apiPosts")
public interface PostRepository extends JpaRepository<Post, Long>{
	
	Optional<Post> findByPostCommentsId(@Param(value="postCommentId") Long id);
	
}
