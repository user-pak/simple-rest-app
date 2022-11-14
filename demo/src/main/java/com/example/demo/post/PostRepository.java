package com.example.demo.post;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="apiPosts", path="apiPosts")
public interface PostRepository extends JpaRepository<Post, Long>{
	
	@EntityGraph(attributePaths= {"postComments"})
	Optional<Post> findOneByPostCommentsId(Long commentId);
	
}
