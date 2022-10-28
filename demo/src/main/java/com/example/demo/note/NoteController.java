package com.example.demo.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.DemoControllerException;

@RestController
public class NoteController {
	
	private final NoteRepository repository;
	
	@Autowired
	protected NoteController(NoteRepository repository) {
		
		this.repository = repository;
	}

	@GetMapping("/notes/{id}")
	public ModelAndView findById(@PathVariable Integer id) {
		
		ModelAndView mav = new ModelAndView("note");
				mav.addObject("note", repository.findById(id).get());
		return mav;
	}
	
	@PutMapping("/notes/{id}")
	@PreAuthorize("authentication.name == #replacedNote.writer")
	public Note replaceNote(@RequestBody Note replacedNote, @PathVariable Integer id) {
		
		return repository.findById(id).map(note -> {
			note.setTitle(replacedNote.getTitle());
			note.setContent(replacedNote.getContent());
			return repository.save(note);
		}).orElseThrow(() -> new DemoControllerException("노트를 찾을 수 없습니다"));
	}
}
