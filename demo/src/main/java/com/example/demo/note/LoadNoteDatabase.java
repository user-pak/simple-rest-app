package com.example.demo.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LoadNoteDatabase implements CommandLineRunner{
	
	private final NoteRepository noteRepository;
	
	@Autowired
	public LoadNoteDatabase(NoteRepository noteRepository) {
		this.noteRepository = noteRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		noteRepository.deleteAll();
		noteRepository.save(new Note("note1","content1"));
		noteRepository.save(new Note("note2","content2"));
	}
	


}
