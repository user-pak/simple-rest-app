package com.example.demo.note;

import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.demo.note.Note;
import com.example.demo.user.User;

@Embeddable
public class Comment {

//	@ManyToOne(targetEntity=User.class)
//	@JoinColumn(name="nickname")
	private String writer;
	private String comment;

	protected Comment() {}

	public Comment(String writer, String comment) {
		super();
		this.writer = writer;
		this.comment = comment;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null || this.getClass() != obj.getClass()) return false;
		Comment comment = (Comment) obj;
		return Objects.equals(this.writer, comment.writer) && Objects.equals(this.comment, comment.comment);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.writer, this.comment);
	}
}
