package com.example.demo.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
}
