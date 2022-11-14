package com.example.demo.post;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;
import com.example.demo.AbstractEntity;
import com.example.demo.audit.Audit;
import com.example.demo.user.User;
import com.example.demo.user.UserOnlyContainsNickname;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="Post")
@Table(name="post")
@EntityListeners(value = { AuditingEntityListener.class })
public class Post extends AbstractEntity implements UserOnlyContainsNickname{

	private String title;
	private String content;
	@Embedded
	private Audit audit = new Audit();	
	@CreatedBy
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(referencedColumnName ="nickname", name="createdBy")
	@JsonIgnore
	private User createdBy;	
	@Transient
	private String nickname;
	@OneToMany(mappedBy="post",cascade=CascadeType.ALL, orphanRemoval=true)
	private List<PostComment> postComments = new ArrayList<>();
	
	protected Post() {}

	public Post(String title, String content) {
		super();
		Assert.hasText(title, "타이틀이 없습니다");
		Assert.hasText(content, "컨텐트가 없습니다");
		this.title = title;
		this.content = content;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
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
		Assert.notNull(postComment, "댓글이 존재하지 않습니다");
		this.postComments.add(postComment);
		postComment.setPost(this);
		
	}
	
	public void removePostComment(PostComment postComment) {
		Assert.notNull(postComment, "댓글이 존재하지 않습니다");
		this.postComments.remove(postComment);
		postComment.setPost(null);
	}

	public Audit getAudit() {
		return audit;
	}

	@Override
	public String getNickname() {
		// TODO Auto-generated method stub
		if(this.createdBy != null) {
			this.nickname = this.createdBy.getNickname();
		}
		return this.nickname;
	}

	
}
