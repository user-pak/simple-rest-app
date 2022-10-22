package com.example.demo.image;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Image {
	
	@Id@GeneratedValue
	private Integer imageId;
	private String imageTitle;
	private String imageFilename;
	
	protected Image() {}

	public Image(String imageTitle, String imageFilename) {
		super();
		this.imageTitle = imageTitle;
		this.imageFilename = imageFilename;
	}

	public Integer getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}

	public String getImageTitle() {
		return imageTitle;
	}

	public void setImageTitle(String imageTitle) {
		this.imageTitle = imageTitle;
	}

	public String getImageFilename() {
		return imageFilename;
	}

	public void setImageFilename(String imageFilename) {
		this.imageFilename = imageFilename;
	}
	
	

}
