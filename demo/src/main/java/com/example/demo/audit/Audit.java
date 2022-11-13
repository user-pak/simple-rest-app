package com.example.demo.audit;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Embeddable
public class Audit {

	@CreatedDate
	private LocalDateTime createdOn;
	@LastModifiedDate
	private LocalDateTime updatedOn;
	
	public Audit() {}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(LocalDateTime updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	@PrePersist
	public void prePersist() {
		this.createdOn = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
	}
	
	@PreUpdate
	public void preUpdate() {
		this.updatedOn = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
	}
	
}
