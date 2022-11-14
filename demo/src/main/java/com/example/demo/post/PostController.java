package com.example.demo.post;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
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
	
	public PostController(@Autowired PostRepository postRepository) {
		this.postRepository = postRepository;

	}

	@PostMapping("/apiPosts/{id}/postComments")
	public ResponseEntity<Post> addPostComment(@PathVariable Long id, @RequestBody PostComment postComment) {
		
		Post post = postRepository.findById(id).orElseThrow(()->new DemoControllerException("포스트가 없습니다"));
		post.addPostComment(postComment);
		return ResponseEntity.status(HttpStatus.CREATED).body(postRepository.save(post));
	}
	
	@PutMapping("/apiPosts/{id}/postComments/{postCommentId}")
	public ResponseEntity<PostComment> replaceComment(@PathVariable Long id, @PathVariable Long postCommentId, @RequestBody PostComment requestComment) {
		
		Post post = postRepository.findOneByPostCommentsId(postCommentId)
				.map(src -> {
						src.getPostComments().get(0).setReview(requestComment.getReview());
						return postRepository.saveAndFlush(src);
				})
				.orElseThrow(() ->new DemoControllerException("포스트가 없습니다"));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(post.getPostComments().get(0));
		
	}
	
	@DeleteMapping("/apiPosts/{id}/postComments/{postCommentId}")
	public ResponseEntity<?> deleteComment(@PathVariable Long id, @PathVariable Long postCommentId) {
		
		postRepository.findOneByPostCommentsId(postCommentId)
				.map(src-> {
						src.removePostComment(src.getPostComments().get(0));
						return postRepository.saveAndFlush(src);
				})
				.orElseThrow(() -> new DemoControllerException("포스트가 없습니다"));
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/posts/{id}")
	public ModelAndView findById(@PathVariable Long id) {
		
		ModelMapper mapper = new ModelMapper();
		ModelAndView mav = new ModelAndView("post");
		Post findById = postRepository.findById(id).orElseThrow(() -> new DemoControllerException("포스트가 없습니다"));
		TypeMap<Post,PostDTO> propertyMapper = mapper.createTypeMap(Post.class, PostDTO.class);
		propertyMapper.addMapping(post -> post.getAudit().getCreatedOn(), PostDTO::setCreatedOn);
		propertyMapper.addMapping(post -> post.getAudit().getUpdatedOn(), PostDTO::setUpdatedOn);
		propertyMapper.addMapping(post -> post.getNickname(), PostDTO::setNickname);
		PostDTO postDTO = mapper.map(findById, PostDTO.class);
			
		mav.addObject("post",postDTO);
		return mav;
	}
	
}
