package com.example.demo.image;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.demo.AbstractEntity;
import com.example.demo.audit.Audit;
import com.example.demo.user.User;
import com.example.demo.user.UserOnlyContainsNickname;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="image")
@EntityListeners(value = { AuditingEntityListener.class })
public class Image extends AbstractEntity implements UserOnlyContainsNickname{
	
	private String imageTitle;
	@Lob
	private byte[] imageFile;
	private String imageFilename;
	@Transient
	private String imageFileURL;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="createdBy",referencedColumnName="nickname")
	@JsonIgnore
	@CreatedBy
	private User createdBy;
	@Transient
	private String nickname;
	@Embedded
	private Audit audit = new Audit();
	
	protected Image() {}

	public Image(String imageTitle, String imageFilename, byte[] imageFile) {
		super();
		this.imageTitle = imageTitle;
		if(imageFile != null) {
			this.imageFile = imageFile;
			this.setImageFilename(imageFilename);
		}
	}

	public String getImageTitle() {
		return imageTitle;
	}

	public void setImageTitle(String imageTitle) {
		this.imageTitle = imageTitle;
	}

	public byte[] getImageFile() {
		return imageFile;
	}

	public void setImageFile(byte[] imageFile) {
		this.imageFile = imageFile;
	}

	public String getImageFilename() {
		return imageFilename;
	}

	public void setImageFilename(String imageFilename) {
		this.imageFilename = imageFilename;
	}
	
	public String getImageFileURL() {
		return imageFileURL;
	}

	public void setImageFileURL(String imageFileURL) {
		this.imageFileURL = imageFileURL;
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
