package com.example.demo.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.DemoControllerException;

@RestController
public class PostController {
	
	private final PostRepository postRepository;
	private final PostCommentRepository commentRepository;
	
	public PostController(@Autowired PostRepository postRepository, @Autowired PostCommentRepository commentRepository) {
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
	}

	@PostMapping("/apiPosts/{id}/postComments")
	public ResponseEntity<Post> addPostComment(@PathVariable Long id, @RequestBody PostComment postComment) {
		
		Post post = postRepository.findById(id).orElseThrow(()->new DemoControllerException("포스트가 없습니다"));
		post.addPostComment(postComment);
		return ResponseEntity.status(HttpStatus.CREATED).body(postRepository.save(post));
	}
	
	@PutMapping("/apiPosts/{id}/postComments/{postCommentId}")
	public ResponseEntity<PostComment> replaceComment(@PathVariable Long id, @PathVariable Long postCommentId, @RequestBody PostComment requestComment) {
		
		PostComment postComment = commentRepository.findById(postCommentId)
				.map(comment -> {
					comment.setReview(requestComment.getReview());
					return commentRepository.save(comment);
				})
				.orElseThrow(() ->new DemoControllerException("댓글이 없습니다"));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(postComment);
		
	}
	
	@DeleteMapping("/apiPosts/{id}/postComments/{postCommentId}")
	public ResponseEntity<Post> deleteComment(@PathVariable Long id, @PathVariable Long postCommentId) {
		
		postRepository.findById(id)
				.map(post-> {
					post.removePostComment(
							commentRepository.findById(postCommentId).orElseThrow(() -> new DemoControllerException("댓글이 없습니다")));
					return postRepository.save(post);
				}).orElseThrow(() -> new DemoControllerException("포스트가 없습니다"));
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/posts/{id}")
	public ModelAndView findById(@PathVariable Long id) {
		
		ModelAndView mav = new ModelAndView("post");
		mav.addObject("post", postRepository.findById(id).orElseThrow(() -> new DemoControllerException("포스트가 없습니다")));
		return mav;
	}
}
