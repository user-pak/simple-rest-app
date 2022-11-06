package com.example.demo.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DemoControllerException;

@RestController
public class PostController {
	
	private final PostRepository postRepository;
	
	public PostController(@Autowired PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	@PostMapping("/apiPosts/{id}/comments")
	public ResponseEntity<Post> addPostComment(@PathVariable Long id, @RequestBody PostComment postComment) {
		
		Post post = postRepository.findById(id).orElseThrow(()->new DemoControllerException("포스트가 없습니다"));
		post.addPostComment(postComment);
		return ResponseEntity.status(HttpStatus.CREATED).body(postRepository.save(post));
	}
	
	@PutMapping("/apiPosts/{id}/comments/{postCommentId}")
	public ResponseEntity<Post> replaceComment(@PathVariable Long id, @PathVariable Long postCommentId, @RequestBody PostComment postComment) {
		
		Post postWithCommentId = postRepository.findByPostCommentsId(postCommentId)
				.map(post-> {
					post.getPostComments().get(0).setReview(postComment.getReview());
					return postRepository.save(post);
					})
				.orElseThrow(() -> new DemoControllerException("포스트가 없습니다"));
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(postWithCommentId);
		
	}
	
	@DeleteMapping("/apiPosts/{id}/comments/{postCommentId}")
	public ResponseEntity<Post> deleteComment(@PathVariable Long id, @PathVariable Long postCommentId) {
		
		Post postWithCommentId = postRepository.findByPostCommentsId(postCommentId)
				.map(post-> {
					post.removePostComment(post.getPostComments().get(0));
					return postRepository.save(post);
				}).orElseThrow(() -> new DemoControllerException("포스트가 없습니다"));
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(postWithCommentId);
	}
}
