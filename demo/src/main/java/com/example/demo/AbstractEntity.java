package com.example.demo;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public class AbstractEntity {

	@Id
	@GeneratedValue
//	@Version
	private Long id;
	
	public Long getId() {return id;}
	
	@Override
	public boolean equals(Object obj) {
		
		if(this == obj) return true;
		if(this.id == null || obj == null || !(this.getClass().equals(obj.getClass()))) return false;
		AbstractEntity abstractEntity = (AbstractEntity)obj;
		return this.id.equals(abstractEntity.id);
	}
	
	@Override
	public int hashCode() {
		return this.id == null ? 0 : this.id.hashCode();
	}
}
