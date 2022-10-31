package com.example.demo.note;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import com.example.demo.note.Comment;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class NoteJpaRepositoryTests {
	
	@Autowired
	private TestEntityManager testManager;
	
	@Test
	public void shouldCreateEntity() throws Exception {
		Note testEntity = testManager.persist(new Note("노트1", "쿠푸의노트", "kupu"));
		testEntity.getComment().add(new Comment("popo","포포의코멘트"));
		testEntity.getComment().add(new Comment("kupu","포포의코멘트의쿠푸의코멘트"));
		testManager.persist(testEntity);
		Note findEntity = testManager.find(Note.class, testEntity.getNoteId());
		assertThat(findEntity)
		.extracting(note -> note.getTitle(), note -> note.getContent(), note-> note.getWriter()) 
		.containsExactly("노트1","쿠푸의노트","kupu");
		assertThat(findEntity.getComment()).extracting(comment-> comment.getWriter(), comment-> comment.getComment())
			.containsOnly(tuple("popo","포포의코멘트"), tuple("kupu","포포의코멘트의쿠푸의코멘트"));
		
	}
}
