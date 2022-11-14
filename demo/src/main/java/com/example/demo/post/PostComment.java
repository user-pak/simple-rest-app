package com.example.demo.post;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

@Entity(name="PostComment")
@Table(name="POST_COMMENT")
@EntityListeners(value = { AuditingEntityListener.class })
public class PostComment extends AbstractEntity implements UserOnlyContainsNickname{
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Post post;
	@Embedded
	private Audit audit = new Audit();
	@CreatedBy
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(referencedColumnName="nickname", name="createdBy")
	@JsonIgnore
	private User createdBy;
	@Transient
	private String nickname;
	private String review;
	
	protected PostComment() {}
	
	public PostComment(String review) {
		
		Assert.hasText(review,  "코멘트가 없습니다");
		this.review = review;
	}

	public void setPost(Post post) {
		this.post = post;
	}


	public void setReview(String review) {
		this.review = review;
	}

	public String getReview() {
		return review;
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
