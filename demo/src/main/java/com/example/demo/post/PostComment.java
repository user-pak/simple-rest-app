package com.example.demo.post;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.example.demo.AbstractEntity;

@Entity(name="PostComment")
@Table(name="POST_COMMENT")
public class PostComment extends AbstractEntity{
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Post post;
	private String name;
	private String review;
	
	protected PostComment() {}
	
	public PostComment(String name, String review) {
		
		Assert.hasText(name, "작성자가 없습니다");
		Assert.hasText(review,  "코멘트가 없습니다");
		this.name = name;
		this.review = review;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getReview() {
		return review;
	}
	

}
