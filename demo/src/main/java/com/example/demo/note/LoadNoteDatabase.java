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
		Note note1 = new Note("노트1", "쿠푸의노트", "kupu");
		note1.getComment().add(new Comment("popo","포포의코멘트"));
		note1.getComment().add(new Comment("kupu","포포의코멘트의쿠푸의코멘트"));
		noteRepository.save(note1);
		noteRepository.save(new Note("노트2", "노트내용2", "popo"));
	}
	


}
