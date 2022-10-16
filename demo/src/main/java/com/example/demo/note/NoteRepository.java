package com.example.demo.note;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@RepositoryRestResource(collectionResourceRel="apiNotes", path="apiNotes")
public interface NoteRepository extends CrudRepository<Note,Integer>{
	
	Note findByTitle(@Param(value = "title") String title);
	

}
