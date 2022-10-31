package com.example.demo.note;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class Note {

	@Id@GeneratedValue
	private Integer noteId;
	private String title;
	private String content;
//	@OneToMany(targetEntity=User.class)
//	@JoinColumn(name="nickname")
	private String writer;
	@ElementCollection(targetClass=Comment.class)
	@CollectionTable(name="comment",joinColumns= @JoinColumn(name="refNotes"))
	private List<Comment> comment = new ArrayList<>();
//	@CreationTimestamp
//	private Timestamp creationTime;
	
	protected Note() {}

	public Note(String title, String content, String writer) {
		super();
		this.title = title;
		this.content = content;
		this.writer = writer;
	}

	public Note(Integer noteId, String title, String content, String writer, List<Comment> comment) {
		super();
		this.noteId = noteId;
		this.title = title;
		this.content = content;
		this.writer = writer;
		this.comment = comment;
	}

	public Integer getNoteId() {
		return noteId;
	}

	public void setNoteId(Integer noteId) {
		this.noteId = noteId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public List<Comment> getComment() {
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}
	
	public void addComment(Comment comment) {
		this.comment.add(comment);
	}
	
	
}
