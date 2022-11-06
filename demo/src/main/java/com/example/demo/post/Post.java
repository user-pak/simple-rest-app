package com.example.demo.post;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.util.Assert;

import com.example.demo.AbstractEntity;

@Entity(name="Post")
@Table(name="post")
public class Post extends AbstractEntity{

	private String title;
	private String name;
	private String content;
	@OneToMany(mappedBy="post",cascade=CascadeType.ALL, orphanRemoval=true)
	private List<PostComment> postComments = new ArrayList<>();
	
	protected Post() {}

	public Post(String title, String name, String content) {
		super();
		Assert.hasText(title, "타이틀이 없습니다");
		Assert.hasText(name, "작성자가 없습니다");
		Assert.hasText(content, "컨텐트가 없습니다");
		this.title = title;
		this.name = name;
		this.content = content;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return this.content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public List<PostComment> getPostComments() {
		return this.postComments;
	}
	
	public void addPostComment(PostComment postComment) {
		Assert.notNull(postComment, "코멘트가 존재하지 않습니다");
		this.postComments.add(postComment);
		postComment.setPost(this);
		
	}
	
	public void removePostComment(PostComment postComment) {
		Assert.notNull(postComment, "코멘트가 존재하지 않습니다");
		this.postComments.remove(postComment);
		postComment.setPost(null);
	}
}
